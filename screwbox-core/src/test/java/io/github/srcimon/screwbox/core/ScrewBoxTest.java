package io.github.srcimon.screwbox.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScrewBoxTest {

    @Test
    void createEngine_missingJava2dJvmOption_throwsException() {
        assertThatThrownBy(ScrewBox::createEngine)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Please run application with JVM Option '-Dsun.java2d.opengl=true' to avoid massive fps drop.");
    }
}
