package dev.screwbox.pathfinding;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.graphics.postfilter.CrtMonitorPostFilter;
import dev.screwbox.core.graphics.postfilter.WarpPostFilter;
import dev.screwbox.core.scenes.SceneTransition;
import dev.screwbox.core.scenes.animations.BlackHoleAnimation;
import dev.screwbox.pathfinding.scenes.DemoScene;

public class PathfindingApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Pathfinding");

        engine.graphics().postProcessing().addViewportFilter(new WarpPostFilter(Percent.of(0.1))).addViewportFilter(new CrtMonitorPostFilter());
        engine.scenes()
            .add(new DemoScene())
            .switchTo(DemoScene.class, SceneTransition.custom().introAnimation(new BlackHoleAnimation()).introDurationMillis(1500));

        engine.assets()
            .enableLogging()
            .prepareClassPackageAsync(PathfindingApp.class);

        engine.graphics().camera().setZoom(2.7);

        engine.start();
    }
}
