package io.github.srcimon.screwbox.platformer.achievements;

import dev.screwbox.core.achievements.AchievementDefinition;
import dev.screwbox.core.achievements.AchievementDetails;

public class FallIntoWaterAchievement implements AchievementDefinition {

    @Override
    public AchievementDetails details() {
        return AchievementDetails.title("fall into water");
    }
}
