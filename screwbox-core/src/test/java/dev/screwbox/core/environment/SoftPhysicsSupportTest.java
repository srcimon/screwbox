package dev.screwbox.core.environment;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;

@ExtendWith(EnvironmentExtension.class)
class SoftPhysicsSupportTest {

    private static final Vector POSITION = $(20, 10);

    @Test
    void createRope_startNull_throwsException(DefaultEnvironment environment) {
        assertThatThrownBy(() -> SoftPhysicsSupport.createRope(null, POSITION, 4, environment))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("start must not be null");
    }

    @Test
    void createRope_endNull_throwsException(DefaultEnvironment environment) {
        assertThatThrownBy(() -> SoftPhysicsSupport.createRope(POSITION, null, 4, environment))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("end must not be null");
    }

    @Test
    void createRope_twoNodes_throwsException(DefaultEnvironment environment) {
        var other = $(40, 10);
        assertThatThrownBy(() -> SoftPhysicsSupport.createRope(POSITION, other, 2, environment))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("nodeCount must be between 3 and 4096 (actual value: 2)");
    }

    @Test
    void createRope_startEqualsEnd_throwsException(DefaultEnvironment environment) {
        assertThatThrownBy(() -> SoftPhysicsSupport.createRope(POSITION, POSITION, 4, environment))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("rope start should be different from end");
    }

