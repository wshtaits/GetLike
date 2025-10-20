package space.getlike.dependencies

import space.getlike.data.achievements.AchievementsProvider
import space.getlike.data.database.queries.ContactsQueries
import space.getlike.data.database.queries.MessagesQueries
import space.getlike.data.database.queries.MessagingQueries
import space.getlike.data.database.queries.ProfilesQueries
import space.getlike.util_authentication.Authentication

interface DataDependencies {

    val contactsQueries: ContactsQueries
    val messagesQueries: MessagesQueries
    val messagingQueries: MessagingQueries
    val profilesQueries: ProfilesQueries

    val authentication: Authentication

    val achievementsProvider: AchievementsProvider
}