package dev.screwbox.playground;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.utils.TileMap;

import static dev.screwbox.core.Vector.$;

public class PlaygroundApp {

    static Angle rotation = Angle.degrees(0);

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");
        engine.graphics().light().setAmbientLight(Percent.quarter());
        engine.graphics().camera()
            .move($(40, 40))
            .setZoom(4);
        engine.graphics().configuration().setLightQuality(Percent.threeQuarter());
        var map = TileMap.fromString("""
               O   O
               # ###    ##
            #     ## O
                      O  ##
            ###    ######
            """);
        engine.environment()
            .enableAllFeatures()
            .importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('#', tile -> new Entity().bounds(tile.bounds()).add(new OccluderComponent()).add(new RenderComponent(Sprite.placeholder(Color.DARK_GREEN, 16))))
                .assign('O', tile -> new Entity().bounds(tile.bounds()).add(new OccluderComponent(false)).add(new RenderComponent(Sprite.placeholder(Color.GREY, 16)))))

            .addSystem(new LogFpsSystem())
            .addSystem(e -> e.graphics().canvas().fillWith(Color.BLUE))
            .addSystem(e -> {
                Line between = rotation.applyOn(Line.between(e.mouse().position(), e.mouse().position().add(50, -10)));
                e.graphics().world().drawLine(between, LineDrawOptions.color(Color.WHITE).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
                e.graphics().light().addDirectionalLight(between, 80, Color.BLACK);
                if (e.keyboard().isDown(Key.Q)) {
                    rotation = rotation.addDegrees(e.loop().delta(-40));
                }
                if (e.keyboard().isDown(Key.E)) {
                    rotation = rotation.addDegrees(e.loop().delta(40));
                }
            });

        engine.start();
    }

}