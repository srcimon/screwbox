package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

//TODO javadoc
//TODO changelog
//TODO test
public class TweenSpinComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public boolean isSpinHorizontal;

    public TweenSpinComponent() {
        this(true);
    }

    public TweenSpinComponent(final boolean isSpinHorizontal) {
        this.isSpinHorizontal = isSpinHorizontal;
    }

}
