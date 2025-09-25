package com.alejandro.calculadora

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.calculadora.databinding.ActivityMainBinding
import com.alejandro.calculadora.enums.Operators
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    /*
    * TODO: Solucionar bug eliminar 0 al inicio.
    * TODO: Solucionat bug Delete
    * TODO: Implementar decimales
    */

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentOperation: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateConsole("0", false)
    }

    /**
     * When the user clicks a number button, that number is added to the console.
     * If the console has a 0, it is replaced by the new number.
     */
    fun onClickedNumber(view: View) {
        val pressedButton = view as Button
        val value = pressedButton.text.toString().toInt()
        updateConsole(value.toString())
    }

    fun onClickedDecimal(view: View) {

    }

    fun onOperatorClicked(view: View) {
        val pressedButton = view as Button
        val operator = pressedButton.text.toString()
        updateConsole(operator)
    }

    fun onClickedEqual(view: View) {
        val numbers = ArrayDeque<Double>()
        val operators = ArrayDeque<Operators>()
        var i = 0
        while (i < currentOperation.length) {
            val char = currentOperation[i]

            when {
                char.isDigit() -> {
                    val numberBuilder = StringBuilder()
                    while (i < currentOperation.length && currentOperation[i].isDigit()) {
                        numberBuilder.append(currentOperation[i])
                        i++
                    }
                    numbers.addLast(numberBuilder.toString().toDouble())
                    i-- // Decrement to handle the outer loop increment
                }
                char.isWhitespace() -> {}

                else -> {
                    val operator = getOperator(char)
                    if (operator != null) {
                        while (operators.isNotEmpty() && operators.last().precedence >= operator.precedence) {
                            val op = operators.removeLast()
                            val b = numbers.removeLast()
                            val a = numbers.removeLast()
                            numbers.addLast(performOperation(op, a, b))
                        }
                        operators.addLast(operator)
                    }
                }
            }
            i++
        }

        while (operators.isNotEmpty()) {
            val op = operators.removeLast()
            val b = numbers.removeLast()
            val a = numbers.removeLast()
            numbers.addLast(performOperation(op, a, b))
        }

        updateConsole(numbers.last().toString(), false)
    }

    /**
     * When the user clicks the button to clear the console, the console is set to 0.
     */
    fun onClickedClear(view: View) {
        currentOperation = ""
        binding.tvConsole.text = "0"
    }

    /**
     * Delete the last character in the console
     */
    fun onClickedDelete(view: View) {
        val currentText = binding.tvConsole.text.toString()
        if (currentText.isNotEmpty() && currentText != "0") {
            updateConsole(currentText.dropLast(1), false)
        }
    }

    /**
     * When the user clicks the button to change the symbol (+/-)
     * the number in the console changes its symbol.
     */
    fun onClickedChangeSymbol(view: View) {

    }

    private fun updateConsole(newText: String, append: Boolean = true) {
        if(append) currentOperation += newText
        else currentOperation = newText
        if(currentOperation[0] == '0' && newText == "0") currentOperation = "0"
        else currentOperation.drop(0)
        binding.tvConsole.text = currentOperation
    }

    /**
     * Performs a mathematical operation on two numbers.
     * @param operator The operator to perform.
     * @param a The first number.
     * @param b The second number.
     * @return The result of the operation.
     */
    private fun performOperation(operator: Operators, a: Double, b: Double): Double {
        return when (operator) {
            Operators.PLUS -> a + b
            Operators.MINUS -> a - b
            Operators.MULTIPLY -> a * b
            Operators.DIVIDE -> a / b
            Operators.SQUARE -> sqrt(a)
        }
    }

    /**
     * Determines the operator from a character.
     * @param opChar The character representing the operator.
     * @return The corresponding Operator enum or null if not found.
     */
    private fun getOperator(opChar: Char): Operators? {
        return when (opChar) {
            '+' -> Operators.PLUS
            '-' -> Operators.MINUS
            'X' -> Operators.MULTIPLY
            '/' -> Operators.DIVIDE
            '√' -> Operators.SQUARE
            else -> null
        }
    }
}