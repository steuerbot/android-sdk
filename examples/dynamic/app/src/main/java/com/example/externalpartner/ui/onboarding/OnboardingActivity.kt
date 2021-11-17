package com.example.externalpartner.ui.onboarding

import android.content.Intent
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
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest

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
                val config = Bundle();
                config.putString("theme",
                    "{\"name\":\"CustomTheme\",\"font\":\"Inter-Bold\",\"colors\":{\"primary100\":\"#4e00c7\",\"primary90\":\"#7222f5\",\"primary80\":\"#7D33F6\",\"primary60\":\"#9259f5\",\"primary40\":\"#ab85f5\",\"primary20\":\"#d9cafc\"}}"
                )
                startActivity(
                    Intent()
                        .setClassName(
                            "com.example.externalpartner",
                            "com.steuerbot.sdk.SteuerbotActivity"
                        )
                        .putExtras(config)
                )
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
