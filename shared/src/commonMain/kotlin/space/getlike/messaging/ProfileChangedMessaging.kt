package space.getlike.messaging

import space.getlike.models.Profile
import space.getlike.util_messaging.Messaging

object ProfileChangedMessaging : Messaging<Profile>(
    name = "ProfileChanged",
    serializer = Profile.serializer(),
)