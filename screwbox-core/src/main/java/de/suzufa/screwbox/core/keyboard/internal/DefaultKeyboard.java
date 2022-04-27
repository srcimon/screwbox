package de.suzufa.screwbox.core.keyboard.internal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.core.keyboard.KeyCombination;
import de.suzufa.screwbox.core.keyboard.Keyboard;
import de.suzufa.screwbox.core.utils.internal.Swappable;

public class DefaultKeyboard implements Keyboard, KeyListener {

    private final Set<Integer> pressedKeys = new HashSet<>();
    private final Swappable<Set<Integer>> justPressedKeys = Swappable.of(new HashSet<>(), new HashSet<>());

    @Override
    public void keyTyped(final KeyEvent event) {
        // not used
    }

    @Override
    public void keyPressed(final KeyEvent event) {
        pressedKeys.add(event.getKeyCode());
        justPressedKeys.backup().add(event.getKeyCode());
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
        for (final Key key : combination.getKeys()) {
            if (!isDown(key)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean justPressed(final Key key) {
        return justPressedKeys.primary().contains(key.code());
    }

    @Override
    public boolean anyKeyIsDown() {
        return !pressedKeys.isEmpty();
    }

    public void update() {
        justPressedKeys.primary().clear();
        justPressedKeys.swap();
    }

}
