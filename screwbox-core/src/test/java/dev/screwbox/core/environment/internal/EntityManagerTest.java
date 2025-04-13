package dev.screwbox.core.environment.internal;

import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EntityManagerTest {

    private EntityManager entityManager;

    @BeforeEach
    void beforeEach() {
        entityManager = new EntityManager();
    }

    @Test
    void removeEntity_changesNotDelayed_removesEntity() {
        Entity entityA = new Entity();
        Entity entityB = new Entity();
        entityManager.addEntity(entityA);
        entityManager.addEntity(entityB);

        entityManager.removeEntity(entityA);

        assertThat(entityManager.allEntities()).doesNotContain(entityA);
        assertThat(entityManager.allEntities()).contains(entityB);
    }

    @Test
    void removeEntity_changesDelayed_doesntDeleteEntitiesYet() {
        Entity entity = new Entity();
        entityManager.addEntity(entity);
        entityManager.delayChanges();

        entityManager.removeEntity(entity);

        assertThat(entityManager.allEntities()).hasSize(1);
    }

    @Test
    void removeEntity_changesNotDelayed_removesEntityFromArchetypeCaches() {
        Entity entity = new Entity().add(new PhysicsComponent());
        Archetype archetype = Archetype.of(PhysicsComponent.class);

        entityManager.removeEntity(entity);

        assertThat(entityManager.entitiesMatching(archetype)).isEmpty();

    }

    @Test
    void addEntity_changesNotDelayed_addsEntity() {
        entityManager.addEntity(new Entity());
        entityManager.addEntity(new Entity());

        assertThat(entityManager.allEntities()).hasSize(2);
    }

    @Test
    void addEntity_changesDelayed_doesntAddEntitiesYet() {
        entityManager.delayChanges();
        entityManager.addEntity(new Entity());
        entityManager.addEntity(new Entity());

        assertThat(entityManager.allEntities()).isEmpty();
    }

    @Test
    void pickUpChanges_pendingChanges_addsEntities() {
        entityManager.delayChanges();
        entityManager.addEntity(new Entity());
        entityManager.addEntity(new Entity());
        entityManager.pickUpChanges();

        assertThat(entityManager.allEntities()).hasSize(2);
    }

    @Test
    void entitiesMatching_archetypeNull_throwsException() {
        assertThatThrownBy(() -> entityManager.entitiesMatching(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("archetype must not be null");
    }

    @Test
    void entitiesMatching_oneEntityMatches_returnsMatchingEntity() {
        entityManager.addEntity(new Entity().add(new PhysicsComponent()));
        entityManager.addEntity(new Entity().add(new StaticColliderComponent()));
        Archetype archetype = Archetype.of(PhysicsComponent.class);

        var result = entityManager.entitiesMatching(archetype);

        assertThat(result).hasSize(1);
    }

    @Test
    void entitiesMatching_noMatches_returnsEmptyList() {
        entityManager.addEntity(new Entity().add(new PhysicsComponent()));
        entityManager.addEntity(new Entity().add(new StaticColliderComponent()));
        Archetype archetype = Archetype.of(TransformComponent.class);

        var result = entityManager.entitiesMatching(archetype);

        assertThat(result).isEmpty();
    }

    @Test
    void componentAddedToManagedEntity_changedDelayed_cachesAreNotRefreshed() {
        Archetype colliderEntities = Archetype.of(ColliderComponent.class);
        Entity entity = new Entity().add(new PhysicsComponent());
        entityManager.addEntity(entity);
        entityManager.entitiesMatching(colliderEntities); // create cached request

        entity.add(new ColliderComponent());

        assertThat(entityManager.entitiesMatching(colliderEntities)).isEmpty();
    }

    @Test
    void componentAddedToManagedEntity_pickedUpChanges_cachesAreRefreshed() {
        Archetype colliderEntities = Archetype.of(ColliderComponent.class);
        Entity entity = new Entity().add(new PhysicsComponent());
        entityManager.addEntity(entity);
        entityManager.entitiesMatching(colliderEntities); // create cached request

        entity.add(new ColliderComponent());

        entityManager.pickUpChanges();

        assertThat(entityManager.entitiesMatching(colliderEntities)).contains(entity);
    }

    @Test
    void findById_entityPresent_returnsEntity() {
        Entity entity = new Entity(123);

        entityManager.addEntity(entity);

        assertThat(entityManager.findById(123)).isEqualTo(entity);
    }

    @Test
    void findById_entityMissing_returnsNull() {
        Entity entity = new Entity(123);

        entityManager.addEntity(entity);

        assertThat(entityManager.findById(666)).isNull();
    }
}
