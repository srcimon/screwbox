package dev.screwbox.core.generation;

import dev.screwbox.core.graphics.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AutoTileTest {

    @Test
    void createMask_tileOffsetNull_throwsException() {
        assertThatThrownBy(() -> AutoTile.createMask(null, offset -> true))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("tile offset must not be null");
    }

    @Test
    void index3x3_northNeighbourIsConnected_isFour() {
        var mask = AutoTile.createMask(Offset.at(10, 10), offset -> offset.equals(Offset.at(11, 10)));
        assertThat(mask.index3x3()).isEqualTo(4);
    }

    @Test
    void index3x3_noNeighbourIsConnected_isZero() {
        var mask = AutoTile.createMask(Offset.at(10, 10), offset -> false);

        assertThat(mask.index3x3()).isZero();
    }

    @Test
    void index3x3_allNeighboursAreConnected_isMaxIndex() {
        var mask = AutoTile.createMask(Offset.at(10, 10), offset -> true);

        assertThat(mask.index3x3()).isEqualTo(255);
    }

}
