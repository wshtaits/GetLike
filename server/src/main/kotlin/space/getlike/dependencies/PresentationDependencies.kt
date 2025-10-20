package space.getlike.dependencies

import space.getlike.presentation.achievements.AchievementsHandler
import space.getlike.data.repositories.ChatRepository
import space.getlike.data.repositories.MessagingRepository
import space.getlike.data.repositories.ProfilesRepository
import space.getlike.util_messaging.ServerMessagingSender

interface PresentationDependencies {

    val profilesRepository: ProfilesRepository
    val chatRepository: ChatRepository
    val messagingRepository: MessagingRepository

    val messagingSender: ServerMessagingSender

    val achievementsHandler: AchievementsHandler
}