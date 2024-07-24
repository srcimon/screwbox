package io.github.srcimon.screwbox.core.utils;

import java.util.ArrayList;
import java.util.List;
//TODO javadoc test and changelog
public class TextUtil {

    //TODO javadoc test and changelog
    public static List<String> wrapLines(String text, int lineLength) {
        if(lineLength >= text.length()) {
            return List.of(text);
        }
        //TODO Optimize performance
        List<String> lines = new ArrayList<>();
        return List.of("Line 1", "ANother line", "A third line");//TODO FIXME
    }
}
