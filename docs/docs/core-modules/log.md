# Log

Logging in ScrewBox applications.

## Logging

ScrewBox engine modules use the `Log` module to log noteworthy events.
By default all logging events go directly to the console.

``` java
log.info("this is a message");

// prints this is a message with 2 parameters
log.error("this is a {} with {} parameters", "message", 2);
```

### Customizing output

To change the logging output to another target than the console a custom `LoggingAdapter` can be set.
Example code for a custom adapter that creates an [UI notification](ui.md#notifications) for each log event.

``` java
public class NotificationLoggingAdapter implements LoggingAdapter {

    private final Ui ui;

    public NotificationLoggingAdapter(final Ui ui) {
        this.ui = ui;
    }

    @Override
    public void log(LogLevel level, String message) {
        ui.showNotification(NotificationDetails.text(level.name() + ": " + message));
    }
}
```

### Filtering output

The actual log output can be filtered by minimum severity.
Also the log can be fully disabled.

``` java
log.setMinimumSeverity(LogLevel.WARNING);
log.disable();
```

## Logging recommendations

Logging isn't that important in a game engine most of the time.
The `Log` module is only a simple proxy that allows full customization.
For your logging purpose you can choose whatever library fits your needs best.
However in most cases the engine log module should do fine.