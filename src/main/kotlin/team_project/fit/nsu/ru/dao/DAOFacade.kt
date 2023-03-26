package team_project.fit.nsu.ru.dao

import team_project.fit.nsu.ru.models.Command

interface DAOFacade {

    suspend fun allCommands(): List<Command>

    suspend fun addNewCommand(command: Command)
}