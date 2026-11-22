package dev.screwbox.playground.design;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.assets.AssetBundle;
import dev.screwbox.core.scenes.SceneTransition;
import dev.screwbox.core.scenes.animations.ColorFadeAnimation;

public enum SceneTransitions implements AssetBundle<SceneTransition> {

    BLEND_IN(Asset.asset(() -> SceneTransition.custom().introDurationMillis(2000).introAnimation(new ColorFadeAnimation())));

    private final Asset<SceneTransition> transition;

    SceneTransitions(Asset<SceneTransition> transition) {
        this.transition = transition;
    }

    @Override
    public Asset<SceneTransition> asset() {
        return transition;
    }

}
