package space.getlike.data

import space.getlike.Urls
import space.getlike.util_core.utils.mime
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object Resources {

    object Avatars {

        private val DIRECTORY = File("server/src/main/resources/uploads/avatars")

        @OptIn(ExperimentalUuidApi::class)
        fun saveAndGetUrl(bytes: ByteArray): String? {
            val mime = bytes.mime ?: return null
            val fileName = Uuid.Companion.random().toString()
            val file = File(DIRECTORY, "$fileName.${mime.fileExtension}")
            file.parentFile.mkdirs()
            file.writeBytes(bytes)
            return Urls.avatar(fileName, mime)
        }

        fun getFile(fileName: String): File =
            File(DIRECTORY, fileName)
    }

    object Terms {

        val text: String
            get() = this::class.java.classLoader?.getResource("terms.md")?.readText().orEmpty()
    }

    object Firebase {

        val credentials: InputStream
            get() = this::class.java.classLoader.getResourceAsStream("firebase-service-account-key.json")
                ?: throw FileNotFoundException("firebase-service-account-key.json not found")
    }

    object Database {

        const val URL = "jdbc:postgresql://localhost:5432/getlike"
        const val DRIVER = "org.postgresql.Driver"

        val user = System.getenv("GET_LIKE_DB_USER")
            ?: error("Environment variable GET_LIKE_DB_USER not set")

        val password = System.getenv("GET_LIKE_DB_PASSWORD")
            ?: error("Environment variable GET_LIKE_DB_PASSWORD not set")
    }
}