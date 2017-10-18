package com.thomaskuenneth.openweathermapweather;

class WeatherData {

    String name;
    String description;
    String icon;
    Double temp;

    WeatherData(String name, String description,
                String icon, Double temp) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.temp = temp;
    }
}
