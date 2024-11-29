package com.connector.utils;
import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.enterprise.context.ApplicationScoped;

//Classe onde é realizado os calculos de OEE
@ApplicationScoped
public class OEECalculator {

    public double calculateAvailability(double productionTime, double plannedProductionTime) {
        return productionTime / plannedProductionTime;
    }

    public double calculatePerformance(double itemsProduced, double productionCapacity) {
        return itemsProduced / productionCapacity;
    }

    public double calculateQuality(double itemsProduced, double defectiveItems) {
        return (itemsProduced - defectiveItems) / itemsProduced;
    }

    public double calculateOEE(double availability, double performance, double quality) {
        return availability * performance * quality * 100;
    }

    public double roundToTwoDecimalPlaces(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
