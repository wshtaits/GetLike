package space.getlike.data.repositories

import space.getlike.Emojis
import space.getlike.data.Resources
import space.getlike.dependencies.DataDependencies
import space.getlike.models.Achievement
import space.getlike.models.Avatar
import space.getlike.models.Profile
import space.getlike.models.ProfileContent
import space.getlike.models.ProfileStatus
import space.getlike.models.LocalContact
import kotlin.collections.orEmpty

class ProfilesRepository(
    private val deps: DataDependencies,
) {

    fun getProfile(profileId: String, selfProfileId: String? = null): Profile? {
        val contactIds = if (!selfProfileId.isNullOrEmpty()) {
            deps.contactsQueries.selectContactIds(selfProfileId)
        } else {
            emptyList()
        }
        val profileContent = deps.profilesQueries.selectProfileById(profileId) ?: return null
        return profileContent.toProfile(
            status = when (profileContent.id) {
                selfProfileId -> ProfileStatus.Self
                in contactIds -> ProfileStatus.Contact
                else -> ProfileStatus.NotContact
            },
            achievements = deps.achievementsProvider.getAchievementsByProfileId(profileId),
        )
    }

    fun getProfiles(profileIds: Collection<String>, selfProfileId: String? = null): List<Profile> {
        val contactIds = if (!selfProfileId.isNullOrEmpty()) {
            deps.contactsQueries.selectContactIds(selfProfileId)
        } else {
            emptyList()
        }
        val profileIdToAchievement = deps.achievementsProvider.getAchievementsByProfileIds(profileIds)
        return deps.profilesQueries.selectAllByIds(profileIds)
            .map { profileContent ->
                profileContent.toProfile(
                    status = when (profileContent.id) {
                        selfProfileId -> ProfileStatus.Self
                        in contactIds -> ProfileStatus.Contact
                        else -> ProfileStatus.NotContact
                    },
                    achievements = profileIdToAchievement[profileContent.id].orEmpty(),
                )
            }
    }

    fun createProfile(
        profileId: String,
        avatarUri: String?,
        name: String,
    ): Profile {
        val profileContent = ProfileContent(
            id = profileId,
            avatar = Avatar(
                uri = avatarUri?.ifBlank { null },
                fallbackEmoji = Emojis.fromString(name),
            ),
            name = name,
            totalLikesSent = 0,
            totalLikesReceived = 0,
        )

        deps.profilesQueries.upsert(profileContent)

        return profileContent.toProfile(
            status = ProfileStatus.Self,
            achievements = deps.achievementsProvider.getAchievementsByProfileId(profileId),
        )
    }

    fun editProfile(
        profileId: String,
        newName: String?,
        newAvatarBytes: ByteArray?,
    ): Profile? {
        val profileContent = deps.profilesQueries.selectProfileById(profileId) ?: return null

        val newAvatarUri = if (newAvatarBytes != null) {
            val oldAvatarUrl = deps.profilesQueries.selectAvatarUrl(profileId)
            if (!oldAvatarUrl.isNullOrEmpty()) {
                val oldFileName = oldAvatarUrl.substringAfterLast("/")
                val oldFile = Resources.Avatars.getFile(oldFileName)
                oldFile.delete()
            }
            Resources.Avatars.saveAndGetUrl(newAvatarBytes)
        } else {
            profileContent.avatar.uri
        }

        val newAvatar = Avatar(
            uri = newAvatarUri,
            fallbackEmoji = Emojis.fromString(newName ?: profileContent.name),
        )

        val newProfileContent = profileContent.copy(
            name = newName ?: profileContent.name,
            avatar = newAvatar,
        )

        deps.profilesQueries.upsert(newProfileContent)

        return newProfileContent.toProfile(
            status = ProfileStatus.Self,
            achievements = deps.achievementsProvider.getAchievementsByProfileId(profileId),
        )
    }

    fun getContactIds(profileId: String): List<String> =
        deps.contactsQueries.selectContactIds(profileId)

    fun getBeingContactIds(profileId: String): List<String> =
        deps.contactsQueries.selectBeingContactIds(profileId)

    fun getContactIdsAndBeingContactIds(profileId: String): List<String> =
        deps.contactsQueries.selectContactIdsAndBeingContactIds(profileId)

    fun getContactIdToLocalContactIdMap(
        profileId: String,
        localContacts: List<LocalContact>
    ): Map<String, String> {
        val phoneToLocalContactIdMap = localContacts
            .flatMap { contact ->
                contact.phones.map { phone ->
                    phone.filter { char -> char.isDigit() } to contact.id
                }
            }
            .toMap()

        val contactIdToPhoneMap = deps.authentication.getProfileIdsByPhones(phoneToLocalContactIdMap.keys)
            .filter { (contactId, _) -> contactId != profileId }
            .mapValues { (_, phone) -> phone.filter { char -> char.isDigit() } }

        return contactIdToPhoneMap
            .mapNotNull { (contactId, phone) ->
                val localContactId = phoneToLocalContactIdMap[phone] ?: return@mapNotNull null
                contactId to localContactId
            }
            .toMap()
    }

    fun addContact(profileId: String, contactId: String): Profile? {
        val contactContent = deps.profilesQueries.selectProfileById(contactId) ?: return null

        deps.contactsQueries.upsert(profileId, contactId)

        return contactContent.toProfile(
            status = ProfileStatus.Contact,
            achievements = deps.achievementsProvider.getAchievementsByProfileId(contactId),
        )
    }

    fun addContacts(profileId: String, contactIds: Collection<String>) =
        deps.contactsQueries.upsert(profileId, contactIds)

    fun isContact(profileId: String, contactId: String): Boolean =
        deps.contactsQueries.contains(profileId, contactId)

    fun searchContact(phone: String): String? =
        deps.authentication.getProfileIdByPhone(phone)

    fun getProfileAchievements(profileId: String): List<Achievement> =
        deps.achievementsProvider.getAchievementsByProfileId(profileId)

    fun getAchievementsByProfileIds(profileIds: Collection<String>): Map<String, List<Achievement>> =
        deps.achievementsProvider.getAchievementsByProfileIds(profileIds)
}
