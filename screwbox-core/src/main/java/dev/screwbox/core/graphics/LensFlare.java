package dev.screwbox.core.graphics;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.CircleDrawOptions;

import javax.swing.text.View;

//TODO document and test
public class LensFlare {

    private final Percent opacity = Percent.of(0.125);

    /**
     * Renders the lens flare on the specified {@link Canvas}.
     */
    public void renderGlowLensFlareTo(Vector position, Color color, double radius, Viewport viewport) {
        var delta = viewport.camera().position().substract(position);
        var a = viewport.camera().position().add(delta.multiply(-2.5));
        var b = viewport.camera().position().add(delta.multiply(-2.0));
        var c = viewport.camera().position().add(delta.multiply(2.5));
        var d = viewport.camera().position().add(delta.multiply(2.0));

        CircleDrawOptions options2 = CircleDrawOptions.fading(color.opacity(color.opacity().value() * opacity.value()));
        viewport.canvas().drawCircle(viewport.toCanvas(a), viewport.toCanvas(radius * 0.5), options2);
       viewport.canvas().drawCircle(viewport.toCanvas(b), viewport.toCanvas(radius * 1.5), options2);
        viewport.canvas().drawCircle(viewport.toCanvas(c), viewport.toCanvas(radius * 0.5), options2);
        viewport.canvas().drawCircle(viewport.toCanvas(d), viewport.toCanvas(radius * 1.5), options2);
    }
}
