package io.github.simonbas.screwbox.core.entities.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Component;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntityState;
import io.github.simonbas.screwbox.core.entities.components.StateComponent;
import io.github.simonbas.screwbox.core.entities.internal.DefaultEntities;
import io.github.simonbas.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(EntitiesExtension.class)
class StateSystemTest {

    private static class CounterComponent implements Component {
        private static final long serialVersionUID = 1L;
        public int count = 0;
    }

    private static class NextStateIsNull implements EntityState {

        private static final long serialVersionUID = 1L;

        @Override
        public EntityState update(Entity entity, Engine engine) {
            return null;
        }

    }

    private static class BoringState implements EntityState {

        private static final long serialVersionUID = 1L;

        @Override
        public void enter(Entity entity, Engine engine) {
            entity.get(CounterComponent.class).count++;
        }

        @Override
        public EntityState update(Entity entity, Engine engine) {
            return this;
        }

        @Override
        public void exit(Entity entity, Engine engine) {
            entity.get(CounterComponent.class).count++;
        }

    }

    private class PingState implements EntityState {

        private static final long serialVersionUID = 1L;

        @Override
        public void enter(Entity entity, Engine engine) {
            entity.get(CounterComponent.class).count++;
        }

        @Override
        public EntityState update(Entity entity, Engine engine) {
            return new PongState();
        }

    }

    private class PongState implements EntityState {

        private static final long serialVersionUID = 1L;

        @Override
        public EntityState update(Entity entity, Engine engine) {
            return new PingState();
        }

        @Override
        public void exit(Entity entity, Engine engine) {
            entity.get(CounterComponent.class).count++;
        }

    }

    @Test
    void update_firstStateEnterMethodIsExecuted(DefaultEntities entities) {
        Entity entity = new Entity().add(
                new StateComponent(new PingState()),
                new CounterComponent());

        entities.add(entity);
        entities.add(new StateSystem());

        entities.update();

        assertThat(entity.get(StateComponent.class).state).isInstanceOf(PingState.class);
        assertThat(entity.get(CounterComponent.class).count).isEqualTo(1);
    }

    @Test
    void update_nextStateIsNull_throwsException(DefaultEntities entities) {
        Entity entity = new Entity().add(
                new StateComponent(new NextStateIsNull()),
                new CounterComponent());

        entities
                .add(entity)
                .add(new StateSystem());

        assertThatThrownBy(() -> entities.updateTimes(2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Next state must not be null. Returned from EntityState: NextStateIsNull");
    }

    @Test
    void update_updatesState(DefaultEntities entities) {
        Entity entity = new Entity().add(
                new StateComponent(new PingState()),
                new CounterComponent());

        entities.add(entity);
        entities.add(new StateSystem());

        entities.updateTimes(6);

        assertThat(entity.get(StateComponent.class).state).isInstanceOf(PongState.class);
        assertThat(entity.get(CounterComponent.class).count).isEqualTo(5);
    }

    @Test
    void update_doesntExecuteStateChangeMethodsWithoutStateChange(DefaultEntities entities) {
        Entity entity = new Entity().add(
                new StateComponent(new BoringState()),
                new CounterComponent());

        entities.add(entity);
        entities.add(new StateSystem());

        entities.updateTimes(6);

        assertThat(entity.get(StateComponent.class).state).isInstanceOf(BoringState.class);
        assertThat(entity.get(CounterComponent.class).count).isEqualTo(1);
    }
}
