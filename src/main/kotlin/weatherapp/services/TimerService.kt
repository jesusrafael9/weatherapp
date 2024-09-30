import kotlinx.coroutines.*
import weatherapp.services.WeatherService
import java.util.*
import kotlin.concurrent.schedule

object TimerService {
    private val locations = listOf("Santiago", "Zurich", "Auckland", "Sydney", "London", "Georgia")

    private val timerScope = CoroutineScope(Dispatchers.Default)

    fun start() {
        // Configurar un temporizador para ejecutar cada 5 minutos (300,000 milisegundos)
        Timer().schedule(delay = 0, period = 300_000) {
            timerScope.launch {
                locations.forEach { location ->
                    WeatherService.updateWeatherCache(location)
                }
            }
        }
    }

    fun stop() {
        timerScope.cancel()
    }
}