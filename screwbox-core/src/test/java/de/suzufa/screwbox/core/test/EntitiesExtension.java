package de.suzufa.screwbox.core.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.entities.internal.EntityManager;
import de.suzufa.screwbox.core.entities.internal.SystemManager;
import de.suzufa.screwbox.core.graphics.Graphics;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.World;
import de.suzufa.screwbox.core.keyboard.Keyboard;
import de.suzufa.screwbox.core.log.Log;
import de.suzufa.screwbox.core.loop.Loop;
import de.suzufa.screwbox.core.physics.Physics;

public class EntitiesExtension implements Extension, BeforeEachCallback, ParameterResolver {

    private final Map<Class<?>, Object> parameters = new HashMap<>();
    private DefaultEntities entities;

    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        final var gameLoop = mock(Loop.class);
        final var engine = mock(Engine.class);
        final var graphics = mock(Graphics.class);
        final var world = mock(World.class);
        final var log = mock(Log.class);
        final var physics = mock(Physics.class);
        final var keyboard = mock(Keyboard.class);
        final var screen = mock(Window.class);
        final var entityManager = new EntityManager();
        final var systemManager = new SystemManager(engine, entityManager);
        entities = new DefaultEntities(entityManager, systemManager);

        // resolve a real entity engine with many mocked subsystems
        when(engine.entities()).thenReturn(entities);

        // resolve mocks for any subsystem
        when(engine.graphics()).thenReturn(graphics);
        when(engine.log()).thenReturn(log);
        when(engine.physics()).thenReturn(physics);
        when(engine.loop()).thenReturn(gameLoop);
        when(engine.keyboard()).thenReturn(keyboard);
        when(graphics.world()).thenReturn(world);
        when(graphics.window()).thenReturn(screen);

        // resolve test method parameters
        parameters.put(Loop.class, gameLoop);
        parameters.put(Graphics.class, graphics);
        parameters.put(World.class, world);
        parameters.put(Window.class, screen);
        parameters.put(Log.class, log);
        parameters.put(Physics.class, physics);
        parameters.put(Keyboard.class, keyboard);
        parameters.put(Engine.class, engine);
        parameters.put(DefaultEntities.class, entities);
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
