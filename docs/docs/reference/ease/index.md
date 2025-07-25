# Ease

Ease configures the direction and the progress of a value change.

## Usage example

There are lots of places that `Ease` can be used to enhance animations or transitions.
This code for example switches to the start scene of the game and uses an ease function make the outro animation more appealing.

``` java
scenes.switchTo(StartScene.class, SceneTransition.custom()
    .outroEase(Ease.SINE_IN)
    .outroAnimation(new CirclesAnimation())
    .outroDurationMillis(2000)
    .introDurationMillis(250));
```

## Overview

| Ease                    | Visualization                             |
|-------------------------|-------------------------------------------|
| `Ease.FLICKER`          | ![FLICKER](FLICKER.png)                   |
| `Ease.IN_PLATEAU`       | ![IN_PLATEAU](IN_PLATEAU.png)             |
| `Ease.IN_PLATEAU_OUT`   | ![IN_PLATEAU_OUT](IN_PLATEAU_OUT.png)     |
| `Ease.LINEAR_IN`        | ![LINEAR_IN](LINEAR_IN.png)               |
| `Ease.LINEAR_OUT`       | ![LINEAR_OUT](LINEAR_OUT.png)             |
| `Ease.PLATEAU_OUT`      | ![PLATEAU_OUT](PLATEAU_OUT.png)           |
| `Ease.PLATEAU_OUT_SLOW` | ![PLATEAU_OUT_SLOW](PLATEAU_OUT_SLOW.png) |
| `Ease.SIN_IN_OUT_TWICE` | ![SIN_IN_OUT_TWICE](SIN_IN_OUT_TWICE.png) |
| `Ease.SINE_IN`          | ![SINE_IN](SINE_IN.png)                   |
| `Ease.SINE_IN_OUT`      | ![SINE_IN_OUT](SINE_IN_OUT.png)           |
| `Ease.SINE_OUT`         | ![SINE_OUT](SINE_OUT.png)                 |
| `Ease.SPARKLE`          | ![SPARKLE](SPARKLE.png)                   |
| `Ease.SQUARE_IN`        | ![SQUARE_IN](SQUARE_IN.png)               |
| `Ease.SQUARE_OUT`       | ![SQUARE_OUT](SQUARE_OUT.png)             |
| `Ease.S_CURVE_IN`       | ![S_CURVE_IN](S_CURVE_IN.png)             |
| `Ease.S_CURVE_OUT`      | ![S_CURVE_OUT](S_CURVE_OUT.png)           |