    @Test
    void createRope_idPool_throwsException() {
        var other = $(40, 10);
        assertThatThrownBy(() -> SoftPhysicsSupport.createRope(POSITION, other, 4, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("idPool must not be null");
    }

    @Test
    void createRope_validParameters_createsRope(DefaultEnvironment environment) {
        List<Entity> rope = SoftPhysicsSupport.createRope(POSITION, $(30, 10), 8, environment);

        assertThat(rope).hasSize(8).allMatch(node -> node.hasComponent(PhysicsComponent.class));

        assertThat(rope.getFirst().position()).isEqualTo(POSITION);
        assertThat(rope.getFirst().hasComponent(RopeComponent.class)).isTrue();
        assertThat(rope.getFirst().get(SoftLinkComponent.class).targetId).isEqualTo(rope.get(1).forceId());
        assertThat(rope.getFirst().get(SoftLinkComponent.class).length).isEqualTo(1.43, offset(0.01));

        assertThat(rope.getLast().hasComponent(SoftLinkComponent.class)).isFalse();
        assertThat(rope.getLast().position()).isEqualTo($(30, 10));
    }

    @Test
    void createStabilizedSoftBody_polygonNull_throwsException(DefaultEnvironment environment) {
        assertThatThrownBy(() -> SoftPhysicsSupport.createStabilizedSoftBody(null, environment))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("polygon must not be null");
    }

    @Test
    void createSoftBody_polygonNull_throwsException(DefaultEnvironment environment) {
        assertThatThrownBy(() -> SoftPhysicsSupport.createSoftBody(null, environment))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("polygon must not be null");
    }

    @Test
    void createSoftBody_idPoolNull_throwsException() {
        var polygon = Polygon.ofNodes(List.of($(20, 2), $(40, 3)));
        assertThatThrownBy(() -> SoftPhysicsSupport.createSoftBody(polygon, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("idPool must not be null");
    }

    @Test
    void createSoftBody_onlyOneNode_throwsException(DefaultEnvironment environment) {
        var polygon = Polygon.ofNodes(List.of($(20, 2)));
        assertThatThrownBy(() -> SoftPhysicsSupport.createSoftBody(polygon, environment))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("polygon must have between 2 and 4096 nodes (actual value: 1)");
    }

    @Test
    void createSoftBody_threeNodes_createsSoftBody(DefaultEnvironment environment) {
        List<Entity> softBody = SoftPhysicsSupport.createSoftBody(Polygon.ofNodes(List.of($(20, 2), $(40, 3), $(30, 20))), environment);

        assertThat(softBody)
            .hasSize(3)
            .allMatch(node -> node.hasComponent(SoftLinkComponent.class))
            .allMatch(node -> node.hasComponent(PhysicsComponent.class))
            .allMatch(node -> node.hasComponent(TransformComponent.class))
            .noneMatch(node -> node.hasComponent(SoftStructureComponent.class));

        assertThat(softBody.getFirst().position()).isEqualTo($(20, 2));
        assertThat(softBody.getFirst().get(SoftLinkComponent.class).targetId).isEqualTo(softBody.get(1).forceId());
        assertThat(softBody.getFirst().get(SoftLinkComponent.class).length).isEqualTo(20.02, offset(0.01));

        assertThat(softBody.getFirst().hasComponent(SoftBodyComponent.class)).isTrue();
        assertThat(softBody.getLast().position()).isEqualTo($(30, 20));
    }

    @Test
    void updateLinkLengths_noLinksWithinList_noException() {
        var noLinkEntities = List.of(new Entity());

        assertThatNoException().isThrownBy(() -> SoftPhysicsSupport.updateLinkLengths(noLinkEntities));
    }

    @Test
    void updateLinkLengths_entitiesWithLinksWithinList_initializesLengths() {
        Entity start = new Entity(0)
            .bounds(Bounds.atPosition(0, 3, 1, 1))
            .add(new SoftStructureComponent(List.of(1, 2)))
            .add(new SoftLinkComponent(3));

        var entities = List.of(
            start,
            new Entity(1)
                .bounds(Bounds.atPosition(10, 3, 1, 1)),
            new Entity(2)
                .bounds(Bounds.atPosition(20, 3, 1, 1)),
            new Entity(3)
                .bounds(Bounds.atPosition(30, 3, 1, 1)));

        SoftPhysicsSupport.updateLinkLengths(entities);

        assertThat(start.get(SoftStructureComponent.class).lengths[0]).isEqualTo(10.0);
        assertThat(start.get(SoftStructureComponent.class).lengths[1]).isEqualTo(20.0);
        assertThat(start.get(SoftLinkComponent.class).length).isEqualTo(30.0);
    }

    @Test
    void updateLinkLengths_targetEntityIsMissing_throwsException() {
        var entities = List.of(
            new Entity(0)
                .bounds(Bounds.atPosition(0, 3, 1, 1))
                .add(new SoftLinkComponent(3)));

        assertThatThrownBy(() -> SoftPhysicsSupport.updateLinkLengths(entities))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("missing target entity with id 3");

    }

    @Test
    void createStabilizedSoftBody_validParameters_createsSoftBody(DefaultEnvironment environment) {
        List<Entity> softBody = SoftPhysicsSupport.createStabilizedSoftBody(Polygon.ofNodes(List.of($(20, 2), $(40, 3), $(30, 20))), environment);

        assertThat(softBody)
            .hasSize(3)
            .allMatch(node -> node.hasComponent(SoftLinkComponent.class))
            .allMatch(node -> node.hasComponent(PhysicsComponent.class))
            .allMatch(node -> node.hasComponent(TransformComponent.class));

        assertThat(softBody.getFirst().position()).isEqualTo($(20, 2));
        assertThat(softBody.getFirst().get(SoftLinkComponent.class).targetId).isEqualTo(softBody.get(1).forceId());
        assertThat(softBody.getFirst().get(SoftLinkComponent.class).length).isEqualTo(20.02, offset(0.01));
        assertThat(softBody.getLast().get(SoftStructureComponent.class).targetIds[0]).isEqualTo(-2147483646);
        assertThat(softBody.getLast().get(SoftStructureComponent.class).lengths[0]).isEqualTo(19.72, offset(0.01));

        assertThat(softBody.getFirst().hasComponent(SoftBodyComponent.class)).isTrue();
        assertThat(softBody.getLast().position()).isEqualTo($(30, 20));
    }
}