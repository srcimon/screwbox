package io.github.srcimon.screwbox.core.environment.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.audio.SoundSystem;
import io.github.srcimon.screwbox.core.environment.core.QuitOnKeySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.LightRenderSystem;
import io.github.srcimon.screwbox.core.environment.light.OptimizeLightPerformanceSystem;
import io.github.srcimon.screwbox.core.environment.logic.AreaTriggerSystem;
import io.github.srcimon.screwbox.core.environment.logic.StateSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleBurstSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleInteractionSystem;
import io.github.srcimon.screwbox.core.environment.physics.*;
import io.github.srcimon.screwbox.core.environment.rendering.CameraSystem;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.FixedRotationSystem;
import io.github.srcimon.screwbox.core.environment.rendering.FixedSpinSystem;
import io.github.srcimon.screwbox.core.environment.rendering.FlipSpriteSystem;
import io.github.srcimon.screwbox.core.environment.rendering.MovementRotationSystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderOverLightSystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenLightSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacitySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenPositionSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenScaleSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenSystem;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.core.utils.Cache;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultEnvironmentTest {

    private static final String SAVEGAME_NAME = "mysave.sav";
    private static final Path SAVEGAME = Path.of(SAVEGAME_NAME);

    DefaultEnvironment environment;

    @Mock
    Engine engine;

    @BeforeEach
    void beforeEach() {
        environment = new DefaultEnvironment(engine);
    }

    @Test
    void addSystem_usingOrder_orderIsUsedInExecution() {
        final Cache<String, String> cache = new Cache<>();
        environment.addSystem(Order.SystemOrder.SIMULATION, e -> cache.put("key", "second"));
        environment.addSystem(Order.SystemOrder.PRESENTATION_BACKGROUND, e -> cache.put("key", "last"));
        environment.addSystem(Order.SystemOrder.OPTIMIZATION, e -> cache.put("key", "first"));
        environment.update();

        assertThat(cache.get("key")).contains("last");
    }

    @Test
    void addEntity_entityNull_exception() {
        assertThatThrownBy(() -> environment.addEntity((Entity) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void importSource_sourceNull_exception() {
        assertThatThrownBy(() -> environment.importSource((String) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Source must not be null");
    }

    @Test
    void importSource_sourceLiszNull_exception() {
        assertThatThrownBy(() -> environment.importSource((List<String>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Source must not be null");
    }

    @Test
    void addEntity_byIdAndComponent_addsEntity() {
        environment.addEntity(4, new TransformComponent(Vector.zero()));

        assertThat(environment.tryFetchById(4)).isNotEmpty();
        assertThat(environment.entityCount()).isEqualTo(1);
    }

    @Test
    void addEntity_byComponent_addsEntity() {
        environment.addEntity(new TransformComponent(Vector.zero()));

        assertThat(environment.entities()).allMatch(entity -> entity.hasComponent(TransformComponent.class));
        assertThat(environment.entityCount()).isEqualTo(1);
    }

    @Test
    void entityCount_oneEntityOfTwoMatchesArchetype_returnsOne() {
        environment.addEntity(new TransformComponent());
        environment.addEntity(new RenderComponent());

        assertThat(environment.entityCount(Archetype.of(TransformComponent.class))).isOne();
    }

    @Test
    void addEntity_freshEntity_addsEntity() {
        Entity freshEntity = new Entity();

        environment.addEntity(freshEntity);

        assertThat(environment.entities()).contains(freshEntity);
        assertThat(environment.entityCount()).isEqualTo(1);
    }

    @Test
    void toggleSystem_systemPresent_removesSystem() {
        environment.addSystem(new LightRenderSystem());

        environment.toggleSystem(new LightRenderSystem());

        assertThat(environment.isSystemPresent(LightRenderSystem.class)).isFalse();
    }

    @Test
    void toggleSystem_systemNotPresent_addsSystem() {
        environment.toggleSystem(new LightRenderSystem());

        assertThat(environment.isSystemPresent(LightRenderSystem.class)).isTrue();
    }

    @Test
    void systems_returnsAllSystemsInOrder() {
        LightRenderSystem lightRenderSystem = new LightRenderSystem();
        RenderSystem renderSystem = new RenderSystem();

        environment.addSystem(lightRenderSystem).addSystem(renderSystem);

        assertThat(environment.systems()).containsExactly(renderSystem, lightRenderSystem);
    }

    @Test
    void tryFetchById_idNotPresent_isEmpty() {
        Optional<Entity> entity = environment.tryFetchById(149);

        assertThat(entity).isEmpty();
    }

    @Test
    void tryFetchById_idPresent_returnsEntityWithMatchingId() {
        Entity entityWithMatchingId = new Entity(149);
        environment.addEntity(new Entity(20)).addEntity(entityWithMatchingId);

        Optional<Entity> entity = environment.tryFetchById(149);

        assertThat(entity).isEqualTo(Optional.of(entityWithMatchingId));
    }

    @Test
    void saveToFile_nameNull_throwsException() {
        assertThatThrownBy(() -> environment.saveToFile(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void saveToFile_validName_createsSaveFile() {
        environment.saveToFile(SAVEGAME_NAME);

        assertThat(Path.of(SAVEGAME_NAME)).exists();
    }

    @Test
    void savegameFileExists_nameNull_throwsException() {
        assertThatThrownBy(() -> environment.savegameFileExists(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void savegameFileExists_doesntExist_false() {
        boolean exists = environment.savegameFileExists(SAVEGAME_NAME);

        assertThat(exists).isFalse();
    }

    @Test
    void savegameFileExists_invalidName_throwsException() {
        assertThatThrownBy(() -> environment.savegameFileExists("test."))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("savegame name is invalid: test.");

        assertThatThrownBy(() -> environment.savegameFileExists("test" + File.separator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("savegame name is invalid: test" + File.separator);
    }

    @Test
    void savegameExists_File_exists_isTrue() {
        environment.saveToFile(SAVEGAME_NAME);

        boolean exists = environment.savegameFileExists(SAVEGAME_NAME);

        assertThat(exists).isTrue();
    }

    @Test
    void loadFromFile_nameNull_throwsException() {
        assertThatThrownBy(() -> environment.loadFromFile(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void loadFromFile_doesntExist_throwsException() {
        assertThatThrownBy(() -> environment.loadFromFile("not-there.sav"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("could not load savegame: not-there.sav");
    }

    @Test
    void deleteSavegame_File_nameIsNull_throwsException() {
        assertThatThrownBy(() -> environment.deleteSavegameFile(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void deleteSavegame_savegameFileDoesntExist_throwsException() {
        assertThatThrownBy(() -> environment.deleteSavegameFile("not-there.sav"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("could not delete savegame: not-there.sav");
    }

    @Test
    void deleteSavegame_savegameFileExists_deletesSAve() {
        environment.saveToFile(SAVEGAME_NAME);

        environment.deleteSavegameFile(SAVEGAME_NAME);

        assertThat(environment.savegameFileExists(SAVEGAME_NAME)).isFalse();
    }

    @Test
    void loadFromFile_saveExists_replacesExistingEntitiesWithSavedOnes() {
        environment.addEntity(1, new TransformComponent($$(0, 0, 32, 32)));
        environment.saveToFile(SAVEGAME_NAME);
        environment.clearEntities();
        environment.addEntity(2, new TransformComponent($$(0, 0, 32, 32)));

        environment.loadFromFile(SAVEGAME_NAME);

        assertThat(environment.tryFetchById(1)).isPresent();
        assertThat(environment.tryFetchById(2)).isEmpty();
    }

    @Test
    void addOrReplaceSystem_systemNull_throwsException() {
        assertThatThrownBy(() -> environment.addOrReplaceSystem(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("system must not be null");
    }

    @Test
    void addOrReplaceSystem_systemPresent_replacesSystem() {
        QuitOnKeySystem oldSystem = new QuitOnKeySystem();
        QuitOnKeySystem newSystem = new QuitOnKeySystem();

        environment.addSystem(oldSystem);

        environment.addOrReplaceSystem(newSystem);

        assertThat(environment.systems()).containsExactly(newSystem);
    }

    @Test
    void addOrReplaceSystem_systemPresentAndUpdatesBetween_replacesSystem() {
        when(engine.keyboard()).thenReturn(mock(Keyboard.class));

        QuitOnKeySystem oldSystem = new QuitOnKeySystem();
        QuitOnKeySystem newSystem = new QuitOnKeySystem();

        environment.addSystem(oldSystem);

        environment.update();

        environment.addOrReplaceSystem(newSystem);

        assertThat(environment.systems()).containsExactly(newSystem);
    }

    @Test
    void addOrReplaceSystem_systemMissing_addsSystem() {
        environment.addOrReplaceSystem(new TweenOpacitySystem());

        assertThat(environment.systems()).hasSize(1).allMatch(system -> system.getClass().equals(TweenOpacitySystem.class));
    }

    @Test
    void removeSystemIfPresent_notPresent_noException() {
        assertThatNoException().isThrownBy(() -> environment.removeSystemIfPresent(TweenOpacitySystem.class));
    }

    @Test
    void removeSystemIfPresent_systemPresent_removesSystem() {
        environment.addSystem(new TweenOpacitySystem());

        environment.removeSystemIfPresent(TweenOpacitySystem.class);

        assertThat(environment.systems()).isEmpty();
    }

    @Test
    void enableTweening_addsTweeningSystems() {
        environment.enableTweening();

        assertThat(environment.systems()).hasSize(7)
                .anyMatch(system -> system.getClass().equals(TweenSystem.class))
                .anyMatch(system -> system.getClass().equals(TweenDestroySystem.class))
                .anyMatch(system -> system.getClass().equals(TweenScaleSystem.class))
                .anyMatch(system -> system.getClass().equals(TweenScaleSystem.class))
                .anyMatch(system -> system.getClass().equals(TweenLightSystem.class))
                .anyMatch(system -> system.getClass().equals(TweenPositionSystem.class))
                .anyMatch(system -> system.getClass().equals(TweenOpacitySystem.class));
    }

    @Test
    void enableAudio_addsAudioSystem() {
        environment.enableAudio();

        assertThat(environment.systems()).hasSize(1)
                .anyMatch(system -> system.getClass().equals(SoundSystem.class));
    }

    @Test
    void enableLogic_addsLogicSystems() {
        environment.enableLogic();

        assertThat(environment.systems()).hasSize(2)
                .anyMatch(system -> system.getClass().equals(AreaTriggerSystem.class))
                .anyMatch(system -> system.getClass().equals(StateSystem.class));
    }

    @Test
    void enableParticles_addsParticleSystems() {
        environment.enableParticles();

        assertThat(environment.systems()).hasSize(3)
                .anyMatch(system -> system.getClass().equals(ParticleEmitterSystem.class))
                .anyMatch(system -> system.getClass().equals(ParticleInteractionSystem.class))
                .anyMatch(system -> system.getClass().equals(ParticleBurstSystem.class));
    }

    @Test
    void enableRendering_addsRenderingSystems() {
        environment.enableRendering();

        assertThat(environment.systems()).hasSize(9)
                .anyMatch(system -> system.getClass().equals(MovementRotationSystem.class))
                .anyMatch(system -> system.getClass().equals(FixedRotationSystem.class))
                .anyMatch(system -> system.getClass().equals(FixedSpinSystem.class))
                .anyMatch(system -> system.getClass().equals(CameraSystem.class))
                .anyMatch(system -> system.getClass().equals(FlipSpriteSystem.class))
                .anyMatch(system -> system.getClass().equals(RenderOverLightSystem.class))
                .anyMatch(system -> system.getClass().equals(RenderSystem.class));
    }

    @Test
    void enablePhysics_addsPhysicsSystems() {
        environment.enablePhysics();

        assertThat(environment.systems()).hasSize(12)
                .anyMatch(system -> system.getClass().equals(MovementPathSystem.class))
                .anyMatch(system -> system.getClass().equals(GravitySystem.class))
                .anyMatch(system -> system.getClass().equals(AttachmentSystem.class))
                .anyMatch(system -> system.getClass().equals(MovementTargetSystem.class))
                .anyMatch(system -> system.getClass().equals(MagnetSystem.class))
                .anyMatch(system -> system.getClass().equals(FrictionSystem.class))
                .anyMatch(system -> system.getClass().equals(OptimizePhysicsPerformanceSystem.class))
                .anyMatch(system -> system.getClass().equals(CursorAttachmentSystem.class))
                .anyMatch(system -> system.getClass().equals(ChaoticMovementSystem.class))
                .anyMatch(system -> system.getClass().equals(PhysicsSystem.class))
                .anyMatch(system -> system.getClass().equals(PhysicsGridUpdateSystem.class))
                .anyMatch(system -> system.getClass().equals(CollisionDetectionSystem.class));
    }

    @Test
    void enableLight_addsLightSystems() {
        environment.enableLight();

        assertThat(environment.systems()).hasSize(2)
                .anyMatch(system -> system.getClass().equals(LightRenderSystem.class))
                .anyMatch(system -> system.getClass().equals(OptimizeLightPerformanceSystem.class));
    }

    @Test
    void addSystem_orderNull_throwsException() {
        assertThatThrownBy(() -> environment.addSystem(null, Engine::stop))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("order must not be null");
    }

    @Test
    void addSystem_withOrderSystemNull_throwsException() {
        assertThatThrownBy(() -> environment.addSystem(Order.SystemOrder.PREPARATION, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("system must not be null");
    }

    @Test
    void addSystem_orderGiven_addsSystemWithOrder() {
        List<String> systemsExecuted = new ArrayList<>();

        environment.addSystem(e -> systemsExecuted.add("first"));
        environment.addSystem(Order.SystemOrder.PREPARATION, e -> systemsExecuted.add("second"));

        environment.update();

        assertThat(systemsExecuted).containsExactly("second", "first");
    }

    @Test
    void tryFetchSingletonComponent_componentNotPresent_isEmpty() {
        var singleton = environment.tryFetchSingletonComponent(ColliderComponent.class);

        assertThat(singleton).isEmpty();
    }

    @Test
    void tryFetchSingletonComponent_singletonPresent_returnsComponent() {
        ColliderComponent component = new ColliderComponent();
        environment.addEntity(component);

        var singleton = environment.tryFetchSingletonComponent(ColliderComponent.class);

        assertThat(singleton).contains(component);
    }

    @Test
    void tryFetchSingletonComponent_moreThanOneSingleton_throwsException() {
        environment.addEntity(new ColliderComponent());
        environment.addEntity(new ColliderComponent());

        assertThatThrownBy(() -> environment.tryFetchSingletonComponent(ColliderComponent.class))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("singleton has been found multiple times: ColliderComponent");
    }

    @Test
    void tryFetchSingleton_componentNotPresent_isEmpty() {
        var singleton = environment.tryFetchSingleton(ColliderComponent.class);

        assertThat(singleton).isEmpty();
    }

    @Test
    void tryFetchSingleton_singletonPresent_returnsEntity() {
        var entity = new Entity().add(new ColliderComponent());
        environment.addEntity(entity);

        var singleton = environment.tryFetchSingleton(ColliderComponent.class);

        assertThat(singleton).contains(entity);
    }

    @Test
    void tryFetchSingleton_moreThanOneSingleton_throwsException() {
        environment.addEntity(new ColliderComponent());
        environment.addEntity(new ColliderComponent());

        assertThatThrownBy(() -> environment.tryFetchSingleton(ColliderComponent.class))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("singleton has been found multiple times: ColliderComponent");
    }

    @Test
    void hasSingleton_componentNotPresent_isFalse() {
        var singleton = environment.hasSingleton(ColliderComponent.class);

        assertThat(singleton).isFalse();
    }

    @Test
    void hasSingleton_singletonPresent_isTrue() {
        environment.addEntity(new ColliderComponent());

        var singleton = environment.hasSingleton(ColliderComponent.class);

        assertThat(singleton).isTrue();
    }

    @Test
    void hasSingleton_moreThanOneSingleton_throwsException() {
        environment.addEntity(new ColliderComponent());
        environment.addEntity(new ColliderComponent());

        assertThatThrownBy(() -> environment.hasSingleton(ColliderComponent.class))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("singleton has been found multiple times: ColliderComponent");
    }

    @Test
    void fetchSingletonComponent_singletonFound_returnsSingleton() {
        var collider = new ColliderComponent();

        environment.addEntity(collider);

        var singleton = environment.fetchSingletonComponent(ColliderComponent.class);

        assertThat(singleton).isEqualTo(collider);
    }

    @Test
    void fetchSingletonComponent_singletonNotFound_throwsException() {
        assertThatThrownBy(() -> environment.fetchSingletonComponent(ColliderComponent.class))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("singleton component has not been found: ColliderComponent");
    }

    @Test
    void removeAllComponentsOfType_twoEntitiesWithGivenComponentAfterUpdate_componentNoLongerPresent() {
        environment.addEntity(1, new CameraTargetComponent());
        environment.addEntity(2, new CollisionDetectionComponent(), new PhysicsComponent());
        environment.addEntity(3, new CollisionDetectionComponent());

        environment.removeAllComponentsOfType(CollisionDetectionComponent.class);

        environment.update();

        assertThat(environment.fetchAllHaving(CollisionDetectionComponent.class)).isEmpty();
        assertThat(environment.tryFetchById(1)).isNotEmpty();
        assertThat(environment.tryFetchById(2)).isNotEmpty();
        assertThat(environment.tryFetchById(3)).isEmpty();
    }

    @Test
    void removeAll_removesAllMatchingEntities() {
        environment.addEntity(1, new CameraTargetComponent());
        environment.addEntity(2, new CollisionDetectionComponent(), new PhysicsComponent());
        environment.addEntity(3, new CollisionDetectionComponent());

        environment.removeAll(Archetype.of(CollisionDetectionComponent.class));

        environment.update();

        assertThat(environment.tryFetchById(1)).isNotEmpty();
        assertThat(environment.tryFetchById(2)).isEmpty();
        assertThat(environment.tryFetchById(3)).isEmpty();
    }

    @Test
    void enableAllFeatures_noSystemPresent_addsAllSystems() {
        environment.enableAllFeatures();

        assertThat(environment.systems()).hasSize(36)
                .anyMatch(system -> system.getClass().equals(PhysicsSystem.class));
    }

    @AfterEach
    void afterEach() throws IOException {
        if (Files.exists(SAVEGAME)) {
            Files.delete(SAVEGAME);
        }
    }
}
