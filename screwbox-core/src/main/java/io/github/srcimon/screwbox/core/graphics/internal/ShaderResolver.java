package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.ShaderSetup;

@FunctionalInterface
public interface ShaderResolver {

    ShaderSetup resolveShader(ShaderSetup overlayShader, ShaderSetup customShader);
}
