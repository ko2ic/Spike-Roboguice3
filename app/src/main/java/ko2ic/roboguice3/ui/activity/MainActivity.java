package ko2ic.roboguice3.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import de.greenrobot.event.EventBus;
import ko2ic.roboguice3.R;
import ko2ic.roboguice3.infrastructure.repository.WeatherRepository;
import roboguice.activity.RoboAppCompatActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboAppCompatActivity {

    @InjectView(R.id.button)
    private Button button;

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
        // TODO roboguiceでできるか不明
        //registerObserver(Object instance, Class<T> event)
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // TODO roboguiceでできるか不明
        //unregisterObserver(Object instance, Class<T> event)
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(WeatherRepository.WeatherEventSuccess event){
        Toast toast = Toast.makeText(this, String.format("You got weater. %s", event.getWeather().title), Toast.LENGTH_LONG);
        toast.show();
    }
}
