package team_project.fit.nsu.ru.dao

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import team_project.fit.nsu.ru.dao.DatabaseFactory.dbQuery
import team_project.fit.nsu.ru.models.Command
import team_project.fit.nsu.ru.models.CommandType
import team_project.fit.nsu.ru.models.Commands
import java.util.*

class DAOFacadeImpl : DAOFacade {
    private var commandCount = transaction { Commands.selectAll().count().toInt() }

    private fun resultRowToCommand(row: ResultRow): Command =
        Command(
            id = row[Commands.id],
            commandType = CommandType.valueOf(row[Commands.commandType]),
            objectId = if (row[Commands.objectId] != "null") UUID.fromString(row[Commands.objectId]) else null,
            objectType = row[Commands.objectType],
            fieldId = if (row[Commands.fieldId] != "null") UUID.fromString(row[Commands.fieldId]) else null,
            fieldName = row[Commands.fieldName],
            fieldType = row[Commands.fieldType],
            fieldValue = row[Commands.fieldValue]
        )

    override suspend fun allCommands(): List<Command> = dbQuery {
        Commands.selectAll().map(::resultRowToCommand)
    }

    override suspend fun neededCommands(id: Int): List<Command> = dbQuery {
        Commands.select { Commands.id greater id }.map(::resultRowToCommand)
    }


    override suspend fun addNewCommand(command: Command) {
        if (command.id != -1) {
            return
        }
        dbQuery {
            Commands.insert {
                it[id] = ++commandCount
                it[commandType] = command.commandType.name
                it[objectId] = command.objectId.toString()
                it[objectType] = command.objectType
                it[fieldId] = command.fieldId.toString()
                it[fieldName] = command.fieldName
                it[fieldType] = command.fieldType
                it[fieldValue] = command.fieldValue
            }
        }
    }
}

val dao: DAOFacade = DAOFacadeImpl().apply {
    runBlocking {
        if (allCommands().isEmpty()) {
            println("EMPTY")
        } else {
            println("NOT EMPTY [${allCommands().size}]")
        }
    }
}