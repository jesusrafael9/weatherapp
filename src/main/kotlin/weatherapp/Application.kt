package weatherapp


import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import weatherapp.plugins.*

fun main() {

    TimerService.start()

    val server = embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)

    server.addShutdownHook {
        // Detener el temporizador cuando la aplicación se detiene
        TimerService.stop()
        println("TimerService detenido.")
    }

    server.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()  // Configura la negociación de contenido para usar kotlinx.serialization
    }
    configureRouting()
}
