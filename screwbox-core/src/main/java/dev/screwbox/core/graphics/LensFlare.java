package dev.screwbox.core.graphics;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.CircleDrawOptions;

//TODO document and test
public class LensFlare {


    private final Percent opacity = Percent.of(0.125);

    /**
     * Renders the lens flare on the specified {@link Canvas}.
     */
    public void renderGlowLensFlareTo(Vector position, Color color, double radius, Viewport viewport) {
        Vector camPosition = viewport.camera().position();
        var delta = camPosition.substract(position);
        var a = camPosition.add(delta.multiply(-2.5));
        var b = camPosition.add(delta.multiply(-2.0));
        var c = camPosition.add(delta.multiply(2.5));

        CircleDrawOptions options2 = CircleDrawOptions.fading(color.opacity(color.opacity().value() * opacity.value()));
        viewport.canvas().drawCircle(viewport.toCanvas(a), viewport.toCanvas(radius * 0.5), options2);
        viewport.canvas().drawCircle(viewport.toCanvas(b), viewport.toCanvas(radius * 1.5), options2);
        viewport.canvas().drawCircle(viewport.toCanvas(c), viewport.toCanvas(radius * 2.0), options2);
    }
}
