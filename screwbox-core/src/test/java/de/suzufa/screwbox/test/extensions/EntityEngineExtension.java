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
import de.suzufa.screwbox.core.graphics.window.Window;
import de.suzufa.screwbox.core.graphics.world.World;
import de.suzufa.screwbox.core.loop.GameLoop;
import de.suzufa.screwbox.core.loop.Metrics;

public class EntityEngineExtension implements Extension, BeforeEachCallback, ParameterResolver {

    private final Map<Class<?>, Object> parameters = new HashMap<>();
    private DefaultEntityEngine entityEngine;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        var metrics = mock(Metrics.class);
        var gameLoop = mock(GameLoop.class);
        var engine = mock(Engine.class);
        var graphics = mock(Graphics.class);
        var world = mock(World.class);
        var screen = mock(Window.class);
        var entityManager = new DefaultEntityManager();
        var systemManager = new DefaultSystemManager(engine, entityManager);
        entityEngine = new DefaultEntityEngine(entityManager, systemManager);
        when(engine.entityEngine()).thenReturn(entityEngine);
        when(engine.graphics()).thenReturn(graphics);
        when(graphics.world()).thenReturn(world);
        when(graphics.window()).thenReturn(screen);
        when(engine.loop()).thenReturn(gameLoop);
        when(gameLoop.metrics()).thenReturn(metrics);
        parameters.put(Metrics.class, metrics);
        parameters.put(Graphics.class, graphics);
        parameters.put(World.class, world);
        parameters.put(Window.class, screen);
        parameters.put(DefaultEntityEngine.class, entityEngine);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        var type = parameterContext.getParameter().getType();
        return parameters.containsKey(type);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        var type = parameterContext.getParameter().getType();
        return parameters.get(type);
    }

}
