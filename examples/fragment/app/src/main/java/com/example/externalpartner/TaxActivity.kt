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
            .setPartnerId("vivid")
            .setToken("your-user-token")
            .setApiUrl("https://api.test2.steuerbot.com")
            .setUser(User("sdk01@byom.de", "Max").setSurname("Power"))
            .setPaymentLink("https://www.steuerbot.com")
            .setLanguage(Language.EN)
            .setLightTheme("{\"name\":\"ExampleLight\",\"colors\":{\"primary100\":\"#880D1E\",\"primary90\":\"#DD2D4A\",\"primary80\":\"#F26A8D\",\"primary60\":\"#F49CBB\",\"accentCold80\":\"#CBEEF3\"},\"text\":{\"fontSizes\":{\"xsmall\":8,\"small\":12,\"medium\":18,\"large\":22,\"xlarge\":26}},\"button\":{\"fontSize\":2,\"lineHeight\":0,\"height\":10,\"smallHeight\":0,\"box\":{\"height\":0,\"smallHeight\":0,\"fontSize\":0,\"lineHeight\":0}}}")
            .setDarkTheme("{\"name\":\"ExampleDark\",\"colors\":{\"gray0\":\"#202121\",\"gray10\":\"#000000\",\"gray20\":\"#626263\",\"gray30\":\"#79797A\",\"gray40\":\"#9A9A9B\",\"gray50\":\"#BBBBBC\",\"gray60\":\"#DCDCDD\",\"gray70\":\"#E5E5E6\",\"gray80\":\"#EEEEEF\",\"gray90\":\"#F8F8F9\",\"gray100\":\"#FFFFFF\",\"primary100\":\"#B7A9ED\",\"primary90\":\"#A07EED\",\"primary80\":\"#8947F6\",\"primary60\":\"#6C3ECC\",\"primary40\":\"#40239F\",\"primary20\":\"#29185C\",\"warning100\":\"#FEE0AA\",\"warning90\":\"#FED68A\",\"warning80\":\"#FEBD63\",\"warning60\":\"#E4AA69\",\"warning40\":\"#B78859\",\"warning20\":\"#504535\",\"danger100\":\"#FEA3A2\",\"danger90\":\"#FF7B83\",\"danger80\":\"#FE5C62\",\"danger60\":\"#D25B75\",\"danger40\":\"#AB3E44\",\"danger20\":\"#411D21\",\"success100\":\"#B1DDA6\",\"success90\":\"#A1DD8A\",\"success80\":\"#63DD6B\",\"success60\":\"#72C181\",\"success40\":\"#5A9670\",\"success20\":\"#2B4B2B\",\"info100\":\"#A8C7F9\",\"info90\":\"#92B1F9\",\"info80\":\"#7999F9\",\"info60\":\"#6884D7\",\"info40\":\"#5D75BF\",\"info20\":\"#243254\",\"accentCold80\":\"#CBEEF3\"},\"text\":{\"fontSizes\":{\"xsmall\":8,\"small\":12,\"medium\":18,\"large\":22,\"xlarge\":26}},\"button\":{\"fontSize\":2,\"lineHeight\":0,\"height\":10,\"smallHeight\":0,\"box\":{\"height\":0,\"smallHeight\":0,\"fontSize\":0,\"lineHeight\":0}}}")
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