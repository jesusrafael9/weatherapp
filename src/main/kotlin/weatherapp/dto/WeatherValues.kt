package weatherapp.dto

import kotlinx.serialization.Serializable;

@Serializable
data class WeatherValues(
    val cloudCover: Int,
    val humidity: Int,
    val temperature: Double,
    val uvIndex: Int,
    val windSpeed: Double
)

