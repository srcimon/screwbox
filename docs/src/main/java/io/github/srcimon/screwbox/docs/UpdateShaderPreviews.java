package io.github.srcimon.screwbox.docs;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.ShaderBundle;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;

public class UpdateShaderPreviews {

    public static void main(String[] args) {
        final var object = SpriteBundle.BOX_STRIPED.get().singleFrame().addBorder(4, Color.TRANSPARENT).image();
        final var background = SpriteBundle.SKY.get().singleImage();

        for (final var shader : ShaderBundle.values()) {
            shader.get().createPreview(object, background, 10).scaled(2).exportGif(shader.name());
        }
    }
}
