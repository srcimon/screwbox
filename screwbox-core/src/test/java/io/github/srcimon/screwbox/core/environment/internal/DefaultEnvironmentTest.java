package io.github.srcimon.screwbox.core.environment.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.LightRenderSystem;
import io.github.srcimon.screwbox.core.environment.light.OptimizeLightPerformanceSystem;
import io.github.srcimon.screwbox.core.environment.logic.AreaTriggerSystem;
import io.github.srcimon.screwbox.core.environment.logic.StateSystem;
import io.github.srcimon.screwbox.core.environment.physics.*;
import io.github.srcimon.screwbox.core.environment.rendering.*;
import io.github.srcimon.screwbox.core.environment.tweening.*;
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
import static org.assertj.core.api.Assertions.*;

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

        assertThat(environment.fetchById(4)).isNotEmpty();
        assertThat(environment.entityCount()).isEqualTo(1);
    }

    @Test
    void addEntity_byComponent_addsEntity() {
        environment.addEntity(new TransformComponent(Vector.zero()));

        assertThat(environment.entities()).allMatch(entity -> entity.hasComponent(TransformComponent.class));
        assertThat(environment.entityCount()).isEqualTo(1);
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
    void fetchById_idNotPresent_isEmpty() {
        Optional<Entity> entity = environment.fetchById(149);

        assertThat(entity).isEmpty();
    }

    @Test
    void fetchById_idPresent_returnsEntityWithMatchingId() {
        Entity entityWithMatchingId = new Entity(149);
        environment.addEntity(new Entity(20)).addEntity(entityWithMatchingId);

        Optional<Entity> entity = environment.fetchById(149);

        assertThat(entity).isEqualTo(Optional.of(entityWithMatchingId));
    }

    @Test
    void createSavegamenameNull_throwsException() {
        assertThatThrownBy(() -> environment.createSavegame(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void createSavegame_validName_createsSaveFile() {
        environment.createSavegame(SAVEGAME_NAME);

        assertThat(Path.of(SAVEGAME_NAME)).exists();
    }

    @Test
    void savegameExists_nameNull_throwsException() {
        assertThatThrownBy(() -> environment.savegameExists(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void savegameExists_doesntExist_false() {
        boolean exists = environment.savegameExists(SAVEGAME_NAME);

        assertThat(exists).isFalse();
    }

    @Test
    void savegameExists_invalidName_throwsException() {
        assertThatThrownBy(() -> environment.savegameExists("test."))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("savegame name is invalid: test.");

        assertThatThrownBy(() -> environment.savegameExists("test" + File.separator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("savegame name is invalid: test" + File.separator);
    }

    @Test
    void savegameExists_exists_isTrue() {
        environment.createSavegame(SAVEGAME_NAME);

        boolean exists = environment.savegameExists(SAVEGAME_NAME);

        assertThat(exists).isTrue();
    }

    @Test
    void loadSavegame_nameNull_throwsException() {
        assertThatThrownBy(() -> environment.loadSavegame(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void loadSavegame_doesntExist_throwsException() {
        assertThatThrownBy(() -> environment.loadSavegame("not-there.sav"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("could not load savegame: not-there.sav");
    }

    @Test
    void deleteSavegame_nameIsNull_throwsException() {
        assertThatThrownBy(() -> environment.deleteSavegame(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void deleteSavegame_savegameDoesntExist_throwsException() {
        assertThatThrownBy(() -> environment.deleteSavegame("not-there.sav"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("could not delete savegame: not-there.sav");
    }

    @Test
    void deleteSavegame_savegameExists_deletesSAve() {
        environment.createSavegame(SAVEGAME_NAME);

        environment.deleteSavegame(SAVEGAME_NAME);

        assertThat(environment.savegameExists(SAVEGAME_NAME)).isFalse();
    }

    @Test
    void loadSavegame_saveExists_replacesExistingEntitiesWithSavedOnes() {
        environment.addEntity(1, new TransformComponent($$(0, 0, 32, 32)));
        environment.createSavegame(SAVEGAME_NAME);
        environment.clearEntities();
        environment.addEntity(2, new TransformComponent($$(0, 0, 32, 32)));

        environment.loadSavegame(SAVEGAME_NAME);

        assertThat(environment.fetchById(1)).isPresent();
        assertThat(environment.fetchById(2)).isEmpty();
    }

    @Test
    void addOrReplaceSystem_systemNull_throwsException() {
        assertThatThrownBy(() -> environment.addOrReplaceSystem(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("system must not be null");
    }

    @Test
    void addOrReplaceSystem_systemPresent_doesNothing() {
        TweenOpacitySystem oldSystem = new TweenOpacitySystem();
        TweenOpacitySystem newSystem = new TweenOpacitySystem();

        environment.addSystem(oldSystem);

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

        assertThat(environment.systems()).hasSize(4)
                .anyMatch(system -> system.getClass().equals(TweenSystem.class))
                .anyMatch(system -> system.getClass().equals(TweenDestroySystem.class))
                .anyMatch(system -> system.getClass().equals(TweenPositionSystem.class))
                .anyMatch(system -> system.getClass().equals(TweenOpacitySystem.class));
    }

    @Test
    void enableLogic_addsLogicSystems() {
        environment.enableLogic();

        assertThat(environment.systems()).hasSize(2)
                .anyMatch(system -> system.getClass().equals(AreaTriggerSystem.class))
                .anyMatch(system -> system.getClass().equals(StateSystem.class));
    }

    @Test
    void enableRendering_addsRenderingSystems() {
        environment.enableRendering();

        assertThat(environment.systems()).hasSize(5)
                .anyMatch(system -> system.getClass().equals(ReflectionRenderSystem.class))
                .anyMatch(system -> system.getClass().equals(RotateSpriteSystem.class))
                .anyMatch(system -> system.getClass().equals(FlipSpriteSystem.class))
                .anyMatch(system -> system.getClass().equals(ScreenTransitionSystem.class))
                .anyMatch(system -> system.getClass().equals(RenderSystem.class));
    }

    @Test
    void enablePhysics_addsPhysicsSystems() {
        environment.enablePhysics();

        assertThat(environment.systems()).hasSize(7)
                .anyMatch(system -> system.getClass().equals(AutomovementSystem.class))
                .anyMatch(system -> system.getClass().equals(GravitySystem.class))
                .anyMatch(system -> system.getClass().equals(MagnetSystem.class))
                .anyMatch(system -> system.getClass().equals(OptimizePhysicsPerformanceSystem.class))
                .anyMatch(system -> system.getClass().equals(ChaoticMovementSystem.class))
                .anyMatch(system -> system.getClass().equals(PhysicsSystem.class))
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
        assertThatThrownBy(() -> environment.addSystem(null, e -> e.stop()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("order must not be null");
    }

    @Test
    void addSystem_withOrderSystemNull_throwsException() {
        assertThatThrownBy(() -> environment.addSystem(SystemOrder.PREPARATION, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("system must not be null");
    }

    @Test
    void addSystem_orderGiven_addsSystemWithOrder() {
        List<String> systemsExecuted = new ArrayList<>();

        environment.addSystem(e -> systemsExecuted.add("first"));
        environment.addSystem(SystemOrder.PREPARATION, e -> systemsExecuted.add("second"));

        environment.update();

        assertThat(systemsExecuted).containsExactly("second", "first");
    }

    @Test
    void fetchSingletonComponent_componentNotPresent_isEmpty() {
        var singleton = environment.fetchSingletonComponent(TweenDestroyComponent.class);

        assertThat(singleton).isEmpty();
    }

    @Test
    void fetchSingletonComponent_singletonPresent_returnsComponent() {
        TweenDestroyComponent component = new TweenDestroyComponent();
        environment.addEntity(component);

        var singleton = environment.fetchSingletonComponent(TweenDestroyComponent.class);

        assertThat(singleton).contains(component);
    }

    @Test
    void fetchSingletonComponent_moreThanOneSingleton_returnsComponent() {
        environment.addEntity(new TweenDestroyComponent());
        environment.addEntity(new TweenDestroyComponent());

        assertThatThrownBy(() -> environment.fetchSingletonComponent(TweenDestroyComponent.class))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("singleton component has been found multiple times: TweenDestroyComponent");
    }

    @AfterEach
    void afterEach() throws IOException {
        if (Files.exists(SAVEGAME)) {
            Files.delete(SAVEGAME);
        }
    }
}
