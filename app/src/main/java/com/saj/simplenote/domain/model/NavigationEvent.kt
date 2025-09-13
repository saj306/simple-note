package com.saj.simplenote.domain.model

class NavigationEvent(
    private var action: () -> Unit = {}
) {

    fun setNavigateAction(action: () -> Unit) {
        this.action = action
    }

    fun navigate() {
        action()
    }
}