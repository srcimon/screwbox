package io.github.simonbas.screwbox.core.graphics;

import java.io.Serial;
import java.io.Serializable;

public class Dimension implements Serializable, Comparable<Dimension> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int width;
    private final int height;

    /**
     * Returns a new instance of {@link Dimension} with the given {@link #width()}
     * and {@link #height()}.
     */
    public static Dimension of(final int width, final int height) {
        return new Dimension(width, height);
    }

    /**
     * Returns a new instance of {@link Dimension} with the given {@link #width()}
     * and {@link #height()}. Values of {@link #width()} and {@link #height()} are
     * rounded.
     */
    public static Dimension of(final double width, final double height) {
        return of((int) width, (int) height);
    }

    /**
     * Returns a new instance of {@link Dimension} with the given {@link #width()}
     * as {@link #width()} and {@link #height()}.
     */
    public static Dimension square(int sideLength) {
        return of(sideLength, sideLength);
    }

    private Dimension(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * The width of this {@link Dimension}.
     */
    public int width() {
        return width;
    }

    /**
     * The height of this {@link Dimension}.
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
        Dimension other = (Dimension) obj;
        if (height != other.height)
            return false;
        return width == other.width;
    }

    @Override
    public int compareTo(final Dimension other) {
        return Integer.compare(pixelCount(), other.pixelCount());
    }

    public int pixelCount() {
        return width * height;
    }

    public Offset center() {
        return Offset.at(width / 2.0, height / 2.0);
    }

}
