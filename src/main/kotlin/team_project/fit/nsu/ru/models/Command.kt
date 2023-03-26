package team_project.fit.nsu.ru.models

import java.util.UUID
import org.jetbrains.exposed.sql.*

data class Command(
    val id: Int,
    val commandType: CommandType,
    val objectId: UUID? = null,
    val objectType: String? = null,
    val fieldId: UUID? = null,
    val fieldName: String? = null,
    val fieldType: String? = null,
    val fieldValue: String? = null
)

enum class CommandType {
    CREATE_OBJECT_COMMAND, ADD_FIELD_COMMAND, SET_FIELD_VALUE
}

object Commands : Table() {
    val id = integer("id")
    val commandType = varchar("commandType", 30)
    val objectId = varchar("objectId", 100).nullable()
    val objectType = varchar("objectType", 50).nullable()
    val fieldId = varchar("fieldId", 100).nullable()
    val fieldName = varchar("fieldName", 50).nullable()
    val fieldType = varchar("fieldType", 50).nullable()
    val fieldValue = varchar("fieldValue", 100).nullable()
}