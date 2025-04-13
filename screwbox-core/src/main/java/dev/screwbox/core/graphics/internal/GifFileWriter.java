package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Duration;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class GifFileWriter implements Closeable {

    private final ImageOutputStream outputStream;
    private final ImageWriter imageWriter;

    public GifFileWriter(final String fileName) {
        try {
            outputStream = new FileImageOutputStream(new File(fileName));
            imageWriter = ImageIO.getImageWritersBySuffix("gif").next();
            imageWriter.setOutput(outputStream);
            imageWriter.prepareWriteSequence(null);
        } catch (IOException e) {
            throw new IllegalStateException("error opening gif output stream", e);
        }
    }

    public void addImage(final BufferedImage image, final Duration duration) {
        try {
            final var metadata = createMetadata(duration);
            final var gifImage = new IIOImage(image, null, metadata);
            imageWriter.writeToSequence(gifImage, imageWriter.getDefaultWriteParam());
        } catch (IOException e) {
            throw new IllegalStateException("error adding image to gif file", e);
        }
    }

    private IIOMetadata createMetadata(final Duration duration) throws IOException {
        final var imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_ARGB);
        final var metadata = imageWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriter.getDefaultWriteParam());
        final var root = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());
        root.appendChild(createImageControlUsingDelay(duration));
        root.appendChild(createAppExtension());
        metadata.setFromTree(metadata.getNativeMetadataFormatName(), root);
        return metadata;
    }

    private IIOMetadataNode createAppExtension() {
        final var appExtensionNetscape = new IIOMetadataNode("ApplicationExtension");
        appExtensionNetscape.setAttribute("applicationID", "NETSCAPE");
        appExtensionNetscape.setAttribute("authenticationCode", "2.0");
        appExtensionNetscape.setUserObject(new byte[]{0x1, (byte) (0), (byte) ((0) & 0xFF)});//loop forever

        final var appExtension = new IIOMetadataNode("ApplicationExtensions");
        appExtension.appendChild(appExtensionNetscape);
        return appExtension;
    }

    private IIOMetadataNode createImageControlUsingDelay(final Duration duration) {
        final var controlExtension = new IIOMetadataNode("GraphicControlExtension");
        controlExtension.setAttribute("disposalMethod", "restoreToBackgroundColor");
        controlExtension.setAttribute("userInputFlag", "FALSE");
        controlExtension.setAttribute("transparentColorFlag", "FALSE");
        final int delayInHundredsOfSeconds = Math.max((int) duration.milliseconds() / 10, 1);
        controlExtension.setAttribute("delayTime", Integer.toString(delayInHundredsOfSeconds));
        controlExtension.setAttribute("transparentColorIndex", "0");
        return controlExtension;
    }

    @Override
    public void close() {
        try {
            imageWriter.endWriteSequence();
            outputStream.close();
        } catch (IOException e) {
            throw new IllegalStateException("error closing gif output stream", e);
        }
    }

}
