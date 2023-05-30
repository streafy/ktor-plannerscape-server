package team_project.fit.nsu.ru.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import team_project.fit.nsu.ru.dao.dao
import team_project.fit.nsu.ru.models.Command

fun Route.commandRouting() {
    route("/commands") {
        get {
            call.respond(dao.allCommands())
        }
        get("/{id}") {
            val id: Int? = call.parameters["id"]?.toInt()
            println("$id <-- id here")
            call.respond(dao.neededCommands(id ?: 0))
        }
        post {
            val commands = call.receive<List<Command>>()
            commands.forEach { dao.addNewCommand(it) }
            call.respondText("Commands stored correctly", status = HttpStatusCode.Created)
        }
    }
}