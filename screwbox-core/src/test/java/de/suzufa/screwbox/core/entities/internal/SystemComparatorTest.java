package de.suzufa.screwbox.core.entities.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.systems.CombineStaticCollidersSystem;
import de.suzufa.screwbox.core.entities.systems.PhysicsSystem;
import de.suzufa.screwbox.core.entities.systems.ScreenTransitionSystem;

class SystemComparatorTest {

    @Test
    void sortsSystemsByUpdatePriority() {
        List<EntitySystem> systems = new ArrayList<>();
        systems.add(new PhysicsSystem());
        systems.add(new ScreenTransitionSystem());
        systems.add(new CombineStaticCollidersSystem());

        Collections.sort(systems, new SystemComparator());

        assertThat(systems.get(0)).isInstanceOf(CombineStaticCollidersSystem.class);
        assertThat(systems.get(1)).isInstanceOf(PhysicsSystem.class);
        assertThat(systems.get(2)).isInstanceOf(ScreenTransitionSystem.class);
    }
}
