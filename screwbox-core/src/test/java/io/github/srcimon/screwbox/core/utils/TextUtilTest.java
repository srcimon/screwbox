package io.github.srcimon.screwbox.core.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TextUtilTest {

    @Test
    void wrapLines_textShorterThanLineLength_returnsOneLine() {
        var result = TextUtil.wrapLines("not enough text", 30);
        assertThat(result).containsExactly("not enough text");
    }

    @Test
    void wrapLines_textLengthExactlyLineLength_returnsOneLine() {
        var result = TextUtil.wrapLines("not enough text", 15);
        assertThat(result).containsExactly("not enough text");
    }

    @Test
    void wrapLines_lengthZero_throwsException() {
        assertThatThrownBy(() -> TextUtil.wrapLines("not enough text", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("line length must be positive");
    }

    @Test
    void wrapLines_textNull_throwsException() {
        assertThatThrownBy(() -> TextUtil.wrapLines(null, 4))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("text must not be null");
    }

    @Test
    void wrapLines_textLengthCanBeDividedByLineLength_returnsLines() {
        var result = TextUtil.wrapLines("this_is_a_test!!", 4);
        assertThat(result).containsExactly("this", "_is_", "a_te", "st!!");
    }

    @Test
    void wrapLines_textLengthCantBeDividedByLineLength_returnsLines() {
        var result = TextUtil.wrapLines("this_is_a_test", 4);
        assertThat(result).containsExactly("this", "_is_", "a_te", "st");
    }

    @Test
    void wrapLines_lineStartsEndEndsWithSpace_removesSpaces() {
        var result = TextUtil.wrapLines("this isa te", 4);
        assertThat(result).containsExactly("this", "isa", "te");
    }

    @Test
    void wrapLines_lineStartsWithSpace_fitsInMoreCharsInLine() {
        var result = TextUtil.wrapLines("this is a test", 4);
        assertThat(result).containsExactly("this", "is a", "test");
    }

    @Test
    void wrapLines_lineEndSplitsWords_triesToPushWordsInNextLine() {
        var result = TextUtil.wrapLines("this is clearly a nicer test than you could imagine", 8);

        assertThat(result).containsExactly("this is", "clearly", "a nicer", "test", "than you", "could", "imagine");
    }
}
