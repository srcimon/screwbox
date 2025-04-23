package dev.screwbox.core.utils;

import dev.screwbox.core.graphics.Size;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AsciiMapTest {

    @Test
    void fromString_validString_createsMapWithBoundsContainingAllTiles() {
        var map = AsciiMap.fromString("""
                p
                ########
                """, 8);

        assertThat(map.bounds()).isEqualTo($$(0, 0, 64, 16));
        assertThat(map.tiles())
                .hasSize(9)
                .anyMatch(tile -> tile.equals(new AsciiMap.Tile(Size.square(8), 0, 0, 'p')))
                .anyMatch(tile -> tile.equals(new AsciiMap.Tile(Size.square(8), 4, 1, '#')))
                .allMatch(tile -> map.bounds().contains(tile.bounds()));
    }


    @Test
    void tileAt_noTileAtPosition_isEmpty() {
        var map = AsciiMap.fromString("abc");

        assertThat(map.tileAt(10, 2)).isEmpty();
    }

    @Test
    void tileAt_tileIsPresent_containsTile() {
        var map = AsciiMap.fromString("abc");

        assertThat(map.tileAt(2, 0)).contains(new AsciiMap.Tile(Size.square(16), 2, 0, 'c'));
    }

    @Test
    void fromString_emptyText_hasNoContent() {
        var map = AsciiMap.fromString("");

        assertThat(map.bounds()).isEqualTo($$(0, 0, 0, 0));
        assertThat(map.tiles()).isEmpty();
        assertThat(map.blocks()).isEmpty();
    }

    @Test
    void fromString_stringNull_throwsException() {
        assertThatThrownBy(() -> AsciiMap.fromString(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("map must not be null");
    }

    @Test
    void fromString_sizeZero_throwsException() {
        assertThatThrownBy(() -> AsciiMap.fromString("xy", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("size must be positive");
    }

    @Test
    void blocks_noTwoTilesSame_noBlocks() {
        var map = AsciiMap.fromString("""
                abcdE
                fgHiI
                """);

        assertThat(map.blocks()).isEmpty();
    }

    @Test
    void blocks_fourBlocks_containsFourBlocks() {
        var map = AsciiMap.fromString("""
                aaabbbb
                xxxyyz
                """, 8);

        assertThat(map.blocks()).hasSize(4);

        AsciiMap.Block first = map.blocks().getFirst();
        assertThat(first.value()).isEqualTo('a');
        assertThat(first.tiles()).hasSize(3);
        assertThat(first.bounds()).isEqualTo($$(0, 0, 24, 8));
        assertThat(first.size()).isEqualTo(Size.of(24, 8));

        AsciiMap.Block second = map.blocks().get(1);
        assertThat(second.value()).isEqualTo('b');
        assertThat(second.tiles()).hasSize(4);
        assertThat(second.bounds()).isEqualTo($$(24, 0, 32, 8));

        AsciiMap.Block third = map.blocks().get(2);
        assertThat(third.value()).isEqualTo('x');
        assertThat(third.tiles()).hasSize(3);
        assertThat(third.bounds()).isEqualTo($$(0, 8, 24, 8));

        AsciiMap.Block fourth = map.blocks().getLast();
        assertThat(fourth.value()).isEqualTo('y');
        assertThat(fourth.tiles()).hasSize(2);
        assertThat(fourth.bounds()).isEqualTo($$(24, 8, 16, 8));
    }

    @Test
    void blocks_multilineBlock_containsMultilineBlock() {
        var map = AsciiMap.fromString("""
                
                
                aaabbbbbbbbbbbbbbbbbb
                aaabbb
                """, 8);

        assertThat(map.blocks()).hasSize(3);
        assertThat(map.blocks()).anyMatch(block -> block.tiles().size() == 18);
    }

    @Test
    void blocks_multipleBlocks_everyTileIsWithinBlock() {
        var map = AsciiMap.fromString("""
                aaabbbbcccc
                aaabbbccc
                   bbbcc
                """, 8);

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
        var map = AsciiMap.fromString("""
                abc
                abc
                ab
                """, 8);

        assertThat(map.blocks()).hasSize(3);
        assertThat(map.blocks().getFirst().tiles()).hasSize(2);
        assertThat(map.blocks().getFirst().value()).isEqualTo('c');

    }
}
