package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Camera;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.SpriteBundle;
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
        var result = reflectionImage.create();

        assertThat(result.getWidth()).isEqualTo(200);
        assertThat(result.getHeight()).isEqualTo(40);
        assertThat(Frame.fromImage(result).colors()).containsExactly(Color.TRANSPARENT);
    }

    @Test
    void create_entityInAreaAdded_createImage() {
        Entity reflectableSprite = new Entity()
                .add(new TransformComponent(200, 100, 40, 50))
                .add(new RenderComponent(SpriteBundle.ICON, 4));

        when(graphics.camera()).thenReturn(camera);
        when(graphics.toCanvas(Bounds.$$(180, 75, 40, 50), 1, 1)).thenReturn(new ScreenBounds(2, 4, 16, 16));
        reflectionImage.addEntity(reflectableSprite, null);
        var result = reflectionImage.create();

        assertThat(result.getWidth()).isEqualTo(200);
        assertThat(result.getHeight()).isEqualTo(40);
        assertThat(Frame.fromImage(result).colors()).hasSize(9);
    }

    @Test
    void create_entityOutsideOfAreaAdded_createTransparentImage() {
        Entity reflectableSprite = new Entity()
                .add(new TransformComponent(200, 100, 40, 50))
                .add(new RenderComponent(SpriteBundle.ICON, 4));

        when(graphics.toCanvas(Bounds.$$(180, 75, 40, 50), 1, 1)).thenReturn(new ScreenBounds(200, 400, 16, 16));
        reflectionImage.addEntity(reflectableSprite, null);
        var result = reflectionImage.create();

        assertThat(result.getWidth()).isEqualTo(200);
        assertThat(result.getHeight()).isEqualTo(40);
        assertThat(Frame.fromImage(result).colors()).containsExactly(Color.TRANSPARENT);
    }

    @Test
    void create_entityWithHigherDrawOrderAdded_createTransparentImage() {
        Entity reflectableSprite = new Entity()
                .add(new TransformComponent(200, 100, 40, 50))
                .add(new RenderComponent(SpriteBundle.ICON, 400));

        reflectionImage.addEntity(reflectableSprite, null);

        var result = reflectionImage.create();

        assertThat(result.getWidth()).isEqualTo(200);
        assertThat(result.getHeight()).isEqualTo(40);
        assertThat(Frame.fromImage(result).colors()).containsExactly(Color.TRANSPARENT);
    }
}
