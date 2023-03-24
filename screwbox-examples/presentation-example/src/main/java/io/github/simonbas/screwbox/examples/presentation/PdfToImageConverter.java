package io.github.simonbas.screwbox.examples.presentation;

import io.github.simonbas.screwbox.core.graphics.internal.ImageUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfToImageConverter {

    public static List<Image> convertPdfToImage(File file, int height) {
        List<Image> images = new ArrayList<>();
        try (PDDocument document = PDDocument.load(file)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 80, ImageType.RGB);
                double scale = (double) height / bim.getHeight();
                images.add(ImageUtil.scale(bim, scale));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return images;
    }
}
