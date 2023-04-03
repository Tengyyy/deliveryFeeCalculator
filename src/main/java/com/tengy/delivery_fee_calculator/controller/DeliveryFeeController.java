package com.tengy.delivery_fee_calculator.controller;

import com.tengy.delivery_fee_calculator.exceptions.ForbiddenVehicleTypeException;
import com.tengy.delivery_fee_calculator.exceptions.RequestException;
import com.tengy.delivery_fee_calculator.model.Fee;
import com.tengy.delivery_fee_calculator.model.Weather;
import com.tengy.delivery_fee_calculator.model.constants.City;
import com.tengy.delivery_fee_calculator.model.constants.VehicleType;
import com.tengy.delivery_fee_calculator.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api")
public class DeliveryFeeController {


    @Autowired
    WeatherService weatherService;

    @GetMapping(path="/delivery_fee",
                produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
                })
    public ResponseEntity<Fee> getDeliveryFee(@RequestParam(value="city") String cityString, @RequestParam(value="vehicle_type") String vehicleTypeString, @RequestParam(value="time", required = false) String timeString){

        City city = null;

        switch (cityString.toLowerCase()){
            case "tallinn" -> city = City.TALLINN;
            case "tartu" -> city = City.TARTU;
            case "parnu", "pärnu" -> city = City.PARNU;
            default -> {
                throw new RequestException("City parameter incorrect");
            }
        }

        VehicleType vehicleType = null;

        switch (vehicleTypeString.toLowerCase()){
            case "car" -> vehicleType = VehicleType.CAR;
            case "scooter" -> vehicleType = VehicleType.SCOOTER;
            case "bike" -> vehicleType = VehicleType.BIKE;
            default -> {
                throw new RequestException("Vehicle type incorrect");
            }
        }

        if(vehicleType == VehicleType.CAR){
            Fee fee = FeeCalculator.calculateFee(vehicleType, city);
            return new ResponseEntity<>(fee, HttpStatus.OK);
        }

        Weather weather = null;

        if(timeString == null){
            weather = weatherService.getMostRecentWeather(city);
        }
        else {
            try {
                Timestamp timestamp = Timestamp.valueOf(timeString);

                weather = weatherService.getClosestWeather(city, timestamp);

            }
            catch(IllegalArgumentException ex){
                throw new RequestException("Timestamp format incorrect");
            }
        }

        if(weather == null){
            throw new RuntimeException("Weather data not found");
        }

        Fee fee = FeeCalculator.calculateFee(weather, vehicleType, city);

        if(fee.windSpeedExtraFee() == -1 || fee.weatherPhenomenonExtraFee() == -1){
            throw new ForbiddenVehicleTypeException();
        }

        return new ResponseEntity<>(fee, HttpStatus.OK);
    }
}
