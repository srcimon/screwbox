package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.smoke.SmokeObstacleComponent;
import dev.screwbox.core.environment.smoke.WindComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.SplitScreenOptions;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.ViewportLayout;
import dev.screwbox.core.graphics.layouts.HorizontalLayout;
import dev.screwbox.core.graphics.layouts.TableLayout;
import dev.screwbox.core.graphics.layouts.VerticalLayout;
import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.core.utils.TileMap;

import static dev.screwbox.core.graphics.SplitScreenOptions.viewports;

public class PlaygroundApp {

    static Color color = Color.WHITE;

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.loop().unlockFps();
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem());

        var map = TileMap.fromString("""
            
            ###       #   ##
            #         ## #### ###
                      #
                      #        ####           #####
             WWWWWWWW # #  #           #   #
                      #        #  #           #   ################
                      #        #  #           #
            
            """, Size.square(32));

        screwBox.graphics().smoke().setOptions(SmokeOptions.vaporPreset().baseVelocity(Vector.y(-0.05)));


        screwBox.environment().importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
            .assign('#', (source, idPool) -> new Entity().bounds(source.bounds()).add(new SmokeObstacleComponent()).add(new RenderComponent(Sprite.placeholder(Color.DARK_GREEN, 32))))
            .assign('W', (source, idPool) -> new Entity().bounds(source.bounds()).add(new RenderComponent(Sprite.placeholder(Color.WHITE.opacity(0.2), Size.square(32)))).add(new WindComponent(Vector.y(0.2)))));
        screwBox.environment().addSystem(x -> {
            x.mouse().hoverViewport().camera().changeZoomBy(x.mouse().unitsScrolled() / -20.0);
            x.graphics().smoke().push(screwBox.mouse().position(), Vector.$(100, -30).multiply(screwBox.loop().delta()));
            x.graphics().smoke().emit(screwBox.mouse().position(), 1 * screwBox.loop().delta(), color);
            if (x.mouse().isPressedLeft()) {
                color = Color.random();
            }
            int viewportCount = x.graphics().viewports().size();

            if (x.keyboard().isPressed(Key.T)) {
                x.graphics().enableSplitScreenMode(viewports(viewportCount + 1).layout(new TableLayout(3, false)).padding(4));
            } else if (x.keyboard().isPressed(Key.Z)) {
                if (viewportCount == 1) {
                    x.graphics().disableSplitScreenMode();
                } else {
                    x.graphics().enableSplitScreenMode(viewports(viewportCount - 1).padding(4));
                }
            } else if (x.keyboard().isPressed(Key.U)) {
                ViewportLayout layout = ListUtil.randomFrom(new TableLayout(3, true), new TableLayout(3, false), new TableLayout(), new HorizontalLayout(), new VerticalLayout());
                x.graphics().enableSplitScreenMode(viewports(x.graphics().viewports().size()).layout(layout));
            }
            x.mouse().hoverViewport().camera().move(x.keyboard().wsadMovement(500 * screwBox.loop().delta()));
        });
        screwBox.graphics().enableSplitScreenMode(SplitScreenOptions.viewports(2));
        screwBox.start();
    }
}