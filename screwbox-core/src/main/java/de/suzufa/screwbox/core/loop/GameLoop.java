package de.suzufa.screwbox.core.loop;

public interface GameLoop {

    GameLoop setTargetFps(int targetFps);

    Metrics metrics();

    int targetFps();
}
