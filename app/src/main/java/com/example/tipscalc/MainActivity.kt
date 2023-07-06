package com.example.tipscalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.RadioGroup
import com.example.tipscalc.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateButton.isEnabled = false

        binding.costOfServiceEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                enableCalculateButton()
            }
        })

        binding.qosRadioButtonGroup.setOnCheckedChangeListener { _, _ ->
            enableCalculateButton()
        }

        binding.calculateButton.setOnClickListener {
            binding.tipAmoutTextView.text = getString(R.string.tip_amount, calculateTip())
        }
    }

    fun enableCalculateButton() {
        val isCostOfServiceNotEmpty = binding.costOfServiceEditText.text?.isNotEmpty()
        val isQualityOfServiceSelected = binding.qosRadioButtonGroup.checkedRadioButtonId != -1

        binding.calculateButton.isEnabled = (isCostOfServiceNotEmpty ?: false) && isQualityOfServiceSelected
    }

    fun calculateTip(): String {
        val radioButtonCheckedType = binding.qosRadioButtonGroup.checkedRadioButtonId

        val tipsValue = when (radioButtonCheckedType) {
            R.id.topGradeRadioButton -> 0.2
            R.id.middleGradeRadioButton -> 0.18
            R.id.lowGradeRadioButton -> 0.15
            else -> 0.0
        }

        val costOfServiceValue = binding.costOfServiceEditText.text
            .toString()
            .toDouble()

        val isRoundUpChecked = binding.roundUpSwitch.isChecked

        var tipResult: Double = 0.0

        if (isRoundUpChecked) {
            tipResult = Math.ceil(costOfServiceValue * tipsValue)
        } else {
            tipResult = costOfServiceValue * tipsValue
        }

        return NumberFormat.getCurrencyInstance().format(tipResult)
    }
}