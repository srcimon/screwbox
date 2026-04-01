package dev.screwbox.core.scenes.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.scenes.SceneTransition;

import static java.util.Objects.isNull;

public record ActiveTransition(
    Time started,
    Class<? extends Scene> targetScene,
    SceneTransition transition) {

    public Time switchTime() {
        return transition.outroDuration().addTo(started);
    }

    public Percent introProgress(final Time time) {
        final Time introStartTime = transition.outroDuration().addTo(started);
        return transition.introDuration().progress(introStartTime, time);
    }

    public Percent outroProgress(final Time time) {
        return transition.outroDuration().progress(started, time);
    }

    public PostProcessingFilter introFilter(final Time time) {
        if (isNull(transition.introAnimation())) {
            return null;
        }
        final Percent progress = transition.introEase().applyOn(introProgress(time));
        return (source, target, context) -> transition.introAnimation().apply(source, target, context.bounds(), progress);
    }

    public PostProcessingFilter outroFilter(final Time time) {
        if (isNull(transition.outroAnimation())) {
            return null;
        }
        final Percent progress = transition.outroEase().applyOn(outroProgress(time));
        return (source, target, context) -> transition.outroAnimation().apply(source, target, context.bounds(), progress);
    }
}