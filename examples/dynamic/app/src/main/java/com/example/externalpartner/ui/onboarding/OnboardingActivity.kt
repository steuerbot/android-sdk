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

        val demoCollectionAdapter = OnboardingAdapter(this)
        val viewPager = binding.viewPager
        viewPager.adapter = demoCollectionAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager)
        { tab, position -> }.attach()

        binding.cta.setOnClickListener { loadReactApp(it) }

        setContentView(binding.root)
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
                    .setPartnerId("vivid")
                    .setToken("your-user-token")
                    .setUser(User("sdk01@byom.de", "Max").setSurname("Power"))
                    .setLanguage("en")
                    .setLightTheme("{\"name\":\"ExampleLight\",\"colors\":{\"primary100\":\"#880D1E\",\"primary90\":\"#DD2D4A\",\"primary80\":\"#F26A8D\",\"primary60\":\"#F49CBB\",\"accentCold80\":\"#CBEEF3\"},\"text\":{\"fontSizes\":{\"xsmall\":8,\"small\":12,\"medium\":18,\"large\":22,\"xlarge\":26}},\"button\":{\"fontSize\":2,\"lineHeight\":0,\"height\":10,\"smallHeight\":0,\"box\":{\"height\":0,\"smallHeight\":0,\"fontSize\":0,\"lineHeight\":0}}}")
                    .setDarkTheme("{\"name\":\"ExampleDark\",\"colors\":{\"gray0\":\"#202121\",\"gray10\":\"#000000\",\"gray20\":\"#626263\",\"gray30\":\"#79797A\",\"gray40\":\"#9A9A9B\",\"gray50\":\"#BBBBBC\",\"gray60\":\"#DCDCDD\",\"gray70\":\"#E5E5E6\",\"gray80\":\"#EEEEEF\",\"gray90\":\"#F8F8F9\",\"gray100\":\"#FFFFFF\",\"primary100\":\"#B7A9ED\",\"primary90\":\"#A07EED\",\"primary80\":\"#8947F6\",\"primary60\":\"#6C3ECC\",\"primary40\":\"#40239F\",\"primary20\":\"#29185C\",\"warning100\":\"#FEE0AA\",\"warning90\":\"#FED68A\",\"warning80\":\"#FEBD63\",\"warning60\":\"#E4AA69\",\"warning40\":\"#B78859\",\"warning20\":\"#504535\",\"danger100\":\"#FEA3A2\",\"danger90\":\"#FF7B83\",\"danger80\":\"#FE5C62\",\"danger60\":\"#D25B75\",\"danger40\":\"#AB3E44\",\"danger20\":\"#411D21\",\"success100\":\"#B1DDA6\",\"success90\":\"#A1DD8A\",\"success80\":\"#63DD6B\",\"success60\":\"#72C181\",\"success40\":\"#5A9670\",\"success20\":\"#2B4B2B\",\"info100\":\"#A8C7F9\",\"info90\":\"#92B1F9\",\"info80\":\"#7999F9\",\"info60\":\"#6884D7\",\"info40\":\"#5D75BF\",\"info20\":\"#243254\",\"accentCold80\":\"#CBEEF3\"},\"text\":{\"fontSizes\":{\"xsmall\":8,\"small\":12,\"medium\":18,\"large\":22,\"xlarge\":26}},\"button\":{\"fontSize\":2,\"lineHeight\":0,\"height\":10,\"smallHeight\":0,\"box\":{\"height\":0,\"smallHeight\":0,\"fontSize\":0,\"lineHeight\":0}}}")
                    .start(this)
            }
    }

}

data class OnboardingScreen(val title: String, val subTitle: String, val imageRes: Int)

class OnboardingAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    companion object {
        val screens = arrayOf(
            OnboardingScreen(
                "Your Tax Declaration!",
                "Start now and get 1.051 € on average",
                R.drawable.intro_2
            ), OnboardingScreen(
                "It's easy and fast!",
                "Finish your taxes in 20 minutes",
                R.drawable.intro_3
            )
        )
    }

    override fun getItemCount(): Int = screens.size
    override fun createFragment(position: Int): Fragment {
        val fragment = OnboardingFragment(screens.get(position))
        fragment.arguments = Bundle().apply {}
        return fragment
    }
}

class OnboardingFragment(val screen: OnboardingScreen) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_onboarding, container, false)
        root.findViewById<ImageView>(R.id.imageView).setImageResource(screen.imageRes)
        root.findViewById<TextView>(R.id.title).text = screen.title
        root.findViewById<TextView>(R.id.subtitle).text = screen.subTitle
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}
