package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.smoke.SmokeObstacleComponent;
import dev.screwbox.core.environment.smoke.SmokeRenderSystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.utils.TileMap;

public class PlaygroundApp {

    static Color color = Color.WHITE;

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.loop().unlockFps();
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(new SmokeRenderSystem());

        var map = TileMap.fromString("""
            
            ###       #   ##
            #         ## #### ###
                      #
                      #        ####           #####
                      #        #  #           #   #
                      #        #  #           #   ################
                      #        #  #           #
            
            """, Size.square(32));

        screwBox.environment().importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
            .assign('#', (source, idPool) -> new Entity().bounds(source.bounds()).add(new SmokeObstacleComponent()).add(new RenderComponent(Sprite.placeholder(Color.DARK_GREEN, 32)))));
        screwBox.environment().addSystem(x -> {
            x.graphics().camera().changeZoomBy(x.mouse().unitsScrolled() / -20.0);
            x.graphics().smoke().push(screwBox.mouse().position(), Vector.$(50,-30).multiply(screwBox.loop().delta() ));
            x.graphics().smoke().emit(screwBox.mouse().position(),1 * screwBox.loop().delta(), color);
            if (x.mouse().isPressedLeft()) {
                color = Color.random();
            }
            x.graphics().camera().move(x.keyboard().wsadMovement(500 * screwBox.loop().delta()));
        });

        screwBox.start();
    }

}