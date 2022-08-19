package de.suzufa.screwbox.core.graphics;

import static de.suzufa.screwbox.core.graphics.internal.ImageUtil.applyFilter;
import static de.suzufa.screwbox.core.graphics.internal.ImageUtil.flipHorizontally;
import static de.suzufa.screwbox.core.graphics.internal.ImageUtil.flipVertically;
import static de.suzufa.screwbox.core.graphics.internal.ImageUtil.scale;
import static de.suzufa.screwbox.core.graphics.internal.ImageUtil.toBufferedImage;
import static java.lang.String.format;
import static java.util.Objects.isNull;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.ImageIcon;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.graphics.internal.AwtMapper;

public final class Frame implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Frame INVISIBLE = new Frame(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));

    private final Duration duration;
    private ImageContainer imageContainer;

    private final class ImageContainer implements Serializable {

        private static final long serialVersionUID = 1L;
        private final ImageIcon image;
        private transient Image imageFlippedH;
        private transient Image imageFlippedV;
        private transient Image imageFlippedHandV;

        ImageContainer(final Image image) {
            this.image = new ImageIcon(image);
            this.imageFlippedH = flipHorizontally(image);
            this.imageFlippedV = flipVertically(image);
            this.imageFlippedHandV = flipHorizontally(flipVertically(image));
        }

        Image image(final boolean flippedH, final boolean flippedV) {
            if (flippedH) {
                return flippedV ? getFlippedHandV() : getFlippedH();
            }
            return flippedV ? getFlippedV() : image.getImage();
        }

        private Image getFlippedV() {
            if (isNull(imageFlippedV)) {
                imageFlippedV = flipVertically(image.getImage());
            }
            return imageFlippedV;
        }

        private Image getFlippedH() {
            if (isNull(imageFlippedH)) {
                imageFlippedH = flipHorizontally(image.getImage());
            }
            return imageFlippedH;
        }

        private Image getFlippedHandV() {
            if (isNull(imageFlippedHandV)) {
                imageFlippedHandV = flipHorizontally(flipVertically(image.getImage()));
            }
            return imageFlippedHandV;
        }

    }

    /**
     * Returns an invisible {@link Frame}.
     */
    public static Frame invisible() {
        return INVISIBLE;
    }

    public Frame(final Image image) {
        this(image, Duration.none());
    }

    public Frame(final Image image, final Duration duration) {
        this.imageContainer = new ImageContainer(image);
        this.duration = duration;
    }

    public Image image() {
        return image(false, false);
    }

    public Image image(final boolean flippedH, final boolean flippedV) {
        return imageContainer.image(flippedH, flippedV);
    }

    public Duration duration() {
        return duration;
    }

    /**
     * Returns the size of the frames {@link #image()}.
     */
    public Dimension size() {
        return Dimension.of(imageContainer.image.getIconWidth(), imageContainer.image.getIconHeight());
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
     * @see #colorAt(Dimension)
     */
    public Color colorAt(final int x, final int y) {
        final Image image = image();
        if (x < 0 || x > image.getWidth(null) || y < 0 || y > image.getHeight(null)) {
            throw new IllegalArgumentException(format("Position is out of bounds: %d:%d", x, y));
        }
        final BufferedImage bufferedImage = toBufferedImage(image);
        final int rgb = bufferedImage.getRGB(x, y);
        final java.awt.Color awtColor = new java.awt.Color(rgb, true);
        return AwtMapper.toColor(awtColor);
    }

    /**
     * Returns a new instance. The new {@link Frame}s old {@link Color} is replaced
     * with a new one. This method is quite slow.
     */
    public Frame replaceColor(final Color oldColor, final Color newColor) {
        final Image oldImage = imageContainer.image.getImage();
        final Image newImage = applyFilter(oldImage, new ReplaceColorFilter(oldColor, newColor));
        return new Frame(newImage);
    }

    // TODO: Test and Javadoc
    // TODO: javadoc and fix cast and test
    // TODO: check no negative scaling
    public Frame scaled(final double scale) {
        return new Frame(scale(image(), scale));
    }

}
