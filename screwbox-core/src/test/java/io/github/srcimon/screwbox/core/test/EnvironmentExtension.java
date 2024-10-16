package io.github.srcimon.screwbox.core.test;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.audio.Audio;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.core.log.Log;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.mouse.Mouse;
import io.github.srcimon.screwbox.core.particles.Particles;
import io.github.srcimon.screwbox.core.physics.Physics;
import io.github.srcimon.screwbox.core.window.Window;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

public class EnvironmentExtension implements Extension, BeforeEachCallback, ParameterResolver {

    private final Map<Class<?>, Object> parameters = new HashMap<>();

    @Override
    public void beforeEach(final ExtensionContext context) {
        final var gameLoop = Mockito.mock(Loop.class);
        final var engine = Mockito.mock(Engine.class);
        final var graphics = Mockito.mock(Graphics.class);
        final var world = Mockito.mock(World.class);
        final var log = Mockito.mock(Log.class);
        final var physics = Mockito.mock(Physics.class);
        final var keyboard = Mockito.mock(Keyboard.class);
        final var particles = Mockito.mock(Particles.class);
        final var window = Mockito.mock(Window.class);
        final var screen = Mockito.mock(Screen.class);
        final var viewport = Mockito.mock(Canvas.class);
        final var camera = Mockito.mock(Camera.class);
        final var mouse = Mockito.mock(Mouse.class);
        final var audio = Mockito.mock(Audio.class);
        final var audioConfiguration = Mockito.mock(AudioConfiguration.class);
        final var entities = new DefaultEnvironment(engine);

        // resolve a real entity engine with many mocked subsystems
        when(engine.environment()).thenReturn(entities);

        // resolve mocks for any subsystem
        when(engine.graphics()).thenReturn(graphics);
        when(engine.log()).thenReturn(log);
        when(engine.physics()).thenReturn(physics);
        when(engine.loop()).thenReturn(gameLoop);
        when(engine.keyboard()).thenReturn(keyboard);
        when(engine.mouse()).thenReturn(mouse);
        when(engine.window()).thenReturn(window);
        when(engine.particles()).thenReturn(particles);
        when(engine.audio()).thenReturn(audio);
        when(audio.configuration()).thenReturn(audioConfiguration);
        when(graphics.world()).thenReturn(world);
        when(graphics.screen()).thenReturn(screen);
        when(graphics.camera()).thenReturn(camera);
        when(graphics.viewport()).thenReturn(viewport);

        // resolve test method parameters
        parameters.put(Loop.class, gameLoop);
        parameters.put(Graphics.class, graphics);
        parameters.put(Camera.class, camera);
        parameters.put(Screen.class, screen);
        parameters.put(Mouse.class, mouse);
        parameters.put(World.class, world);
        parameters.put(Window.class, window);
        parameters.put(Log.class, log);
        parameters.put(Physics.class, physics);
        parameters.put(Keyboard.class, keyboard);
        parameters.put(Engine.class, engine);
        parameters.put(Particles.class, particles);
        parameters.put(Audio.class, audio);
        parameters.put(AudioConfiguration.class, audioConfiguration);
        parameters.put(DefaultEnvironment.class, entities);
        parameters.put(Canvas.class, viewport);
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) {
        final var type = parameterContext.getParameter().getType();
        return parameters.containsKey(type);
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) {
        final var type = parameterContext.getParameter().getType();
        return parameters.get(type);
    }

}
