package io.github.srcimon.screwbox.core.graphics;

import java.util.function.BiFunction;

//TODO make it happen
public enum ShaderMode {

    DEFAULT_ON_TOP((def, cus) -> ShaderSetup.combinedShader(def.shader(), cus.shader()).ease(def.ease()).duration(def.duration()).offset(def.offset())),
    CUSTOM_ON_TOP((def, cus) -> ShaderSetup.combinedShader(cus.shader(), def.shader()).ease(cus.ease()).duration(cus.duration()).offset(cus.offset())),
    DEFAULT_OR_CUSTOM((def, cus) -> {
        if (cus != null) {
            return cus;
        }
        return def;
    }),
    DEFAULT_OVERRULES((def, cus) -> {
        if (def != null) {
            return def;
        }
        return cus;
    });

    private final BiFunction<ShaderSetup, ShaderSetup, ShaderSetup> shaderFunction;

    ShaderMode(BiFunction<ShaderSetup, ShaderSetup, ShaderSetup> shaderFunction) {
        this.shaderFunction = shaderFunction;
    }

    public ShaderSetup getShaderMode(ShaderSetup defaultShader, ShaderSetup customShader) {
        if(defaultShader == null) {
            return customShader;
        }
        if(customShader == null) {
            return defaultShader;
        }
        //TODO simplify function style on top
        return shaderFunction.apply(defaultShader, customShader);
    }
}
