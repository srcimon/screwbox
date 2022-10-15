package de.suzufa.screwbox.examples.light;

import static de.suzufa.screwbox.core.Bounds.$$;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.components.LightBlockingComponent;
import de.suzufa.screwbox.core.entities.components.PointLightComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.systems.DynamicLightSystem;

public class LightExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Light Example");

        Entity light = new Entity()
                .add(new PointLightComponent(300))
                .add(new TransformComponent($$(0, 0, 10, 10)));

        engine.entities()
                .add(new DynamicLightSystem())
                .add(new BlueBackgroundSystem())
                .add(boxAt(40, 40))
                .add(boxAt(140, 20))
                .add(boxAt(200, 600))
                .add(boxAt(220, 400))
                .add(light)
                .add(new EntitySystem() {

                    @Override
                    public void update(Engine engine) {
                        for (var light : engine.entities().fetchAllHaving(PointLightComponent.class)) {
                            light.get(TransformComponent.class).bounds = light.get(TransformComponent.class).bounds
                                    .moveTo(engine.mouse().worldPosition());
                        }

                    }
                });

        engine.start();
    }

    private static Entity boxAt(double x, double y) {
        return new Entity()
                .add(new TransformComponent($$(x, y, 32, 32)))
                .add(new LightBlockingComponent());
    }
}
