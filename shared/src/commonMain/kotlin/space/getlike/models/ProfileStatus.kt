package space.getlike.models

enum class ProfileStatus(val stringValue: String) {
    Self("Self"),
    Contact("Contact"),
    NotContact("NotContact"),
    ;

    val isSelf: Boolean
        get() = this == Self

    val isContact: Boolean
        get() = this == Contact

    val isNotContact: Boolean
        get() = this == NotContact

    companion object {

        fun fromString(value: String): ProfileStatus =
            entries.find { status -> status.stringValue == value } ?: Self
    }
}