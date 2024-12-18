package io.github.srcimon.screwbox.core.ui.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.audio.Audio;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.scenes.internal.DefaultScenes;
import io.github.srcimon.screwbox.core.ui.NotificationDetails;
import io.github.srcimon.screwbox.core.ui.NotificationLayouter;
import io.github.srcimon.screwbox.core.ui.NotificationRenderer;
import io.github.srcimon.screwbox.core.ui.UiInteractor;
import io.github.srcimon.screwbox.core.ui.UiLayouter;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.core.ui.UiRenderer;
import io.github.srcimon.screwbox.core.utils.Latch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@MockitoSettings
class DefaultUiTest {

    @InjectMocks
    DefaultUi ui;

    @Mock
    Canvas canvas;

    @Mock
    Engine engine;

    @Mock
    DefaultScenes scenes;

    @Mock
    UiInteractor interactor;

    @Mock
    UiLayouter layouter;

    @Mock
    UiRenderer renderer;

    @Mock
    NotificationRenderer notificationRenderer;

    @Mock
    NotificationLayouter notificationLayouter;

    @Mock
    Loop loop;

    @Mock
    Audio audio;

    @BeforeEach
    void beforeEach() {
        ui
                .setInteractor(interactor)
                .setLayouter(layouter)
                .setRenderer(renderer)
                .setNotificationLayouter(notificationLayouter)
                .setNotificationRender(notificationRenderer);
    }

    @Test
    void update_noMenu_noInteractionOrRendering() {
        ui.update();

        verify(interactor, never()).interactWith(any(), any(), any());
        verify(layouter, never()).layout(any(), any(), any());
        verify(renderer, never()).renderInactiveItem(any(), any(), any());
        verify(renderer, never()).renderSelectableItem(any(), any(), any());
        verify(renderer, never()).renderSelectedItem(any(), any(), any());
    }

    @Test
    void update_interactorDisablesCurrentSelection_switchesToNextMenuItem() {
        when(scenes.isShowingLoadingScene()).thenReturn(false);
        var activated = Latch.of(false, true);

        UiMenu menu = new UiMenu();
        menu.addItem("some button").activeCondition(e -> activated.active())
                .onActivate(e -> activated.toggle());
        menu.addItem("some button");

        ui.openMenu(menu);

        assertThat(menu.activeItemIndex()).isZero();

        ui.update();

        assertThat(menu.activeItemIndex()).isEqualTo(1);
    }

    @Test
    void update_menuPresent_interactsAndRendersMenu() {
        when(scenes.isShowingLoadingScene()).thenReturn(false);
        UiMenu menu = new UiMenu();
        menu.addItem("some button");

        ui.openMenu(menu);

        ui.update();

        verify(interactor).interactWith(menu, layouter, engine);
    }

    @Test
    void currentMenu_menuOpen_returnsMenu() {
        UiMenu menu = new UiMenu();

        ui.openMenu(menu);

        assertThat(ui.currentMenu()).hasValue(menu);
    }

    @Test
    void currentMenu_noOpenMenu_isEmpty() {
        assertThat(ui.currentMenu()).isEmpty();
    }

    @Test
    void openPreviousMenu_noPreviousMenu_throwsException() {
        assertThatThrownBy(() -> ui.openPreviousMenu())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("there is no previous menu to navigate back to");
    }

    @Test
    void openPreviousMenu_previousMenuPresent_switchesBackToPreviousMenu() {
        UiMenu optionsMenu = new UiMenu();
        ui.openMenu(optionsMenu);
        ui.openMenu(new UiMenu());

        ui.openPreviousMenu();

        assertThat(ui.currentMenu()).contains(optionsMenu);
    }

    @Test
    void openMenu_menuConsumerGiven_addsCustomizedMenu() {
        ui.openMenu(menu -> {
            menu.addItem("test1");
            menu.addItem("test2");
        });

        assertThat(ui.currentMenu()).isPresent();
        assertThat(ui.currentMenu().map(UiMenu::itemCount)).contains(2);
    }

    @Test
    void renderMenu_noMenu_noRendering() {
        ui.renderMenu();

        verifyNoInteractions(renderer);
    }

