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
     *
     * @return recorded {@link String}
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
     * Returns {@code true} if the specified {@link Key} is down at the moment.
     *
     * @param key the {@link Key} to check
     */
    boolean isDown(Key key);

    /**
     * Returns {@code true} if the {@link Key} bound to the specified alias is down at the moment.
     * {@link Key Keys} can be bound to alias via {@link #bindAlias(Enum, Key)} or via specifying an {@link DefaultKey} on the
     * alias enumeration.
     *
     * @since 2.10.0
     */
    boolean isDown(Enum<?> alias);

    /**
     * Returns {@code true} if the {@link Key} bound to the specified alias was pressed in this frame.
     * {@link Key Keys} can be bound to alias via {@link #bindAlias(Enum, Key)} or via specifying an {@link DefaultKey} on the
     * alias enumeration.
     *
     * @since 2.10.0
     */
    boolean isPressed(Enum<?> keyBinding);

    /**
     * Binds an alias to a {@link Key}. Allows changing of controlls afterwards. {@link Key Keys} can also be bound via
     * specifying an {@link DefaultKey} on the alias enumeration. Any enumeration can be an alias. Manual binging via this
     * method overrules the default binding via {@link DefaultKey}.
     *
     * @since 2.10.0
     */
    Keyboard bindAlias(Enum<?> alias, Key key);

    /**
     * Returns the {@link Key} currently bound to the specifyied alias. Will be empty if no alias is bound.
     *
     * @since 2.10.0
     */
    Optional<Key> getKeyForAlias(Enum<?> alias);


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
     *
     * @see #arrowKeysMovement(double)
     */
    Vector wsadMovement(double length);

    /**
     * Returns the resulting {@link Vector} of a arrow-keys-movement scheme with the given length.
     *
     * @see #wsadMovement(double)
     */
    Vector arrowKeysMovement(double length);

    /**
     * Returns {@code true} any {@link Key} was typed right now.
     */
    boolean isAnyKeyPressed();

}
