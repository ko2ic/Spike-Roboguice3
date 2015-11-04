package ko2ic.roboguice3.application.facade;

import com.google.inject.Inject;

import ko2ic.roboguice3.infrastructure.repository.WeatherRepository;

public class WeatherFacade {

    @Inject
    private WeatherRepository repository;

    public void fetchWeahter(String cityCode){
        repository.fetchWeather(cityCode);
    }
}
