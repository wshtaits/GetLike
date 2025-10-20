package space.getlike.requests

import kotlinx.rpc.annotations.Rpc
import space.getlike.models.Profile

@Rpc
interface EditProfileRequest {

    suspend fun execute(newName: String?, newAvatarBytes: ByteArray?): Profile?
}