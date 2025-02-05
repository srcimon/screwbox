package io.github.srcimon.screwbox.platformer.achievements;

import io.github.srcimon.screwbox.core.achievements.AchievementDefinition;
import io.github.srcimon.screwbox.core.achievements.AchievementDetails;

public class FallIntoWaterAchievement implements AchievementDefinition {

    @Override
    public AchievementDetails details() {
        return AchievementDetails.title("fall into water");
    }
}
