package io.github.srcimon.screwbox.core.keyboard.internal;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.keyboard.KeyCombination;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.TrippleLatch;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import static io.github.srcimon.screwbox.core.keyboard.Key.*;

public class DefaultKeyboard implements Keyboard, Updatable, KeyListener {

    private final Set<Integer> downKeys = new HashSet<>();
    private final Set<Integer> unreleasedKeys = new HashSet<>();
    private final TrippleLatch<Set<Integer>> pressedKeys = TrippleLatch.of(
            new HashSet<>(), new HashSet<>(), new HashSet<>());

    @Override
    public void keyTyped(final KeyEvent event) {
        // not used
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

    private double valueOfHighLow(final Key low, final Key high) {
        if (isDown(low)) {
            return isDown(high) ? 0 : -1;
        }
        return isDown(high) ? 1 : 0;
    }

    private List<Key> mapCodes(final Set<Integer> codes) {
        List<Key> keys = new ArrayList<>();
        for (final var code : new ArrayList<>(codes)) {
            Optional<Key> key = Key.fromCode(code);
            key.ifPresent(keys::add);
        }
        return keys;
    }
}
