package team_project.fit.nsu.ru.plugins

import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.serialization.gson.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
}
