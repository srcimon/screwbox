package dev.screwbox.core.environment;

import dev.screwbox.core.environment.ai.PathMovementSystem;
import dev.screwbox.core.environment.ai.PatrolMovementSystem;
import dev.screwbox.core.environment.ai.TargetLockSystem;
import dev.screwbox.core.environment.ai.TargetMovementSystem;
import dev.screwbox.core.environment.audio.SoundSystem;
import dev.screwbox.core.environment.imports.ImportProfile;
import dev.screwbox.core.environment.controls.JumpControlSystem;
import dev.screwbox.core.environment.controls.LeftRightControlSystem;
import dev.screwbox.core.environment.controls.SuspendJumpControlSystem;
import dev.screwbox.core.environment.fluids.DiveSystem;
import dev.screwbox.core.environment.fluids.FloatRotationSystem;
import dev.screwbox.core.environment.fluids.FloatSystem;
import dev.screwbox.core.environment.fluids.FluidInteractionSystem;
import dev.screwbox.core.environment.fluids.FluidRenderSystem;
import dev.screwbox.core.environment.fluids.FluidSystem;
import dev.screwbox.core.environment.fluids.FluidTurbulenceSystem;
import dev.screwbox.core.environment.imports.SimpleBlueprint;
import dev.screwbox.core.environment.light.LightRenderSystem;
import dev.screwbox.core.environment.light.OptimizeLightPerformanceSystem;
import dev.screwbox.core.environment.logic.AreaTriggerSystem;
import dev.screwbox.core.environment.logic.StateSystem;
import dev.screwbox.core.environment.navigation.NavigationRegionComponent;
import dev.screwbox.core.environment.navigation.NavigationSystem;
import dev.screwbox.core.environment.particles.ParticleBurstSystem;
import dev.screwbox.core.environment.particles.ParticleEmitterSystem;
import dev.screwbox.core.environment.physics.ChaoticMovementSystem;
import dev.screwbox.core.environment.physics.CollisionDetailsSystem;
import dev.screwbox.core.environment.physics.CollisionSensorSystem;
import dev.screwbox.core.environment.physics.CursorAttachmentSystem;
import dev.screwbox.core.environment.physics.GravitySystem;
import dev.screwbox.core.environment.physics.MagnetSystem;
import dev.screwbox.core.environment.physics.OptimizePhysicsPerformanceSystem;
import dev.screwbox.core.environment.physics.PhysicsSystem;
import dev.screwbox.core.environment.physics.TailwindSystem;
import dev.screwbox.core.environment.rendering.*;
import dev.screwbox.core.environment.softphysics.RopeRenderSystem;
import dev.screwbox.core.environment.softphysics.RopeSystem;
import dev.screwbox.core.environment.softphysics.SoftBodyCollisionSystem;
import dev.screwbox.core.environment.softphysics.SoftBodyPressureSystem;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderSystem;
import dev.screwbox.core.environment.softphysics.SoftBodyShapeComponent;
import dev.screwbox.core.environment.softphysics.SoftBodySystem;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSystem;
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
     * Can be used to store configuration for an {@link EntitySystem} e.g. {@link NavigationRegionComponent}.
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
     * Can be used to store configuration for an {@link EntitySystem} e.g. {@link NavigationRegionComponent}.
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

    /**
     * Adds an {@link Entity} with the specified name and components to the {@link Environment}.
     */
    Environment addEntity(String name, Component... components);

    /**
     * Adds an {@link Entity} with the specified id and components to the {@link Environment}.
     */
    Environment addEntity(int id, Component... components);

    /**
     * Adds an {@link Entity} with the specified id, name and components to the {@link Environment}.
     */
    Environment addEntity(int id, String name, Component... components);

    /**
     * Adds an {@link Entity} with the specified components to the {@link Environment}.
     */
    Environment addEntity(Component... components);

    /**
     * Adds an {@link Entity} to the {@link Environment}.
     */
    Environment addEntity(Entity entity);

    /**
     * Adds an {@link EntitySystem} to the {@link Environment} with default {@link Order#SIMULATION}.
     */
    Environment addSystem(EntitySystem system);

    /**
     * Adds an {@link EntitySystem} to the {@link Environment} with the given {@link Order} (overwrites annotated {@link Order} if present).
     */
    Environment addSystem(Order order, EntitySystem system);

    /**
     * Adds multiple {@link Entity entities} to the {@link Environment}.
     */
    Environment addEntities(List<Entity> entities);

    /**
     * Adds the specified {@link EntitySystem} to the {@link Environment}. Will replace the {@link EntitySystem} if it is already present.
     */
    Environment addOrReplaceSystem(EntitySystem system);

    /**
     * Removes the {@link EntitySystem} from the {@link Environment}. Won't do anything if {@link EntitySystem} is not present.
     */
    Environment removeSystemIfPresent(Class<? extends EntitySystem> systemType);

    /**
     * Adds multiple {@link EntitySystem entity systems} to the {@link Environment}.
     */
    Environment addSystems(EntitySystem... systems);

    /**
     * Returns a list of all {@link Entity entities} matching the specified {@link Archetype}.
     */
    List<Entity> fetchAll(Archetype archetype);

    /**
     * Returns a list of all {@link Entity entities} that contain the specified {@link Component}.
     */
    default List<Entity> fetchAllHaving(Class<? extends Component> component) {
        return fetchAll(Archetype.of(component));
    }

    /**
     * Returns a list of all {@link Entity entities} that contain both of the specified {@link Component components}.
     */
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
     * Returns the count of entities matching the given {@link Archetype} in this {@link Environment}.
     */
    long entityCount(Archetype archetype);

    /**
     * Returns {@code true} if the {@link Environment} contains at least one {@link Entity} matching the specified {@link Archetype}.
     */
    boolean contains(Archetype archetype);

    /**
     * Adds multiple {@link Entity entities} to the {@link Environment}.
     */
    Environment addEntities(Entity... entities);

    /**
     * Returns {@code true} if the {@link Environment} contains an instance of the specified {@link EntitySystem} class.
     */
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

    //TODO all the things
    Environment runImport(SimpleBlueprint... blueprints);

    //TODO all the things
    <T, I> Environment runImport(ImportProfile<T, I> config);

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
     * @see GravitySystem
     * @see MagnetSystem
     * @see TailwindSystem
     * @see CursorAttachmentSystem
     * @see CollisionDetailsSystem
     * @see OptimizePhysicsPerformanceSystem
     * @see PhysicsSystem
     * @see ChaoticMovementSystem
     */
    Environment enablePhysics();

    /**
     * Adds all systems needed for navigation support in this {@link Environment}.
     *
     * @see NavigationSystem
     */
    Environment enableNavigation();

    /**
     * Adds systems needed for various rendering purposes.
     *
     * @see RenderSceneTransitionSystem
     * @see RenderNotificationsSystem
     * @see RenderUiSystem
     * @see MotionRotationSystem
     * @see FixedRotationSystem
     * @see FlipSpriteSystem
     * @see RenderSystem
     * @see FixedSpinSystem
     * @see CameraSystem
     * @see AutoTileSystem
     */
    Environment enableRendering();

    /**
     * Adds systems needed when working with fluids.
     *
     * @see FluidInteractionSystem
     * @see FloatSystem
     * @see DiveSystem
     * @see FluidTurbulenceSystem
     * @see FluidSystem
     * @see FluidRenderSystem
     * @see FloatRotationSystem
     */
    Environment enableFluids();

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
     * Adds systems to support soft physics like ropes and soft bodies.
     *
     * @see SoftPhysicsSystem
     * @see SoftBodySystem
     * @see SoftBodyRenderSystem
     * @see SoftBodyCollisionSystem
     * @see SoftBodyShapeComponent
     * @see SoftBodyPressureSystem
     * @see RopeRenderSystem
     * @see RopeSystem
     */
    Environment enableSoftPhysics();

    /**
     * Adds systems for particle effects.
     *
     * @see ParticleEmitterSystem
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

    /**
     * Returns the draw order of the currently executed {@link EntitySystem}.
     *
     * @since 3.14.0
     */
    int currentDrawOrder();

    /**
     * Allocates an artificial id that is not already present within the {@link Environment}.
     * Allocated ids are always negative. Allocating ids does not block this ids from being added manually to the {@link Environment}.
     *
     * @since 3.15.0
     */
    int allocateId();

    /**
     * Peeks the next artificial id that will be allocated without actually allocating it.
     * Peeked ids are always negative.
     *
     * @see #allocateId()
     * @since 3.15.0
     */
    int peekId();
}