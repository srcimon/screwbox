package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.core.EngineWatermarkSystem;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.rendering.ReflectionComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.log.Log;
import dev.screwbox.core.utils.AsciiMap;
import dev.screwbox.playground.world.Gravity;
import dev.screwbox.playground.world.Player;
import dev.screwbox.playground.world.Rock;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");
        engine.graphics().configuration().setBackgroundColor(Color.hex("#02010e"));
        engine.graphics().camera().setZoom(3);

        final var map = AsciiMap.fromString("""
                 #
                ###    #
                ###
                
                   P      
                #####     ##
                ##          #  #     ########
                #### ###################### #
                ###### # ### ###  ###########
                """);

        engine.environment()
                .importSource(map)
                .as(new Gravity())
                        .as(new SourceImport.Converter<AsciiMap>() {
                            @Override
                            public Entity convert(AsciiMap object) {
                           return new Entity()
                                   .add(new ReflectionComponent(Percent.half(), 0), c -> {
                                       c.applyWaveDistortionPostFilter = true;
                                       c.frequencyX = 0.2;
                                       c.frequencyY = 0.4;
                                       c.speed = 0.002;
                                   })
                                   .bounds(object.bounds().moveBy(0, object.bounds().height()));
                            }
                        });

        engine.environment()
                .enableAllFeatures()
                .importSource(map.tiles())
                .usingIndex(AsciiMap.Tile::value)
                .when('#').as(new Rock())
                .when('P').as(new Player());

        engine.environment()
                .addSystem(Order.SystemOrder.DEBUG_OVERLAY_EARLY, e -> {
                    for (var entity : e.environment().fetchAllHaving(TransformComponent.class)) {
                        if(e.keyboard().isAnyKeyDown()) {
                            e.graphics().world().drawText(entity.position(), entity.name().orElse("."), SystemTextDrawOptions.systemFont("Arial").bold().alignCenter().size(12));

                        }
                    }
                });
        engine.start();
    }
}