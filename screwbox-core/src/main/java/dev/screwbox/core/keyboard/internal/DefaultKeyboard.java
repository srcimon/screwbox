package dev.screwbox.core.keyboard.internal;

import dev.screwbox.core.Vector;
import dev.screwbox.core.keyboard.DefaultKey;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.keyboard.KeyCombination;
import dev.screwbox.core.keyboard.Keyboard;
import dev.screwbox.core.loop.internal.Updatable;
import dev.screwbox.core.utils.TrippleLatch;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static dev.screwbox.core.keyboard.Key.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class DefaultKeyboard implements Keyboard, Updatable, KeyListener, WindowFocusListener {

    public static final String ALIAS_NULL_MSG = "alias must not be null";

    private final Set<Integer> downKeys = new HashSet<>();
    private final Set<Integer> unreleasedKeys = new HashSet<>();
    private final TrippleLatch<Set<Integer>> pressedKeys = TrippleLatch.of(HashSet::new);
    private final Map<Object, Key> alias = new HashMap<>();

    private StringBuilder textRecorder = null;

    @Override
    public void keyTyped(final KeyEvent event) {
        if (isRecording()) {
            if (BACKSPACE.code() == event.getExtendedKeyCode()) {
                if (!textRecorder.isEmpty()) {
                    textRecorder.deleteCharAt(textRecorder.length() - 1);
                }
            } else {
                textRecorder.append(event.getKeyChar());
            }
        }
    }

    @Override
    public void keyPressed(final KeyEvent event) {
        final int keyCode = event.getKeyCode();
        if (!unreleasedKeys.contains(keyCode)) {
            pressedKeys.active().add(keyCode);
        }
        downKeys.add(keyCode);
        unreleasedKeys.add(keyCode);
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        downKeys.remove(event.getKeyCode());
        unreleasedKeys.remove(event.getKeyCode());
    }

    @Override
    public Keyboard startRecording() {
        textRecorder = new StringBuilder();
        return this;
    }

    @Override
    public boolean isRecording() {
        return nonNull(textRecorder);
    }

    @Override
    public String stopRecording() {
        if (!isRecording()) {
            throw new IllegalStateException("keyboard is not recording");
        }
        final var recordedText = textRecorder.toString();
        textRecorder = null;
        return recordedText;
    }

    @Override
    public Optional<String> recordedText() {
        return isRecording()
                ? Optional.of(textRecorder.toString())
                : Optional.empty();
    }

    @Override
    public boolean isDown(final Key key) {
        return downKeys.contains(key.code());
    }

    @Override
    public boolean isDown(final KeyCombination combination) {
        for (final Key key : combination.keys()) {
            if (!isDown(key)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isPressed(final Key key) {
        return pressedKeys.inactive().contains(key.code());
    }

    @Override
    public boolean isDown(Enum<?> alias) {
        requireNonNull(alias, ALIAS_NULL_MSG);
        return isDown(forceKeyForAlias(alias));
    }

    @Override
    public Keyboard bindAlias(final Enum<?> alias, final Key key) {
        requireNonNull(alias, ALIAS_NULL_MSG);
        requireNonNull(key, "key must not be null");
        this.alias.put(alias, key);
        return this;
    }

    @Override
    public boolean isPressed(final Enum<?> alias) {
        requireNonNull(alias, ALIAS_NULL_MSG);
        return isPressed(forceKeyForAlias(alias));
    }


    @Override
    public List<Key> pressedKeys() {
        return mapCodes(pressedKeys.inactive());
    }

    @Override
    public List<Key> downKeys() {
        return mapCodes(downKeys);
    }

    @Override
    public boolean isAnyKeyDown() {
        return !downKeys.isEmpty();
    }

    @Override
    public void update() {
        pressedKeys.backupInactive().clear();
        pressedKeys.toggle();
    }

    @Override
    public Vector wsadMovement(final double length) {
        return Vector.of(
                        valueOfHighLow(A, D),
                        valueOfHighLow(W, S))
                .length(length);
    }

    @Override
    public Vector arrowKeysMovement(final double length) {
        return Vector.of(
                        valueOfHighLow(ARROW_LEFT, ARROW_RIGHT),
                        valueOfHighLow(ARROW_UP, ARROW_DOWN))
                .length(length);
    }

    @Override
    public boolean isAnyKeyPressed() {
        return !pressedKeys.inactive().isEmpty();
    }

    private double valueOfHighLow(final Key low, final Key high) {
        if (isDown(low)) {
            return isDown(high) ? 0 : -1;
        }
        return isDown(high) ? 1 : 0;
    }

    private List<Key> mapCodes(final Set<Integer> codes) {
        final List<Key> keys = new ArrayList<>();
        for (final var code : new ArrayList<>(codes)) {
            Key.fromCode(code).ifPresent(keys::add);
        }
        return keys;
    }

    @Override
    public Optional<Key> getKeyForAlias(final Enum<?> alias) {
        final var key = this.alias.get(alias);
        if (isNull(key)) {
            final DefaultKey annotation = getDefaultKeyAnnotation(alias);
            if (nonNull(annotation)) {
                bindAlias(alias, annotation.value());
                return getKeyForAlias(alias);
            }
            return Optional.empty();
        }
        return Optional.of(key);
    }

    private DefaultKey getDefaultKeyAnnotation(final Enum<?> alias) {
        try {
            return alias.getClass().getField(alias.name()).getAnnotation(DefaultKey.class);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("could not find field", e);
        }
    }

    private Key forceKeyForAlias(final Enum<?> alias) {
        return getKeyForAlias(alias).orElseThrow(() -> new IllegalStateException("missing key binding for " + alias.getClass().getSimpleName() + "." + alias.name()));
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        // nop
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        downKeys.clear();
        unreleasedKeys.clear();
    }
}
