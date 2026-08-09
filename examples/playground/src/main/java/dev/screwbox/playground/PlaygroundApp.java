package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.smoke.SmokeObstacleComponent;
import dev.screwbox.core.environment.smoke.WindComponent;
import dev.screwbox.core.environment.smoke.WindForceComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.graphics.smoke.styles.NettrunnerSmokeStyle;
import dev.screwbox.core.utils.TileMap;

public class PlaygroundApp {

    static Color color = Color.WHITE;

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem());
        var map = TileMap.fromString("""
                                      G  GG GG GG
            ###       #   ##G
            #     G    ## #### ###   
                      #   
              G        #         ####    G       #####
             WWWWWWWW # #  #           #   #
                      #        #  #           #   ################
                      #        #  #           #
            
            """, Size.square(32));

        screwBox.graphics().smoke().setOptions(SmokeOptions.slowFade().style(new NettrunnerSmokeStyle()));
        screwBox.environment().importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
            .assign('G', (source, idPool) -> new Entity().bounds(Bounds.atPosition(source.position(), 16, 16)).add(new WindForceComponent())
                .add(new RenderComponent(Sprite.placeholder(Color.YELLOW, 16))).add(new PhysicsComponent()))
            .assign('#', (source, idPool) -> new Entity().bounds(source.bounds()).add(new SmokeObstacleComponent()).add(new RenderComponent(Sprite.placeholder(Color.DARK_GREEN, 32))))
            .assign('W', (source, idPool) -> new Entity().bounds(source.bounds())
                .add(new RenderComponent(Sprite.placeholder(Color.WHITE.opacity(0.2), Size.square(32)))).add(new WindComponent(Vector.y(2)))));
        screwBox.environment().addSystem(x -> {
            x.mouse().hoverViewport().camera().changeZoomBy(x.mouse().unitsScrolled() / -20.0);
            if (x.mouse().isDownRight()) {
                x.graphics().smoke().push(screwBox.mouse().position(), Vector.x(500).multiply(screwBox.loop().delta()));
                x.graphics().smoke().emit(screwBox.mouse().position(), 0.4 * screwBox.loop().delta(), color);
            }

            if (x.mouse().isPressedLeft()) {
                color = Color.random();
            }
            x.mouse().hoverViewport().camera().move(x.keyboard().wsadMovement(500 * screwBox.loop().delta()));
        });
        screwBox.start();
    }
}