package io.github.srcimon.screwbox.core.keyboard;

import io.github.srcimon.screwbox.core.Vector;

import java.util.List;
import java.util.Optional;

/**
 * Use keyboard input to controll your game.
 */
public interface Keyboard {

    /**
     * Starts recording all keyboard input to a {@link String}. Will reset {@link #recordedText()} if already
     * started recording.
     *
     * @since 2.6.0
     */
    Keyboard startRecording();

    /**
     * Returns {@code true} if {@link Keyboard} is currently recording input.
     *
     * @since 2.6.0
     */
    boolean isRecording();

    /**
     * Stops recording input an returns the recorded {@link String}.
     * @return recorded {@link String}
     *
     * @since 2.6.0
     */
    String stopRecording();

    /**
     * Returns any recorded text. Will be empty when not recording.
     *
     * @since 2.6.0
     */
    Optional<String> recordedText();

    /**
     * Returns {@code true} if the given {@link Key} is down at the moment.
     *
     * @param key the {@link Key} to check
     */
    boolean isDown(Key key);

    /**
     * Returns {@code true} if the given {@link KeyCombination} is down at the moment.
     *
     * @param combination the {@link KeyCombination} to check
     */
    boolean isDown(KeyCombination combination);

    /**
     * Returns {@code true} if any key is down at the moment.
     */
    boolean isAnyKeyDown();

    /**
     * Returns {@code true} if the specified {@link Key} was typed right now.
     *
     * @param key the {@link Key} to check
     */
    boolean isPressed(Key key);

    /**
     * Returns all pressed {@link Key}s.
     */
    List<Key> pressedKeys();

    /**
     * Returns all currently down {@link Key}s.
     */
    List<Key> downKeys();

    /**
     * Returns the resulting {@link Vector} of a WSAD-movement scheme with the given length.
     * @see #arrowKeysMovement(double)
     */
    Vector wsadMovement(double length);

    /**
     * Returns the resulting {@link Vector} of a arrow-keys-movement scheme with the given length.
     * @see #wsadMovement(double)
     */
    Vector arrowKeysMovement(double length);

    /**
     * Returns {@code true} any {@link Key} was typed right now.
     */
    boolean isAnyKeyPressed();
}
