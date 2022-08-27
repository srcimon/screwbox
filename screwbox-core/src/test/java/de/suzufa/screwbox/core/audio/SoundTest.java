package de.suzufa.screwbox.core.audio;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Duration;

class SoundTest {

    @Test
    void willBreak() {
        Duration length = Sound.fromFile("kill.wav").length();
        System.out.println(length.milliseconds());
    }
}
