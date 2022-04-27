package de.suzufa.screwbox.playground.debo.components;

import de.suzufa.screwbox.core.entityengine.Component;

public class TextComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final String text;
    public final String subtext;

    public TextComponent(final String text, final String subtext) {
        this.text = text;
        this.subtext = subtext;
    }

}
