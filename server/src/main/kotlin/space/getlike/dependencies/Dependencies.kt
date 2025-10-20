package space.getlike.dependencies

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import org.jetbrains.exposed.v1.jdbc.Database
import space.getlike.data.Resources
import space.getlike.presentation.achievements.AchievementsHandler
import space.getlike.data.achievements.AchievementsProvider
import space.getlike.data.database.queries.ContactsQueries
import space.getlike.data.database.queries.MessagesQueries
import space.getlike.data.database.queries.MessagingQueries
import space.getlike.data.database.queries.ProfilesQueries
import space.getlike.presentation.messaging.MessagingContextImpl
import space.getlike.data.repositories.ChatRepository
import space.getlike.data.repositories.MessagingRepository
import space.getlike.data.repositories.ProfilesRepository
import space.getlike.util_authentication.Authentication
import space.getlike.util_messaging.ServerMessagingSender

class Dependencies : DataDependencies, PresentationDependencies {

    //region CommonDependencies

    override val achievementsProvider by lazy { AchievementsProvider(this) }

    //endregion

    //region DataDependencies

    override val contactsQueries by lazy { ContactsQueries() }
    override val messagesQueries by lazy { MessagesQueries() }
    override val messagingQueries by lazy { MessagingQueries() }
    override val profilesQueries by lazy { ProfilesQueries() }

    override val authentication by lazy { Authentication(firebaseAuth) }

    //endregion

    //region PresentationDependencies

    override val profilesRepository by lazy { ProfilesRepository(this) }
    override val chatRepository by lazy { ChatRepository(this) }
    override val messagingRepository by lazy { MessagingRepository(this) }

    override val messagingSender by lazy {
        ServerMessagingSender(
            firebaseMessaging = firebaseMessaging,
            context = messagingContext,
        )
    }

    override val achievementsHandler by lazy { AchievementsHandler(this) }

    //endregion

    val messagingContext by lazy { MessagingContextImpl(messagingRepository) }

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firebaseMessaging: FirebaseMessaging by lazy { FirebaseMessaging.getInstance() }

    init {
        configureDatabase()
        configureFirebase()
    }

    private fun configureDatabase() {
        Database.connect(
            url = Resources.Database.URL,
            driver = Resources.Database.DRIVER,
            user = Resources.Database.user,
            password = Resources.Database.password,
        )
    }

    private fun configureFirebase() {
        val credentials = GoogleCredentials.fromStream(Resources.Firebase.credentials)
        val options = FirebaseOptions.builder()
            .setCredentials(credentials)
            .build()
        FirebaseApp.initializeApp(options)
    }
}