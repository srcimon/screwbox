package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.AutoTile;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class AutoTileSystemTest {

    private static final AutoTile.Mask NOT_CONNECTED_MASK = new AutoTile.Mask(false, false, false, false, false, false, false, false);
    private static final AutoTile.Mask CONNECTED_EAST = new AutoTile.Mask(false, false, true, false, false, false, false, false);
    private static final AutoTile.Mask CONNECTED_WEST = new AutoTile.Mask(false, false, false, false, false, false, true, false);

    @Test
    void update_noAutoTiles_leavesRenderComponentUnchanged(DefaultEnvironment environment) {
        final var renderComponent = new RenderComponent();

        environment
                .addSystem(new AutoTileSystem())
                .addEntity(new Entity()
                        .bounds(Bounds.atOrigin(16, 16, 16, 16))
                        .add(renderComponent));

        environment.update();

        assertThat(renderComponent.sprite).isEqualTo(Sprite.invisible());
    }

    @Test
    void update_unconnectedAutoTiles_updatesSprites(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.atNanos(123));
        Entity first = new Entity(1)
                .bounds(Bounds.atOrigin(16, 16, 16, 16))
                .add(new AutoTileComponent(AutoTileBundle.BUBBLEGUM))
                .add(new RenderComponent());

        Entity second = new Entity(2)
                .bounds(Bounds.atOrigin(64, 0, 16, 16))
                .add(new AutoTileComponent(AutoTileBundle.BUBBLEGUM))
                .add(new RenderComponent());

        environment
                .addSystem(new AutoTileSystem())
                .addEntity(first)
                .addEntity(second);

        environment.update();

        assertThat(first.get(RenderComponent.class).sprite).isEqualTo(spriteForMask(NOT_CONNECTED_MASK));
        assertThat(first.get(AutoTileComponent.class).lastUpdate).isEqualTo(Time.atNanos(123));
        assertThat(first.get(AutoTileComponent.class).mask).isEqualTo(NOT_CONNECTED_MASK);

        assertThat(second.get(RenderComponent.class).sprite).isEqualTo(spriteForMask(NOT_CONNECTED_MASK));
        assertThat(second.get(AutoTileComponent.class).lastUpdate).isEqualTo(Time.atNanos(123));
        assertThat(second.get(AutoTileComponent.class).mask).isEqualTo(NOT_CONNECTED_MASK);
    }

    @Test
    void update_tilesDidNotChangePositionsAfterLastUpdate_noUpdate(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.atNanos(1), Time.atNanos(2));

        Entity entity = new Entity(1)
                .bounds(Bounds.atOrigin(16, 16, 16, 16))
                .add(new AutoTileComponent(AutoTileBundle.BUBBLEGUM))
                .add(new RenderComponent());

        environment
                .addSystem(new AutoTileSystem())
                .addEntity(entity);

        environment.update();
        assertThat(entity.get(AutoTileComponent.class).lastUpdate.nanos()).isEqualTo(1);

        environment.update();
        assertThat(entity.get(AutoTileComponent.class).lastUpdate.nanos()).isEqualTo(1);
    }

    @Test
    void update_newTileAddedAfterFirstUpdate_updatesSprites(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.atNanos(1), Time.atNanos(2));

        Entity first = new Entity(1)
                .bounds(Bounds.atOrigin(0, 0, 16, 16))
                .add(new AutoTileComponent(AutoTileBundle.BUBBLEGUM))
                .add(new RenderComponent());

        environment
                .addSystem(new AutoTileSystem())
                .addEntity(first);

        environment.update();

        assertThat(first.get(AutoTileComponent.class).lastUpdate.nanos()).isEqualTo(1);

        Entity second = new Entity(2)
                .bounds(Bounds.atOrigin(16, 0, 16, 16))
                .add(new AutoTileComponent(AutoTileBundle.BUBBLEGUM))
                .add(new RenderComponent());

        environment.addEntity(second);

        environment.update();

        assertThat(first.get(AutoTileComponent.class).lastUpdate.nanos()).isEqualTo(2);
        assertThat(first.get(AutoTileComponent.class).mask).isEqualTo(CONNECTED_EAST);
        assertThat(first.get(RenderComponent.class).sprite).isEqualTo(spriteForMask(CONNECTED_EAST));

        assertThat(second.get(AutoTileComponent.class).lastUpdate.nanos()).isEqualTo(2);
        assertThat(second.get(AutoTileComponent.class).mask).isEqualTo(CONNECTED_WEST);
        assertThat(second.get(RenderComponent.class).sprite).isEqualTo(spriteForMask(CONNECTED_WEST));
    }

    private Sprite spriteForMask(final AutoTile.Mask mask) {
        return AutoTileBundle.BUBBLEGUM.get().findSprite(mask);
    }
}
