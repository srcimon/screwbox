package io.github.srcimon.screwbox.core.utils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Utils to help you with texts.
 */
public class TextUtil {

    private TextUtil() {
    }

    /**
     * Wraps text lines at a certain line length. Currently does not try to avoid splitting words.
     */
    //TODO dont wrap words
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
