package com.example.externalpartner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.externalpartner.databinding.ActivityTaxBinding
import com.steuerbot.sdk.Language
import com.steuerbot.sdk.Steuerbot
import com.steuerbot.sdk.SteuerbotHardwareBackBtnHandler
import com.steuerbot.sdk.User

class TaxActivity : AppCompatActivity(), SteuerbotHardwareBackBtnHandler {

    private lateinit var binding: ActivityTaxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment: Fragment = Steuerbot()
            .setPartnerId("sdktest")
            .setPartnerName("YourApp")
            .setToken("your-user-token")
            .setApiUrl("https://api.test2.steuerbot.com")
            .setUser(User("sdk01@byom.de", "Max").setSurname("Power"))
            .setPaymentLink("https://www.steuerbot.com")
            .setLanguage(Language.EN)
            .buildFragment()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.reactNativeFragment, fragment)
            .commit()
    }

    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }
}