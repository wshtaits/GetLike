@file:Suppress("DEPRECATION")

package space.getlike.util_settings

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.MasterKeys

class SettingsImpl(
    context: Context,
    name: String,
) : Settings() {

    private val plainPrefs: SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private val securePrefs: SharedPreferences =
        EncryptedSharedPreferences.create(
            /* fileName = */ "${name}_secure",
            /* masterKeyAlias = */ MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            /* context = */ context,
            /* prefKeyEncryptionScheme = */ EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            /* prefValueEncryptionScheme = */ EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )

    override fun clear() {
        plainPrefs.edit { clear() }
        securePrefs.edit { clear() }
    }

    override fun getPlain(key: String): String? =
        plainPrefs.getString(key, "")

    override fun setPlain(key: String, value: String) =
        plainPrefs.edit { putString(key, value) }

    override fun removePlain(key: String) =
        plainPrefs.edit { remove(key) }

    override fun getSecure(key: String): String? =
        securePrefs.getString(key, "")

    override fun setSecure(key: String, value: String) =
        securePrefs.edit { putString(key, value) }

    override fun removeSecure(key: String) =
        securePrefs.edit { remove(key) }
}