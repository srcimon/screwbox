package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.graphics.Size;
import org.junit.jupiter.api.Test;

import static io.github.srcimon.screwbox.core.Bounds.$$;
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
    void blocks_twoBlocks_containsTwoBlocks() {
        var map = AsciiMap.fromString("""
                aaabbbb
                """, 8);

        assertThat(map.blocks()).hasSize(2);
        assertThat(map.blocks().getFirst().value()).isEqualTo('a');
        assertThat(map.blocks().getFirst().tiles().size()).isEqualTo(3);
        assertThat(map.blocks().getLast().value()).isEqualTo('b');
        assertThat(map.blocks().getLast().tiles().size()).isEqualTo(4);
        //TODO add checks
    }
}
