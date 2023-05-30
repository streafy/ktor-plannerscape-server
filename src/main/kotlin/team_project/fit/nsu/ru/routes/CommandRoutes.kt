package team_project.fit.nsu.ru.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import team_project.fit.nsu.ru.dao.dao
import team_project.fit.nsu.ru.models.Command

fun Route.commandRouting() {
    route("/commands") {
        get {
            println("||||   some client asked for all commands")
            val allCommands: List<Command> = dao.allCommands()
            call.respond(allCommands)
            println("||||   returning $allCommands to this client")
        }
        get("/{id}") {
            val id: Int? = call.parameters["id"]?.toInt()
            println("||||   some client asked for all commands from $id")
            val neededCommands: List<Command> = dao.neededCommands(id ?: 0)
            call.respond(neededCommands)
            println("||||   returning $neededCommands to this client")
        }
        post {
            val initialCount: Int = dao.allCommands().size
            val commands = call.receive<List<Command>>()
            commands.forEach { dao.addNewCommand(it) }

            println("||||   added(${commands.size}) $commands\n")
            val allCommands: List<Command> = dao.allCommands()
            val afterAdditionCount: Int = allCommands.size
            println("||||   current storage(${allCommands.size}): $allCommands")
            call.respond((initialCount + 1..afterAdditionCount).toList())
        }
    }
}