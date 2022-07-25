package com.example.externalpartner.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.externalpartner.R
import com.example.externalpartner.databinding.ActivityOnboardingBinding
import com.example.externalpartner.steuerbot.Steuerbot
import com.example.externalpartner.steuerbot.User
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import java.lang.reflect.Constructor

class OnboardingActivity : FragmentActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)

        binding.cta.setOnClickListener { loadReactApp(it) }

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

    fun loadReactApp(view: View) {
        // Creates an instance of SplitInstallManager.
        val splitInstallManager = SplitInstallManagerFactory.create(view.context)

        // Creates a request to install a module.
        val request =
            SplitInstallRequest
                .newBuilder()
                // You can download multiple on demand modules per
                // request by invoking the following method for each
                // module you want to install.
                .addModule("steuerbot")
                .build()

        splitInstallManager
            // Submits the request to install the module through the
            // asynchronous startInstall() task. Your app needs to be
            // in the foreground to submit the request.
            .startInstall(request)
            // You should also be able to gracefully handle
            // request state changes and errors. To learn more, go to
            // the section about how to Monitor the request state.
            .addOnSuccessListener { sessionId ->
                Steuerbot()
                    .setPartnerId("sdktest")
                    .setPartnerName("YourApp")
                    .setToken(getSHA512(binding.editTextTextPassword.text.toString()))
                    .setUser(User(binding.editTextTextEmailAddress.text.toString(), binding.editTextTextPersonName.text.toString()).setSurname(binding.editTextTextPersonName2.text.toString()))
                    .setLanguage("en")
                    .start(this)
            }
    }

}
