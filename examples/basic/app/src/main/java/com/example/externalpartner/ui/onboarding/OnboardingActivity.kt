package com.example.externalpartner.ui.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.externalpartner.R
import com.example.externalpartner.databinding.ActivityOnboardingBinding
import com.steuerbot.sdk.Language
import com.steuerbot.sdk.Steuerbot
import com.steuerbot.sdk.User
import java.math.BigInteger
import java.security.MessageDigest

class OnboardingActivity : FragmentActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)

        binding.cta.setOnClickListener { openReactApp(it) }

        setContentView(binding.root)

        val prefs: SharedPreferences = applicationContext.getSharedPreferences("externalPartner", 0)
        binding.apiUrl.setText(prefs.getString("apiUrl", "https://api.staging.steuerbot.com"))
        binding.editTextTextPersonName.setText(prefs.getString("forename", ""))
        binding.editTextTextPersonName2.setText(prefs.getString("surname", ""))
        binding.editTextTextEmailAddress.setText(prefs.getString("email", ""))
        binding.editTextTextPassword.setText(prefs.getString("password", ""))
        binding.debug.isChecked = prefs.getBoolean("debug", false)
        binding.language.setSelection(resources.getStringArray(R.array.languages).indexOf(prefs.getString("language", "en")))
        binding.lightTheme.setText(prefs.getString("lightTheme", ""))
        binding.darkTheme.setText(prefs.getString("darkTheme", ""))
    }

    private fun getSHA512(input:String):String{
        val md: MessageDigest = MessageDigest.getInstance("SHA-512")
        val messageDigest = md.digest(input.toByteArray())

        // Convert byte array into signum representation
        val no = BigInteger(1, messageDigest)

        // Convert message digest into hex value
        var hashtext: String = no.toString(16)

        // Add preceding 0s to make it 128 chars long
        while (hashtext.length < 128) {
            hashtext = "0$hashtext"
        }

        // return the HashText
        return hashtext
    }

    fun openReactApp(view: View) {
        val prefs: SharedPreferences = applicationContext.getSharedPreferences("externalPartner", 0)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString("apiUrl", binding.apiUrl.text.toString())
        editor.putString("forename", binding.editTextTextPersonName.text.toString())
        editor.putString("surname", binding.editTextTextPersonName2.text.toString())
        editor.putString("email", binding.editTextTextEmailAddress.text.toString())
        editor.putString("password", binding.editTextTextPassword.text.toString())
        editor.putBoolean("debug", binding.debug.isChecked)
        editor.putString("language", binding.language.selectedItem.toString())
        editor.putString("lightTheme", binding.lightTheme.text.toString())
        editor.putString("darkTheme", binding.darkTheme.text.toString())
        editor.commit()

        val steuerbot = Steuerbot()
            .setPartnerId("sdktest")
            .setPartnerName("YourApp")
            .setToken(getSHA512(binding.editTextTextPassword.text.toString()))
            .setApiUrl(binding.apiUrl.text.toString())
            .setUser(User(binding.editTextTextEmailAddress.text.toString(), binding.editTextTextPersonName.text.toString()).setSurname(binding.editTextTextPersonName2.text.toString()))
            .setPaymentLink("externalpartner://payment")
            .setLanguage(Language.EN)

        if(binding.debug.isChecked) {
            steuerbot.activateDebugMode()
        }

        if(binding.language.selectedItem.toString() == "de") {
            steuerbot.setLanguage(Language.DE)
        } else {
            steuerbot.setLanguage(Language.EN)
        }

        if(binding.lightTheme.text.toString() != "") {
            steuerbot.setLightTheme(binding.lightTheme.text.toString())
        }
        if(binding.darkTheme.text.toString() != "") {
            steuerbot.setDarkTheme(binding.darkTheme.text.toString())
        }

        steuerbot
            .start(this)
    }

}
