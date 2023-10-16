package CalculateService

import java.util.*
import kotlin.math.*

//val OPERATORS = arrayOf('+', '-', '*', '/')
//
////计算
//fun calculate(input: String): String? {
//    val expression = input.replace('x', '*').replace('÷', '/')
//    if (!OPERATORS.any { expression.contains(it) } || OPERATORS.any { expression.endsWith(it) }) {
//        return null
//    }
//    return try {
//        //保留小数点后10位
//        val result = BigDecimal(Expression(expression).calculate())
//            .round(MathContext(10, RoundingMode.HALF_UP))
//            .setScale(10, RoundingMode.HALF_UP)
//            .stripTrailingZeros()
//        result.toPlainString()
//    } catch (e: Exception) {
//        null
//    }
//}

val OPERATORS = arrayOf("+", "-", "*", "/")
val PRECEDENCE = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2, "^" to 3)

fun calculate(input: String): String? {
    val expression = input.replace('x', '*').replace('÷', '/')
    val result = evaluateExpression(expression)
    return result?.let {
        val formattedResult = "%.6f".format(it).trimEnd('0').trimEnd('.')
        if (formattedResult == "-0") "0" else formattedResult
    }
}

fun evaluateExpression(expression: String): Double? {
    val tokens = tokenize(expression)
    val postfix = infixToPostfix(tokens)
    return postfix?.let { evaluatePostfix(it) }
}

fun tokenize(expression: String): List<String> {
    val tokens = LinkedList<String>()
    var currentToken = ""
    var isPercentage = false

    var i = 0
    while (i < expression.length) {
        val char = expression[i]

        if (char.isDigit() || char == '.') {
            currentToken += char
        } else if (char == '%') {
            isPercentage = true
        } else if (char in "sctar".toCharArray()) {
            val functionChars = when (expression.substring(i, i + 6)) {
                "arcsin", "arccos", "arctan" -> expression.substring(i, i + 6)
                else -> expression.substring(i, i + 3)
            }
            val end = i + functionChars.length
            val innerExpression = StringBuilder()

            for (j in end until expression.length) {
                if (expression[j] == ')') {
                    i = end + j
                    break
                } else if (expression[j] != '(') {
                    innerExpression.append(expression[j])
                }
            }

            val result = when (functionChars) {
                "sin" -> sin(evaluateExpression(innerExpression.toString()) ?: 0.0)
                "cos" -> cos(evaluateExpression(innerExpression.toString()) ?: 0.0)
                "tan" -> tan(evaluateExpression(innerExpression.toString()) ?: 0.0)
                "arcsin" -> asin(evaluateExpression(innerExpression.toString()) ?: 0.0)
                "arccos" -> acos(evaluateExpression(innerExpression.toString()) ?: 0.0)
                "arctan" -> atan(evaluateExpression(innerExpression.toString()) ?: 0.0)
                else -> 0.0
            }
            tokens.add(result.toString())

            continue
        } else {
            if (currentToken.isNotEmpty()) {
                if (isPercentage) {
                    val value = currentToken.toDouble() / 100.0
                    tokens.add(value.toString())
                    isPercentage = false
                } else {
                    tokens.add(currentToken)
                }
                currentToken = ""
            }
            if (char != ' ') {
                tokens.add(char.toString())
            }
        }
        i++
    }

    if (currentToken.isNotEmpty()) {
        if (isPercentage) {
            val value = currentToken.toDouble() / 100.0
            tokens.add(value.toString())
        } else {
            tokens.add(currentToken)
        }
    }

    tokens.replaceAll { if (it == "π") Math.PI.toString() else if (it == "e") Math.E.toString() else it }
    return tokens.filter { it.isNotEmpty() }
}

fun infixToPostfix(tokens: List<String>): MutableList<String>? {
    val output = mutableListOf<String>()
    val stack = LinkedList<String>()

    for (token in tokens) {
        if (token.isNumeric()) {
            output.add(token)
        } else if (token in OPERATORS) {
            while (stack.isNotEmpty() && stack.last() in OPERATORS &&
                (PRECEDENCE[stack.last()] ?: 0) >= (PRECEDENCE[token] ?: 0)
            ) {
                output.add(stack.removeLast())
            }
            stack.add(token)
        } else if (token == "(") {
            stack.add(token)
        } else if (token == ")") {
            while (stack.isNotEmpty() && stack.last() != "(") {
                output.add(stack.removeLast())
            }
            if (stack.isNotEmpty() && stack.last() == "(") {
                stack.removeLast()
            } else {
                return null
            }
        }
    }

    while (stack.isNotEmpty()) {
        if (stack.last() == "(" || stack.last() == ")") {
            return null
        }
        output.add(stack.removeLast())
    }

    return output
}

fun evaluatePostfix(postfix: List<String>): Double? {
    val stack = LinkedList<Double>()

    for (token in postfix) {
        if (token.isNumeric()) {
            stack.add(token.toDouble())
        } else if (token in OPERATORS && stack.size >= 2) {
            val b = stack.removeLast()
            val a = stack.removeLast()
            val result = when (token) {
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                "/" -> if (b != 0.0) a / b else return null
                "^" -> Math.pow(a, b)
                else -> return null
            }
            stack.add(result)
        } else {
            return null
        }
    }

    return if (stack.size == 1) stack.last else null
}

fun String.isNumeric(): Boolean {
    return this.toDoubleOrNull() != null
}
