package me.seclerp.rider.plugins.efcore.features.migrations.remove

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import me.seclerp.rider.plugins.efcore.cli.api.MigrationsCommandFactory
import me.seclerp.rider.plugins.efcore.cli.api.models.DotnetEfVersion
import me.seclerp.rider.plugins.efcore.cli.execution.CliCommandResult
import me.seclerp.rider.plugins.efcore.features.shared.dialog.CommonDialogWrapper
import java.util.*

class RemoveLastMigrationDialogWrapper(
    toolsVersion: DotnetEfVersion,
    intellijProject: Project,
    selectedProjectId: UUID?,
) : CommonDialogWrapper<RemoveLastMigrationDataContext>(
    RemoveLastMigrationDataContext(intellijProject),
    toolsVersion,
    "Remove Last Migration",
    intellijProject,
    selectedProjectId,
    true
) {
    val migrationsCommandFactory = intellijProject.service<MigrationsCommandFactory>()

    //
    // Constructor
    init {
        initUi()
    }

    override fun generateCommand(): GeneralCommandLine {
        val commonOptions = getCommonOptions()

        return migrationsCommandFactory.removeLast(commonOptions)
    }

    override fun postCommandExecute(commandResult: CliCommandResult) {
        if (!commandResult.succeeded) {
            return
        }

        val folderService = intellijProject.service<RemoveLastMigrationFolderService>()
        folderService.deleteMigrationsFolderIfEmpty(dataCtx.availableMigrations.value.firstOrNull())
    }
}