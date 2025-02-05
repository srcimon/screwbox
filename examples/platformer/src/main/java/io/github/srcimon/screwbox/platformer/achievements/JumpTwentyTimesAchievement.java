package io.github.srcimon.screwbox.platformer.achievements;

import io.github.srcimon.screwbox.core.achievements.AchievementDefinition;
import io.github.srcimon.screwbox.core.achievements.AchievementDetails;

public class JumpTwentyTimesAchievement implements AchievementDefinition {

    @Override
    public AchievementDetails details() {
        return AchievementDetails.title("jump {goal} times")
                .description("press SPACE to jump {goal} times")
                .goal(20);
    }
}
