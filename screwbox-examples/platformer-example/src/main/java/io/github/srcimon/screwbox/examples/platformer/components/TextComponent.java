package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.entities.Component;

import java.io.Serial;

public class TextComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final String text;
    public final String subtext;

    public TextComponent(final String text, final String subtext) {
        this.text = text;
        this.subtext = subtext;
    }

}
