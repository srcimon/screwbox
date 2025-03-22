package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;

import java.awt.*;
import java.util.function.Function;

//TODO document
//TODO changelog
//TODO test
//TODO github issue
public class IntRangeShader extends Shader {

    private final Function<Integer, Shader> subShader;
    private final int from;
    private final int to;

    public IntRangeShader(final int from, final int to, final Function<Integer, Shader> subShader) {
        super("IntRangeShader-%s-%s-%s-%s".formatted(from, to, subShader.apply(from), subShader.apply(to)), true);
        this.subShader = subShader;
        this.from = from;
        this.to = to;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var rangeValue = progress.rangeValue(from, to);
        return subShader.apply(rangeValue).apply(source, progress);
    }
}
