package dev.screwbox.core.graphics;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.utils.Resources;
import dev.screwbox.core.utils.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

//TODO blogpost
//TODO guide
//TODO fix document reference for utils
//TODO redesign interface between Converter Tile and AutoTile -> current interface sucks
//TODO move to own package
public class AutoTile {

    public enum Template {
        TEMPLATE_2X2(1, Mask::index2x2, "assets/autotiles/template_2x2.properties"),
        TEMPLATE_3X3(3, Mask::index3x3, "assets/autotiles/template_3x3.properties");

        private final int aspectRatio;
        private final Function<Mask, Integer> indexFunction;
        private final String mappingProperties;

        Template(final int aspectRatio, final Function<Mask, Integer> indexFunction, String mappingProperties) {
            this.aspectRatio = aspectRatio;
            this.indexFunction = indexFunction;
            this.mappingProperties = mappingProperties;

        }
    }

    public static Asset<AutoTile> assetFromSpriteSheet(final String fileName, final Template template) {
        return Asset.asset(() -> AutoTile.fromSpriteSheet(fileName, template));
    }

    public static AutoTile fromSpriteSheet(final String fileName, final Template template) {
        final var frame = Frame.fromFile(fileName);
        return new AutoTile(frame, template);
    }

    private final Map<Integer, Sprite> tileset = new HashMap<>();
    private final Template template;

    private AutoTile(final Frame frame, Template template) {
        final int aspectRatio = frame.width() / frame.height();
        this.template = template;
        Validate.isTrue(() -> aspectRatio == template.aspectRatio, "aspect ratio of image doesn't match template");//TODO BETTER
        int tileWidth = frame.height() / 4;

        Map<Integer, Offset> mappings = new HashMap<>();
        for (final var entry : Resources.loadProperties(template.mappingProperties).entrySet()) {
            var xy = entry.getValue().split(",");
            mappings.put(Integer.parseInt(entry.getKey()), Offset.at(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
        }

        for (final var mapping : mappings.entrySet()) {
            Offset value = mapping.getValue();
            tileset.put(mapping.getKey(), new Sprite(frame.extractArea(Offset.at(value.x() * tileWidth, value.y() * tileWidth), Size.square(tileWidth))));
        }
    }

    public Sprite findSprite(final Mask mask) {
        final int index = template.indexFunction.apply(mask);
        return tileset.get(index);
    }

    public static Mask createMask(final Offset tileOffset, Predicate<Offset> isNeighbour) {
        Objects.requireNonNull(tileOffset, "tile offset must not be null");
        return new Mask(
                isNeighbour.test(tileOffset.add(0, -1)),
                isNeighbour.test(tileOffset.add(1, -1)),
                isNeighbour.test(tileOffset.add(1, 0)),
                isNeighbour.test(tileOffset.add(1, 1)),
                isNeighbour.test(tileOffset.add(0, 1)),
                isNeighbour.test(tileOffset.add(-1, 1)),
                isNeighbour.test(tileOffset.add(-1, 0)),
                isNeighbour.test(tileOffset.add(-1, -1)));
    }

    public record Mask(boolean north, boolean northEast, boolean east, boolean southEast,
                       boolean south, boolean southWest, boolean west, boolean northWest) {

        public int index2x2() {
            return toInt(north)
                   + toInt(east) * 2
                   + toInt(south) * 4
                   + toInt(west) * 8;
        }

        public int index3x3() {
            return toInt(north)
                   + toInt(northEast && north && east) * 2
                   + toInt(east) * 4
                   + toInt(southEast && east && south) * 8
                   + toInt(south) * 16
                   + toInt(southWest && south && west) * 32
                   + toInt(west) * 64
                   + toInt(northWest && west && north) * 128;
        }

        private int toInt(boolean value) {
            return value ? 1 : 0;
        }

        @Override
        public String toString() {
            return String.valueOf(index2x2());
        }
    }
}
