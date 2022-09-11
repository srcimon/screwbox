package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.StateComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.test.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class StateSystemTest {

    private class CounterComponent implements Component {
        private static final long serialVersionUID = 1L;
        public int count = 0;
    }

    private class NextStateIsNull implements EntityState {

        private static final long serialVersionUID = 1L;

        @Override
        public EntityState update(Entity entity, Engine engine) {
            return null;
        }

    }

    private class BoringState implements EntityState {

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
    void update_firstStateEnterMethodIsExecuted(DefaultEntities entityEngine) {
        Entity entity = new Entity().add(
                new StateComponent(new PingState()),
                new CounterComponent());

        entityEngine.add(entity);
        entityEngine.add(new StateSystem());

        entityEngine.update();

        assertThat(entity.get(StateComponent.class).state).isInstanceOf(PingState.class);
        assertThat(entity.get(CounterComponent.class).count).isEqualTo(1);
    }

    @Test
    void update_nextStateIsNull_throwsException(DefaultEntities entityEngine) {
        Entity entity = new Entity().add(
                new StateComponent(new NextStateIsNull()),
                new CounterComponent());

        entityEngine
                .add(entity)
                .add(new StateSystem());

        assertThatThrownBy(() -> entityEngine.updateTimes(2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Next state must not be null. Returned from EntityState: NextStateIsNull");
    }

    @Test
    void update_updatesState(DefaultEntities entityEngine) {
        Entity entity = new Entity().add(
                new StateComponent(new PingState()),
                new CounterComponent());

        entityEngine.add(entity);
        entityEngine.add(new StateSystem());

        entityEngine.updateTimes(6);

        assertThat(entity.get(StateComponent.class).state).isInstanceOf(PongState.class);
        assertThat(entity.get(CounterComponent.class).count).isEqualTo(5);
    }

    @Test
    void update_doesntExecuteStateChangeMethodsWithoutStateChange(DefaultEntities entityEngine) {
        Entity entity = new Entity().add(
                new StateComponent(new BoringState()),
                new CounterComponent());

        entityEngine.add(entity);
        entityEngine.add(new StateSystem());

        entityEngine.updateTimes(6);

        assertThat(entity.get(StateComponent.class).state).isInstanceOf(BoringState.class);
        assertThat(entity.get(CounterComponent.class).count).isEqualTo(1);
    }
}
