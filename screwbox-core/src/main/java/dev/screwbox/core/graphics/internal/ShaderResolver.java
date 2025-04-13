package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.ShaderSetup;

import static java.util.Objects.isNull;

public class ShaderResolver {

    private ShaderResolver() {
    }

    public static ShaderSetup resolveShader(final ShaderSetup overlayShader, final ShaderSetup customShader, final boolean ignoreOverlay) {
        if (ignoreOverlay || isNull(overlayShader)) {
            return customShader;
        }
        if (isNull(customShader)) {
            return overlayShader;
        }
        return ShaderSetup.combinedShader(customShader.shader(), overlayShader.shader())
                .ease(customShader.ease())
                .duration(customShader.duration())
                .progress(isNull(overlayShader.progress()) ? customShader.progress() : overlayShader.progress())
                .offset(customShader.offset());
    }
}
