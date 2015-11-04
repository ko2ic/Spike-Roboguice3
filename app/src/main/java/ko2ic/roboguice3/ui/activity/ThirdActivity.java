package ko2ic.roboguice3.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import ko2ic.roboguice3.R;
import ko2ic.roboguice3.domain.model.Weather;
import roboguice.activity.RoboAppCompatActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_third)
public class ThirdActivity extends RoboAppCompatActivity {

    @InjectView(R.id.textView)
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Weather weather = (Weather)intent.getSerializableExtra("weather");
        textView.setText(weather.title);
    }
}
