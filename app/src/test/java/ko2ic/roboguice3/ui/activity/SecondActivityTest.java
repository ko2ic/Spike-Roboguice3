package ko2ic.roboguice3.ui.activity;

import android.app.Application;
import android.os.Build;
import android.widget.EditText;

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

import ko2ic.roboguice3.BuildConfig;
import ko2ic.roboguice3.R;
import ko2ic.roboguice3.application.facade.WeatherFacade;
import roboguice.RoboGuice;
import roboguice.config.DefaultRoboModule;
import roboguice.inject.ContextScope;
import roboguice.inject.ResourceListener;
import roboguice.inject.ViewListener;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
public class SecondActivityTest {

    private final WeatherFacade facadeMock = Mockito.mock(WeatherFacade.class);

    @Before
    public void setUp() throws Exception {
        SecondActivityTestModule activityTestModule = new SecondActivityTestModule(RuntimeEnvironment.application);
        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, activityTestModule);
    }

    @After
    public void tearDown() throws Exception {
        RoboGuice.Util.reset();
    }
    @Test
    public void testClickButton(){
        SecondActivity target = Robolectric.buildActivity(SecondActivity.class).create().get();

        EditText editText = (EditText) target.findViewById(R.id.editText);
        editText.setText("test");

        doNothing().when(facadeMock).fetchWeahter("test");

        target.findViewById(R.id.button).performClick();

        verify(facadeMock, times(1)).fetchWeahter("test");
    }

    public class SecondActivityTestModule extends DefaultRoboModule {

        public SecondActivityTestModule(Application application) {
            super(application, new ContextScope(application), new ViewListener(), new ResourceListener(application));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void configure() {
            super.configure();
            bind(WeatherFacade.class).toInstance(facadeMock);
        }
    }
}