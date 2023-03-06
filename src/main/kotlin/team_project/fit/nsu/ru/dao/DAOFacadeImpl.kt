package team_project.fit.nsu.ru.dao

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import team_project.fit.nsu.ru.dao.DatabaseFactory.dbQuery
import team_project.fit.nsu.ru.models.*
import java.util.*

class DAOFacadeImpl : DAOFacade {
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

    override suspend fun addNewCommand(command: Command): Command? = dbQuery {
        val insertStatement = Commands.insert {
            it[id] = command.id
            it[commandType] = command.commandType.name
            it[objectId] = command.objectId.toString()
            it[objectType] = command.objectType
            it[fieldId] = command.fieldId.toString()
            it[fieldName] = command.fieldName
            it[fieldType] = command.fieldType
            it[fieldValue] = command.fieldValue.toString()
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToCommand)
    }
}

val dao: DAOFacade = DAOFacadeImpl().apply {
    runBlocking {
        if (allCommands().isEmpty()) {
            val taskObjectId = UUID.randomUUID()
            val taskTextFieldId = UUID.randomUUID()
            val taskStatusFieldId = UUID.randomUUID()

            addNewCommand(
                Command(
                    id = 1,
                    commandType = CommandType.CREATE_OBJECT_COMMAND,
                    objectId = taskObjectId,
                    objectType = "task"
                )
            )

            addNewCommand(
                Command(
                    id = 2,
                    commandType = CommandType.ADD_FIELD_COMMAND,
                    objectId = taskObjectId,
                    fieldId = taskTextFieldId,
                    fieldName = "taskText",
                    fieldType = "String"
                )
            )

            addNewCommand(
                Command(
                    id = 3,
                    commandType = CommandType.SET_FIELD_VALUE,
                    fieldId = taskTextFieldId,
                    fieldValue = "task from server"
                )
            )

            addNewCommand(
                Command(
                    id = 4,
                    commandType = CommandType.ADD_FIELD_COMMAND,
                    objectId = taskObjectId,
                    fieldId = taskStatusFieldId,
                    fieldName = "taskStatus",
                    fieldType = "String"
                )
            )

            addNewCommand(
                Command(
                    id = 5,
                    commandType = CommandType.SET_FIELD_VALUE,
                    fieldId = taskStatusFieldId,
                    fieldValue = "ACTIVE"
                )
            )
        }
        println("NOT EMPTY [${allCommands().size}]")
    }
}