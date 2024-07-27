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

        final var lines = new ArrayList<String>();
        int processed = 0;
        while (processed < text.length()) {

            int maxEndIndex = Math.min(processed + lineLength, text.length());
            var actual = findBetter(text, maxEndIndex, processed);
            String substring = text.substring(processed, actual);
            lines.add(substring.trim());
            processed += substring.length();
        }

        return lines;
    }

    private static int findBetter(String text, int maxEndIndex, int processed) {
        if (maxEndIndex + 1 < text.length() && text.charAt(maxEndIndex) != ' ') {
            for (int bestEndIndex = maxEndIndex; bestEndIndex > processed + 2; bestEndIndex--) {
                if (text.charAt(bestEndIndex) != ' ') {
                    System.out.println("F");
                   return bestEndIndex;
                }
            }
        }
        return maxEndIndex;
    }
}
