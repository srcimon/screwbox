package io.github.srcimon.screwbox.core.environment.logic;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(EnvironmentExtension.class)
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
    void update_firstStateEnterMethodIsExecuted(DefaultEnvironment environment) {
        Entity entity = new Entity().add(
                new StateComponent(new PingState()),
                new CounterComponent());

        environment.addEntity(entity);
        environment.addSystem(new StateSystem());

        environment.update();

        assertThat(entity.get(StateComponent.class).state).isInstanceOf(PingState.class);
        assertThat(entity.get(CounterComponent.class).count).isEqualTo(1);
    }

    @Test
    void update_nextStateIsNull_throwsException(DefaultEnvironment environment) {
        Entity entity = new Entity().add(
                new StateComponent(new NextStateIsNull()),
                new CounterComponent());

        environment
                .addEntity(entity)
                .addSystem(new StateSystem());

        assertThatThrownBy(() -> environment.updateTimes(2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Next state must not be null. Returned from EntityState: NextStateIsNull");
    }

    @Test
    void update_updatesState(DefaultEnvironment environment) {
        Entity entity = new Entity().add(
                new StateComponent(new PingState()),
                new CounterComponent());

        environment.addEntity(entity);
        environment.addSystem(new StateSystem());

        environment.updateTimes(6);

        assertThat(entity.get(StateComponent.class).state).isInstanceOf(PongState.class);
        assertThat(entity.get(CounterComponent.class).count).isEqualTo(5);
    }

    @Test
    void update_doesntExecuteStateChangeMethodsWithoutStateChange(DefaultEnvironment environment) {
        Entity entity = new Entity().add(
                new StateComponent(new BoringState()),
                new CounterComponent());

        environment.addEntity(entity);
        environment.addSystem(new StateSystem());

        environment.updateTimes(6);

        assertThat(entity.get(StateComponent.class).state).isInstanceOf(BoringState.class);
        assertThat(entity.get(CounterComponent.class).count).isEqualTo(1);
    }
}
