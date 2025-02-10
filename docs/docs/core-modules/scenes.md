# Scenes

Scenes can be used to structure your game.
Typical scenes might be a *game scene*, an *options screen scene*, *credits scene* and so on.

## Using scenes

ScrewBox always uses at least one `Scene`.
When the engine is started without customization the scene will be the `DefaultScene`.
This scene will be empty by default.
Every scene will use its own [Environment](environment).
To create a custom scene implement the `Scene` interface.

The `populate` method will be called in a background thread when the scene is added.
It will add all systems and entities that are needed for the scene.

The `onEnter` and `onExit` methods allow custom event handlers when entering and leaving the scene.
E.g. this scene will automatically switch to fullscreen mode on enter and window mode on exit:

``` java
public class MyScene implements Scene {

    @Override
    public void populate(Environment environment) {
        environment.enableAllFeatures();
        //...
    }

    @Override
    public void onEnter(Engine engine) {
        engine.graphics().configuration().setFullscreen(true);
    }

    @Override
    public void onExit(Engine engine) {
        engine.graphics().configuration().setFullscreen(false);
    }
}
```

``` java
// adding the scene / pupulating it will start in the background
engine.scenes().addScene(new MyScene());

// actually switching the current scene
engine.scenes().switchTo(MyScene.class);
```

### Scene transitions

Switching scenes can be made more visually pleasing by using scene transitions.
To use these add a `SceneTransition` parameter when calling the scene switch.

``` java
scenes.switchTo(StartScene.class, SceneTransition.custom()
    .outroAnimation(new CirclesAnimation())
    .outroDurationMillis(2000)
    .introDurationMillis(250));
```

This configuration allows specifying:

| Parameter      | Description                             |
|----------------|-----------------------------------------|
| outroAnimation | animation used for outro                |
| outroDuration  | duration  of the outro                  |
| outroEase      | the Ease applied on the outro animation |
| introAnimation | animation used for intro                |
| introDuration  | duration  of the intro                  |
| introEase      | the Ease applied on the intro animation |

ScrewBox ships three pre defined animations:

- `CirclesAnimation` Fills the canvas with multiple circles growing in size.
- `ColorFadeAnimation` Fades the screen to the specified color.
- `SpriteFadeAnimation` Fades the screen to the specified Sprite.

Of course you can create custom animations by implementing the `Animation` interface.

:::tip
Switching the scene when a transition is already in progress might result in some unwanted behaviour.
To avoid this check for the transition progress first:

``` java
if(!scenes.isTransitioning()) {
    scenes.switchTo(MyScene.class);
}
```
:::

## Loading scene

If the game switches to a scene that is not done populating it will show the loading scene.
This scene might also be seen when starting the engine.
To avoid showing the loading scene it's recommended to show an [outro animation](#scene-transitions) when switching to a new scene.
Use the scene component to create your own loading scene.

``` java
scenes.setLoadingScene(new MyLoadingScene());
```