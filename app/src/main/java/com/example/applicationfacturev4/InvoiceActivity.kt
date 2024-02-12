package com.example.applicationfacturev4

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class InvoiceActivity : AppCompatActivity() {

    private lateinit var quantityEditText: EditText
    private lateinit var unitPriceEditText: EditText
    private lateinit var amountHtEditText: EditText
    private lateinit var taxRateEditText: EditText
    private lateinit var discountEditText: EditText
    private lateinit var calculateTtcButton: Button
    private lateinit var resetButton: Button
    private lateinit var loyalRadioButton: RadioButton
    private lateinit var notLoyalRadioButton: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)

        initViews()

        if (intent.getBooleanExtra("reset", false)) {
            resetFields()
        }

        notLoyalRadioButton.isChecked = true

        discountEditText.isEnabled = false
        discountEditText.visibility = View.GONE
        calculateTtcButton.isEnabled = false

        setupTextWatchers()

        loyalRadioButton.setOnCheckedChangeListener { _, isChecked ->
            discountEditText.isEnabled = isChecked
            discountEditText.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        calculateTtcButton.setOnClickListener {
            calculateTTC()
        }
        resetButton.setOnClickListener {
            resetFields()
        }

        val amountHtEditText: EditText = findViewById(R.id.amount_ht)
        amountHtEditText.keyListener = null
    }

    private fun initViews() {
        quantityEditText = findViewById(R.id.quantity)
        unitPriceEditText = findViewById(R.id.unit_price)
        amountHtEditText = findViewById(R.id.amount_ht)
        taxRateEditText = findViewById(R.id.tax_rate)
        discountEditText = findViewById(R.id.discount)
        calculateTtcButton = findViewById(R.id.calculate_ttc_button)
        resetButton = findViewById(R.id.reset_button)
        loyalRadioButton = findViewById(R.id.loyal)
        notLoyalRadioButton = findViewById(R.id.not_loyal)
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateTtcButton.isEnabled = quantityEditText.text.trim().isNotEmpty() &&
                        unitPriceEditText.text.trim().isNotEmpty() &&
                        taxRateEditText.text.trim().isNotEmpty()
                calculateAndDisplayAmountHt()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        quantityEditText.addTextChangedListener(textWatcher)
        unitPriceEditText.addTextChangedListener(textWatcher)
        taxRateEditText.addTextChangedListener(textWatcher)
    }

    private fun calculateAndDisplayAmountHt() {
        val quantity = quantityEditText.text.toString().toDoubleOrNull() ?: 0.0
        val unitPrice = unitPriceEditText.text.toString().toDoubleOrNull() ?: 0.0
        val amountHt = quantity * unitPrice
        amountHtEditText.setText(String.format("%.2f €", amountHt))
    }

    private fun calculateTTC() {
        val quantity = quantityEditText.text.toString().toDoubleOrNull() ?: 0.0
        val unitPrice = unitPriceEditText.text.toString().toDoubleOrNull() ?: 0.0
        val taxRate = taxRateEditText.text.toString().toDoubleOrNull() ?: 0.0
        val discount = discountEditText.text.toString().toDoubleOrNull() ?: 0.0

        val amountHt = quantity * unitPrice

        val taxAmount = amountHt * taxRate / 100

        val totalTtc = amountHt + taxAmount - discount

        val intent = Intent(this, TotalAmountActivity::class.java).apply {
            putExtra("totalAmountTTC", totalTtc)
        }
        startActivity(intent)
    }

    private fun resetFields() {
        quantityEditText.text.clear()
        unitPriceEditText.text.clear()
        amountHtEditText.text.clear()
        taxRateEditText.text.clear()
        discountEditText.text.clear()
        discountEditText.isEnabled = false
        discountEditText.visibility = View.GONE
        calculateTtcButton.isEnabled = false
        loyalRadioButton.isChecked = false
        notLoyalRadioButton.isChecked = true
    }
}


