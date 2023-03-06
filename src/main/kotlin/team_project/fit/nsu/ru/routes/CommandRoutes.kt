package team_project.fit.nsu.ru.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import team_project.fit.nsu.ru.dao.dao

fun Route.commandRouting() {
    route("/commands") {
        get {
            call.respond(dao.allCommands())
        }
        post {
            val formParameters = call.receiveParameters()
        }
    }
}