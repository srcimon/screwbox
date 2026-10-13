package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.StaticBoundsComponent;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.smoke.SmokeObstacleComponent;
import dev.screwbox.core.environment.smoke.WindComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.utils.TileMap;

public class PlaygroundApp {


    static Color color = Color.WHITE;

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        screwBox.graphics().configuration().setSmokeCellSize(8).setSmokeBlur(3).setSmokeScale(4);

        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem());

        var map = TileMap.fromString("""
            
             ###       #   ##
             #         ## #### ###
                       #
            
            
            G               
                       P    
              WWWWWW   ######### # #  #           #   #        
                       #        #  #           #   ################
                       #        #  #           #
            
            """, Size.square(32));
        screwBox.graphics().smoke().setOptions(SmokeOptions.noFade());
        screwBox.environment().importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
            .assign('#', (source, idPool) -> new Entity().bounds(source.bounds()).add(new SmokeObstacleComponent()).add(new StaticBoundsComponent()).add(new ColliderComponent()).add(new RenderComponent(Sprite.placeholder(Color.DARK_GREEN, 32))))
            .assign('W', (source, idPool) -> new Entity().bounds(source.bounds()).add(new StaticBoundsComponent())
                .add(new RenderComponent(Sprite.placeholder(Color.WHITE.opacity(0.2), Size.square(32)))).add(new WindComponent(Vector.y(500)))));
        screwBox.environment().addSystem(x -> {
            x.mouse().hoverViewport().camera().changeZoomBy(x.mouse().unitsScrolled() / -20.0);
            x.graphics().world().drawText(x.mouse().position(), "DENSITY " + x.graphics().smoke().densityAt(x.mouse().position()), SystemTextDrawOptions.systemFont("Arial", 20).bold().color(Color.BLUE).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
            x.graphics().world().drawRectangle(Bounds.atPosition(x.mouse().position(), 32, 32), RectangleDrawOptions.filled(x.graphics().smoke().densityAt(x.mouse().position())).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
            x.graphics().world().drawRectangle(Bounds.atPosition(x.mouse().position(), 32, 32), RectangleDrawOptions.outline(Color.WHITE).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
            if (x.mouse().isDownRight()) {
                x.graphics().smoke().push(Bounds.atPosition(screwBox.mouse().position(), 32, 32), Vector.x(8000).multiply(screwBox.loop().delta()));
                x.graphics().smoke().emit(screwBox.mouse().position(), 255 * 0.8 * screwBox.loop().delta(), color);
            }
            if (x.mouse().isPressedLeft()) {
                color = Color.random();
            }
            x.mouse().hoverViewport().camera().move(x.keyboard().wsadMovement(500 * screwBox.loop().delta()));
        });
        screwBox.environment().addSystem(new DebugGridSystem(16, 256));
        screwBox.start();
    }
}