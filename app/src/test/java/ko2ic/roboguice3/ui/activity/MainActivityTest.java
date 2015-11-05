package ko2ic.roboguice3.ui.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;

import com.google.inject.name.Names;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.util.ActivityController;

import ko2ic.roboguice3.BuildConfig;
import ko2ic.roboguice3.R;
import ko2ic.roboguice3.domain.model.Weather;
import ko2ic.roboguice3.infrastructure.repository.WeatherRepository;
import roboguice.RoboGuice;
import roboguice.config.DefaultRoboModule;
import roboguice.event.EventListener;
import roboguice.event.EventManager;
import roboguice.inject.ContextScope;
import roboguice.inject.ResourceListener;
import roboguice.inject.ViewListener;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
public class MainActivityTest {

    private final ActivityController<MainActivity> target = Robolectric.buildActivity(MainActivity.class).create();
    private final EventManager eventManagerMock = Mockito.mock(EventManager.class);

    @Before
    public void setUp() throws Exception {
        MainActivityTestModule activityTestModule = new MainActivityTestModule(RuntimeEnvironment.application);
        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, activityTestModule);
    }

    @After
    public void tearDown() throws Exception {
        RoboGuice.Util.reset();
    }

    @Test
    public void testClickButton(){
        target.get().findViewById(R.id.button).performClick();

        Intent expectedIntent = new Intent(target.get(), SecondActivity.class);
        assertThat(shadowOf(target.get()).getNextStartedActivity(),equalTo(expectedIntent));
    }

    @Test
    public void testOnCreate() throws Exception {
        Button button = (Button) target.get().findViewById(R.id.button);
        assertNotNull(button);
    }

    @Test
    public void testOnStart() throws Exception {
        doNothing().when(eventManagerMock).registerObserver(WeatherRepository.WeatherEventSuccess.class, target.get().listener);

        target.start();

        verify(eventManagerMock, times(1)).registerObserver(WeatherRepository.WeatherEventSuccess.class, target.get().listener);
    }

    @Test
    public void testOnStop() throws Exception {
        doNothing().when(eventManagerMock).unregisterObserver((Class)notNull(), (EventListener) notNull());

        target.stop();

        verify(eventManagerMock,times(1)).unregisterObserver((Class)notNull(), (EventListener) notNull());
    }

    @Test
    public void testOnEvent() throws Exception {
        ShadowLooper.idleMainLooper();
        Weather entity = new Weather();
        entity.title = "test";
        WeatherRepository.WeatherEventSuccess weatherEventSuccess = new WeatherRepository.WeatherEventSuccess(entity);
        target.get().listener.onEvent(weatherEventSuccess);

        assertThat(ShadowToast.getTextOfLatestToast(), equalTo("You got weater. test"));
    }

    public class MainActivityTestModule extends DefaultRoboModule {

        public MainActivityTestModule(Application application) {
            super(application, new ContextScope(application), new ViewListener(), new ResourceListener(application));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void configure() {
            //super.configure();
            // TODO I do not know why the mock is not used
            bind(EventManager.class).annotatedWith(Names.named(DefaultRoboModule.GLOBAL_EVENT_MANAGER_NAME)).toInstance(eventManagerMock);
        }

    }
}