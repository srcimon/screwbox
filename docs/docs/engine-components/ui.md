# Ui

## Notifications

You can use the `Ui` to show notifications.
You can specify the shown text and a sound effect that is played.

``` java
engine.ui().showNotification(NotificationDetails.text("Something happened!")
    .sound(SoundBundle.NOTIFY)
    .icon(SpriteBundle.EXPLOSION));
```

Notifications are automatically rendered by the `RenderNotificationsSystem` that is one of the default systems enabled using `environment.enableAllFeatures()`.

You can customize rendering and layout using your own implementations:

``` java
ui.setNotificationRender(new MyRender())
ui.setNotificationLayouter(new MyLayouter());
```