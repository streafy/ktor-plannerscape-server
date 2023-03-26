package team_project.fit.nsu.ru.dao

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
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
                it[fieldValue] = command.fieldValue.toString()
            }
        }
    }
}

val dao: DAOFacade = DAOFacadeImpl().apply {
    runBlocking {
        if (allCommands().isEmpty()) {
            println("EMPTY [${allCommands().size}] -> LOADING MOCK DATA")
            val taskObjectId = UUID.randomUUID()
            val taskTextFieldId = UUID.randomUUID()
            val taskStatusFieldId = UUID.randomUUID()

            addNewCommand(
                Command(
                    id = -1,
                    commandType = CommandType.CREATE_OBJECT_COMMAND,
                    objectId = taskObjectId,
                    objectType = "task"
                )
            )

            addNewCommand(
                Command(
                    id = -1,
                    commandType = CommandType.ADD_FIELD_COMMAND,
                    objectId = taskObjectId,
                    fieldId = taskTextFieldId,
                    fieldName = "taskText",
                    fieldType = "String"
                )
            )

            addNewCommand(
                Command(
                    id = -1,
                    commandType = CommandType.SET_FIELD_VALUE,
                    fieldId = taskTextFieldId,
                    fieldValue = "task from server"
                )
            )

            addNewCommand(
                Command(
                    id = -1,
                    commandType = CommandType.ADD_FIELD_COMMAND,
                    objectId = taskObjectId,
                    fieldId = taskStatusFieldId,
                    fieldName = "taskStatus",
                    fieldType = "String"
                )
            )

            addNewCommand(
                Command(
                    id = -1,
                    commandType = CommandType.SET_FIELD_VALUE,
                    fieldId = taskStatusFieldId,
                    fieldValue = "ACTIVE"
                )
            )
        } else {
            println("NOT EMPTY [${allCommands().size}]")
        }
    }
}