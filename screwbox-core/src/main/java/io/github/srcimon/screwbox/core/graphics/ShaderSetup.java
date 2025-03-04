package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;
import io.github.srcimon.screwbox.core.graphics.shader.ColorizeShader;
import io.github.srcimon.screwbox.core.graphics.shader.CombinedShader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.util.Objects.isNull;

/**
 * Setup for using a {@link Shader}. {@link Shader Shaders} are used to create still and animated graphic effects.
 * This setup can configure the graphical effects even further.
 *
 * @param shader   {@link Shader} used to create the graphic effect
 * @param offset   {@link Time} offset used for animation start, has no effect on non animated shaders
 * @param duration {@link Duration} of the animation, has no effect on non animated shaders
 * @param ease     {@link Ease} applied for the animation, has no effect on non animated shaders
 * @since 2.15.0
 */
public record ShaderSetup(Shader shader, Time offset, Duration duration, Ease ease) {

    private static final Random RANDOM = new Random();

    /**
     * Creates a new setup using multiple {@link Shader shaders} for a combined effect. The shaders are applied in
     * specified order. Can be used for at least 2 {@link Shader shaders}.
     */
    public static ShaderSetup combinedShader(Shader... shaders) {
        return shader(new CombinedShader(shaders));
    }

    /**
     * Create a new setup using a single {@link Shader}.
     */
    public static ShaderSetup shader(Shader shader) {
        return new ShaderSetup(shader, Time.unset(), Duration.oneSecond(), Ease.LINEAR_IN);
    }

    /**
     * Sets the {@link Time} offset used for animation start. Has no effect on non animated shaders.
     */
    public ShaderSetup offset(Time offset) {
        return new ShaderSetup(shader, offset, duration, ease);
    }

    /**
     * Sets the {@link Time} offset used for animation start to random value.
     *
     * @since 2.16.0
     */
    public ShaderSetup randomOffset() {
        Time randomTime = Time.atNanos(RANDOM.nextLong(0, 60_000_000_000L));
        return new ShaderSetup(shader, randomTime, duration, ease);
    }

    /**
     * Sets the {@link Duration} of the animation. Has no effect on non animated shaders.
     */
    public ShaderSetup duration(Duration duration) {
        return new ShaderSetup(shader, offset, duration, ease);
    }

    /**
     * Sets the {@link Ease} applied for the animation. Has no effect on non animated shaders.
     */
    public ShaderSetup ease(Ease ease) {
        return new ShaderSetup(shader, offset, duration, ease);
    }

    /**
     * Creates an animated preview {@link Sprite} with default settings.
     *
     * @see  #createPreview(Image, Image, int)
     */
    public Sprite createPreview(final Image source) {
        return createPreview(source, SpriteBundle.SHADER_PREVIEW.get().singleImage(), 10);
    }

    /**
     * Creates an animated preview {@link Sprite} without background image.
     *
     * @see  #createPreview(Image, Image, int)
     */
    public Sprite createPreview(final Image source, int frameCount) {
        return createPreview(source, null, frameCount);
    }
    
    /**
     * Creates an animated preview {@link Sprite} for the {@link ShaderSetup}.
     * Frame count will be ignored on non animated {@link Shader shaders}.
     * It's possible to ad a background. The background can also be null.
     */
    public Sprite createPreview(final Image source, final Image background, int frameCount) {
        if (!shader.isAnimated()) {
            final Image preview = shader.apply(source, null);
            final Image previewOnBackground = combine(preview, background);
            return Sprite.fromImage(previewOnBackground);
        }
        final Duration stepDuration = Duration.ofNanos(duration.nanos() / frameCount);
        final var frames = new ArrayList<Frame>();
        for (double i = 0; i < frameCount; i++) {
            final var progress = Percent.of(i / frameCount);
            final Image preview = shader.apply(source, ease.applyOn(progress));
            frames.add(new Frame(combine(preview,background), stepDuration));
        }
        return new Sprite(frames);
    }

    public static void main(String[] args) {
        ShaderSetup.shader(new ColorizeShader(Color.TRANSPARENT)).createPreview(SpriteBundle.BOX_STRIPED.get().addBorder(2, Color.TRANSPARENT).singleImage()).scaled(2).exportGif("NONE");
    }
    private Image combine(final Image image, final Image background) {
        if(isNull(background)) {
            return image;
        }
        final var combinedImage  = ImageOperations.cloneImage(background);
        final var graphics = combinedImage.getGraphics();
        final int width = Math.max(0, (background.getWidth(null) - image.getWidth(null)) / 2);
        final int height = Math.max(0, (background.getHeight(null) - image.getHeight(null)) / 2);
        graphics.drawImage(image, width, height, null);
        graphics.dispose();
        return combinedImage;
    }
}
