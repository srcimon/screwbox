package dev.screwbox.core.environment;

import dev.screwbox.core.environment.ai.PathMovementSystem;
import dev.screwbox.core.environment.ai.PatrolMovementSystem;
import dev.screwbox.core.environment.ai.TargetLockSystem;
import dev.screwbox.core.environment.ai.TargetMovementSystem;
import dev.screwbox.core.environment.audio.SoundSystem;
import dev.screwbox.core.environment.controls.JumpControlSystem;
import dev.screwbox.core.environment.controls.LeftRightControlSystem;
import dev.screwbox.core.environment.controls.SuspendJumpControlSystem;
import dev.screwbox.core.environment.light.LightRenderSystem;
import dev.screwbox.core.environment.light.OptimizeLightPerformanceSystem;
import dev.screwbox.core.environment.logic.AreaTriggerSystem;
import dev.screwbox.core.environment.logic.StateSystem;
import dev.screwbox.core.environment.particles.ParticleBurstSystem;
import dev.screwbox.core.environment.particles.ParticleEmitterSystem;
import dev.screwbox.core.environment.particles.ParticleInteractionSystem;
import dev.screwbox.core.environment.physics.*;
import dev.screwbox.core.environment.rendering.*;
import dev.screwbox.core.environment.tweening.TweenDestroySystem;
import dev.screwbox.core.environment.tweening.TweenLightSystem;
import dev.screwbox.core.environment.tweening.TweenOpacitySystem;
import dev.screwbox.core.environment.tweening.TweenPositionSystem;
import dev.screwbox.core.environment.tweening.TweenShaderSystem;
import dev.screwbox.core.environment.tweening.TweenSpinSystem;
import dev.screwbox.core.environment.tweening.TweenSystem;
import dev.screwbox.core.scenes.Scene;

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
 * @see <a href="http://screwbox.dev/docs/core-modules/environment">Documentation</a>
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

    /**
     * Removes the specified {@link Entity}.
     */
    Environment remove(Entity entity);

    /**
     * Removes all specified {@link Entity entities}.
     */
    Environment remove(List<Entity> entities);

    /**
     * Removes all current {@link Entity}s. All {@link EntitySystem}s stay untouched.
     */
    Environment clearEntities();

    /**
     * Adds the specified {@link EntitySystem} when not present or removes it when present.
     */
    Environment toggleSystem(EntitySystem entitySystem);

    /**
     * Removes the specified {@link EntitySystem}.
     */
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
     * Provides a compact syntax for importing {@link Entity entities} from a custom source
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
     * Returns all {@link EntitySystem entity systems} currently attached.
     *
     * @return all currently attached {@link EntitySystem entity systems}.
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
     * @see CollisionSensorSystem
     * @see FrictionSystem
     * @see GravitySystem
     * @see FluidSystem
     * @see MagnetSystem
     * @see FluidInteractionSystem
     * @see FloatSystem
     * @see DiveSystem
     * @see CursorAttachmentSystem
     * @see CollisionDetailsSystem
     * @see OptimizePhysicsPerformanceSystem
     * @see AirFrictionSystem
     * @see PhysicsSystem
     * @see ChaoticMovementSystem
     * @see PhysicsGridUpdateSystem
     */
    Environment enablePhysics();

    /**
     * Adds systems needed for various rendering purposes.
     *
     * @see RenderSceneTransitionSystem
     * @see FluidRenderSystem
     * @see RenderNotificationsSystem
     * @see FloatRotationSystem
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
     * Adds systems needed for ai.
     *
     * @see PatrolMovementSystem
     * @see PathMovementSystem
     * @see TargetMovementSystem
     * @see TargetLockSystem
     * @since 2.12.0
     */
    Environment enableAi();

    /**
     * Adds systems needed for controlling the player {@link Entity}.
     *
     * @see JumpControlSystem
     * @see SuspendJumpControlSystem
     * @see LeftRightControlSystem
     * @since 2.15.0
     */
    Environment enableControls();

    /**
     * Adds systems needed for tweening.
     *
     * @see TweenSystem
     * @see TweenDestroySystem
     * @see TweenPositionSystem
     * @see TweenOpacitySystem
     * @see TweenLightSystem
     * @see TweenSpinSystem
     * @see TweenShaderSystem
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
     * Adds systems for light rendering. Enables light rendering in the {@link Environment}.
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
     * Adds all systems including particle effects, tweening, logic, rendering, physics and light.
     *
     * @see #enableAi()
     * @see #enableParticles()
     * @see #enableLight()
     * @see #enableLogic()
     * @see #enableRendering()
     * @see #enablePhysics()
     * @see #enableTweening()
     * @see #enableAudio()
     */
    Environment enableAllFeatures();

    /**
     * Adds all {@link EntitySystem entity systems} that reside in specified package.
     * Ignores all {@link EntitySystem entity systems} that don't have a default constructor.
     *
     * @throws IllegalArgumentException when no entity system could be added from package
     * @since 2.13.0
     */
    Environment addSystemsFromPackage(String packageName);
}