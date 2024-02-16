package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.graphics.internal.AwtMapper;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.utils.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

import static java.lang.String.format;
import static java.util.Objects.isNull;

public final class Frame implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Frame INVISIBLE = new Frame(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));

    private final Duration duration;
    private final ImageIcon imageCont;

    /**
     * Returns an invisible {@link Frame}.
     */
    public static Frame invisible() {
        return INVISIBLE;
    }

    /**
     * Returns a {@link Frame} from the given {@link Image}.
     */
    public static Frame fromImage(final Image image) {
        return new Frame(image);
    }

    /**
     * Returns a {@link Frame} created from a file.
     */
    public static Frame fromFile(final String fileName) {
        final byte[] imageData = Resources.loadBinary(fileName);
        try (var inputStream = new ByteArrayInputStream(imageData)) {
            final BufferedImage image = ImageIO.read(inputStream);
            if (isNull(image)) {
                throw new IllegalArgumentException("image cannot be read: " + fileName);
            }
            return new Frame(image);
        } catch (final IOException e) {
            throw new IllegalArgumentException("error while reading image: " + fileName, e);
        }
    }

    private Frame(final Image image) {
        this(image, Duration.none());
    }

    public Frame(final Image image, final Duration duration) {
        this.imageCont = new ImageIcon(image);
        this.duration = duration;
    }

    /**
     * Returns a new {@link Frame} created from a sub image of this {@link Frame}.
     */
    public Frame extractArea(final Offset offset, final Size size) {
        if (offset.x() < 0
                || offset.y() < 0
                || offset.x() + size.width() > size().width()
                || offset.y() + size.height() > size().height()) {
            throw new IllegalArgumentException("given offset and size are out offrame bounds");
        }
        final var image = ImageUtil.toBufferedImage(image());
        final var subImage = image.getSubimage(offset.x(), offset.y(), size.width(), size.height());
        return new Frame(subImage, duration);
    }

    public Image image() {
        return imageCont.getImage();
    }

    public Duration duration() {
        return duration;
    }

    /**
     * Returns the size of the frames {@link #image()}.
     */
    public Size size() {
        return Size.of(imageCont.getIconWidth(), imageCont.getIconHeight());
    }

    /**
     * Returns the {@link Color} of the pixel at the given position.
     *
     * @throws IllegalArgumentException when position is out of bounds.
     * @see #colorAt(int, int)
     */
    public Color colorAt(final Offset offset) {
        return colorAt(offset.x(), offset.y());
    }

    /**
     * Returns the {@link Color} of the pixel at the given position.
     *
     * @throws IllegalArgumentException when position is out of bounds.
     * @see #colorAt(Offset)
     */
    public Color colorAt(final int x, final int y) {
        final Image image = image();
        if (x < 0 || x >= image.getWidth(null) || y < 0 || y >= image.getHeight(null)) {
            throw new IllegalArgumentException(format("position is out of bounds: %d:%d", x, y));
        }
        final BufferedImage bufferedImage = ImageUtil.toBufferedImage(image);
        final int rgb = bufferedImage.getRGB(x, y);
        final java.awt.Color awtColor = new java.awt.Color(rgb, true);
        return AwtMapper.toColor(awtColor);
    }

    /**
     * Returns a new instance. The new {@link Frame}s old {@link Color} is replaced
     * with a new one. This method is quite slow.
     */
    public Frame replaceColor(final Color oldColor, final Color newColor) {
        final Image oldImage = imageCont.getImage();
        final Image newImage = ImageUtil.applyFilter(oldImage, new ReplaceColorFilter(oldColor, newColor));
        return new Frame(newImage);
    }

    /**
     * Returns a scaled version of the current {@link Frame}.
     */
    public Frame scaled(final double scale) {
        final int width = (int) (size().width() * scale);
        final int height = (int) (size().height() * scale);
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Scaled image is size is invalid");
        }
        final var newImage = image().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new Frame(newImage, duration);
    }

    /**
     * Returns a new instance of a {@link Frame} without transparent pixels on the left and the right side.
     */
    public Frame cropHorizontal() {
        int minX = 0;
        int maxX = size().width() - 1;

        while (minX < maxX && hasOnlyTransparentPixelInColumn(minX)) {
            minX++;
        }

        while (maxX > minX && hasOnlyTransparentPixelInColumn(maxX)) {
            maxX--;
        }

        final var offset = Offset.at(minX, 0);
        final var target = Offset.at(maxX + 1, size().height());
        final var size = Size.definedBy(offset, target);
        return extractArea(offset, size);
    }

    private boolean hasOnlyTransparentPixelInColumn(final int x) {
        for (int y = 0; y < size().height(); y++) {
            if (!colorAt(x, y).equals(Color.TRANSPARENT)) {
                return false;
            }
        }
        return true;
    }
}