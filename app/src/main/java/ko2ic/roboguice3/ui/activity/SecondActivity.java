package ko2ic.roboguice3.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import ko2ic.roboguice3.R;
import ko2ic.roboguice3.application.facade.WeatherFacade;
import ko2ic.roboguice3.infrastructure.repository.WeatherRepository;
import ko2ic.roboguice3.infrastructure.repository.event.common.RuntimeExceptionEvent;
import roboguice.activity.RoboAppCompatActivity;
import roboguice.config.DefaultRoboModule;
import roboguice.event.EventListener;
import roboguice.event.EventManager;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_second)
public class SecondActivity extends RoboAppCompatActivity {

    @Inject
    private WeatherFacade facade;

    @Named(DefaultRoboModule.GLOBAL_EVENT_MANAGER_NAME)
    @Inject
    protected EventManager globalEventManager;

    @InjectView(R.id.editText)
    private EditText editText;

    @InjectView(R.id.button)
    private Button button;

    // Roboguiceでやる場合
    private EventListener listener = new EventListener<WeatherRepository.WeatherEventSuccess>() {
        @Override
        public void onEvent(WeatherRepository.WeatherEventSuccess event) {
            Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
            intent.putExtra("weather", event.getWeather());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facade.fetchWeahter(editText.getText().toString());
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

    // 以下はEventBusでやる場合
    public void onEvent(WeatherRepository.WeatherEventSuccess event){
        Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
        intent.putExtra("weather", event.getWeather());
        startActivity(intent);
    }

    public void onEvent(WeatherRepository.WeatherEventFailure event){
        Toast toast = Toast.makeText(this, String.format("Http Status Code is %s .", event.getStatusCode()), Toast.LENGTH_LONG);
        toast.show();
    }

    public void onEvent(RuntimeExceptionEvent event){
        Log.e("Spike-Roboguice3", "program exception", event.getThrowable());
    }
}
