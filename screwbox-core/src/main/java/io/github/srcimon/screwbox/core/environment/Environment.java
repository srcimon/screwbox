package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.environment.audio.SoundSystem;
import io.github.srcimon.screwbox.core.environment.light.LightRenderSystem;
import io.github.srcimon.screwbox.core.environment.light.OptimizeLightPerformanceSystem;
import io.github.srcimon.screwbox.core.environment.logic.AreaTriggerSystem;
import io.github.srcimon.screwbox.core.environment.logic.StateSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleBurstSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleInteractionSystem;
import io.github.srcimon.screwbox.core.environment.physics.*;
import io.github.srcimon.screwbox.core.environment.rendering.CameraSystem;
import io.github.srcimon.screwbox.core.environment.rendering.FixedRotationSystem;
import io.github.srcimon.screwbox.core.environment.rendering.FixedSpinSystem;
import io.github.srcimon.screwbox.core.environment.rendering.FlipSpriteSystem;
import io.github.srcimon.screwbox.core.environment.rendering.MovementRotationSystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderSceneTransitionSystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderSystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderUiSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenLightSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacitySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenPositionSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenSpinSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenSystem;
import io.github.srcimon.screwbox.core.scenes.Scene;

import java.util.ArrayList;
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
     * <p>
     * Please note: There is currently no way to prevent that such a {@link Component} is added more than once (for performance reasons).
     *
     * @throws IllegalStateException will be thrown when more than one instance is found
     * @see #tryFetchSingleton(Class)
     * @see #hasSingleton(Class)
     */
    <T extends Component> Optional<T> tryFetchSingletonComponent(Class<T> component);

    /**
     * Returns a {@link Component} that is expected not have more than on instance in the {@link Environment}.
     * Can be used to store configuration for an {@link EntitySystem} e.g. {@link PhysicsGridConfigurationComponent}.
     * <p>
     * Please note: There is currently no way to prevent that such a {@link Component} is added more than once (for performance reasons).
     *
     * @throws IllegalStateException will be thrown when not exactly one instance is found
     * @see #tryFetchSingleton(Class)
     * @see #hasSingleton(Class)
     */
    default <T extends Component> T fetchSingletonComponent(Class<T> component) {
        return tryFetchSingletonComponent(component).orElseThrow(() -> new IllegalStateException("singleton component has not been found: " + component.getSimpleName()));
    }

    /**
     * Returns an {@link Entity} that is expected to be the only {@link Entity} in the {@link Environment} that contains the given singleton {@link Component}.
     * <p>
     * Please note: There is currently no way to prevent that such a {@link Component} is added more than once (for performance reasons).
     *
     * @throws IllegalStateException will be thrown when more than one instance is found
     * @see #fetchSingleton(Class)
     * @see #tryFetchSingletonComponent(Class)
     * @see #hasSingleton(Class)
     */
    Optional<Entity> tryFetchSingleton(Class<? extends Component> component);

    /**
     * Returns an {@link Entity} that is expected to be the only {@link Entity} in the {@link Environment} that contains the given singleton {@link Component}.
     * <p>
     * Please note: There is currently no way to prevent that such a {@link Component} is added more than once (for performance reasons).
     *
     * @throws IllegalStateException will be thrown when more than one instance is found
     * @see #tryFetchSingleton(Class)
     * @see #tryFetchSingletonComponent(Class)
     * @see #hasSingleton(Class)
     */
    default Entity fetchSingleton(final Class<? extends Component> component) {
        return tryFetchSingleton(component).orElseThrow(() -> new IllegalStateException("didn't find singleton entity"));
    }

    /**
     * Returns {@code true} if the {@link Environment} contains the given singleton {@link Component}.
     * <p>
     * Please note: There is currently no way to prevent that such a {@link Component} is added more than once (for performance reasons).
     *
     * @throws IllegalStateException will be thrown when more than one instance is found
     * @see #tryFetchSingletonComponent(Class)
     * @see #tryFetchSingletonComponent(Class)
     */
    boolean hasSingleton(Class<? extends Component> component);

    /**
     * Tries to fetch a single {@link Entity} of the specified {@link Archetype}.
     */
    Optional<Entity> tryFetchSingleton(Archetype archetype);

    /**
     * Tries to fetch a single {@link Entity} of the specified {@link Archetype} but will throw
     * {@link IllegalArgumentException} when there is not exactly one entity matching the {@link Archetype}.
     */
    default Entity fetchSingleton(final Archetype archetype) {
        return tryFetchSingleton(archetype).orElseThrow(() -> new IllegalStateException("did not find singleton entity"));
    }

    Environment addEntity(String name, Component... components);

    Environment addEntity(int id, Component... components);

    Environment addEntity(int id, String name, Component... components);

    Environment addEntity(Component... components);

    Environment addEntity(Entity entity);

    /**
     * Adds an {@link EntitySystem} to the {@link Environment} with default {@link Order.SystemOrder#SIMULATION}.
     */
    Environment addSystem(EntitySystem system);

    /**
     * Adds an {@link EntitySystem} to the {@link Environment} with the given {@link Order.SystemOrder} (overwrites annotated {@link Order.SystemOrder} if present).
     */
    Environment addSystem(Order.SystemOrder order, EntitySystem system);

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
     * Removes all {@link Entity entities} matching the specified {@link Archetype}.
     */
    default Environment removeAll(final Archetype archetype) {
        remove(new ArrayList<>(fetchAll(archetype)));
        return this;
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

    /**
     * Returns the total count of entities in this environment.
     */
    long entityCount();

    /**
     * Returns the count of entities matching the given {@link Archetype} in this environment.
     */
    long entityCount(Archetype archetype);

    boolean contains(Archetype archetype);

    Environment addEntities(Entity... entities);

    boolean isSystemPresent(Class<? extends EntitySystem> type);

    /**
     * Removes all {@link Component}s with the given type from all {@link #entities()}.
     *
     * @param componentType the type of the {@link Component} to remove
     */
    Environment removeAllComponentsOfType(Class<? extends Component> componentType);

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
    Environment saveToFile(String name);

    /**
     * Loads a previously created savegame file and restores the saved
     * {@link #entities()} in the {@link Environment}.
     */
    Environment loadFromFile(String name);

    /**
     * Deletes the savegame with the given name. Triggers an {@link Exception} if
     * there is no such savegame.
     */
    Environment deleteSavegameFile(String name);

    /**
     * Returns true if there is a savegame with the given name. Value is cached for a second to prevent
     * excessive disc usage.
     */
    boolean savegameFileExists(String name);

    /**
     * Adds all systems needed for physics support in this {@link Environment}.
     *
     * @see MovementPathSystem
     * @see MovementTargetSystem
     * @see CollisionDetectionSystem
     * @see FrictionSystem
     * @see GravitySystem
     * @see MagnetSystem
     * @see CursorAttachmentSystem
     * @see OptimizePhysicsPerformanceSystem
     * @see PhysicsSystem
     * @see ChaoticMovementSystem
     * @see PhysicsGridUpdateSystem
     */
    Environment enablePhysics();

    /**
     * Adds systems needed for various rendering purposes.
     *
     * @see RenderSceneTransitionSystem
     * @see RenderUiSystem
     * @see MovementRotationSystem
     * @see FixedRotationSystem
     * @see FlipSpriteSystem
     * @see RenderSystem
     * @see FixedSpinSystem
     * @see CameraSystem
     */
    Environment enableRendering();

    /**
     * Adds systems needed for tweening.
     *
     * @see TweenSystem
     * @see TweenDestroySystem
     * @see TweenPositionSystem
     * @see TweenOpacitySystem
     * @see TweenLightSystem
     * @see TweenSpinSystem
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

    /**
     * Adds systems for particle effects.
     *
     * @see ParticleEmitterSystem
     * @see ParticleInteractionSystem
     * @see ParticleBurstSystem
     */
    Environment enableParticles();

    /**
     * Adds system for playing sounds.
     *
     * @see SoundSystem
     */
    Environment enableAudio();

    /**
     * Adds all systems including particle effects, tweening, logic, rendering, physics and light (so beware, it might get a little dark if you forget to add some lights).
     *
     * @see #enableParticles()
     * @see #enableLight()
     * @see #enableLogic()
     * @see #enableRendering()
     * @see #enablePhysics()
     * @see #enableTweening()
     * @see #enableAudio()
     */
    Environment enableAllFeatures();
}