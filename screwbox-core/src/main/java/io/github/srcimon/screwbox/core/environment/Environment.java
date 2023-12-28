package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.environment.light.LightRenderSystem;
import io.github.srcimon.screwbox.core.environment.light.OptimizeLightPerformanceSystem;
import io.github.srcimon.screwbox.core.environment.logic.AreaTriggerSystem;
import io.github.srcimon.screwbox.core.environment.logic.StateSystem;
import io.github.srcimon.screwbox.core.environment.physics.AutomovementSystem;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionSystem;
import io.github.srcimon.screwbox.core.environment.physics.GravitySystem;
import io.github.srcimon.screwbox.core.environment.physics.MagnetSystem;
import io.github.srcimon.screwbox.core.environment.physics.OptimizePhysicsPerformanceSystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsSystem;
import io.github.srcimon.screwbox.core.environment.rendering.ReflectionRenderSystem;
import io.github.srcimon.screwbox.core.environment.rendering.FlipSpriteSystem;
import io.github.srcimon.screwbox.core.environment.rendering.RotateSpriteSystem;
import io.github.srcimon.screwbox.core.environment.rendering.ScreenTransitionSystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacitySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenSystem;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.scenes.Scene;

import java.util.List;
import java.util.Optional;

/**
 * The {@link Environment} manages all {@link #entities()} and all {@link #systems()} that are contained in a {@link Scene}.
 * This is a very simple implementation of and´ <a href="https://en.wikipedia.org/wiki/Entity_component_system">Entity Component System (ECS)</a>.
 *
 * @see Entity
 * @see EntitySystem
 * @see Component
 * @see Archetype
 */
public interface Environment {

    Environment addEntity(String name, Component... components);

    Environment addEntity(int id, Component... components);

    Environment addEntity(int id, String name, Component... components);

    Environment addEntity(Component... components);

    Environment addEntity(Entity entity);

    //TODO javadoc
    Environment addSystem(EntitySystem system);

    //TODO javadoc
    //TODO test
    Environment addSystem(SystemOrder order, EntitySystem system);

    Environment addEntities(List<Entity> entities);

    Environment addOrReplaceSystem(EntitySystem system);

    Environment removeSystemIfPresent(Class<? extends EntitySystem> systemType);

    Environment addSystems(EntitySystem... systems);

    List<Entity> fetchAll(Archetype archetype);

    default List<Entity> fetchAllHaving(Class<? extends Component> component) {
        return fetchAll(Archetype.of(component));
    }

    default List<Entity> fetchAllHaving(Class<? extends Component> componentA, Class<? extends Component> componentB) {
        return fetchAll(Archetype.of(componentA, componentB));
    }

    Optional<Entity> fetch(Archetype archetype);

    default Optional<Entity> fetchHaving(Class<? extends Component> component) {
        return fetch(Archetype.of(component));
    }

    default Optional<Entity> fetchHaving(Class<? extends Component> componentA, Class<? extends Component> componentB) {
        return fetch(Archetype.of(componentA, componentB));
    }

    Entity forcedFetch(Archetype archetype);

    default Entity forcedFetchHaving(Class<? extends Component> component) {
        return forcedFetch(Archetype.of(component));
    }

    default Entity forcedFetchHaving(Class<? extends Component> componentA, Class<? extends Component> componentB) {
        return forcedFetch(Archetype.of(componentA, componentB));
    }

    Entity forcedFetchById(int id);

    Optional<Entity> fetchById(int id);

    Environment remove(Entity entity);

    Environment remove(List<Entity> entities);

    /**
     * Drops all current {@link Entity}s. All {@link EntitySystem}s stay untouched.
     */
    Environment clearEntities();

    Environment toggleSystem(EntitySystem entitySystem);

    void remove(Class<? extends EntitySystem> systemType);

    long entityCount();

    boolean contains(Archetype archetype);

    Environment addSystem(Entity... entities);

    boolean isSystemPresent(Class<? extends EntitySystem> type);

    /**
     * Returns all {@link Entity}s currently attached.
     */
    List<Entity> entities();

    /**
     * Provides a compact syntax for importing {@link Entity}s from a custom source
     * using conditions and {@link SourceImport.Converter}.
     *
     * @see #importSource(List) for multiple sources
     */
    <T> SourceImport<T> importSource(T source);

    /**
     * Provides a compact syntax for importing {@link Entity}s from multiple custom
     * sources using conditions and {@link SourceImport.Converter}.
     *
     * @see #importSource(Object) for single source
     */
    <T> SourceImport<T> importSource(List<T> source);

    /**
     * Returns all {@link EntitySystem}s currently attached.
     */
    List<EntitySystem> systems();

    /**
     * Creates a savegame file with the given name. The savegame contains all
     * {@link #entities()} attached to the {@link Environment}.
     */
    Environment createSavegame(String name);

    /**
     * Loads a previously created savegame file and restores the saved
     * {@link #entities()} in the {@link Environment}.
     */
    Environment loadSavegame(String name);

    /**
     * Deletes the savegame with the given name. Triggers an {@link Exception} if
     * there is no such savegame.
     */
    Environment deleteSavegame(String name);

    /**
     * Returns true if there is a savegame with the given name. Value is cached for a second to prevent
     * exessive disc usage.
     */
    boolean savegameExists(String name);

    /**
     * Adds all systems needed for physics support in this {@link Environment}.
     *
     * @see AutomovementSystem
     * @see CollisionDetectionSystem
     * @see GravitySystem
     * @see MagnetSystem
     * @see OptimizePhysicsPerformanceSystem
     * @see PhysicsSystem
     */
    Environment enablePhysics();

    /**
     * Adds systems needed for rendering {@link Sprite}s.
     *
     * @see ReflectionRenderSystem
     * @see RotateSpriteSystem
     * @see FlipSpriteSystem
     * @see ScreenTransitionSystem
     * @see RenderSystem
     */
    Environment enableRendering();

    /**
     * Adds systems needed for tweening.
     *
     * @see TweenSystem
     * @see TweenDestroySystem
     * @see TweenOpacitySystem
     */
    Environment enableTweening();

    /**
     * Adds systems needed for stateful entities and area triggers.
     *
     * @see AreaTriggerSystem
     * @see StateSystem
     */
    Environment enableLogic();

    /**
     * Adds systems for light rendering. Enables light rendering in the {@link Environment}. If your screen stays dark you have to add some light components.
     *
     * @see LightRenderSystem
     * @see OptimizeLightPerformanceSystem
     */
    Environment enableLight();
}