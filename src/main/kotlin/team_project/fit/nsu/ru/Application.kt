package team_project.fit.nsu.ru

import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking
import team_project.fit.nsu.ru.dao.DatabaseFactory
import team_project.fit.nsu.ru.dao.dao
import team_project.fit.nsu.ru.plugins.*
import java.util.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureRouting()


    runBlocking {
        println(dao.allCommands())
    }
}
