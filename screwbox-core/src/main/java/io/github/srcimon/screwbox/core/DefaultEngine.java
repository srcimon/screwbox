package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.assets.Assets;
import io.github.srcimon.screwbox.core.assets.internal.DefaultAssets;
import io.github.srcimon.screwbox.core.async.Async;
import io.github.srcimon.screwbox.core.async.internal.DefaultAsync;
import io.github.srcimon.screwbox.core.audio.Audio;
import io.github.srcimon.screwbox.core.audio.internal.AudioAdapter;
import io.github.srcimon.screwbox.core.audio.internal.DefaultAudio;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.internal.*;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.core.keyboard.internal.DefaultKeyboard;
import io.github.srcimon.screwbox.core.log.ConsoleLoggingAdapter;
import io.github.srcimon.screwbox.core.log.Log;
import io.github.srcimon.screwbox.core.log.internal.DefaultLog;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.loop.internal.DefaultLoop;
import io.github.srcimon.screwbox.core.mouse.Mouse;
import io.github.srcimon.screwbox.core.mouse.internal.DefaultMouse;
import io.github.srcimon.screwbox.core.physics.Physics;
import io.github.srcimon.screwbox.core.physics.internal.DefaultPhysics;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.scenes.Scenes;
import io.github.srcimon.screwbox.core.scenes.internal.DefaultScenes;
import io.github.srcimon.screwbox.core.ui.Ui;
import io.github.srcimon.screwbox.core.ui.internal.DefaultUi;
import io.github.srcimon.screwbox.core.utils.MacOsSupport;
import io.github.srcimon.screwbox.core.window.Window;
import io.github.srcimon.screwbox.core.window.internal.DefaultWindow;
import io.github.srcimon.screwbox.core.window.internal.MacOsWindowFrame;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.management.ManagementFactory;
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
    private final DefaultAssets assets;
    private final DefaultWindow window;
    private final WarmUpIndicator warmUpIndicator;
    private final ExecutorService executor;
    private final String name;

    private boolean stopCalled = false;

    DefaultEngine(final String name) {
        final WindowFrame frame = MacOsSupport.isMacOs() ? new MacOsWindowFrame() : new WindowFrame();

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

        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
            executor.shutdown();
            thread.getThreadGroup().uncaughtException(thread, throwable);
        });

        final DefaultScreen screen = new DefaultScreen(frame, new StandbyRenderer());
        final var graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        window = new DefaultWindow(frame, configuration, executor, screen, graphicsDevice);
        final DefaultWorld world = new DefaultWorld(screen);
        final DefaultLight light = new DefaultLight(screen, world, configuration, executor);
        graphics = new DefaultGraphics(configuration, screen, world, light, graphicsDevice);
        scenes = new DefaultScenes(this, executor);
        ui = new DefaultUi(this, scenes);
        keyboard = new DefaultKeyboard();
        mouse = new DefaultMouse(graphics);
        loop = new DefaultLoop(List.of(ui, graphics, scenes, keyboard, mouse, window));
        audio = new DefaultAudio(executor, new AudioAdapter(), graphics);
        log = new DefaultLog(new ConsoleLoggingAdapter());
        warmUpIndicator = new WarmUpIndicator(loop, log);
        physics = new DefaultPhysics(this);
        async = new DefaultAsync(executor);
        assets = new DefaultAssets(async, log);
        for (var component : List.of(frame, frame.getCanvas())) {
            component.addMouseListener(mouse);
            component.addMouseMotionListener(mouse);
            component.addMouseWheelListener(mouse);
            component.addKeyListener(keyboard);
        }
        this.name = name;
        window.setTitle(name);
        validateJvmOptions();
    }

    private void validateJvmOptions() {
        final List<String> jvmOptions = ManagementFactory.getRuntimeMXBean().getInputArguments();
        if (!jvmOptions.contains("-Dsun.java2d.opengl=true")) {
            log.warn("Please run application with the following JVM Option to avoid massive fps drop: -Dsun.java2d.opengl=true");
        }
        if (MacOsSupport.isMacOs() && !jvmOptions.contains(MacOsSupport.FULLSCREEN_JVM_OPTION)) {
            log.warn("Please run application with the following JVM Option to support fullscreen on MacOS: " + MacOsSupport.FULLSCREEN_JVM_OPTION);
        }
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
        if (!stopCalled) {
            stopCalled = true;
            executor.execute(() -> {
                ui.closeMenu();
                loop.stop();
                loop.awaitTermination();
                window.close();
                log.info(String.format("engine stopped (%,d frames total)", loop().frameNumber()));
                executor.shutdown();
            });
        }
    }

    @Override
    public Environment environment() {
        return scenes.activeEnvironment();
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
}
