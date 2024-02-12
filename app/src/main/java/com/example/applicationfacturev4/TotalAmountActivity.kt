package com.example.applicationfacturev4

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class TotalAmountActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total_amount)

        val backButton = findViewById<Button>(R.id.back)
        backButton.setOnClickListener {
            // Intent pour démarrer InvoiceActivity
            val intent = Intent(this, InvoiceActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("reset", true)
            startActivity(intent)
        }

        // Récupérer le montant total TTC de la facture depuis l'intent
        val totalAmountTTC = intent.getDoubleExtra("totalAmountTTC", 0.0)

        // Afficher le montant total TTC de la facture dans un TextView
        val totalAmountTextView = findViewById<TextView>(R.id.total_amount)
        // Formatage du montant total TTC pour inclure deux décimales
        totalAmountTextView.text = String.format("%.2f €", totalAmountTTC)
    }
}
