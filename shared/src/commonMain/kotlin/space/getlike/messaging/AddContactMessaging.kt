package space.getlike.messaging

import space.getlike.models.Profile
import space.getlike.util_messaging.Messaging

object AddContactMessaging : Messaging<Profile>(
    name = "AddContact",
    serializer = Profile.serializer(),
)