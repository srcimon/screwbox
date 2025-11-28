package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Size;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;

public class ImageStore implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Serial
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(ImageOperations.cloneImage(image), "png", out);
    }

    @Serial
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = ImageIO.read(in);
    }

    private transient Image image;
    private final Size size;

    public ImageStore(final Image image) {
        this.image = image;
        this.size = Size.of(image.getWidth(null), image.getHeight(null));
    }

    public Image image() {
        return image;
    }

    public Size size() {
        return size;
    }
}
