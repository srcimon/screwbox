package io.github.simonbas.screwbox.core;

import io.github.simonbas.screwbox.core.assets.Assets;
import io.github.simonbas.screwbox.core.assets.internal.DefaultAssets;
import io.github.simonbas.screwbox.core.async.Async;
import io.github.simonbas.screwbox.core.async.internal.DefaultAsync;
import io.github.simonbas.screwbox.core.audio.Audio;
import io.github.simonbas.screwbox.core.audio.internal.AudioAdapter;
import io.github.simonbas.screwbox.core.audio.internal.DefaultAudio;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.graphics.Graphics;
import io.github.simonbas.screwbox.core.graphics.GraphicsConfiguration;
import io.github.simonbas.screwbox.core.graphics.internal.*;
import io.github.simonbas.screwbox.core.keyboard.Keyboard;
import io.github.simonbas.screwbox.core.keyboard.internal.DefaultKeyboard;
import io.github.simonbas.screwbox.core.log.ConsoleLoggingAdapter;
import io.github.simonbas.screwbox.core.log.Log;
import io.github.simonbas.screwbox.core.log.internal.DefaultLog;
import io.github.simonbas.screwbox.core.loop.Loop;
import io.github.simonbas.screwbox.core.loop.internal.DefaultLoop;
import io.github.simonbas.screwbox.core.mouse.Mouse;
import io.github.simonbas.screwbox.core.mouse.internal.DefaultMouse;
import io.github.simonbas.screwbox.core.physics.Physics;
import io.github.simonbas.screwbox.core.physics.internal.DefaultPhysics;
import io.github.simonbas.screwbox.core.savegame.Savegame;
import io.github.simonbas.screwbox.core.savegame.internal.DefaultSavegame;
import io.github.simonbas.screwbox.core.scenes.Scene;
import io.github.simonbas.screwbox.core.scenes.Scenes;
import io.github.simonbas.screwbox.core.scenes.internal.DefaultScenes;
import io.github.simonbas.screwbox.core.ui.Ui;
import io.github.simonbas.screwbox.core.ui.internal.DefaultUi;
import io.github.simonbas.screwbox.core.window.Window;
import io.github.simonbas.screwbox.core.window.internal.DefaultWindow;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.String.format;

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
    private final DefaultWindow window;
    private final WarmUpIndicator warmUpIndicator;
    private final ExecutorService executor;
    private final String name;

    DefaultEngine(final String name) {
        final WindowFrame frame = isMacOs() ? new MacOsWindowFrame() : new WindowFrame();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });

        final GraphicsConfiguration configuration = new GraphicsConfiguration();
        executor = Executors.newCachedThreadPool(runnable -> {
            final Thread newThread = new Thread(runnable);
            newThread.setUncaughtExceptionHandler((thread, throwable) -> exceptionHandler(throwable));
            return newThread;
        });
        final var ct = Thread.currentThread();
        executor.submit(() -> {
            try {
                while(ct.isAlive()) {
                    Thread.sleep(250);
                }
                executor.shutdown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();;
            }
        });
        final DefaultScreen screen = new DefaultScreen(frame, new StandbyRenderer());
        final var graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        window = new DefaultWindow(frame, configuration, executor, screen, graphicsDevice);
        audio = new DefaultAudio(executor, new AudioAdapter());
        final DefaultWorld world = new DefaultWorld(screen);
        final DefaultLight light = new DefaultLight(screen, world, configuration, executor);
        graphics = new DefaultGraphics(configuration, screen, world, light, graphicsDevice);
        scenes = new DefaultScenes(this, executor);
        ui = new DefaultUi(this, scenes);
        keyboard = new DefaultKeyboard();
        mouse = new DefaultMouse(graphics);
        loop = new DefaultLoop(List.of(ui, graphics, scenes, keyboard, mouse));
        log = new DefaultLog(new ConsoleLoggingAdapter());
        warmUpIndicator = new WarmUpIndicator(loop, log);
        physics = new DefaultPhysics(this);
        async = new DefaultAsync(executor);
        assets = new DefaultAssets(async, log);
        savegame = new DefaultSavegame(scenes);
        frame.addMouseListener(mouse);
        frame.addMouseMotionListener(mouse);
        frame.addMouseWheelListener(mouse);
        frame.addKeyListener(keyboard);
        frame.getCanvas().addMouseListener(mouse);
        frame.getCanvas().addMouseMotionListener(mouse);
        frame.getCanvas().addMouseWheelListener(mouse);
        frame.getCanvas().addKeyListener(keyboard);
        this.name = name;
        window.setTitle(name);
    }

    @Override
    public void start(final Class<? extends Scene> sceneClass) {
        scenes().switchTo(sceneClass);
        start();
    }

    @Override
    public void start() {
        if (loop.startTime().isSet()) {
            throw new IllegalStateException("engine can only be started once.");
        }
        log.info(format("engine with name '%s' started", name));
        try {
            window.open();
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
            loop.awaitTermination();
            window.close();
            log.info(String.format("engine stopped (%,d frames total)", loop().frameNumber()));
        });
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
    public boolean isWarmedUp() {
        return warmUpIndicator.isWarmedUp();
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
    public Window window() {
        return window;
    }

    @Override
    public Assets assets() {
        return assets;
    }

    private void exceptionHandler(final Throwable throwable) {
        stop();
        log().error(throwable);
    }

    private boolean isMacOs() {
        return "Mac OS X".equalsIgnoreCase(System.getProperty("os.name", "UNKNOWN-OS"));
    }
}
