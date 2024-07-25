package io.github.srcimon.screwbox.core.utils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

//TODO javadoc test and changelog
public class TextUtil {

    //TODO javadoc test and changelog
    public static List<String> wrapLines(final String text, final int lineLength) {
        requireNonNull(text, "text must not be null");
        Assert.isPositive(lineLength, "line length must be positive");

        if (lineLength >= text.length()) {
            return List.of(text);
        }
        //TODO Optimize performance
        List<String> lines = new ArrayList<>();
        for (int chars = 0; chars < text.length(); chars += lineLength) {
            String substring = text.substring(chars, Math.min(chars + lineLength, text.length()));
            System.out.println(substring);
            lines.add(substring);
        }
        return lines;
    }
}
