package de.suzufa.screwbox.core.log;

public interface Log {

    Log log(LogLevel level, String message);

    Log debug(String message);

    Log info(String message);

    Log warn(String message);

    Log error(String message);

    Log error(String message, Exception e);

    Log setAdapter(LoggingAdapter adapter);
}
