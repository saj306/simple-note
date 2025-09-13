package com.saj.simplenote.domain.model

class ParameterizedNavigationEvent<T>(
    private var action: (T?) -> Unit = {}
) {

    fun setNavigateAction(action: (T?) -> Unit) {
        this.action = action
    }

    fun navigate(parameter: T? = null) {
        action(parameter)
    }
}
