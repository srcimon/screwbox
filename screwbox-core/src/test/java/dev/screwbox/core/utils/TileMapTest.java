package dev.screwbox.core.utils;

import dev.screwbox.core.graphics.AutoTile;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TileMapTest {

    private static final AutoTile.Mask MASK = AutoTile.createMask(Offset.origin(), offset -> false);

    @Test
    void fromString_validString_createsMapWithBoundsContainingAllTiles() {
        var map = TileMap.fromString("""
                p
                a#c#d#e#
                """, Size.square(8));

        assertThat(map.bounds()).isEqualTo($$(0, 0, 64, 16));
        assertThat(map.tiles())
                .hasSize(9)
                .anyMatch(tile -> tile.equals(new TileMap.Tile<>(Size.square(8), 0, 0, 'p', MASK)))
                .anyMatch(tile -> tile.equals(new TileMap.Tile<>(Size.square(8), 4, 1, 'd', MASK)))
                .allMatch(tile -> map.bounds().contains(tile.bounds()));
    }

    @Test
    void tileAt_noTileAtPosition_isEmpty() {
        var map = TileMap.fromString("abc");

        assertThat(map.tileAt(10, 2)).isEmpty();
    }

    @Test
    void tileAt_tileIsPresent_containsTile() {
        var map = TileMap.fromString("abc");

        final var tile = map.tileAt(2, 0).orElseThrow();

        assertThat(tile.size()).isEqualTo(Size.square(16));
        assertThat(tile.column()).isEqualTo(2);
        assertThat(tile.row()).isZero();
        assertThat(tile.value()).isEqualTo('c');
        assertThat(tile.autoTileMask().index2x2()).isZero();
    }

    @Test
    void fromString_emptyText_hasNoContent() {
        var map = TileMap.fromString("");

        assertThat(map.bounds()).isEqualTo($$(0, 0, 0, 0));
        assertThat(map.tiles()).isEmpty();
        assertThat(map.blocks()).isEmpty();
    }

    @Test
    void values_emptyMap_isEmpty() {
        var map = TileMap.fromString("");

        assertThat(map.values()).isEmpty();
    }

    @Test
    void fromImageFile_validFile_isBuild() {
        var map = TileMap.fromImageFile("utils/TileMap_map.png");
        assertThat(map.tileAt(3,2)).isPresent();

        TileMap.Tile<Color> tile = map.tileAt(3, 2).orElseThrow();
        assertThat(tile.row()).isEqualTo(2);
        assertThat(tile.column()).isEqualTo(3);
        assertThat(tile.autoTileMask()).isNotNull();

        assertThat(map.values()).containsExactlyInAnyOrder(Color.hex("#4300ff"), Color.hex("#c8ff00"), Color.hex("#ff0000"));
    }

    @Test
    void values_populatedMap_returnsDistinctValues() {
        var map = TileMap.fromString("affssa");

        assertThat(map.values()).containsExactlyInAnyOrder('a', 'f', 's');
    }

    @Test
    void fromString_stringNull_throwsException() {
        assertThatThrownBy(() -> TileMap.fromString(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("map must not be null");
    }

    @Test
    void fromString_sizeZero_throwsException() {
        assertThatThrownBy(() -> TileMap.fromString("xy", Size.of(0, 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("tile size must be valid");
    }

    @Test
    void blocks_noTwoTilesSame_noBlocks() {
        var map = TileMap.fromString("""
                abcdE
                fgHiI
                """);

        assertThat(map.blocks()).isEmpty();
    }

    @Test
    void blocks_fourBlocks_containsFourBlocks() {
        var map = TileMap.fromString("""
                aaabbbb
                xxxyyz
                """, Size.square(8));

        assertThat(map.blocks()).hasSize(4);

        var first = map.blocks().getFirst();
        assertThat(first.value()).isEqualTo('a');
        assertThat(first.tiles()).hasSize(3);
        assertThat(first.bounds()).isEqualTo($$(0, 0, 24, 8));
        assertThat(first.size()).isEqualTo(Size.of(24, 8));

        var second = map.blocks().get(1);
        assertThat(second.value()).isEqualTo('b');
        assertThat(second.tiles()).hasSize(4);
        assertThat(second.bounds()).isEqualTo($$(24, 0, 32, 8));

        var third = map.blocks().get(2);
        assertThat(third.value()).isEqualTo('x');
        assertThat(third.tiles()).hasSize(3);
        assertThat(third.bounds()).isEqualTo($$(0, 8, 24, 8));

        var fourth = map.blocks().getLast();
        assertThat(fourth.value()).isEqualTo('y');
        assertThat(fourth.tiles()).hasSize(2);
        assertThat(fourth.bounds()).isEqualTo($$(24, 8, 16, 8));
    }

    @Test
    void blocks_multilineBlock_containsMultilineBlock() {
        var map = TileMap.fromString("""
                
                
                aaabbbbbbbbbbbbbbbbbb
                aaabbb
                """, Size.square(8));

        assertThat(map.blocks()).hasSize(3);
        assertThat(map.blocks()).anyMatch(block -> block.tiles().size() == 18);
    }

    @Test
    void blocks_multipleBlocks_everyTileIsWithinBlock() {
        var map = TileMap.fromString("""
                aaabbbbcccc
                aaabbbccc
                   bbbcc
                """, Size.square(8));

        assertThat(map.blocks()).isNotEmpty();
        for (var block : map.blocks()) {
            assertThat(block.tiles()).isNotEmpty();
            for (var tile : block.tiles()) {
                assertThat(block.bounds().contains(tile.bounds())).isTrue();
            }
        }
    }

    @Test
    void blocks_verticalBlock_alsoGetAdded() {
        var map = TileMap.fromString("""
                abc
                abc
                ab
                """, Size.square(8));

        assertThat(map.blocks()).hasSize(3);
        assertThat(map.blocks().getFirst().tiles()).hasSize(2);
        assertThat(map.blocks().getFirst().value()).isEqualTo('c');
    }

    @Test
    void findSprite_validAutoTile_returnsSpriteForEveryTile() {
        var map = TileMap.fromString("""
                abc
                abc
                ab
                """, Size.square(8));

        for(var tile : map.tiles()) {
            assertThat(tile.findSprite(AutoTileBundle.TEMPLATE_3X3)).isNotNull();
        }
    }
}
