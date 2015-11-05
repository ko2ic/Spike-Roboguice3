package ko2ic.roboguice3.infrastructure.repository;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import ko2ic.roboguice3.domain.model.Weather;
import ko2ic.roboguice3.infrastructure.repository.event.common.AbstractHttpStatusErrorEvent;
import ko2ic.roboguice3.infrastructure.repository.event.common.RuntimeExceptionEvent;
import roboguice.config.DefaultRoboModule;
import roboguice.event.EventManager;

public class WeatherRepository {

    private Context mContext;

    // Roboguiceでやる場合
    @Named(DefaultRoboModule.GLOBAL_EVENT_MANAGER_NAME)
    @Inject
    protected EventManager globalEventManager;

    @Inject
    public WeatherRepository(Provider<Context> provider) {
        mContext = provider.get();
    }

    public void fetchWeather(String cityCode) {
        String url = "http://weather.livedoor.com/forecast/webservice/json/v1?city=" + cityCode;

        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Weather entity = new Weather();

                            String title = response.getString("title");

                            entity.title = title;
//                            EventBus.getDefault().post(new WeatherEventSuccess(entity));
                            globalEventManager.fire(new WeatherEventSuccess(entity));
                        } catch (JSONException e) {
                            EventBus.getDefault().post(new RuntimeExceptionEvent(e));
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (response == null) {
                            EventBus.getDefault().post(new WeatherEventFailure(404));
                            return;
                        }
                        EventBus.getDefault().post(new WeatherEventFailure(response.statusCode));
                    }
                }));


    }

    public static class WeatherEventSuccess {

        private Weather mWeather;

        public WeatherEventSuccess(Weather weather) {
            this.mWeather = weather;
        }

        public Weather getWeather() {
            return mWeather;
        }
    }

    public class WeatherEventFailure extends AbstractHttpStatusErrorEvent {

        public WeatherEventFailure(int statusCode) {
            super(statusCode);
        }
    }
}
