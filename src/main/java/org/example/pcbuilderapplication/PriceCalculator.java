package org.example.pcbuilderapplication;

public class PriceCalculator {

    public static double calculateTotal(double cpu, double gpu, double ram, double storage) {
        return cpu + gpu + ram + storage;
    }
}