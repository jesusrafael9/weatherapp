package weatherapp.services

import redis.clients.jedis.Jedis
import kotlinx.serialization.encodeToString
import weatherapp.dto.WeatherValues
import kotlinx.serialization.json.Json

object RedisClient {
    private val redisHost = System.getenv("REDIS_HOST") ?: "localhost"
    private val redisPort = System.getenv("REDIS_PORT")?.toInt() ?: 6379
    private val jedis = Jedis(redisHost, redisPort)

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