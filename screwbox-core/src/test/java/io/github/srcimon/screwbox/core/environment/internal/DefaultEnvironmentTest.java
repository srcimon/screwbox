package io.github.srcimon.screwbox.core.environment.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;
import io.github.srcimon.screwbox.core.environment.systems.RenderLightSystem;
import io.github.srcimon.screwbox.core.environment.systems.RenderSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        environment.addSystem(new RenderLightSystem());

        environment.toggleSystem(new RenderLightSystem());

        assertThat(environment.isSystemPresent(RenderLightSystem.class)).isFalse();
    }

    @Test
    void toggleSystem_systemNotPresent_addsSystem() {
        environment.toggleSystem(new RenderLightSystem());

        assertThat(environment.isSystemPresent(RenderLightSystem.class)).isTrue();
    }

    @Test
    void systems_returnsAllSystemsInOrder() {
        RenderLightSystem renderLightSystem = new RenderLightSystem();
        RenderSystem renderSystem = new RenderSystem();

        environment.addSystem(renderLightSystem).addSystem(renderSystem);

        assertThat(environment.systems()).containsExactly(renderSystem, renderLightSystem);
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

    @AfterEach
    void afterEach() throws IOException {
        if (Files.exists(SAVEGAME)) {
            Files.delete(SAVEGAME);
        }
    }
}
