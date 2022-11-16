package de.suzufa.screwbox.playground.debo;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.assets.Demo;
import de.suzufa.screwbox.core.ui.WobblyUiLayouter;
import de.suzufa.screwbox.playground.debo.scenes.DeadScene;
import de.suzufa.screwbox.playground.debo.scenes.PauseScene;
import de.suzufa.screwbox.playground.debo.scenes.StartScene;

public class DeboApplication {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Debo Game");

        engine.ui().setLayouter(new WobblyUiLayouter());

        // TODO: shite
        engine.async().run(DeboApplication.class, () -> {
            for (var clazz : new Demo()
                    .findAllClassesUsingClassLoader("de.suzufa.screwbox.playground.debo")) {
                for (var field : clazz.getDeclaredFields()) {
                    if (field.getType().isAssignableFrom(Asset.class)) {
                        try {
//                        field.setAccessible(true);
                            Asset object = (Asset) field.get(Asset.class);
                            Time time = Time.now();
                            object.load();
                            long milliseconds = Duration.since(time).milliseconds();
                            System.out
                                    .println("loading asset " + clazz.getSimpleName() + "." + field.getName() + " took "
                                            + milliseconds + " ms");
                        } catch (IllegalArgumentException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        engine.scenes()
                .add(new DeadScene())
                .add(new PauseScene())
                .add(new StartScene());

        engine.start(StartScene.class);
    }
}