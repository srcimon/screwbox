package io.github.srcimon.screwbox.core.keyboard;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

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
    PAGE_DOWN(35),
    PAGE_UP(36),
    ARROW_LEFT(37),
    ARROW_UP(38),
    ARROW_RIGHT(39),
    ARROW_DOWN(40),
    COMMA(44),
    DOT(46),
    NUMBER_0(48),
    NUMBER_1(49),
    NUMBER_2(50),
    NUMBER_3(51),
    NUMBER_4(52),
    NUMBER_5(54),
    NUMBER_6(55),
    NUMBER_7(56),
    NUMBER_8(57),
    NUMBER_9(58),
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
    N(78),
    O(79),
    P(80),
    Q(81),
    R(82),
    S(83),
    T(84),
    U(85),
    V(86),
    W(87),
    X(88),
    Y(89),
    Z(90),
    NUMPAD_0(96),
    NUMPAD_1(97),
    NUMPAD_2(98),
    NUMPAD_3(99),
    NUMPAD_4(100),
    NUMPAD_5(101),
    NUMPAD_6(102),
    NUMPAD_7(103),
    NUMPAD_8(104),
    NUMPAD_9(105),
    F1(112),
    F2(113),
    F3(114),
    F4(115),
    F5(116),
    F6(117),
    F7(118),
    F8(119),
    F9(120),
    F10(121),
    F11(122),
    F12(123),
    AE(16777444),
    OE(16777462),
    UE(16777468);

    private static final Map<Integer, Key> REVERSE_LOOKUP = initializeMouse();

    private static Map<Integer, Key> initializeMouse() {
        return Arrays.stream(Key.values())
                .collect(toMap(Key::code, key -> key, (code, key) -> key));
    }

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

    /**
     * Returns the {@link Key} with the given code. Empty when code is unknown.
     */
    public static Optional<Key> fromCode(final int code) {
        return Optional.ofNullable(REVERSE_LOOKUP.get(code));
    }
}
