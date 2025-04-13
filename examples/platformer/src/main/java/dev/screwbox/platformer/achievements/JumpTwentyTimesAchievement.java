package dev.screwbox.platformer.achievements;

import dev.screwbox.core.achievements.AchievementDefinition;
import dev.screwbox.core.achievements.AchievementDetails;

public class JumpTwentyTimesAchievement implements AchievementDefinition {

    @Override
    public AchievementDetails details() {
        return AchievementDetails.title("jump {goal} times")
                .description("press SPACE to jump {goal} times")
                .goal(20);
    }
}
