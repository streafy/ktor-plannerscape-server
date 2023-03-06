package team_project.fit.nsu.ru.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import team_project.fit.nsu.ru.routes.commandRouting

fun Application.configureRouting() {
    routing {
        commandRouting()
    }
}
