package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.io.Serial;


/**
 * Blends between original image and result of inner {@link Shader}.
 *
 * @since 3.21.0
 */
public class BlendShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Shader innerShader;

    public BlendShader(final Shader innerShader) {
        super("BlendShader-" + innerShader.cacheKey());
        this.innerShader = innerShader;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var innerResult = innerShader.apply(source, progress);
        final var result = ImageOperations.createEmptyImageOfSameSize(source);
        final Graphics2D graphics = result.createGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) progress.invert().value()));
        graphics.drawImage(source, 0, 0, null);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) progress.value()));
        graphics.drawImage(innerResult, 0, 0, null);
        return result;
    }
}
