package weatherapp.services

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

import kotlinx.serialization.json.*
import weatherapp.dto.WeatherValues
import java.util.*
import java.util.Properties
import java.io.File

object WeatherService {

    private val client = HttpClient()

    private val apiKey: String by lazy {
        System.getenv("API_KEY") ?: throw IllegalStateException("API Key not found")
    }

    suspend fun fetchWeather(location: String): WeatherValues {

        // Simular un fallo del 20%
        if (Math.random() < 0.2) throw Exception("The API Request Failed")

        val url = "https://api.tomorrow.io/v4/timelines?location=$location&fields=temperature,cloudCover,humidity,uvIndex,windSpeed&apikey=$apiKey"
        val response: HttpResponse = client.get(url)
        val jsonResponse = response.bodyAsText()

        val jsonElement = Json.parseToJsonElement(jsonResponse)
        val valuesJson = jsonElement.jsonObject["data"]
            ?.jsonObject?.get("timelines")
            ?.jsonArray?.firstOrNull()
            ?.jsonObject?.get("intervals")
            ?.jsonArray?.firstOrNull()
            ?.jsonObject?.get("values")
            ?.toString()

        return (valuesJson?.let {
            Json.decodeFromString<WeatherValues>(it)
        } ?: throw Exception("No weather data found"))
    }

    suspend fun getWeatherWithRetry(location: String): WeatherValues {
        val maxRetries = 3
        var attempt = 0
        var result: WeatherValues? = null
        while (attempt < maxRetries) {
            try {
                val weatherData: WeatherValues = fetchWeather(location)
                result = weatherData
                break  // Si tiene éxito, salir del loop
            } catch (e: Exception) {
                attempt++
                val errorMessage = "${Date()} - Error fetching data for $location: ${e.message}. Attempt $attempt of $maxRetries"
                println("DEBUG: $errorMessage")  // Imprimir para depurar
                RedisClient.logError(errorMessage)
                if (attempt == maxRetries) {
                    println("DEBUG: Failed to fetch weather data after $maxRetries retries")
                    throw Exception("Failed to fetch weather data after $maxRetries retries")
                }
            }
        }
        return result ?: throw Exception("Failed to retrieve weather data")
    }

    suspend fun updateWeatherCache(location: String) {
        val weatherData = getWeatherWithRetry(location)
        RedisClient.set(location, weatherData)
    }
}