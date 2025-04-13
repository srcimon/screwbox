package dev.screwbox.core.graphics;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThatNoException;

class ShaderBundleTest {

    @ParameterizedTest
    @EnumSource(ShaderBundle.class)
    void verifyAllShadersAreValid(ShaderBundle shader) {
        assertThatNoException().isThrownBy(shader::get);
    }

}
