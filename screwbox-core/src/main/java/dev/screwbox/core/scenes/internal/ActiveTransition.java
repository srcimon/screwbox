package dev.screwbox.core.scenes.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.scenes.SceneTransition;

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

    public void drawIntro(final Canvas canvas, final Time time) {
        final Percent progress = transition.introEase().applyOn(introProgress(time));
        transition.introAnimation().draw(canvas, progress);
    }

    public void drawOutro(final Canvas canvas, final Time time) {
        final Percent progress = transition.outroEase().applyOn(outroProgress(time));
        transition.outroAnimation().draw(canvas, progress);
    }

    public Percent outroProgress(final Time time) {
        return transition.outroDuration().progress(started, time);
    }
}