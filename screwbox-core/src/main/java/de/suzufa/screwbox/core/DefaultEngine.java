package de.suzufa.screwbox.core;

import static java.lang.String.format;

import de.suzufa.screwbox.core.audio.Audio;
import de.suzufa.screwbox.core.audio.internal.DefaultAudio;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.graphics.Graphics;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.internal.DefaultGraphics;
import de.suzufa.screwbox.core.graphics.internal.DefaultWindow;
import de.suzufa.screwbox.core.graphics.internal.WindowFrame;
import de.suzufa.screwbox.core.keyboard.Keyboard;
import de.suzufa.screwbox.core.keyboard.internal.DefaultKeyboard;
import de.suzufa.screwbox.core.log.ConsoleLoggingAdapter;
import de.suzufa.screwbox.core.log.Log;
import de.suzufa.screwbox.core.log.internal.DefaultLog;
import de.suzufa.screwbox.core.loop.GameLoop;
import de.suzufa.screwbox.core.loop.internal.DefaultGameLoop;
import de.suzufa.screwbox.core.loop.internal.DefaultMetrics;
import de.suzufa.screwbox.core.mouse.Mouse;
import de.suzufa.screwbox.core.mouse.internal.DefaultMouse;
import de.suzufa.screwbox.core.physics.Physics;
import de.suzufa.screwbox.core.physics.internal.DefaultPhysics;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.core.scenes.Scenes;
import de.suzufa.screwbox.core.scenes.internal.DefaultScenes;
import de.suzufa.screwbox.core.ui.Ui;
import de.suzufa.screwbox.core.ui.internal.DefaultUi;

class DefaultEngine implements Engine {

    private final DefaultGameLoop gameLoop;
    private final DefaultGraphics graphics;
    private final DefaultKeyboard keyboard;
    private final DefaultScenes scenes;
    private final DefaultAudio audio;
    private final DefaultPhysics physics;
    private final DefaultMouse mouse;
    private final DefaultUi ui;
    private final DefaultLog log;

    DefaultEngine() {
        final WindowFrame frame = new WindowFrame(this);
        final DefaultMetrics metrics = new DefaultMetrics();
        final GraphicsConfiguration configuration = new GraphicsConfiguration();
        final DefaultWindow window = new DefaultWindow(frame, configuration);
        audio = new DefaultAudio();
        graphics = new DefaultGraphics(configuration, window);
        scenes = new DefaultScenes(this);
        keyboard = new DefaultKeyboard();
        ui = new DefaultUi(this);
        mouse = new DefaultMouse(graphics);
        gameLoop = new DefaultGameLoop(metrics, keyboard, mouse, ui, graphics, scenes);
        physics = new DefaultPhysics(this);
        log = new DefaultLog(new ConsoleLoggingAdapter());
        frame.addMouseListener(mouse);
        frame.addMouseMotionListener(mouse);
        frame.addKeyListener(keyboard);
    }

    @Override
    public void start(final Class<? extends Scene> sceneClass) {
        scenes().switchTo(sceneClass);
        start();
    }

    @Override
    public void start() {
        if (scenes.sceneCount() == 0) {
            throw new IllegalStateException("no scene present");
        }
        log.info("engine started");
        try {
            graphics.window().open();
            gameLoop.start();
        } catch (final RuntimeException e) {
            audio.shutdown();
            graphics.window().close();
            throw e;
        }
    }

    @Override
    public void stop() {
        var frames = loop().metrics().frameNumber();

        log.info(format("engine stopped (total frames: %,d)", frames));
        ui.closeMenu();
        gameLoop.stop();
        graphics.window().close();
        audio.shutdown();
    }

    @Override
    public EntityEngine entityEngine() {
        return scenes.activeEntityEngine();
    }

    @Override
    public GameLoop loop() {
        return gameLoop;
    }

    @Override
    public Graphics graphics() {
        return graphics;
    }

    @Override
    public Keyboard keyboard() {
        return keyboard;
    }

    @Override
    public Scenes scenes() {
        return scenes;
    }

    @Override
    public Audio audio() {
        return audio;
    }

    @Override
    public Physics physics() {
        return physics;
    }

    @Override
    public Mouse mouse() {
        return mouse;
    }

    @Override
    public Ui ui() {
        return ui;
    }

    @Override
    public Log log() {
        return log;
    }

}
