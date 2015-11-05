package ko2ic.roboguice3;

import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import ko2ic.roboguice3.infrastructure.AppApplication;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class AppApplicationTest {

    /** テスト対象 */
    private final AppApplication target = (AppApplication) RuntimeEnvironment.application;

    @Test
    public void testOnLowMemory() {
        // 呼び出してエラーにならないことだけ確認
        target.onLowMemory();
    }
}