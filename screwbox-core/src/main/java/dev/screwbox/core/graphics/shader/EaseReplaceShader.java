package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Ease;
import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Shader;

import java.awt.*;
import java.io.Serial;

/**
 * Replaces the {@link Ease} of the underlying shader by applying {@link Ease} on the provided progress.
 *
 * @since 2.17.0
 */
public class EaseReplaceShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Ease ease;
    private final Shader subShader;

    public EaseReplaceShader(final Ease ease, final Shader subShader) {
        super("change-ease-shader-%s-%s".formatted(ease, subShader.cacheKey()));
        this.ease = ease;
        this.subShader = subShader;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        return subShader.apply(source, ease.applyOn(progress));
    }
}
