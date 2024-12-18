package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationDetailsTest {

    @Test
    void newInstance_validValues_setsValues() {
        var notification = NotificationDetails.text("this is a text")
                .icon(SpriteBundle.BOX_STRIPED)
                .sound(SoundBundle.PLING);

        assertThat(notification.text()).isEqualTo("this is a text");
        assertThat(notification.icon()).contains(SpriteBundle.BOX_STRIPED.get());
        assertThat(notification.sound()).contains(SoundBundle.PLING.get());
    }

    @Test
    void text_textIsEmpty_throwsException() {
        assertThatThrownBy(() -> NotificationDetails.text(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("text must not be empty");
    }

    @Test
    void text_textNull_throwsException() {
        assertThatThrownBy(() -> NotificationDetails.text(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("text must not be null");
    }
}
