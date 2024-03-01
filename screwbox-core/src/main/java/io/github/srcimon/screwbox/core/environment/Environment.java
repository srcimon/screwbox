package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.environment.light.LightRenderSystem;
import io.github.srcimon.screwbox.core.environment.light.OptimizeLightPerformanceSystem;
import io.github.srcimon.screwbox.core.environment.logic.AreaTriggerSystem;
import io.github.srcimon.screwbox.core.environment.logic.StateSystem;
import io.github.srcimon.screwbox.core.environment.physics.*;
import io.github.srcimon.screwbox.core.environment.rendering.*;
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

    /**
     * Returns a {@link Component} that is expected not have more than on instance in the {@link Environment}.
     * Can be used to store configuration for an {@link EntitySystem} e.g. {@link PhysicsGridConfigurationComponent}.
     * <p/>
     * Please note: There is currently no way to prevent that such a {@link Component} is added more than once (for performance reasons).
     *
     * @throws IllegalStateException will be thrown when more than one instance is found
     * @see #tryFetchSingletonEntity(Class)
     * @see #hasSingleton(Class)
     */
    <T extends Component> Optional<T> tryFetchSingleton(Class<T> component);

    /**
     * Returns an {@link Entity} that is expected to be the only {@link Entity} in the {@link Environment} that contains the given singleton {@link Component}.
     * <p/>
     * Please note: There is currently no way to prevent that such a {@link Component} is added more than once (for performance reasons).
     *
     * @throws IllegalStateException will be thrown when more than one instance is found
     * @see #tryFetchSingleton(Class)
     * @see #hasSingleton(Class)
     */
    Optional<Entity> tryFetchSingletonEntity(Class<? extends Component> component);

    /**
     * Returns {@code true} if the {@link Environment} contains the given singleton {@link Component}.
     * <p/>
     * Please note: There is currently no way to prevent that such a {@link Component} is added more than once (for performance reasons).
     *
     * @throws IllegalStateException will be thrown when more than one instance is found
     * @see #tryFetchSingleton(Class)
     * @see #tryFetchSingleton(Class)
     */
    boolean hasSingleton(Class<? extends Component> component);

    //TODO apply singleton naming schema
    Optional<Entity> tryFetch(Archetype archetype);

    //TODO apply singleton naming schema
    Entity fetch(Archetype archetype);

    Environment addEntity(String name, Component... components);

    Environment addEntity(int id, Component... components);

    Environment addEntity(int id, String name, Component... components);

    Environment addEntity(Component... components);

    Environment addEntity(Entity entity);

    /**
     * Adds an {@link EntitySystem} to the {@link Environment} with default {@link SystemOrder#SIMULATION}.
     */
    Environment addSystem(EntitySystem system);

    /**
     * Adds an {@link EntitySystem} to the {@link Environment} with the given {@link SystemOrder} (overwrites annotated {@link SystemOrder} if present.
     */
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

    /**
     * Fetches an {@link Entity} by {@link Entity#id()}.
     *
     * @throws IllegalStateException when {@link Entity} was not found
     * @see #tryFetchById(int)
     */
    Entity fetchById(int id);

    /**
     * Fetches an {@link Entity} by {@link Entity#id()}.
     *
     * @see #fetchById(int)
     */
    Optional<Entity> tryFetchById(int id);

    Environment remove(Entity entity);

    Environment remove(List<Entity> entities);

    /**
     * Removes all current {@link Entity}s. All {@link EntitySystem}s stay untouched.
     */
    Environment clearEntities();

    Environment toggleSystem(EntitySystem entitySystem);

    void remove(Class<? extends EntitySystem> systemType);

    long entityCount();

    boolean contains(Archetype archetype);

    Environment addEntities(Entity... entities);

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
     * @see ChaoticMovementSystem
     * @see PhysicsGridUpdateSystem
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