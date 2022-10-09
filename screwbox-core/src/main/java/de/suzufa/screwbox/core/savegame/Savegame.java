package de.suzufa.screwbox.core.savegame;

import de.suzufa.screwbox.core.scenes.Scene;

//TODO:javadoc
public interface Savegame {

//TODO: JAVADOC AND TEST
    Savegame create(String name);

//TODO: JAVADOC AND TEST
    Savegame create(String name, Class<? extends Scene> scene);

    // TODO: JAVADOC AND TEST
    Savegame load(String name);

    // TODO: JAVADOC AND TEST
    Savegame load(String name, Class<? extends Scene> scene);

    // TODO: JAVADOC AND TEST
    boolean exists(String name);

    // TODO: setFolder(String folder)

}
