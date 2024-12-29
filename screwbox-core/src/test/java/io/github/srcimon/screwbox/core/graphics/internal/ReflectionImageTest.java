package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@MockitoSettings
class ReflectionImageTest {

    @Mock
    Graphics graphics;

    @Mock
    Camera camera;

    ReflectionImage reflectionImage;

    @BeforeEach
    void setUp() {
        reflectionImage = new ReflectionImage(graphics, 4, Size.of(200, 40), new ScreenBounds(0, 0, 400, 80), null);
    }

    @Test
    void create_noEntitiesAdded_createsEmptyImageOfCorrectSize() {
        var result = reflectionImage.create(false);

        assertThat(result.size()).isEqualTo(Size.of(200, 40));
        assertThat(result.singleFrame().colors()).containsExactly(Color.TRANSPARENT);
    }

    @Test
    void create_entityInAreaAdded_createImage() {
        Entity reflectableSprite = new Entity()
                .add(new TransformComponent(200, 100, 40, 50))
                .add(new RenderComponent(SpriteBundle.ICON, 4));

        when(graphics.camera()).thenReturn(camera);
        when(graphics.toCanvas(Bounds.$$(180, 75, 40, 50), 1, 1)).thenReturn(new ScreenBounds(2, 4, 16, 16));
        reflectionImage.addEntity(reflectableSprite);
        var result = reflectionImage.create(false);

        assertThat(result.size()).isEqualTo(Size.of(200, 40));
        assertThat(result.singleFrame().colors()).hasSize(629);
    }

    @Test
    void create_entityOutsideOfAreaAdded_createTransparentImage() {
        Entity reflectableSprite = new Entity()
                .add(new TransformComponent(200, 100, 40, 50))
                .add(new RenderComponent(SpriteBundle.ICON, 4));

        when(graphics.toCanvas(Bounds.$$(180, 75, 40, 50), 1, 1)).thenReturn(new ScreenBounds(200, 400, 16, 16));
        reflectionImage.addEntity(reflectableSprite);
        var result = reflectionImage.create(false);

        assertThat(result.size()).isEqualTo(Size.of(200, 40));
        assertThat(result.singleFrame().colors()).containsExactly(Color.TRANSPARENT);
    }

    @Test
    void create_entityWithHigherDrawOrderAdded_createTransparentImage() {
        Entity reflectableSprite = new Entity()
                .add(new TransformComponent(200, 100, 40, 50))
                .add(new RenderComponent(SpriteBundle.ICON, 400));

        reflectionImage.addEntity(reflectableSprite);

        var result = reflectionImage.create(false);

        assertThat(result.size()).isEqualTo(Size.of(200, 40));
        assertThat(result.singleFrame().colors()).containsExactly(Color.TRANSPARENT);
    }
}
