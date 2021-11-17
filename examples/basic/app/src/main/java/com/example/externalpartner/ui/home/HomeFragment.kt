package com.example.externalpartner.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.externalpartner.R
import com.example.externalpartner.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val pocketContainer = binding.pocketContainer
        val li = LayoutInflater.from(context)

        binding.alert.container.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_onboarding)
        }

        arrayOf(
            PocketDefinition("Main Pocket", "10,00 €", R.drawable.card_1),
            PocketDefinition("Stock Rewards", "Get your reward", R.drawable.card_2),
            PocketDefinition("Investment Pocket", "Fill out data", R.drawable.card_3),
            PocketDefinition("Free spendings", "Click to create", R.drawable.card_4),
        ).forEach {
            val view = li.inflate(R.layout.view_home_pocket, pocketContainer, false)
            view.findViewById<ImageView>(R.id.image).setImageResource(it.imageSrc)
            view.findViewById<TextView>(R.id.title).text = it.title
            view.findViewById<TextView>(R.id.subtitle).text = it.subTitle
            pocketContainer.addView(view)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class PocketDefinition(val title: String, val subTitle: String, val imageSrc: Int)