    @Test
    void showNotification_notificationNull_throwsException() {
        assertThatThrownBy(() -> ui.showNotification(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("notification must not be null");
    }

    @Test
    void showNotification_soundMuted_doesntPlayAudio() {
        when(loop.lastUpdate()).thenReturn(Time.now());
        when(engine.loop()).thenReturn(loop);

        ui.setNotificationSound(null);
        ui.showNotification(NotificationDetails.text("first"));

        verifyNoInteractions(audio);
    }

    @Test
    void showNotification_twoTimes_playsNotificationSound() {
        when(loop.lastUpdate()).thenReturn(Time.now());
        when(engine.loop()).thenReturn(loop);
        when(engine.audio()).thenReturn(audio);

        ArgumentCaptor<Supplier<Sound>> sound = ArgumentCaptor.forClass(Supplier.class);
        ui.showNotification(NotificationDetails.text("first"));
        verify(audio).playSound(sound.capture());

        assertThat(sound.getValue()).isEqualTo(SoundBundle.NOTIFY);

        var secondSound = SoundBundle.PLING;
        ui.showNotification(NotificationDetails.text("second").sound(secondSound));
        verify(audio).playSound(secondSound.get());
    }

    @Test
    void showNotification_twoTimes_addsNotifications() {
        when(loop.lastUpdate()).thenReturn(Time.now());
        when(engine.loop()).thenReturn(loop);
        when(engine.audio()).thenReturn(audio);

        Time before = Time.now();
        ui.showNotification(NotificationDetails.text("first"));
        ui.showNotification(NotificationDetails.text("second"));

        assertThat(ui.notifications()).hasSize(2)
                .isUnmodifiable().first()
                .matches(notification -> Duration.between(before, notification.timeCreated()).isLessThan(Duration.ofMillis(250)))
                .matches(notification -> notification.text().equals("first"));
    }

    @Test
    void updated_noOutdatedNotification_leavesNotificationsUntouched() {
        when(loop.lastUpdate()).thenReturn(Time.now());

        when(engine.loop()).thenReturn(loop);
        when(engine.audio()).thenReturn(audio);

        ui.showNotification(NotificationDetails.text("notification"));

        ui.update();

        assertThat(ui.notifications()).hasSize(1);
    }

    @Test
    void updated_outdatedNotificationPresent_removesNotification() {
        when(loop.lastUpdate()).thenReturn(Time.now(), Time.now().addSeconds(10));

        when(engine.loop()).thenReturn(loop);
        when(engine.audio()).thenReturn(audio);

        ui.showNotification(NotificationDetails.text("notification"));

        ui.update();

        assertThat(ui.notifications()).isEmpty();
    }

    @Test
    void setNotificationTimeout_timeoutNull_throwsException() {
        assertThatThrownBy(() -> ui.setNotificationTimeout(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("timeout must not be null");
    }

    @Test
    void setNotificationRenderer_renderNull_throwsExceptions() {
        assertThatThrownBy(() -> ui.setNotificationRender(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("renderer must not be null");
    }

    @Test
    void setNotificationLayouter_layouterNull_throwsExceptions() {
        assertThatThrownBy(() -> ui.setNotificationLayouter(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("layouter must not be null");
    }

    @Test
    void renderNotifications_noNotifications_noInteractionsWithRenderAndLayouter() {
        ui.renderNotifications();

        verifyNoInteractions(notificationLayouter);
        verifyNoInteractions(notificationRenderer);
    }

    @Test
    void renderNotifications_twoNotifications_rendersBothAfterLayouting() {
        when(loop.lastUpdate()).thenReturn(Time.now(), Time.now().addSeconds(10));
        when(engine.loop()).thenReturn(loop);
        ui.setNotificationSound(null);

        ui.setNotificationLayouter((index, notification, canvasBounds) -> new ScreenBounds(index, 0, 100, 100));

        ui.showNotification(NotificationDetails.text("first"));
        ui.showNotification(NotificationDetails.text("second"));

        ui.renderNotifications();

        verify(notificationRenderer).render(ui.notifications().getFirst(), new ScreenBounds(0, 0, 100, 100), canvas);
        verify(notificationRenderer).render(ui.notifications().getLast(), new ScreenBounds(1, 0, 100, 100), canvas);
    }
}
