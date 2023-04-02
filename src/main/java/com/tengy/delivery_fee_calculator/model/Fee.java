package com.tengy.delivery_fee_calculator.model;

public record Fee(double totalFee, double regionalBaseFee, double airTemperatureExtraFee, double windSpeedExtraFee, double weatherPhenomenonExtraFee, String currency) {
}
