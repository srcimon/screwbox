package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import dev.screwbox.core.utils.TileMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Vector.y;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class SoftBodyBoundarySystemTest {

    @Test
    void update_bigSoftBodyLandsOnSmallTerrain_doesNotFallThrough(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.01);
        var box = SoftPhysicsSupport.createSoftBody(Bounds.$$(0, 0, 100, 100), environment);
        box.root().add(new SoftBodyBoundaryComponent());

        var scene = TileMap.fromString("""
                BBB
            
                 T
                 P
            """);

        var idOfSoftBody = environment.peekId();
        environment
            .addEntity(new Entity().add(new GravityComponent(y(500))))
            .importSource(ImportOptions.indexedSources(scene.blocks(), TileMap.Block::value)
                .assignComplex('B', (block, idPool) -> {
                    var softBody = SoftPhysicsSupport.createSoftBody(block.bounds(), idPool);
                    softBody.root().add(new SoftBodyBoundaryComponent());
                    return softBody;
                }))
            .importSource(ImportOptions.indexedSources(scene.tiles(), TileMap.Tile::value)
                .assign('T', tile -> new Entity()
                    .bounds(tile.bounds())
                    .add(new ColliderComponent())))
            .importSource(ImportOptions.indexedSources(scene.tiles(), TileMap.Tile::value)
                .assign('P', tile -> new Entity(1000)
                    .bounds(tile.bounds())
                    .add(new ColliderComponent())))
            .enableSoftPhysics()
            .enablePhysics();


        environment.updateTimes(100);

        var probeY = environment.fetchById(1000).position().y();
        var softBodyY = environment.fetchById(idOfSoftBody).position().y();

        assertThat(probeY).isGreaterThan(softBodyY);
    }
}
