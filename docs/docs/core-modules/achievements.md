# Achievements

Add achievement to challenge players with custom goals.

## Adding achievements

To add a new achievement create a new class implementing the `AchievementDefinition` interface.

```java title="CustomAchievement.java"
public class CustomAchievement implements AchievementDefinition {
    @Override
    public AchievementDetails details() {
        return AchievementDetails.title("Title of the achievement")
                .description("For learning purpose")
                .goal(4);
    }
}
```

``` java
// add the achievement
achievements.add(new CustomAchievement());

// progress fom 0 of 4 to 2 of 4
achievements.progess(CustomAchievement.class);
achievements.progess(CustomAchievement.class);

// unlock achievement by reaching goal
achievements.progess(CustomAchievement.classm, 2);
```

### Manual progression

In the example above we used manual progression for the achievement.
Manual progression should only be used when necessary.

### Automatic progression

Automatic progression can be added by overwriting the `progress` method.
ScrewBox will automatically update the progress of such achievements.
An example for an automatic progression:

```java title="ClickTenTimesAchievement.java"
public class ClickTenTimesAchievement implements AchievementDefinition {
    @Override
    public AchievementDetails details() {
        return AchievementDetails.title("Click {goal} times.")
                .description("Achieved when clicked {goal} times.")
                .goal(10);
    }

    @Override
    public int progress(Engine engine) {
        return engine.mouse().isPressedLeft() ? 1 : 0;
    }
}
```

### Absolute progression

Progression can be additive (default) and also absolute.
Achievements with absolute progression will check the progress value against the goal instead of adding it to the
current progress.
Example for using absolute progression:

```java title="PlayForTwoMinutesAchievement.java"
public class PlayForTwoMinutesAchievement implements AchievementDefinition {

    @Override
    public AchievementDetails details() {
        return AchievementDetails.title("Play for 2 minutes")
                .goal(120)
                .useAbsoluteProgression();
    }

    @Override
    public int progress(Engine engine) {
        return (int) engine.loop().runningTime().seconds();
    }
}
```

## Reaction