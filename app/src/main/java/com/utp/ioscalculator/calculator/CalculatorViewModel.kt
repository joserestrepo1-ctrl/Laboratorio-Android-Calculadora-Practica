package com.utp.ioscalculator.calculator

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CalculatorViewModel: ViewModel() {

    private val model = CalculatorModel()
    val result = mutableStateOf("0")
    var contentResult = mutableListOf("0")
    private var isCalculated = false

    fun showContent() {
        result.value = if (contentResult.isNotEmpty()) {
            contentResult.joinToString("").replace(".", ",")
        } else {
            "0"
        }
    }

    private fun isOperator(s: String): Boolean = s == "÷" || s == "x" || s == "-" || s == "+" || s == "%"

    fun manageClickButtons(valueButton: String) {
        if (valueButton.isEmpty()) return

        when (valueButton) {
            "AC" -> {
                contentResult.clear()
                contentResult.add("0")
                isCalculated = false
            }
            "⁺/₋" -> {
                val lastIndex = contentResult.size - 1
                if (lastIndex >= 0) {
                    val last = contentResult[lastIndex]
                    if (!isOperator(last)) {
                        val currentVal = last.toDoubleOrNull() ?: 0.0
                        if (currentVal != 0.0) {
                            val toggled = if (last.startsWith("-")) last.substring(1) else "-$last"
                            contentResult[lastIndex] = toggled
                        }
                    }
                }
                isCalculated = false
            }
            "%","÷", "x", "-", "+" -> {
                val last = contentResult.lastOrNull()
                if (last != null) {
                    if (isOperator(last)) {
                        contentResult[contentResult.size - 1] = valueButton
                    } else {
                        contentResult.add(valueButton)
                    }
                }
                isCalculated = false
            }
            "=" -> {
                calculateResult()
            }
            "," -> {
                if (isCalculated) {
                    contentResult.clear()
                    contentResult.add("0.")
                    isCalculated = false
                } else {
                    val lastIndex = contentResult.size - 1
                    if (lastIndex >= 0) {
                        val last = contentResult[lastIndex]
                        if (!isOperator(last)) {
                            if (!last.contains(".")) {
                                contentResult[lastIndex] = "$last."
                            }
                        } else {
                            contentResult.add("0.")
                        }
                    } else {
                        contentResult.add("0.")
                    }
                }
            }
            else -> { // Digits
                if (isCalculated) {
                    contentResult.clear()
                    contentResult.add(valueButton)
                    isCalculated = false
                } else {
                    val lastIndex = contentResult.size - 1
                    if (lastIndex < 0 || isOperator(contentResult[lastIndex])) {
                        contentResult.add(valueButton)
                    } else {
                        val last = contentResult[lastIndex]
                        if (last == "0") {
                            contentResult[lastIndex] = valueButton
                        } else {
                            contentResult[lastIndex] = last + valueButton
                        }
                    }
                }
            }
        }
        showContent()
    }

    fun calculateResult() {
        //To make
    }

    fun add(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return model.add(n1, n2).toString()
    }

    fun subtract(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return model.subtract(n1, n2).toString()
    }

    fun multiply(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return model.multiply(n1, n2).toString()
    }

    fun divide(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return if (n2 != 0.0) {
            model.divide(n1, n2).toString()
        } else {
            "Sin definir"
        }
    }
}
