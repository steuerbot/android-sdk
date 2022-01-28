package com.example.externalpartner.steuerbot

import android.content.Context

class User {
    private lateinit var clazz: Class<*>;
    private lateinit var instance: Any;
    constructor(email: String, forename: String) {
        clazz = Class.forName("com.steuerbot.sdk.User")
        instance = clazz.getConstructor(String::class.java, String::class.java).newInstance(email, forename);
    }

    fun setSurname(surname: String): User {
        clazz.getMethod("setSurname", String::class.java).invoke(instance, surname)
        return this
    }

    fun getInstance(): Any {
        return instance;
    }
}