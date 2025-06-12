package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.controls.SuspendJumpControlComponent;
import dev.screwbox.core.environment.controls.SuspendJumpControlSystem;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.utils.AsciiMap;
import dev.screwbox.playground.world.Gravity;
import dev.screwbox.playground.world.Player;
import dev.screwbox.playground.world.Rock;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().camera().setZoom(3);

        final var map = AsciiMap.fromString("""
                
                  #
                  #
                 ##
                 
                   P
                #####
        
                ###########
                ###### # ###
                ######   #
                ####### ## #
                 # ###   ###     
                #################
                  ###   ## #
                """);

        engine.environment()
                .importSource(map)
                .as(new Gravity());

        engine.environment()
                .enableAllFeatures()
                .importSource(map.tiles())
                .usingIndex(AsciiMap.Tile::value)
                .when('#').as(new Rock())
                .when('P').as(new Player());

        engine.environment()

                .addSystem(Order.SystemOrder.DEBUG_OVERLAY_EARLY, e -> {
            for (var entity : e.environment().fetchAllHaving(TransformComponent.class)) {
               // e.graphics().world().drawText(entity.position(), entity.name().orElse("."), SystemTextDrawOptions.systemFont("Arial").bold().alignCenter().size(12));
            }
        });
        engine.start();
    }
}