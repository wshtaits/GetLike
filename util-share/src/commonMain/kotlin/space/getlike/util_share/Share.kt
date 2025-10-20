package space.getlike.util_share

abstract class Share {

    internal abstract val destinationToSharing: Map<ShareDestination, Sharing>

    fun text(
        text: String,
        title: String? = null,
        phone: String? = null,
        destination: ShareDestination = ShareDestination.System,
    ) =
        destinationToSharing[destination]?.text(text, title, phone)

    fun availableDestinations(): List<ShareDestination> =
        destinationToSharing
            .filter { (_, sharing) -> sharing.isAvailable() }
            .map { (destination, _) -> destination }
}
