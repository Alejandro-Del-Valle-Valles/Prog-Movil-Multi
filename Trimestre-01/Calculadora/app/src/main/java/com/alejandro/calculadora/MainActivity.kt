package com.alejandro.calculadora

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.calculadora.databinding.ActivityMainBinding
import com.alejandro.calculadora.enums.Operators
import java.util.ArrayDeque
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentOperation = "0" // Stores the current mathematical expression as a string

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge display
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateConsole("0", false) // Initialize console to "0"
    }

    /**
     * Appends the clicked number to the console.
     * Replaces "0" if it's the only content.
     */
    fun onClickedNumber(view: View) {
        val pressedButton = view as Button
        val valueString = pressedButton.text.toString()

        if (currentOperation == "0" && valueString != "0") {
            updateConsole(valueString, false)
        }
    }

    /**
     * Appends a decimal point if the current number doesn't have one.
     */
    fun onClickedDecimal(view: View) {
        if (currentOperation == "0") {
            updateConsole("0.") // Start with "0." if console is "0"
            return
        }

        // Prevent adding a decimal if not preceded by a digit or if current number already has one
        if (currentOperation.isEmpty() || (!currentOperation.last().isDigit() && currentOperation.last() != '.')) {
            return
        }

        // Check if the current number segment already has a decimal point
        var lastNumberHasDecimal = false
        var i = currentOperation.length - 1
        while (i >= 0) {
            if (currentOperation[i] == '.') {
                lastNumberHasDecimal = true
                break
            }
            if (!currentOperation[i].isDigit()) { // Stop if an operator or non-digit is found
                break
            }
            i--
        }

        if (!lastNumberHasDecimal) {
            updateConsole(".")
        }
    }


    /**
     * Appends the clicked operator to the console.
     * Prevents duplicate or misplaced operators.
     */
    fun onOperatorClicked(view: View) {
        val pressedButton = view as Button
        val operatorSymbol = pressedButton.text.toString()

        // Avoid duplicate operators or operators at the start (unless it's '√')
        if (currentOperation.isNotEmpty() && !currentOperation.last().isDigit()
            && currentOperation.last() != '.' && operatorSymbol != "√") {
            // Replace the last operator if another is pressed (and it's not '√')
            if (getOperator(currentOperation.last()) != null && currentOperation.length > 1) {
                currentOperation = currentOperation.dropLast(1)
            } else {
                return // Don't add operator if no valid preceding number (unless '√')
            }
        } else if (currentOperation.isEmpty() && operatorSymbol != "√") {
            return // Don't add operators (except '√') if the console is empty
        }
        updateConsole(operatorSymbol)
    }

    /**
     * Calculates the expression in the console.
     * Uses two stacks (shunting-yard like approach) for numbers and operators.
     */
    fun onClickedEqual(view: View) {
        try {
            val numbers = ArrayDeque<Double>()
            val operators = ArrayDeque<Operators>()
            var i = 0
            var expectOperand = true // True if we expect a number or unary operator like '√'

            while (i < currentOperation.length) {
                val char = currentOperation[i]

                when {
                    // Number (digit or decimal point followed by digit)
                    char.isDigit() || (char == '.' && i + 1 < currentOperation.length && currentOperation[i + 1].isDigit()) -> {
                        val numberBuilder = StringBuilder()
                        // Check for unary minus at the start of a number
                        if (char == '-' && (numbers.isEmpty() || !expectOperand)) {
                            numberBuilder.append(char)
                            i++
                            if (i >= currentOperation.length || (!currentOperation[i].isDigit() && currentOperation[i] != '.')) {
                                updateConsole("Error: '-' without number", false)
                                return
                            }
                        }

                        while (i < currentOperation.length && (currentOperation[i].isDigit() || currentOperation[i] == '.')) {
                            numberBuilder.append(currentOperation[i])
                            i++
                        }
                        val numberString = numberBuilder.toString()
                        if (numberString.count { it == '.' } <= 1 && numberString != "." && numberString != "-.") {
                            try {
                                numbers.addLast(numberString.toDouble())
                                expectOperand = false // After a number, expect an operator
                            } catch (e: NumberFormatException) {
                                updateConsole("Error: Number format", false)
                                return
                            }
                        } else {
                            updateConsole("Error: Invalid number", false)
                            return
                        }
                        i-- // Adjust index for outer loop
                    }
                    // Square root operator
                    char == '√' -> {
                        operators.addLast(Operators.SQUARE)
                        expectOperand = true // Square root expects an operand
                    }
                    // Unary minus (e.g., -5, 5*-5)
                    char == '-' && expectOperand -> {
                        val numberBuilder = StringBuilder()
                        numberBuilder.append(char)
                        i++
                        while (i < currentOperation.length && (currentOperation[i].isDigit() || currentOperation[i] == '.')) {
                            numberBuilder.append(currentOperation[i])
                            i++
                        }
                        val numberString = numberBuilder.toString()
                        if (numberString.length > 1 && numberString.count { it == '.' } <= 1 && numberString != "-.") {
                            try {
                                numbers.addLast(numberString.toDouble())
                                expectOperand = false
                            } catch (e: NumberFormatException) {
                                updateConsole("Error: Number format", false)
                                return
                            }
                        } else {
                            updateConsole("Error: '-' without valid number", false)
                            return
                        }
                        i-- // Adjust index
                    }
                    // Other operators
                    else -> {
                        val operator = getOperator(char)
                        if (operator != null) {
                            // Operator precedence logic
                            while (operators.isNotEmpty() &&
                                operators.last() != Operators.SQUARE && // Square root doesn't pop based on precedence this way
                                operators.last().precedence >= operator.precedence) {
                                applyOperator(numbers, operators) { errorMessage ->
                                    updateConsole(errorMessage, false);
                                    return@applyOperator
                                }
                            }
                            operators.addLast(operator)
                            expectOperand = true // After a binary operator, expect an operand
                        } else if (!char.isWhitespace()) {
                            updateConsole("Error: Invalid character", false)
                            return
                        }
                    }
                }
                i++
            }

            // Apply remaining operators
            while (operators.isNotEmpty()) {
                applyOperator(numbers, operators) { errorMessage ->
                    updateConsole(errorMessage, false)
                    return@applyOperator
                }
            }

            // Display result or error
            if (numbers.size == 1) {
                val result = numbers.last()
                if (result.isInfinite() || result.isNaN()) {
                    updateConsole("Error", false)
                } else if (result == result.toInt().toDouble()) { // Check if it's a whole number
                    updateConsole(result.toInt().toString(), false)
                } else {
                    // Format to prevent excessive decimals
                    updateConsole(String.format("%.10f", result).trimEnd('0').trimEnd('.'), false)
                }
            } else if (numbers.isEmpty() && currentOperation.isNotEmpty() && !currentOperation.all { getOperator(it) != null || it == '√' || it.isWhitespace() }) {
                updateConsole("Error", false) // Malformed expression (e.g. "5+", then equals)
            } else if (numbers.isEmpty() && (currentOperation.isEmpty() || currentOperation == "0")) {
                updateConsole("0", false)
            } else if (currentOperation.contains("√") && currentOperation.length == 1 && numbers.isEmpty()){
                updateConsole("0", false) // Case: only "√" was entered
            } else {
                // Other malformed expressions
                if (currentOperation.isNotEmpty() && numbers.isEmpty() && operators.isEmpty()) {
                    // This case might be covered by others, e.g. entering only an operator
                } else if (numbers.size != 1) {
                    updateConsole("Error: Malformed expression", false)
                } else {
                    updateConsole("Error", false)
                }
            }
        } catch (e: ArithmeticException) {
            updateConsole("Error: ${e.message}", false)
        } catch (e: Exception) {
            updateConsole("Error", false) // Generic error
        }
    }

    /**
     * Applies the last operator from the stack to the numbers from the stack.
     * Handles errors by calling the onError lambda.
     */
    private fun applyOperator(
        numbers: ArrayDeque<Double>,
        operators: ArrayDeque<Operators>,
        onError: (String) -> Unit // Callback for error reporting
    ) {
        if (operators.isEmpty()) {
            onError("Error: Missing operators"); return
        }
        val op = operators.removeLast()

        if (op == Operators.SQUARE) { // Unary square root
            if (numbers.isNotEmpty()) {
                val a = numbers.removeLast()
                try {
                    numbers.addLast(performOperation(op, a, 0.0)) // b is not used for square root
                } catch (e: ArithmeticException) {
                    onError(e.message ?: "Error: Square root"); return
                }
            } else {
                onError("Error: Square root without operand"); return
            }
        } else { // Binary Operators
            if (numbers.size < 2) {
                onError("Error: Operator needs two operands"); return
            }
            val b = numbers.removeLast()
            val a = numbers.removeLast()
            try {
                numbers.addLast(performOperation(op, a, b))
            } catch (e: ArithmeticException) {
                onError(e.message ?: "Error: Arithmetic"); return
            }
        }
    }


    /**
     * Clears the console, setting it to "0".
     */
    fun onClickedClear(view: View) {
        currentOperation = ""
        updateConsole("0", false)
    }

    /**
     * Deletes the last character from the console.
     * Sets to "0" if the console becomes empty.
     */
    fun onClickedDelete(view: View) {
        if (currentOperation.isNotEmpty()) {
            currentOperation = currentOperation.dropLast(1)
            if (currentOperation.isEmpty()) {
                updateConsole("0", false)
            } else {
                // No need to re-evaluate with equals, just update display
                // If you want to re-evaluate on delete, call onClickedEqual or a similar function
                updateConsole(currentOperation, false)
            }
        }
    }

    /**
     * Changes the sign of the last entered number or the current number being typed.
     */
    fun onClickedChangeSymbol(view: View) {
        if (currentOperation.isEmpty() || currentOperation == "0") {
            return // Do nothing for empty or "0"
        }

        // Find the start of the last number
        var lastOperatorIndex = -1
        for (i in currentOperation.length - 1 downTo 0) {
            val char = currentOperation[i]
            // An operator (not '√' in this context) marks the end of the previous part
            if (getOperator(char) != null && char != '√') {
                // Complex conditions to correctly find the split point for numbers like "5--3" or "-3"
                if (i > 0 && currentOperation[i-1].isDigit()) {
                    lastOperatorIndex = i; break
                } else if (getOperator(currentOperation[i-1]) != null) { // Handles cases like "5*-3"
                    lastOperatorIndex = i; break
                }
                // If operator is at the start and it's '-', it's part of the number
                if (i == 0 && char == '-') continue

                lastOperatorIndex = i; break // Default split
            }
        }

        val numberStartIndex = lastOperatorIndex + 1
        var currentNumberStr = currentOperation.substring(numberStartIndex)

        // If no number part found or it's "0" or starts with an operator (that isn't a leading minus)
        if (currentNumberStr.isEmpty() || currentNumberStr == "0" || (getOperator(currentNumberStr.first()) != null && currentNumberStr.first() != '-')) {
            return
        }

        val prefix = if (lastOperatorIndex != -1) currentOperation.substring(0, numberStartIndex) else ""

        if (currentNumberStr.startsWith("-")) {
            // From negative to positive
            currentNumberStr = currentNumberStr.substring(1)
            if (currentNumberStr.isEmpty() && prefix.isEmpty()) { // Was just "-"
                updateConsole("0", false); return
            }
        } else {
            // From positive to negative
            // Avoid issues like "X--Y" turning into "X---Y" if prefix ends with "-"
            // This is simplified: just prepend "-" to the number string.
            // onClickedEqual handles "X--Y" as "X+Y".
            currentNumberStr = "-$currentNumberStr"
        }

        // Reconstruct the operation string
        var newOperation = prefix + currentNumberStr

        if (newOperation.endsWith(".-")) { // Handle rare case like "5.-" from "5." -> "+/-"
            newOperation = newOperation.dropLast(1)
        }
        if (newOperation.isEmpty()){ // If everything was cleared
            newOperation = "0"
        }

        updateConsole(newOperation, false)
    }


    /**
     * Updates the console display and the internal currentOperation string.
     * @param newText The text to set or append.
     * @param append If true, appends newText; otherwise, replaces currentOperation.
     */
    private fun updateConsole(newText: String, append: Boolean = true) {
        val previousText = currentOperation

        if (append) {
            // Handle initial "0" being replaced by a number (but not by ".")
            if (currentOperation == "0" && newText != "." && getOperator(newText.firstOrNull() ?: ' ') == null) {
                currentOperation = newText
            } else {
                currentOperation += newText
            }
        } else {
            currentOperation = newText
        }

        // Prevent leading zeros for integers (e.g., "05" becomes "5")
        // but allow for decimals like "0.5" or when typing "00"
        if (currentOperation.startsWith("0") && currentOperation.length > 1 && currentOperation[1] != '.' && getOperator(currentOperation[1]) == null) {
            if (newText.length == 1 && getOperator(newText.first()) == null && previousText == "0"){
                // This condition might need refinement if '0' followed by operator is allowed and then a number
                // currentOperation = currentOperation.drop(0) // This would remove the '0' if '0' was previous and newText is a number.
            } else if (!(newText.length == 1 && getOperator(newText.first()) != null) && currentOperation != "0."){
                if (currentOperation.all { it == '0' }) { // If all zeros, keep one "0"
                    currentOperation = "0"
                } else if (!currentOperation.contains('.')){ // If not a decimal, remove leading zeros
                    currentOperation = currentOperation.replaceFirst("^0+(?!$)".toRegex(), "")
                }
            }
        }

        if (currentOperation.isEmpty()) {
            currentOperation = "0" // Default to "0" if empty
        }

        binding.tvConsole.text = currentOperation
    }

    /**
     * Performs the specified mathematical operation.
     * @param operator The operation to perform.
     * @param a The first operand.
     * @param b The second operand (ignored for unary operations like square root).
     * @return The result of the operation.
     * @throws ArithmeticException for division by zero or sqrt of negative.
     */
    private fun performOperation(operator: Operators, a: Double, b: Double): Double {
        return when (operator) {
            Operators.PLUS -> a + b
            Operators.MINUS -> a - b
            Operators.MULTIPLY -> a * b
            Operators.DIVIDE -> {
                if (b == 0.0) throw ArithmeticException("Division by zero")
                a / b
            }
            Operators.SQUARE -> {
                if (a < 0) throw ArithmeticException("Sqrt of negative")
                sqrt(a)
            }
        }
    }

    /**
     * Converts a character to its corresponding Operators enum.
     * @param opChar The character representing the operator.
     * @return The Operators enum, or null if not a valid operator.
     */
    private fun getOperator(opChar: Char): Operators? {
        return when (opChar) {
            '+' -> Operators.PLUS
            '-' -> Operators.MINUS
            'X' -> Operators.MULTIPLY // Assuming 'X' is used for multiplication
            '/' -> Operators.DIVIDE
            '√' -> Operators.SQUARE
            else -> null
        }
    }
}
