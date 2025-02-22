package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.achievements.Achievements;
import io.github.srcimon.screwbox.core.achievements.internal.DefaultAchievements;
import io.github.srcimon.screwbox.core.achievements.internal.NotifyOnAchievementCompletion;
import io.github.srcimon.screwbox.core.assets.Assets;
import io.github.srcimon.screwbox.core.assets.internal.DefaultAssets;
import io.github.srcimon.screwbox.core.async.Async;
import io.github.srcimon.screwbox.core.async.internal.DefaultAsync;
import io.github.srcimon.screwbox.core.audio.Audio;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.audio.internal.AudioAdapter;
import io.github.srcimon.screwbox.core.audio.internal.AudioLinePool;
import io.github.srcimon.screwbox.core.audio.internal.DefaultAudio;
import io.github.srcimon.screwbox.core.audio.internal.DynamicSoundSupport;
import io.github.srcimon.screwbox.core.audio.internal.MicrophoneMonitor;
import io.github.srcimon.screwbox.core.audio.internal.WarmupAudioTask;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.internal.AttentionFocus;
import io.github.srcimon.screwbox.core.graphics.internal.DefaultCamera;
import io.github.srcimon.screwbox.core.graphics.internal.DefaultCanvas;
import io.github.srcimon.screwbox.core.graphics.internal.DefaultGraphics;
import io.github.srcimon.screwbox.core.graphics.internal.DefaultLight;
import io.github.srcimon.screwbox.core.graphics.internal.DefaultScreen;
import io.github.srcimon.screwbox.core.graphics.internal.DefaultViewport;
import io.github.srcimon.screwbox.core.graphics.internal.ViewportManager;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.AsyncRenderer;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.DefaultRenderer;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.FirewallRenderer;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.StandbyProxyRenderer;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.core.keyboard.internal.DefaultKeyboard;
import io.github.srcimon.screwbox.core.log.ConsoleLoggingAdapter;
import io.github.srcimon.screwbox.core.log.Log;
import io.github.srcimon.screwbox.core.log.internal.DefaultLog;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.loop.internal.DefaultLoop;
import io.github.srcimon.screwbox.core.mouse.Mouse;
import io.github.srcimon.screwbox.core.mouse.internal.DefaultMouse;
import io.github.srcimon.screwbox.core.particles.Particles;
import io.github.srcimon.screwbox.core.particles.internal.DefaultParticles;
import io.github.srcimon.screwbox.core.physics.Physics;
import io.github.srcimon.screwbox.core.physics.internal.DefaultPhysics;
import io.github.srcimon.screwbox.core.scenes.Scenes;
import io.github.srcimon.screwbox.core.scenes.internal.DefaultScenes;
import io.github.srcimon.screwbox.core.ui.Ui;
import io.github.srcimon.screwbox.core.ui.internal.DefaultUi;
import io.github.srcimon.screwbox.core.utils.internal.MacOsSupport;
import io.github.srcimon.screwbox.core.window.Window;
import io.github.srcimon.screwbox.core.window.internal.DefaultWindow;
import io.github.srcimon.screwbox.core.window.internal.InitializeFontDrawingTask;
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
import static java.util.Objects.nonNull;

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
    private final DefaultParticles particles;
    private final DefaultAchievements achievements;
    private final WarmUpIndicator warmUpIndicator;
    private final ExecutorService executor;
    private final String name;
    private final String version;

    private boolean stopCalled = false;

    DefaultEngine(final String name) {
        log = new DefaultLog(new ConsoleLoggingAdapter());
        if (!ManagementFactory.getRuntimeMXBean().getInputArguments().contains("-Dsun.java2d.opengl=true")) {
            log.warn("Please run application with the following JVM option to avoid massive fps drop: -Dsun.java2d.opengl=true");
        }
        if (MacOsSupport.isMacOs() && !MacOsSupport.jvmCanAccessMacOsSpecificCode()) {
            log.warn("Please run application with the following JVM option to add full MacOs support: " + MacOsSupport.FULLSCREEN_JVM_OPTION);
        }

        final GraphicsConfiguration configuration = new GraphicsConfiguration();
        final WindowFrame frame = MacOsSupport.isMacOs()
                ? new MacOsWindowFrame(configuration.resolution())
                : new WindowFrame(configuration.resolution());

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });

        executor = Executors.newCachedThreadPool(runnable -> {
            final Thread newThread = new Thread(runnable);
            newThread.setUncaughtExceptionHandler((thread, throwable) -> exceptionHandler(throwable));
            return newThread;
        });

        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
            executor.shutdown();
            thread.getThreadGroup().uncaughtException(thread, throwable);
        });

        final var defaultRenderer = new DefaultRenderer();
        final var asyncRenderer = new AsyncRenderer(defaultRenderer, executor);
        final var firewallRenderer = new FirewallRenderer(asyncRenderer);
        final var standbyProxyRenderer = new StandbyProxyRenderer(firewallRenderer);

        final var clip = new ScreenBounds(Offset.origin(), configuration.resolution());
        final DefaultCanvas screenCanvas = new DefaultCanvas(standbyProxyRenderer, clip);
        final DefaultCamera camera = new DefaultCamera(screenCanvas);
        final var viewportManager = new ViewportManager(new DefaultViewport(screenCanvas, camera), standbyProxyRenderer);
        final DefaultScreen screen = new DefaultScreen(frame, standbyProxyRenderer, createRobot(), screenCanvas, viewportManager, configuration);
        final var graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        window = new DefaultWindow(frame, configuration, graphicsDevice, standbyProxyRenderer);
        final DefaultLight light = new DefaultLight(configuration, viewportManager, executor);
        final AudioAdapter audioAdapter = new AudioAdapter();
        final AudioConfiguration audioConfiguration = new AudioConfiguration();
        final AudioLinePool audioLinePool = new AudioLinePool(audioAdapter, audioConfiguration);
        final MicrophoneMonitor microphoneMonitor = new MicrophoneMonitor(executor, audioAdapter, audioConfiguration);
        scenes = new DefaultScenes(this, screenCanvas, executor);
        final AttentionFocus attentionFocus = new AttentionFocus(viewportManager);
        graphics = new DefaultGraphics(configuration, screen, light, graphicsDevice, asyncRenderer, viewportManager, attentionFocus);
        particles = new DefaultParticles(scenes, attentionFocus);
        final DynamicSoundSupport dynamicSoundSupport = new DynamicSoundSupport(attentionFocus, audioConfiguration);
        audio = new DefaultAudio(executor, audioConfiguration, dynamicSoundSupport, microphoneMonitor, audioLinePool);
        ui = new DefaultUi(this, scenes, screenCanvas);
        keyboard = new DefaultKeyboard();
        mouse = new DefaultMouse(screen, viewportManager);
        achievements = new DefaultAchievements(this, new NotifyOnAchievementCompletion(ui));
        loop = new DefaultLoop(List.of(achievements, keyboard, graphics, scenes, viewportManager, ui, mouse, window, camera, particles, audio, screen));
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
        executor.execute(new InitializeFontDrawingTask());
        executor.execute(new WarmupAudioTask(audioLinePool));
        this.name = name;
        this.version = detectVersion();
        window.setTitle(name);
    }

    private String detectVersion() {
        final String versionInfo = DefaultEngine.class.getPackage().getImplementationVersion();
        return nonNull(versionInfo) ? versionInfo : "0.0.0 (dev-mode)";
    }

    @Override
    public void start() {
        if (loop.startTime().isSet()) {
            throw new IllegalStateException("engine can only be started once.");
        }
        log.info(format("'%s' started using engine version %s", name, version()));
        try {
            window.open();
            loop.start();
        } catch (final RuntimeException e) {
            exceptionHandler(e);
        }
    }

    @Override
    public void stop() {
        if (loop.startTime().isUnset()) {
            throw new IllegalStateException("engine has not been started yet");
        }
        shutdownEngine();
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
    public Particles particles() {
        return particles;
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
    public String version() {
        return version;
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
    public Achievements achievements() {
        return achievements;
    }

    @Override
    public Assets assets() {
        return assets;
    }

    private void exceptionHandler(final Throwable throwable) {
        log().error(throwable);
        shutdownEngine();
        System.exit(0);
    }

    private void shutdownEngine() {
        if (!stopCalled) {
            stopCalled = true;
            executor.execute(() -> {
                ui.closeMenu();
                loop.stop();
                loop.awaitTermination();
                audio.stopAllPlaybacks();
                window.close();
                executor.shutdown();
            });

            log.info("engine stopped after running for %s and rendering %,d frames".formatted(
                    loop.runningTime().humanReadable(),
                    loop().frameNumber()));
        }
    }

    private Robot createRobot() {
        try {
            return new Robot();
        } catch (final AWTException e) {
            throw new IllegalStateException("could not create robot for screenshots");
        }
    }
}
