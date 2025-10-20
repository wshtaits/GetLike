package space.getlike.util_logger

internal expect fun logMessage(message: String)

abstract class Logger internal constructor() {

    abstract fun setCustomKey(key: String, value: String)

    abstract fun logException(throwable: Throwable)

    fun logEvent(
        screen: String,
        name: String,
        element: Element,
        action: Action,
        params: Map<String, Any>? = null,
    ) {
        val event = "${screen}_${name}_${element.stringValue}_${action.stringValue}"
        logEvent(event, params)
    }

    fun logAppLaunch() =
        logEvent("App_Launch", null)

    protected abstract fun logEvent(name: String, params: Map<String, Any>?)

    companion object {

        fun log(message: String) =
            logMessage(message)
    }

    enum class Element(val stringValue: String) {
        Screen(stringValue = "Screen"),
        Field(stringValue = "Field"),
        Action(stringValue = "Action"),
        Button(stringValue = "Button"),
        KeyboardButton(stringValue = "KeyboardButton"),
        ListItem(stringValue = "ListItem"),
        Image(stringValue = "Image"),
        Menu(stringValue = "Menu"),
        MenuItem(stringValue = "MenuItem"),
    }

    enum class Action(val stringValue: String) {
        Launch(stringValue = "Launch"),
        Click(stringValue = "Click"),
        Change(stringValue = "Change"),
        Dismiss(stringValue = "Dismiss"),
        Confirm(stringValue = "Confirm"),
        Press(stringValue = "Press"),
        Release(stringValue = "Release"),
    }
}