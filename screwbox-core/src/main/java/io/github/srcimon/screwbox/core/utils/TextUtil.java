package io.github.srcimon.screwbox.core.utils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

//TODO javadoc test and changelog
public class TextUtil {

    private TextUtil() {
    }
    //TODO optimize -> dont split words
    //TODO javadoc test and changelog
    public static List<String> wrapLines(final String text, final int lineLength) {
        requireNonNull(text, "text must not be null");
        Validate.positive(lineLength, "line length must be positive");

        if (lineLength >= text.length()) {
            return List.of(text);
        }
        final var lines = new ArrayList<String>();
        for (int chars = 0; chars < text.length(); chars += lineLength) {
            final int endIndex = Math.min(chars + lineLength, text.length());
            lines.add(text.substring(chars, endIndex).trim());
        }
        return lines;
    }
}
