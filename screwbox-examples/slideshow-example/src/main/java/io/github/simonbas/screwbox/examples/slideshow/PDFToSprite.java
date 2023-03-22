package io.github.simonbas.screwbox.examples.slideshow;

import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.core.graphics.internal.ImageUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Deprecated
public class PDFToSprite {

    public static Sprite fromPdf(File file) {

        try {
            PDDocument document = null;
            document = PDDocument.load(file);

            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                double scale = 400.0 / bim.getHeight();
                return Sprite.fromImage(ImageUtil.scale(bim, scale));
            }
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
