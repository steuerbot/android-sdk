package your.package.id

import android.content.Context

class Steuerbot {
    private lateinit var clazz: Class<*>;
    private lateinit var instance: Any;
    constructor() {
        clazz = Class.forName("com.steuerbot.sdk.Steuerbot")
        instance = clazz.getConstructor().newInstance();
    }

    fun setPartnerId(partnerId: String): Steuerbot {
        clazz.getMethod("setPartnerId", String::class.java).invoke(instance, partnerId)
        return this
    }

    fun setToken(token: String): Steuerbot {
        clazz.getMethod("setToken", String::class.java).invoke(instance, token)
        return this
    }

    fun setLightTheme(lightTheme: String): Steuerbot {
        clazz.getMethod("setLightTheme", String::class.java).invoke(instance, lightTheme)
        return this
    }

    fun setDarkTheme(darkTheme: String): Steuerbot {
        clazz.getMethod("setDarkTheme", String::class.java).invoke(instance, darkTheme)
        return this
    }

    fun setUser(user: User): Steuerbot {
        clazz.getMethod("setUser", user.getInstance().javaClass).invoke(instance, user.getInstance())
        return this
    }

    fun setLanguage(lang: String): Steuerbot {
        val Language: Class<*> = Class.forName("com.steuerbot.sdk.Language")
        val language = Language.getDeclaredMethod("fromValue", String::class.java).invoke(null, lang)

        clazz.getMethod("setLanguage", language.javaClass).invoke(instance, language)
        return this
    }

    fun start(context: Context): Steuerbot {
        clazz.getMethod("start", Context::class.java).invoke(instance, context)
        return this
    }
}