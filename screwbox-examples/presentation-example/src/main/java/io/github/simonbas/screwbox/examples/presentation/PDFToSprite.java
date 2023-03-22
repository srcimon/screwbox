package io.github.simonbas.screwbox.examples.presentation;

import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.core.graphics.internal.ImageUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class PDFToSprite {

    public static List<Sprite> fromPdf(File file) {
        List<Sprite> sprites = new ArrayList<>();
        try {
            PDDocument document = null;
            document = PDDocument.load(file);

            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 80, ImageType.RGB);
                double scale = 800.0 / bim.getHeight();
                sprites.add(Sprite.fromImage(ImageUtil.scale(bim, scale)));
            }
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sprites;
    }
}
