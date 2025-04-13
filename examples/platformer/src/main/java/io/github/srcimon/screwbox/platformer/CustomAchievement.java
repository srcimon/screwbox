package io.github.srcimon.screwbox.platformer;

import dev.screwbox.core.Engine;
import dev.screwbox.core.achievements.AchievementDefinition;
import dev.screwbox.core.achievements.AchievementDetails;

public class CustomAchievement implements AchievementDefinition {
    @Override
    public AchievementDetails details() {
        return AchievementDetails.title("Play for 2 minutes")
                .goal(120)
                .useAbsoluteProgression();
    }

    @Override
    public int progress(Engine engine) {
        return (int)engine.loop().runningTime().seconds();
    }
}
