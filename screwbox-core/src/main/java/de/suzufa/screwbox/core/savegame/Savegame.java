package de.suzufa.screwbox.core.savegame;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.scenes.Scene;

/**
 * Provides methods to save and load the game state.
 */
public interface Savegame {

//TODO:  TEST
    /**
     * Creates a savegame file with the given name. The savegame contains all
     * {@link Entities} attached to the {@link Engine#entities()}.
     * 
     * @see #create(String, Class)
     */
    Savegame create(String name);

//TODO:  TEST
    /**
     * Creates a savegame file with the given name. The savegame contains all
     * {@link Entities} attached to the given {@link Scene}.
     *
     * @see #create(String)
     */
    Savegame create(String name, Class<? extends Scene> scene);

    // TODO: JAVADOC AND TEST
    Savegame load(String name);

    // TODO: JAVADOC AND TEST
    Savegame load(String name, Class<? extends Scene> scene);

    // TODO: JAVADOC AND TEST
    boolean exists(String name);

}
