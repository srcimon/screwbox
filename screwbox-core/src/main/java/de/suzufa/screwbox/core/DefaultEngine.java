package de.suzufa.screwbox.core;

import javax.swing.JFrame;

import de.suzufa.screwbox.core.audio.Audio;
import de.suzufa.screwbox.core.audio.internal.DefaultAudio;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.graphics.Graphics;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.internal.DefaultGraphics;
import de.suzufa.screwbox.core.graphics.internal.DefaultWindow;
import de.suzufa.screwbox.core.keyboard.Keyboard;
import de.suzufa.screwbox.core.keyboard.internal.DefaultKeyboard;
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

	DefaultEngine() {
		final JFrame frame = new JFrame();
		final DefaultMetrics metrics = new DefaultMetrics();
		final GraphicsConfiguration configuration = new GraphicsConfiguration();
		final DefaultWindow window = new DefaultWindow(frame, configuration, metrics);
		audio = new DefaultAudio();
		graphics = new DefaultGraphics(configuration, window);
		scenes = new DefaultScenes(this);
		keyboard = new DefaultKeyboard();
		ui = new DefaultUi(this);
		mouse = new DefaultMouse(graphics);
		gameLoop = new DefaultGameLoop(scenes, graphics, metrics, keyboard, mouse, ui);
		physics = new DefaultPhysics(this);
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

}
