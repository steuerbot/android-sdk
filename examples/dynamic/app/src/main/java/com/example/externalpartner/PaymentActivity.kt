package com.example.externalpartner

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.externalpartner.databinding.ActivityPaymentBinding
import com.steuerbot.sdk.Steuerbot
import com.steuerbot.sdk.action.PaymentSuccessAction

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data: Uri = intent!!.data!!
        val offerId = data.getQueryParameter("offerId")
        val submitId = data.getQueryParameter("submitId")
        val botId = data.getQueryParameter("submitId")

        binding.pay.setOnClickListener {
            Steuerbot.triggerAction(PaymentSuccessAction(3999, submitId, offerId, botId))
            kill()
        }
    }

    private fun kill() {
        finish()
    }
}