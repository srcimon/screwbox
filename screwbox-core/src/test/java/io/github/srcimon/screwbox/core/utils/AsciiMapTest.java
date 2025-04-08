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
    void blocks_fourBlocks_containsFourBlocks() {
        var map = AsciiMap.fromString("""
                aaabbbb
                xxxyyz
                """, 8);

        assertThat(map.blocks()).hasSize(4);
//TODO list contains / when all properties set
        AsciiMap.Block first = map.blocks().getFirst();
        assertThat(first.value()).isEqualTo('a');
        assertThat(first.tiles().size()).isEqualTo(3);

        AsciiMap.Block second = map.blocks().get(1);
        assertThat(second.value()).isEqualTo('b');
        assertThat(second.tiles().size()).isEqualTo(4);

        AsciiMap.Block third = map.blocks().get(2);
        assertThat(third.value()).isEqualTo('x');
        assertThat(third.tiles().size()).isEqualTo(3);

        AsciiMap.Block fourth = map.blocks().getLast();
        assertThat(fourth.value()).isEqualTo('y');
        assertThat(fourth.tiles().size()).isEqualTo(2);
    }
}
