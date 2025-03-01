package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.srcimon.screwbox.core.graphics.Offset.origin;
import static io.github.srcimon.screwbox.core.graphics.Size.square;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FrameTest {

    Frame frame;

    @BeforeEach
    void beforeEach() {
        frame = Frame.fromFile("tile.bmp");
    }

    @ParameterizedTest
    @CsvSource({"-1,1", "100,1", "4,-2", "4,44"})
    void colorAt_outOfBounds_throwsException(int x, int y) {
        assertThatThrownBy(() -> frame.colorAt(x, y))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("position is out of bounds: " + x + ":" + y);
    }

    @Test
    void colorAt_inBounds_returnsColorAtPosition() {
        Color color = frame.colorAt(Offset.at(4, 4));

        assertThat(color).isEqualTo(Color.rgb(199, 155, 119));
    }

    @Test
    void replaceColor_doesntReplaceColorInOldFrame() {
        Color oldColor = frame.colorAt(4, 4);

        frame.replaceColor(oldColor, Color.BLUE);

        Color newColor = frame.colorAt(4, 4);
        assertThat(oldColor).isEqualTo(newColor);
    }

    @Test
    void replaceColor_replaceColorInNewFrame() {
        Color oldColor = frame.colorAt(4, 4);

        Frame newFrame = frame.replaceColor(oldColor, Color.BLUE);

        Color newColor = newFrame.colorAt(4, 4);
        assertThat(newColor).isNotEqualTo(oldColor).isEqualTo(Color.BLUE);
    }

    @Test
    void replaceColor_doesntReplaceOtherColors() {
        frame.replaceColor(Color.BLACK, Color.BLUE);

        assertThat(frame.colorAt(4, 4)).isNotEqualTo(Color.BLUE);
    }

    @Test
    void scaled_validScale_returnsScaledFrame() {
        Frame scaled = frame.scaled(3);

        assertThat(scaled.size()).isEqualTo(square(48));
    }

    @Test
    void scaled_invalidWidth_throwsException() {
        assertThatThrownBy(() -> frame.scaled(-0.1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("scaled image width is invalid");
    }

    @Test
    void fromFile_fileIsImage_returnsFrame() {
        assertThat(frame.size()).isEqualTo(Size.square(16));
    }

    @ParameterizedTest
    @CsvSource({"-1,1,4,4", "100,1,4,4", "4,-2,5,20", "4,4,13,1", "4,0,14,30"})
    void extractArea_outOfBounds_throwsException(int x, int y, int width, int height) {
        Offset offset = Offset.at(x, y);
        Size size = Size.of(width, height);

        assertThatThrownBy(() -> frame.extractArea(offset, size))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("given offset and size are out off frame bounds");
    }

    @Test
    void extractArea_inBounds_returnsExtractArea() {
        Offset offset = Offset.at(4, 8);
        Size size = Size.of(2, 7);

        Frame result = frame.extractArea(offset, size);

        assertThat(result.size()).isEqualTo(size);
        assertThat(result.colorAt(origin())).isEqualTo(frame.colorAt(offset));
    }

    @Test
    void cropHorizontal_noTransparentPixels_doesntCrop() {
        var result = frame.cropHorizontal();

        assertThat(result).isNotEqualTo(frame);
        assertThat(result.size()).isEqualTo(frame.size());
        assertThat(result.duration()).isEqualTo(frame.duration());
    }

    @Test
    void cropHorizontal_hasTransparentPixels_cropsImage() {
        var input = Frame.fromFile("transparent.png");

        var result = input.cropHorizontal();

        assertThat(result.size()).isEqualTo(Size.of(12, 16));
        assertThat(result.duration()).isEqualTo(frame.duration());
        assertThat(result.colorAt(0, 0)).isEqualTo(Color.TRANSPARENT);
        assertThat(result.colorAt(0, 3)).isEqualTo(Color.rgb(102, 57, 49));
    }

    @Test
    void listPixelDifferences_differentSizes_throwsException() {
        Frame other = Frame.invisible();
        assertThatThrownBy(() -> frame.listPixelDifferences(other))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("other frame must have identical size to compare pixels");
    }

    @Test
    void listPixelDifferences_sameFrame_noDifferences() {
        assertThat(frame.listPixelDifferences(frame)).isEmpty();
    }

    @Test
    void listPixelDifferences_differentImages_listsDifferences() {
        var other = Frame.fromFile("transparent.png");

        assertThat(frame.listPixelDifferences(other)).hasSize(256).contains(Offset.at(0, 0));
    }

    @Test
    void colors_transparentImage_containsOnlyTransparent() {
        assertThat(Frame.invisible().colors()).containsExactly(Color.TRANSPARENT);
    }

    @Test
    void colors_frameWithFourColors_containsFourColors() {
        Frame frameWithFourColors = SpriteBundle.DOT_YELLOW.get().singleFrame();

        assertThat(frameWithFourColors.colors()).hasSize(4)
                .contains(Color.rgb(205, 233, 17));
    }

    @Test
    void exportPng_fileNameEndsWithPng_exportsFile(@TempDir Path tempDir) {
        Path exportPath = tempDir.resolve("demo.png");

        frame.exportPng(exportPath.toString());

        assertThat(Files.exists(exportPath)).isTrue();
    }

    @Test
    void exportPng_fileNameDoesntEndWithPng_exportsFile(@TempDir Path tempDir) {
        Path exportPath = tempDir.resolve("demo");

        frame.exportPng(exportPath.toString());

        assertThat(Files.exists(tempDir.resolve("demo.png"))).isTrue();
    }

    @Test
    void exportPng_invalidFileName_throwsException() {
        assertThatThrownBy(() -> frame.exportPng("////not-a-file-name"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("could not export frame as png file: ////not-a-file-name");
    }

    @Test
    void exportPng_fileNameNull_throwsException() {
        assertThatThrownBy(() -> frame.exportPng(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("file name must not be null");
    }

    @Test
    void addBorder_colorNull_throwsException() {
        assertThatThrownBy(() -> frame.addBorder(4, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("color must not be null");
    }

    @Test
    void addBorder_widthZero_throwsException() {
        assertThatThrownBy(() -> frame.addBorder(0, Color.RED))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("width must be positive");
    }

    @Test
    void addBorder_validParameters_addsBorder() {
        var result = frame.addBorder(4, Color.ORANGE);

        assertThat(result.size()).isEqualTo(Size.square(24));
        assertThat(result.colorAt(0, 0)).isEqualTo(Color.ORANGE);
        assertThat(result.colorAt(23, 23)).isEqualTo(Color.ORANGE);
    }

    @Test
    void prepareShader_stillShaderCachePrepared_fillsCache() {
        frame.prepareShader(ShaderBundle.GRAYSCALE);

        assertThat(frame.shaderCacheSize()).isOne();
    }

    @Test
    void prepareShader_animatedShaderCachePrepared_fillsCache() {
        frame.prepareShader(ShaderBundle.WATER);

        assertThat(frame.shaderCacheSize()).isEqualTo(100);
    }

    @Test
    void clearShaderCache_cacheHasEntries_isEmpty() {
        frame.prepareShader(ShaderBundle.WATER);

        frame.clearShaderCache();

        assertThat(frame.shaderCacheSize()).isZero();
    }

    @Test
    void prepareShader_multipleShaders_fillsCache() {
        frame.prepareShader(ShaderBundle.WATER);
        frame.prepareShader(ShaderBundle.GRAYSCALE);
        frame.prepareShader(ShaderBundle.FLASHING_RED);

        assertThat(frame.shaderCacheSize()).isEqualTo(201);

        frame.prepareShader(ShaderBundle.WATER);
        frame.prepareShader(ShaderBundle.GRAYSCALE);
        frame.prepareShader(ShaderBundle.FLASHING_RED);

        assertThat(frame.shaderCacheSize()).isEqualTo(201);
    }
}
