package weatherapp.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import weatherapp.services.RedisClient

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("is running")
        }

        get("/weather/{location}") {
            val location = call.parameters["location"]
            if (location != null) {
                val weatherData = RedisClient.get(location)
                if (weatherData != null) {

                    call.respond(HttpStatusCode.OK, weatherData)
                } else {

                    call.respond(HttpStatusCode.NotFound, "No data available for $location")
                }
            } else {

                call.respond(HttpStatusCode.BadRequest, "Location parameter is missing")
            }
        }
    }
}
