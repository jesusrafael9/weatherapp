package weatherapp.services

import redis.clients.jedis.Jedis
import kotlinx.serialization.encodeToString
import weatherapp.dto.WeatherValues
import kotlinx.serialization.json.Json

object RedisClient {
    private val jedis = Jedis("redis", 6379)

    fun set(location: String, data: WeatherValues) {
        val jsonData = Json.encodeToString(data)  // Serializar a JSON
        jedis.set(location, jsonData)  // Almacenar el JSON en Redis
    }

    fun get(location: String): WeatherValues? {
        val jsonData = jedis.get(location) ?: return null
        return Json.decodeFromString(jsonData)
    }

    fun logError(errorMessage: String) {
        jedis.rpush("weather_errors", errorMessage)
    }
}