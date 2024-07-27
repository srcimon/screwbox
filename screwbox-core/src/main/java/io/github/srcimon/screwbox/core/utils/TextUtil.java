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
     * Wraps text lines at a certain line length. Tries to avoid to split words.
     */
    public static List<String> wrapLines(final String text, final int lineLength) {
        requireNonNull(text, "text must not be null");
        Validate.positive(lineLength, "line length must be positive");

        if(text.length() <= lineLength) {
            return List.of(text);
        }

        final var lines = new ArrayList<String>();
        int processed = 0;
        while (processed < text.length()) {
            int latestLineEnd = Math.min(processed + lineLength, text.length());
            int betterEnd = latestLineEnd;
            while (betterEnd < text.length() && betterEnd > processed + 1 && text.charAt(betterEnd) != ' ') {
                betterEnd--;
            }
            var optimizedLineEnd = betterEnd == processed + 1 ? latestLineEnd : betterEnd;
            String substring = text.substring(processed, optimizedLineEnd);
            lines.add(substring.trim());
            processed += substring.length();
            if (processed < text.length() && text.charAt(processed) == ' ') {
                processed++;
            }
        }

        return lines;
    }

}
