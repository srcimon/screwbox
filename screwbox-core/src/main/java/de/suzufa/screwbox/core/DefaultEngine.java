package de.suzufa.screwbox.core;

import static java.lang.String.format;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.ExecutorService;

import de.suzufa.screwbox.core.assets.Assets;
import de.suzufa.screwbox.core.assets.internal.DefaultAssets;
import de.suzufa.screwbox.core.async.Async;
import de.suzufa.screwbox.core.async.internal.DefaultAsync;
import de.suzufa.screwbox.core.audio.Audio;
import de.suzufa.screwbox.core.audio.internal.AudioAdapter;
import de.suzufa.screwbox.core.audio.internal.DefaultAudio;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.graphics.Graphics;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.internal.DefaultGraphics;
import de.suzufa.screwbox.core.graphics.internal.DefaultLight;
import de.suzufa.screwbox.core.graphics.internal.DefaultScreen;
import de.suzufa.screwbox.core.graphics.internal.DefaultWindow;
import de.suzufa.screwbox.core.graphics.internal.DefaultWorld;
import de.suzufa.screwbox.core.graphics.internal.FrameAdapter;
import de.suzufa.screwbox.core.graphics.internal.StandbyRenderer;
import de.suzufa.screwbox.core.graphics.internal.WindowFrame;
import de.suzufa.screwbox.core.keyboard.Keyboard;
import de.suzufa.screwbox.core.keyboard.internal.DefaultKeyboard;
import de.suzufa.screwbox.core.log.ConsoleLoggingAdapter;
import de.suzufa.screwbox.core.log.Log;
import de.suzufa.screwbox.core.log.internal.DefaultLog;
import de.suzufa.screwbox.core.loop.Loop;
import de.suzufa.screwbox.core.loop.internal.DefaultLoop;
import de.suzufa.screwbox.core.loop.internal.Updatable;
import de.suzufa.screwbox.core.mouse.Mouse;
import de.suzufa.screwbox.core.mouse.internal.DefaultMouse;
import de.suzufa.screwbox.core.physics.Physics;
import de.suzufa.screwbox.core.physics.internal.DefaultPhysics;
import de.suzufa.screwbox.core.savegame.Savegame;
import de.suzufa.screwbox.core.savegame.internal.DefaultSavegame;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.core.scenes.Scenes;
import de.suzufa.screwbox.core.scenes.internal.DefaultScenes;
import de.suzufa.screwbox.core.ui.Ui;
import de.suzufa.screwbox.core.ui.internal.DefaultUi;

class DefaultEngine implements Engine {

    private final DefaultLoop loop;
    private final DefaultGraphics graphics;
    private final DefaultKeyboard keyboard;
    private final DefaultScenes scenes;
    private final DefaultAudio audio;
    private final DefaultPhysics physics;
    private final DefaultMouse mouse;
    private final DefaultUi ui;
    private final DefaultLog log;
    private final DefaultAsync async;
    private final DefaultSavegame savegame;
    private final DefaultAssets assets;

    private final ExecutorService executor;
    private final String name;

    DefaultEngine(final String name, EngineFactory factory) {
        FrameAdapter frameAdapter = factory.frameAdapter();
        final GraphicsConfiguration configuration = new GraphicsConfiguration();
        executor = factory.executorService();
        final WindowFrame frame = factory.windowFrame();
        final DefaultScreen screen = new DefaultScreen(frameAdapter, new StandbyRenderer());
        final DefaultWindow window = new DefaultWindow(frameAdapter, configuration, executor, screen);
        window.setTitle(name);
        audio = new DefaultAudio(executor, new AudioAdapter());
        final DefaultWorld world = new DefaultWorld(screen);
        final DefaultLight light = new DefaultLight(screen, world, configuration, executor);
        graphics = new DefaultGraphics(configuration, screen, window, world, light);
        scenes = new DefaultScenes(this);
        keyboard = new DefaultKeyboard();
        ui = new DefaultUi(this);
        mouse = new DefaultMouse(graphics);
        final List<Updatable> updatables = List.of(ui, graphics, scenes, keyboard, mouse, light);
        loop = new DefaultLoop(updatables);
        physics = new DefaultPhysics(this, executor);
        log = new DefaultLog(new ConsoleLoggingAdapter());
        async = new DefaultAsync(executor, this::exceptionHandler);
        assets = new DefaultAssets(async, log);
        savegame = new DefaultSavegame(scenes);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                stop();
            }
        });
        frame.addWindowFocusListener(frame);
        frame.addMouseListener(mouse);
        frame.addMouseMotionListener(mouse);
        frame.addMouseWheelListener(mouse);
        frame.addKeyListener(keyboard);
        frame.getCanvas().addMouseListener(mouse);
        frame.getCanvas().addMouseMotionListener(mouse);
        frame.getCanvas().addMouseWheelListener(mouse);
        frame.getCanvas().addKeyListener(keyboard);
        this.name = name;
    }

    @Override
    public void start(final Class<? extends Scene> sceneClass) {
        scenes().switchTo(sceneClass);
        start();
    }

    @Override
    public void start() {
        log.info(format("engine with name '%s' started", name));
        try {
            graphics.window().open();
            loop.start();
        } catch (final RuntimeException e) {
            exceptionHandler(e);
        }
    }

    @Override
    public void stop() {
        executor.execute(() -> {
            ui.closeMenu();
            loop.stop();
            awaitLoopTermination();
            graphics.window().close();
            log.info(format("engine stopped (%,d frames total)", loop().frameNumber()));
            executor.shutdown();
        });
    }

    private void awaitLoopTermination() {
        while (loop.isLooping()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public Entities entities() {
        return scenes.activeEntities();
    }

    @Override
    public Loop loop() {
        return loop;
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

    @Override
    public String name() {
        return name;
    }

    @Override
    public Async async() {
        return async;
    }

    @Override
    public Savegame savegame() {
        return savegame;
    }

    @Override
    public Assets assets() {
        return assets;
    }

    private void exceptionHandler(final Throwable throwable) {
        stop();
        log().error(throwable);
    }

}
