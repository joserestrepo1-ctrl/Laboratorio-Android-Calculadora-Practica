package com.utp.ioscalculator.calculator

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.utp.ioscalculator.history.HistoryViewModel

class CalculatorViewModel: ViewModel() {

    private val model = CalculatorModel()
    // Instancia de envio de datos al historial
    private val historyViewModel = HistoryViewModel()

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

            "%" -> {
                val lastIndex = contentResult.size - 1
                if (lastIndex >= 0) {
                    val last = contentResult[lastIndex]
                    if (!isOperator(last)) {
                        val currentVal = last.toDoubleOrNull() ?: 0.0
                        val percentVal = currentVal / 100.0
                        // Lo guardamos limpio sin el .0
                        contentResult[lastIndex] = percentVal.toString().removeSuffix(".0")
                    }
                }
            }

            "=" -> {
                calculateResult()
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
            }

            else -> {
                if (isOperator(valueButton)) {
                    if (isOperator(contentResult.last())) {
                        contentResult[contentResult.size - 1] = valueButton
                    } else {
                        contentResult.add(valueButton)
                    }
                    isCalculated = false
                } else {
                    val lastIndex = contentResult.size - 1
                    if (isCalculated) {
                        contentResult.clear()
                        contentResult.add(valueButton)
                        isCalculated = false
                    } else if (isOperator(contentResult[lastIndex])) {
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
        if (contentResult.size < 3) return

        try {
            val operacionCompleta = contentResult.joinToString(" ")

            // PASO 1: Primero resolvemos todas las Multiplicaciones y Divisiones
            var i = 1
            while (i < contentResult.size) {
                val op = contentResult[i]
                if (op == "x" || op == "÷") {
                    val n1 = contentResult[i - 1]
                    val n2 = contentResult[i + 1]

                    val res = if (op == "x") multiply(n1, n2) else divide(n1, n2)

                    // Reemplazamos los 3 elementos (n1, op, n2) por el resultado único
                    contentResult.removeAt(i - 1)
                    contentResult.removeAt(i - 1)
                    contentResult.removeAt(i - 1)
                    contentResult.add(i - 1, res)
                    // No aumentamos 'i' porque la lista se encogió
                } else {
                    i += 2 // Saltamos al siguiente operador
                }
            }

            // PASO 2: Ahora que solo quedan sumas y restas, resolvemos normal
            while (contentResult.size >= 3) {
                val n1 = contentResult[0]
                val op = contentResult[1]
                val n2 = contentResult[2]

                val res = when (op) {
                    "+" -> add(n1, n2)
                    "-" -> subtract(n1, n2)
                    else -> n1
                }

                contentResult.removeAt(0)
                contentResult.removeAt(0)
                contentResult.removeAt(0)
                contentResult.add(0, res)
            }

            val finalRes = contentResult[0]
            historyViewModel.addEntry(operacionCompleta, finalRes.replace(".", ","))

            isCalculated = true
            showContent()
        } catch (_: Exception) {
            result.value = "Error"
            contentResult.clear()
            contentResult.add("0")
        }
    }


    fun add(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return model.add(n1, n2).toString().removeSuffix(".0")
    }

    fun subtract(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return model.subtract(n1, n2).toString().removeSuffix(".0")
    }

    fun multiply(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return model.multiply(n1, n2).toString().removeSuffix(".0")
    }

    fun divide(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return if (n2 != 0.0) {
            model.divide(n1, n2).toString().removeSuffix(".0")
        } else {
            "Sin definir"
        }
    }
}