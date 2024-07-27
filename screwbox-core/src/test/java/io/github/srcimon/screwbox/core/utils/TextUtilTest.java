package io.github.srcimon.screwbox.core.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TextUtilTest {

    @Test
    void lineWrap_textShorterThanLineLength_returnsOneLine() {
        var result = TextUtil.lineWrap("not enough text", 30);
        assertThat(result).containsExactly("not enough text");
    }

    @Test
    void lineWrap_textLengthExactlyLineLength_returnsOneLine() {
        var result = TextUtil.lineWrap("not enough text", 15);
        assertThat(result).containsExactly("not enough text");
    }

    @Test
    void lineWrap_lengthZero_throwsException() {
        assertThatThrownBy(() -> TextUtil.lineWrap("not enough text", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("line length must be positive");
    }

    @Test
    void lineWrap_textNull_throwsException() {
        assertThatThrownBy(() -> TextUtil.lineWrap(null, 4))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("text must not be null");
    }
    @Test
    void lineWrap_textLengthCanBeDividedByLineLength_returnsLines() {
        var result = TextUtil.lineWrap("this_is_a_test!!", 4);
        assertThat(result).containsExactly("this", "_is_", "a_te", "st!!");
    }


    @Test
    void lineWrap_textLengthCantBeDividedByLineLength_returnsLines() {
        var result = TextUtil.lineWrap("this_is_a_test", 4);
        assertThat(result).containsExactly("this", "_is_", "a_te", "st");
    }

    @Test
    void lineWrap_lineStartsEndEndsWithSpace_removesSpaces() {
        var result = TextUtil.lineWrap("this isa te", 4);
        assertThat(result).containsExactly("this", "isa", "te");
    }

    @Test
    void lineWrap_lineStartsWithSpace_fitsInMoreCharsInLine() {
        var result = TextUtil.lineWrap("this is a test", 4);
        assertThat(result).containsExactly("this", "is a", "test");
    }

    @Test
    void lineWrap_lineEndSplitsWords_triesToPushWordsInNextLine() {
        var result = TextUtil.lineWrap("this is clearly a nicer test than you could imagine", 8);

        assertThat(result).containsExactly("this is", "clearly", "a nicer", "test", "than you", "could", "imagine");
    }
}
