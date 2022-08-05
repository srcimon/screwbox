package de.suzufa.screwbox.core.graphics;

import static de.suzufa.screwbox.core.graphics.internal.ImageConverter.flipHorizontally;
import static de.suzufa.screwbox.core.graphics.internal.ImageConverter.flipVertically;
import static java.util.Objects.isNull;

import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.graphics.internal.ImageConverter;

public final class Frame implements Serializable {

    private static final long serialVersionUID = 1L;

    private final ImageContainer imageContainer;

    private final Duration duration;

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

    public Frame(final Image image) {
        this(image, Duration.zero());
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
    public Color colorAt(final Dimension position) {
        return colorAt(position.width(), position.height());
    }

    /**
     * Returns the {@link Color} of the pixel at the given position.
     * 
     * @throws IllegalArgumentException when position is out of bounds.
     * @see #colorAt(Dimension)
     */
    public Color colorAt(final int x, final int y) {
        return ImageConverter.colorAt(imageContainer.image.getImage(), x, y);
    }

    /**
     * Replaces the given {@link Color} with the new one. Is quite costly.
     */
    public void replaceColor(final Color oldColor, final Color newColor) {
        final Image oldImage = imageContainer.image.getImage();
        final Image newImage = ImageConverter.replaceColor(oldImage, oldColor, newColor);
        imageContainer.image.setImage(newImage);
    }
}
