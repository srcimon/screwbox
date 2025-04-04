package io.github.srcimon.screwbox.platformer.achievements;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.achievements.AchievementDefinition;
import io.github.srcimon.screwbox.core.achievements.AchievementDetails;

public class PlayForFiveMinutesAchievement implements AchievementDefinition {

    @Override
    public AchievementDetails details() {
        return AchievementDetails.title("play the game for 5 minutes")
                .useAbsoluteProgression()
                .goal(300);
    }

    @Override
    public int progress(Engine engine) {
        return (int) engine.loop().runningTime().seconds();
    }
}
