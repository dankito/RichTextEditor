package net.dankito.richtexteditor.java.fx.localization

import org.slf4j.LoggerFactory
import java.util.*


class Localization {

    companion object {
        private val MessagesResourceBundleName = "Editor_Messages"

        private val log = LoggerFactory.getLogger(Localization::class.java)
    }


    var languageLocale: Locale = Locale.getDefault()
        set(value) {
            field = value
            Locale.setDefault(field)
            messagesResourceBundle = ResourceBundle.getBundle(MessagesResourceBundleName, field, UTF8ResourceBundleControl())
        }

    var messagesResourceBundle: ResourceBundle? = null
        private set


    init {
        try {
            messagesResourceBundle = ResourceBundle.getBundle(MessagesResourceBundleName, languageLocale, UTF8ResourceBundleControl())
        } catch (e: Exception) {
            log.error("Could not load $MessagesResourceBundleName. No Strings will now be translated, only their resource keys will be displayed.", e)
        }

    }


    fun getLocalizedString(resourceKey: String): String {
        try {
            messagesResourceBundle?.let { messagesResourceBundle ->
                return messagesResourceBundle.getString(resourceKey)
            }
        } catch (e: Exception) {
            log.error("Could not get Resource for key {} from String Resource Bundle {}", resourceKey, MessagesResourceBundleName)
        }

        return resourceKey
    }

    fun getLocalizedString(resourceKey: String, vararg formatArguments: Any): String {
        return String.format(getLocalizedString(resourceKey), *formatArguments)
    }

}
