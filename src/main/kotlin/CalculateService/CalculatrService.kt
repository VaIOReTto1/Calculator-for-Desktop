package CalculateService

import java.util.*
import kotlin.math.*

val OPERATORS = arrayOf("+", "-", "*", "/", "^", "!")
val PRECEDENCE = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2, "^" to 3, "!" to 3)

fun calculate(input: String): String? {
    val expression = input.replace('x', '*').replace('÷', '/')
    val result = evaluateExpression(expression)
    return result?.let {
        val formattedResult = "%.6f".format(it).trimEnd('0').trimEnd('.')
        if (formattedResult == "-0") "0" else formattedResult
    }
}

fun evaluateExpression(expression: String): Double? {
    println(expression)
    val tokens = tokenize(expression)
    println(tokens)
    val postfix = infixToPostfix(tokens)
    println(postfix)
    return postfix?.let { evaluatePostfix(it) }
}

fun tokenize(expression: String): List<String> {
    val tokens = LinkedList<String>()
    var currentToken = ""
    var isPercentage = false
    var currentIndex = 0

    while (currentIndex < expression.length) {
        val currentChar = expression[currentIndex]

        when {
            currentChar.isDigit() || currentChar == '.' -> {
                currentToken += currentChar
            }
            currentChar == '%' -> {
                isPercentage = true
            }
            //计算三角函数
            currentChar in "scta".toCharArray() -> {
                val functionChars = when (expression.substring(currentIndex, currentIndex + 6)) {
                    "arcsin", "arccos", "arctan" -> expression.substring(currentIndex, currentIndex + 6)
                    else -> expression.substring(currentIndex, currentIndex + 3)
                }
                val end = currentIndex + functionChars.length
                val innerExpression = StringBuilder()

                for (j in end until expression.length) {
                    if (expression[j] == ')') {
                        currentIndex = end + j
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
            }
            //计算排列组合数
            currentChar == 'C' -> {
                currentIndex += 2
                var selectNumber = 0
                var totalNumber = 0
                var number = expression[currentIndex]
                while (number.isDigit()) {
                    selectNumber = selectNumber * 10 + (number - '0')
                    currentIndex++
                    number = expression[currentIndex]
                }
                currentIndex++
                number = expression[currentIndex]
                while (number.isDigit()) {
                    totalNumber = totalNumber * 10 + (number - '0')
                    currentIndex++
                    number = expression[currentIndex]
                }
                val result = calculateCombination(selectNumber, totalNumber)
                tokens.add(result)
            }
            //计算根号
            currentChar == '√' -> {
                currentIndex++
                val sb = StringBuilder()

                for (k in currentIndex until expression.length) {
                    val number = expression[k]
                    if (number.isDigit()) {
                        sb.append(number)
                    }
                }

                val result = sb.toString().toDouble().pow(0.5)
                tokens.add(result.toString())
            }
            //计算对数
            currentChar == 'l' -> {
                currentIndex += 4
                val logarithm = StringBuilder()
                val baseNumber = StringBuilder()
                var number = expression[currentIndex]
                while (number.isDigit()) {
                    logarithm.append(number)
                    currentIndex++
                    number = expression[currentIndex]
                }
                currentIndex++
                number = expression[currentIndex]
                while (number.isDigit()) {
                    baseNumber.append(number)
                    currentIndex++
                    number = expression[currentIndex]
                }
                val result = log(logarithm.toString().toDouble(), baseNumber.toString().toDouble())
                tokens.add(result.toString())
            }
            else -> {
                //计算百分比
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
                if (currentChar != ' ') {
                    tokens.add(currentChar.toString())
                }
            }
        }

        currentIndex++
    }

    //计算百分比
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

//计算排列组合数
fun calculateCombination(k: Int, n: Int): String {
    val result = (factorial(n, n - k + 1)) / factorial(k, 2)
    return result.toString()
}

//计算阶乘
fun factorial(max: Int, min: Int): Long {
    var result: Long = 1
    for (i in min..max) {
        result *= i.toLong()
    }
    return result
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
        } else if (token == "!") {
            //计算阶乘
            if (stack.size >= 1) {
                val operand1 = stack.removeLast()
                val result = factorial(operand1.toInt(), 2).toDouble()
                stack.add(result)
            } else {
                return null
            }
        } else if (token in OPERATORS && stack.size >= 2) {
            val operand2 = stack.removeLast()
            val operand1 = stack.removeLast()
            val result = when (token) {
                "+" -> operand1 + operand2
                "-" -> operand1 - operand2
                "*" -> operand1 * operand2
                "/" -> if (operand2 != 0.0) operand1 / operand2 else return null
                "^" -> Math.pow(operand1, operand2)
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