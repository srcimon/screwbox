package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Pixelfont;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.Cache;

import java.util.Arrays;

import static io.github.srcimon.screwbox.core.graphics.Color.BLACK;
import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;


/**
 * An {@link AssetBundle} for {@link Pixelfont}s shipped with the {@link ScrewBox} game engine. Default {@link Color}
 * is {@link Color#WHITE}.
 */
public enum FontsBundle implements AssetBundle<Pixelfont> {

    BOLDZILLA(Asset.asset(() -> loadFont("assets/pixelfonts/BOLDZILLA.png", Size.square(8),
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' ', '.', ',', ':', '!', '?', '-'))),

    SKINNY_SANS(Asset.asset(() -> loadFontCropped("assets/pixelfonts/SKINNY_SANS.png", Size.square(8),
            'A', 'a', 'B', 'b', 'C', 'c', 'D', 'd', 'E', 'e', 'F', 'f', 'G', 'g', 'H', 'h', 'I', 'i', 'J', 'j', 'K', 'k', 'L', 'l', 'M', 'm', 'N', 'n', 'O', 'o', 'P', 'p', 'Q', 'q', 'R', 'r', 'S', 's', 'T', 't', 'U', 'u', 'V', 'v', 'W', 'w', 'X', 'x', 'Y', 'y', 'Z', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' ', '.', ',', ':', '!', '?', '-', '(', ')', '[', ']')));

    private final Cache<Color, Asset<Pixelfont>> cache = new Cache<>();
    private final Asset<Pixelfont> asset;

    FontsBundle(final Asset<Pixelfont> supplier) {
        this.asset = supplier;
    }

    /**
     * Returns a colored {@link Asset} version of the {@link Pixelfont}.
     */
    public Asset<Pixelfont> customColor(final Color color) {
        return cache.getOrElse(color, () -> Asset.asset(() -> asset.get().replaceColor(WHITE, color)));
    }

    /**
     * Returns a colored version of the {@link Pixelfont}.
     */
    public Pixelfont getCustomColor(final Color color) {
        return customColor(color).get();
    }

    @Override
    public Asset<Pixelfont> asset() {
        return asset;
    }

    private static Pixelfont loadFont(final String resouce, final Size size, final Character... characters) {
        final Pixelfont font = new Pixelfont();
        final var sprites = Sprite.multipleFromFile(resouce, size);
        font.addCharacters(Arrays.asList(characters), sprites);
        return font.replaceColor(BLACK, WHITE);
    }

    private static Pixelfont loadFontCropped(final String resouce, final Size size, final Character... characters) {
        final Pixelfont font = new Pixelfont();
        final var sprites = Sprite.multipleFromFile(resouce, size);
        font.addCharacters(Arrays.asList(characters), sprites.stream().map(Sprite::cropHorizontal).toList());
        return font.replaceColor(BLACK, WHITE);
    }
}
