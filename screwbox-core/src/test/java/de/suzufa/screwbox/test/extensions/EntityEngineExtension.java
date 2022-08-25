package de.suzufa.screwbox.test.extensions;

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
import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityEngine;
import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityManager;
import de.suzufa.screwbox.core.entityengine.internal.DefaultSystemManager;
import de.suzufa.screwbox.core.graphics.Graphics;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.World;
import de.suzufa.screwbox.core.log.Log;
import de.suzufa.screwbox.core.loop.GameLoop;

public class EntityEngineExtension implements Extension, BeforeEachCallback, ParameterResolver {

    private final Map<Class<?>, Object> parameters = new HashMap<>();
    private DefaultEntityEngine entityEngine;

    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        final var gameLoop = mock(GameLoop.class);
        final var engine = mock(Engine.class);
        final var graphics = mock(Graphics.class);
        final var world = mock(World.class);
        final var log = mock(Log.class);
        final var screen = mock(Window.class);
        final var entityManager = new DefaultEntityManager();
        final var systemManager = new DefaultSystemManager(engine, entityManager);
        entityEngine = new DefaultEntityEngine(entityManager, systemManager);
        when(engine.entityEngine()).thenReturn(entityEngine);
        when(engine.graphics()).thenReturn(graphics);
        when(engine.log()).thenReturn(log);
        when(graphics.world()).thenReturn(world);
        when(graphics.window()).thenReturn(screen);
        when(engine.loop()).thenReturn(gameLoop);
        parameters.put(GameLoop.class, gameLoop);
        parameters.put(Graphics.class, graphics);
        parameters.put(World.class, world);
        parameters.put(Window.class, screen);
        parameters.put(Log.class, log);
        parameters.put(DefaultEntityEngine.class, entityEngine);
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
