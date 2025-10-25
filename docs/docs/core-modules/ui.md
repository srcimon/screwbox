# UI

The `engine.ui()` component adds support for interactive menus and user notifications.

## Menus

You can also use the UI to create a basic user interface.
To create a custom menu extend the abstract `UiMenu` class.

``` java title="OptionsMenu.java"
public class OptionsMenu extends UiMenu {

    public OptionsMenu() {
        addItem("start new game")
          .onActivate(engine -> engine.scenes().switchTo(GameScene.class));
          
        addItem("quit")
          .onActivate(engine -> engine.stop());
    }
}
```

``` java
ui.openMenu(new OptionsMenu());
```

### Inline definition

It's also possible to define your menu inline:

``` java title="InlineExample.java"
ui.openMenu(menu -> {
    menu.addItem("goto fullscreen")
            .onActivate(engine -> engine.graphics().configuration().toggleFullscreen());
    menu.addItem("quit")
            .onActivate(Engine::stop);
});
```

### Dynamic labels and disabled items

Menu items can use dynamic labels.
These labels will be refreshed on every frame.
Also menu items can be disabled on condition.
The condition will be checked on every frame.

``` java
// disabled on fullscreen
menu.addItem("goto fullscreen")
        .onActivate(engine -> engine.graphics().configuration().toggleFullscreen())
        .activeCondition(engine -> !engine.graphics().configuration().isFullscreen());
        
// dynamic label
menu.addItem(engine -> "current fps: " + engine.loop().fps())
        .onActivate(Engine::stop);
```

### Navigation

To create sub menus you can always go to the last menu opened.

``` java
addItem("back").onActivate(engine -> engine.ui().openPreviousMenu());
addItem("close").onActivate(engine -> engine.ui().closeMenu());
```

You can also add your own reactions when exiting a menu.
To do so overwrite the `onExit`-method.

### Customizing menus

You can customize menus to your needs.
To do so simply inject your own `UiInteractor`, `UiLayout` and `UiDesign`:

``` java
engine.ui()
    .setInteractor(new KeyboardAndMouseInteractor())
    .setLayout(new WobblyUiLayout())
    .setDesign(new SimpleUiDesign());
```

## Notifications

You can use the `Ui` to show notifications.
You can specify the shown text and a sound effect that is played.

``` java
engine.ui().showNotification(NotificationDetails
    .text("Something happened!")
    .sound(SoundBundle.NOTIFY)
    .icon(SpriteBundle.EXPLOSION));
```

Notifications are automatically rendered by the `RenderNotificationsSystem` that is one of the default systems enabled using `environment.enableAllFeatures()`.

You can customize design and layout using your own implementations:

``` java
ui.setNotificationDesign(new MyDesign())
ui.setNotificationLayout(new MyLayout());
```