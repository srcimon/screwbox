package dev.screwbox.core;

import dev.screwbox.core.achievements.Achievements;
import dev.screwbox.core.achievements.internal.DefaultAchievements;
import dev.screwbox.core.achievements.internal.NotifyOnAchievementCompletion;
import dev.screwbox.core.assets.Assets;
import dev.screwbox.core.assets.internal.DefaultAssets;
import dev.screwbox.core.async.Async;
import dev.screwbox.core.async.internal.DefaultAsync;
import dev.screwbox.core.audio.Audio;
import dev.screwbox.core.audio.AudioConfiguration;
import dev.screwbox.core.audio.internal.AudioAdapter;
import dev.screwbox.core.audio.internal.AudioLinePool;
import dev.screwbox.core.audio.internal.DefaultAudio;
import dev.screwbox.core.audio.internal.DynamicSoundSupport;
import dev.screwbox.core.audio.internal.MicrophoneMonitor;
import dev.screwbox.core.audio.internal.WarmupAudioTask;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.internal.AttentionFocus;
import dev.screwbox.core.graphics.internal.DefaultCamera;
import dev.screwbox.core.graphics.internal.DefaultCanvas;
import dev.screwbox.core.graphics.internal.DefaultGraphics;
import dev.screwbox.core.graphics.internal.DefaultLight;
import dev.screwbox.core.graphics.internal.DefaultScreen;
import dev.screwbox.core.graphics.internal.DefaultViewport;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.graphics.internal.renderer.RenderPipeline;
import dev.screwbox.core.keyboard.Keyboard;
import dev.screwbox.core.keyboard.internal.DefaultKeyboard;
import dev.screwbox.core.log.ConsoleLoggingAdapter;
import dev.screwbox.core.log.Log;
import dev.screwbox.core.log.internal.DefaultLog;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.loop.internal.DefaultLoop;
import dev.screwbox.core.mouse.Mouse;
import dev.screwbox.core.mouse.internal.DefaultMouse;
import dev.screwbox.core.particles.Particles;
import dev.screwbox.core.particles.internal.DefaultParticles;
import dev.screwbox.core.physics.Physics;
import dev.screwbox.core.physics.internal.DefaultPhysics;
import dev.screwbox.core.scenes.Scenes;
import dev.screwbox.core.scenes.internal.DefaultScenes;
import dev.screwbox.core.ui.Ui;
import dev.screwbox.core.ui.internal.DefaultUi;
import dev.screwbox.core.utils.internal.MacOsSupport;
import dev.screwbox.core.window.Window;
import dev.screwbox.core.window.internal.DefaultWindow;
import dev.screwbox.core.window.internal.InitializeFontDrawingTask;
import dev.screwbox.core.window.internal.MacOsWindowFrame;
import dev.screwbox.core.window.internal.WindowFrame;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.nonNull;

class DefaultEngine implements Engine {

    private static final String OPENGL_PARAMETER = "-Dsun.java2d.opengl=true";

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
        if (!ManagementFactory.getRuntimeMXBean().getInputArguments().contains(OPENGL_PARAMETER)) {
            log.warn("Please run application with the following JVM option to avoid massive fps drop: {}", OPENGL_PARAMETER);
        }
        if (MacOsSupport.isMacOs() && !MacOsSupport.jvmCanAccessMacOsSpecificCode()) {
            log.warn("Please run application with the following JVM option to add full MacOs support: {}", MacOsSupport.FULLSCREEN_JVM_OPTION);
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

        final var renderPipeline = new RenderPipeline(executor, configuration);
        final var clip = new ScreenBounds(Offset.origin(), configuration.resolution());
        final DefaultCanvas screenCanvas = new DefaultCanvas(renderPipeline.renderer(), clip);
        final DefaultCamera camera = new DefaultCamera(screenCanvas);
        final var viewportManager = new ViewportManager(new DefaultViewport(screenCanvas, camera), renderPipeline);
        final DefaultScreen screen = new DefaultScreen(frame, renderPipeline.renderer(), createRobot(), screenCanvas, viewportManager, configuration);
        final var graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        window = new DefaultWindow(frame, configuration, graphicsDevice, renderPipeline);
        final DefaultLight light = new DefaultLight(configuration, viewportManager, executor);
        final AudioAdapter audioAdapter = new AudioAdapter();
        final AudioConfiguration audioConfiguration = new AudioConfiguration();
        final AudioLinePool audioLinePool = new AudioLinePool(audioAdapter, audioConfiguration);
        final MicrophoneMonitor microphoneMonitor = new MicrophoneMonitor(executor, audioAdapter, audioConfiguration);
        scenes = new DefaultScenes(this, screenCanvas, executor);
        final AttentionFocus attentionFocus = new AttentionFocus(viewportManager);
        graphics = new DefaultGraphics(configuration, screen, light, graphicsDevice, renderPipeline, viewportManager, attentionFocus);
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
        frame.addWindowFocusListener(keyboard);
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
        log.info("'{}' started using engine version {}", name, version());
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
