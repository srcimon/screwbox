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
import dev.screwbox.core.environment.light.DirectionalLightComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.StaticOccluderComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.utils.TileMap;

import static dev.screwbox.core.Vector.$;

public class PlaygroundApp {

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
                .assign('#', tile -> new Entity().bounds(tile.bounds()).add(new StaticOccluderComponent()).add(new OccluderComponent()).add(new RenderComponent(Sprite.placeholder(Color.DARK_GREEN, 16))))
                .assign('O', tile -> new Entity().bounds(tile.bounds()).add(new StaticOccluderComponent()).add(new OccluderComponent(false)).add(new RenderComponent(Sprite.placeholder(Color.GREY, 16)))))

            .addSystem(new LogFpsSystem())
            .addEntity(new Entity().bounds(map.bounds()).add(new CursorAttachmentComponent()).add(new DirectionalLightComponent(), d -> {
                d.angle = Angle.degrees(-10);
            }))
            .addSystem(e -> e.graphics().canvas().fillWith(Color.BLUE));

        engine.start();
    }

}