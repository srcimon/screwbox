package io.github.srcimon.screwbox.core.keyboard.internal;

import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.keyboard.KeyCombination;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.TrippleLatch;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class DefaultKeyboard implements Keyboard, Updatable, KeyListener {

    private final Set<Integer> pressedKeys = new HashSet<>();

    private final TrippleLatch<Set<Integer>> justPressedKeys = TrippleLatch.of(
            new HashSet<>(), new HashSet<>(), new HashSet<>());

    @Override
    public void keyTyped(final KeyEvent event) {
        // not used
    }

    @Override
    public void keyPressed(final KeyEvent event) {
        final int keyCode = event.getKeyCode();
        justPressedKeys.active().add(keyCode);
        pressedKeys.add(keyCode);
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        pressedKeys.remove(event.getKeyCode());
    }

    @Override
    public boolean isDown(final Key key) {
        return pressedKeys.contains(key.code());
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
    public boolean justPressed(final Key key) {
        return justPressedKeys.inactive().contains(key.code());
    }

    @Override
    public boolean isAnyKeyDown() {
        return !pressedKeys.isEmpty();
    }

    @Override
    public void update() {
        justPressedKeys.backupInactive().clear();
        justPressedKeys.toggle();
    }

}
