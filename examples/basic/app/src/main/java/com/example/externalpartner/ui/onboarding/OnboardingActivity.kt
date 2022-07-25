package com.example.externalpartner.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
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
        Steuerbot()
            .setPartnerId("sdktest")
            .setPartnerName("YourApp")
            .setToken(getSHA512(binding.editTextTextPassword.text.toString()))
            .setApiUrl("https://api.test2.steuerbot.com")
            .setUser(User(binding.editTextTextEmailAddress.text.toString(), binding.editTextTextPersonName.text.toString()).setSurname(binding.editTextTextPersonName2.text.toString()))
            .setPaymentLink("externalpartner://payment")
            .setLanguage(Language.EN)
            .activateDebugMode()
            .start(this)
    }

}
