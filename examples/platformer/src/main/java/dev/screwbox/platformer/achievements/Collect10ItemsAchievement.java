package dev.screwbox.platformer.achievements;

import dev.screwbox.core.achievements.AchievementDefinition;
import dev.screwbox.core.achievements.AchievementDetails;

public class Collect10ItemsAchievement implements AchievementDefinition {

    @Override
    public AchievementDetails details() {
        return AchievementDetails.title("collect {goal} items")
                .description("collect cherries and D.E.B.O.")
                .goal(10);
    }
}
