package io.github.srcimon.screwbox.core.keyboard;

/**
 * A key on your {@link Keyboard}.
 */
public enum Key {

    BACKSPACE(8),
    ENTER(10),
    SHIFT_LEFT(16),
    CONTROL(17),
    ESCAPE(27),
    SPACE(32),
    ARROW_LEFT(37),
    ARROW_UP(38),
    ARROW_RIGHT(39),
    ARROW_DOWN(40),
    NUMBER_1(49),
    NUMBER_2(50),
    NUMBER_3(51),
    NUMBER_4(52),
    NUMBER_5(54),
    NUMBER_6(55),
    A(65),
    B(66),
    C(67),
    D(68),
    E(69),
    F(70),
    G(71),
    H(72),
    I(73),
    J(74),
    K(75),
    L(76),
    M(77),
    O(79),
    P(80),
    Q(81);

    private final int keyCode;

    Key(final int keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * The corresponding key code of the key.
     */
    public int code() {
        return keyCode;
    }

}
