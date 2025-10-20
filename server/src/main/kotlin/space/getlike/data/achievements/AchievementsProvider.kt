package space.getlike.data.achievements

import space.getlike.data.achievements.factories.*
import space.getlike.dependencies.DataDependencies
import space.getlike.models.Achievement

class AchievementsProvider(
    private val deps: DataDependencies,
) {

    private val factories = listOf(
        AvatarSetFactory(),

        ContactsAdded1Factory(),
        ContactsAdded10Factory(),
        ContactsAdded100Factory(),

        ContactsMutual1Factory(),
        ContactsMutual10Factory(),
        ContactsMutual100Factory(),

        LikesFromContacts1Factory(),
        LikesFromContacts10Factory(),
        LikesFromContacts100Factory(),

        LikesSent1Factory(),
        LikesSent10KFactory(),
        LikesSent100KFactory(),
        LikesSent1000KFactory(),

        LikesSentMaxAtOnceFactory(),

        LikesReceived1Factory(),
        LikesReceived10KFactory(),
        LikesReceived100KFactory(),
        LikesReceived1000KFactory(),

        LikesReceivedMaxAtOnceFactory(),
    )

    fun getAchievementsByProfileId(profileId: String): List<Achievement> {
        val context = AchievementContext(
            profileContent = deps.profilesQueries.selectProfileById(profileId) ?: return emptyList(),
            contactIds = deps.contactsQueries.selectContactIds(profileId),
            beingContactIds = deps.contactsQueries.selectBeingContactIds(profileId),
            messages = deps.messagesQueries.selectAllByProfileId(profileId),
        )
        return factories.map { factory -> factory.create(context) }
    }

    fun getAchievementsByProfileIds(profileIds: Collection<String>): Map<String, List<Achievement>> =
        profileIds.associateWith { profileId -> getAchievementsByProfileId(profileId) }
}