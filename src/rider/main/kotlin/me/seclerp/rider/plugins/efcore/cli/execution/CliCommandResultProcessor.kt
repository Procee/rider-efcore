package me.seclerp.rider.plugins.efcore.cli.execution

import me.seclerp.observables.Event

abstract class CliCommandResultProcessor {
    private val postExecutedEvent = Event<CliCommandResult>()

    fun process(result: CliCommandResult, retryAction: () -> Unit) {
        doProcess(result, retryAction)
        postExecutedEvent.invoke(result)
    }
    protected abstract fun doProcess(result: CliCommandResult, retryAction: () -> Unit)
    fun withPostExecuted(action: (CliCommandResult) -> Unit): CliCommandResultProcessor {
        postExecutedEvent += action

        return this
    }
}
