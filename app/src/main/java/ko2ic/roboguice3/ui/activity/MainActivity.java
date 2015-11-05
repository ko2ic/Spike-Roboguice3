package ko2ic.roboguice3.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import ko2ic.roboguice3.R;
import ko2ic.roboguice3.infrastructure.repository.WeatherRepository;
import roboguice.activity.RoboAppCompatActivity;
import roboguice.config.DefaultRoboModule;
import roboguice.event.EventListener;
import roboguice.event.EventManager;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboAppCompatActivity {

    @Named(DefaultRoboModule.GLOBAL_EVENT_MANAGER_NAME)
    @Inject
    private EventManager globalEventManager;

    @InjectView(R.id.button)
    private Button button;

    // Roboguiceでやる場合
    protected EventListener listener = new EventListener<WeatherRepository.WeatherEventSuccess>() {
        @Override
        public void onEvent(WeatherRepository.WeatherEventSuccess event) {
            Toast toast = Toast.makeText(MainActivity.this, String.format("You got weater. %s", event.getWeather().title), Toast.LENGTH_LONG);
            toast.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        globalEventManager.registerObserver(WeatherRepository.WeatherEventSuccess.class, listener);
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        globalEventManager.unregisterObserver(WeatherRepository.WeatherEventSuccess.class, listener);
//        EventBus.getDefault().unregister(this);
    }

    // EventBusでやる場合
//    public void onEvent(WeatherRepository.WeatherEventSuccess event){
//        Toast toast = Toast.makeText(this, String.format("You got weater. %s", event.getWeather().title), Toast.LENGTH_LONG);
//        toast.show();
//    }
}
