import space.getlike.convention.client.clientModule

plugins {
    alias(libs.plugins.clientModule)
}

clientModule("main") {
    common(
        implementations = listOf(
            projects.clientChats,
            projects.clientContacts,
            projects.clientProfile,
        ),
    )
}
