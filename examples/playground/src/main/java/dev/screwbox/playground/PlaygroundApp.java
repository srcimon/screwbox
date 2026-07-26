package dev.screwbox.playground;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.importing.AdvancedBlueprint;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.smoke.SmokeObstacleComponent;
import dev.screwbox.core.environment.smoke.SmokeSystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.utils.TileMap;

public class PlaygroundApp {

    static Color color = Color.WHITE;

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        screwBox.graphics().smoke().enable();
        screwBox.loop().unlockFps();
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(new SmokeSystem());

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
            .assign('#', new AdvancedBlueprint<TileMap.Tile<Character>>() {
                @Override
                public Entity assembleFrom(TileMap.Tile<Character> source, IdPool idPool) {
                    return new Entity().bounds(source.bounds()).add(new SmokeObstacleComponent()).add(new RenderComponent(Sprite.placeholder(Color.GREEN, 32)));
                }
            }));
        screwBox.environment().addSystem(x -> {
            x.graphics().camera().changeZoomBy(x.mouse().unitsScrolled() / -20.0);
            x.graphics().smoke().affect(screwBox.mouse().position(), range.multiply(screwBox.loop().delta()));
            x.graphics().smoke().emit(screwBox.mouse().position(), 400 * screwBox.loop().delta(), color);
            if (x.mouse().isPressedLeft()) {
                color = Color.random();
            }
            if (x.mouse().isDownLeft()) {
                range = Angle.degrees(200 * x.loop().delta()).rotate(range);
            }
            x.graphics().camera().move(x.keyboard().wsadMovement(500 * screwBox.loop().delta()));
        });

        screwBox.start();
    }

    static Vector range = Vector.y(-40);
}