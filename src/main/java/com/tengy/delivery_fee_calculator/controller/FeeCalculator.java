package com.tengy.delivery_fee_calculator.controller;

import com.tengy.delivery_fee_calculator.model.Fee;
import com.tengy.delivery_fee_calculator.model.constants.City;
import com.tengy.delivery_fee_calculator.model.constants.Phenomenon;
import com.tengy.delivery_fee_calculator.model.constants.VehicleType;
import com.tengy.delivery_fee_calculator.model.Weather;

import java.util.Map;

public class FeeCalculator {

    private static final Map<String, Phenomenon> weatherPhenomenonMap = Map.ofEntries(
        Map.entry("Clear", Phenomenon.NORMAL),
        Map.entry("Few clouds", Phenomenon.NORMAL),
        Map.entry("Variable clouds", Phenomenon.NORMAL),
        Map.entry("Cloudy with clear spells", Phenomenon.NORMAL),
        Map.entry("Overcast", Phenomenon.NORMAL),
        Map.entry("Light snow shower", Phenomenon.SNOW),
        Map.entry("Moderate snow shower", Phenomenon.SNOW),
        Map.entry("Heavy snow shower", Phenomenon.SNOW),
        Map.entry("Light shower", Phenomenon.RAIN),
        Map.entry("Moderate shower", Phenomenon.RAIN),
        Map.entry("Heavy shower", Phenomenon.RAIN),
        Map.entry("Light rain", Phenomenon.RAIN),
        Map.entry("Moderate rain", Phenomenon.RAIN),
        Map.entry("Heavy rain", Phenomenon.RAIN),
        Map.entry("Glaze", Phenomenon.FORBIDDEN),
        Map.entry("Light sleet", Phenomenon.SNOW),
        Map.entry("Moderate sleet", Phenomenon.SNOW),
        Map.entry("Light snowfall", Phenomenon.SNOW),
        Map.entry("Moderate snowfall", Phenomenon.SNOW),
        Map.entry("Heavy snowfall", Phenomenon.SNOW),
        Map.entry("Blowing snow", Phenomenon.SNOW),
        Map.entry("Drifting snow", Phenomenon.SNOW),
        Map.entry("Hail", Phenomenon.FORBIDDEN),
        Map.entry("Mist", Phenomenon.NORMAL),
        Map.entry("Fog", Phenomenon.NORMAL),
        Map.entry("Thunder", Phenomenon.FORBIDDEN),
        Map.entry("Thunderstorm", Phenomenon.FORBIDDEN)
    );

    public static Fee calculateFee(VehicleType vehicleType, City city){
        double rbf = getRBF(vehicleType, city);

        return new Fee(rbf, rbf, 0, 0, 0, "euro");
    }

    public static Fee calculateFee(Weather weather, VehicleType vehicleType, City city){
        double rbf = getRBF(vehicleType, city);
        double atef = getATEF(vehicleType, weather);
        double wsef = getWSEF(vehicleType, weather);
        double wpef = getWPEF(vehicleType, weather);

        double total = rbf + atef + wsef + wpef;


        return new Fee(total, rbf, atef, wsef, wpef, "euro");
    }

    private static double getRBF(VehicleType vehicleType, City city){
        if(city == City.TALLINN){
            if(vehicleType == VehicleType.CAR) return 4;
            else if(vehicleType == VehicleType.SCOOTER) return 3.5;
            else return 3;
        }

        else if(city == City.TARTU){
            if(vehicleType == VehicleType.CAR) return 3.5;
            else if(vehicleType == VehicleType.SCOOTER) return 3;
            else return 2.5;
        }

        else {
            if(vehicleType == VehicleType.CAR) return 3;
            else if(vehicleType == VehicleType.SCOOTER) return 2.5;
            else return 2;
        }
    }

    private static double getATEF(VehicleType vehicleType, Weather weather){
        if(vehicleType != VehicleType.CAR){
            if (weather.airTemp() < -10) return 1;
            else if (weather.airTemp() < 0) return 0.5;
            else return 0;
        }

        return 0;
    }

    private static double getWSEF(VehicleType vehicleType, Weather weather){
        if(vehicleType != VehicleType.CAR){
          if(weather.windSpeed() > 10 && weather.windSpeed() < 20) return 0.5;
          else if(weather.windSpeed() >= 20) return -1;
        }

        return 0;
    }

    private static double getWPEF(VehicleType vehicleType, Weather weather){
        if(vehicleType != VehicleType.CAR){
            if(weatherPhenomenonMap.containsKey(weather.weatherPhenomenon())){
                Phenomenon phenomenon = weatherPhenomenonMap.get(weather.weatherPhenomenon());

                if(phenomenon == Phenomenon.SNOW) return 1;
                else if(phenomenon == Phenomenon.RAIN) return 0.5;
                else if(phenomenon == Phenomenon.FORBIDDEN) return -1;
            }
        }

        return 0;
    }
}
