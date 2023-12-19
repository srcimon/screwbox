package io.github.srcimon.screwbox.core.graphics;

import java.io.Serial;
import java.io.Serializable;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Size implements Serializable, Comparable<Size> {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Size NONE = Size.of(0, 0);
    private final int width;
    private final int height;

    /**
     * Returns a {@link Size} without any extends.
     */
    public static Size none() {
        return NONE;
    }

    /**
     * Returns a new instance of {@link Size} with the given {@link #width()}
     * and {@link #height()}.
     */
    public static Size of(final int width, final int height) {
        return new Size(width, height);
    }

    /**
     * Returns a new instance of {@link Size} with the given {@link #width()}
     * and {@link #height()}. Values of {@link #width()} and {@link #height()} are
     * rounded.
     */
    public static Size of(final double width, final double height) {
        return of((int) width, (int) height);
    }

    /**
     * Returns a new instance of {@link Size} with the given {@link #width()}
     * as {@link #width()} and {@link #height()}.
     */
    public static Size square(int sideLength) {
        return of(sideLength, sideLength);
    }

    /**
     * Returns a new instance of {@link Size} defined by two edges.
     */
    public static Size definedBy(Offset from, Offset to) {
        final int width = max(from.x(), to.x()) - min(from.x(), to.x());
        final int height = max(from.y(), to.y()) - min(from.y(), to.y());
        return Size.of(width, height);
    }

    private Size(final int width, final int height) {
        if (width < 0) {
            throw new IllegalArgumentException("width must be positive");
        }
        if (height < 0) {
            throw new IllegalArgumentException("height must be positive");
        }
        this.width = width;
        this.height = height;
    }

    /**
     * The width of this {@link Size}.
     */
    public int width() {
        return width;
    }

    /**
     * The height of this {@link Size}.
     */
    public int height() {
        return height;
    }

    @Override
    public String toString() {
        return "Dimension [width=" + width + ", height=" + height + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + width;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Size other = (Size) obj;
        if (height != other.height)
            return false;
        return width == other.width;
    }

    @Override
    public int compareTo(final Size other) {
        return Integer.compare(pixelCount(), other.pixelCount());
    }

    public int pixelCount() {
        return width * height;
    }

    public Offset center() {
        return Offset.at(width / 2.0, height / 2.0);
    }

}
