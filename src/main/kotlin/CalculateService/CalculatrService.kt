package CalculateService

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

//分割字符传入
fun tokenize(expression: String): List<String> {
    val tokens = mutableListOf<String>()
    var currentToken = ""
    var isPercentage = false

    var i = 0
    while (i < expression.length) {
        val char = expression[i]

        if (char.isDigit() || char == '.') {
            currentToken += char
        } else if (char == '%') {
            isPercentage = true
        } else if (char == 's' || char == 'c' || char == 't' || char == 'a') {
            val TFChars = expression.substring(i, i + 3)
            val ITFChars = expression.substring(i, i + 6)

            //计算三角函数
            if (TFChars == "sin" || TFChars == "cos" || TFChars == "tan") {
                val end = i + 3
                val innerExpression = StringBuilder()

                for (j in end until expression.length) {
                    if (expression[j] == ')') {
                        i = end + j
                        break
                    } else if (expression[j] != '(') {
                        innerExpression.append(expression[j])
                    }
                }

                val result = when (TFChars) {
                    "sin" -> sin(evaluateExpression(innerExpression.toString()) ?: 0.0)
                    "cos" -> cos(evaluateExpression(innerExpression.toString()) ?: 0.0)
                    "tan" -> tan(evaluateExpression(innerExpression.toString()) ?: 0.0)
                    else -> 0.0
                }
                tokens.add(result.toString())

                continue
            }
            //计算反三角函数
            else if (ITFChars == "arcsin" || ITFChars == "arccos" || ITFChars == "arctan") {
                val end = i + 6
                println(expression[end])
                val innerExpression = StringBuilder()

                for (j in end until expression.length) {
                    if (expression[j] == ')') {
                        i = end + j
                        break
                    } else if (expression[j] != '(') {
                        innerExpression.append(expression[j])
                    }
                }

                val result = when (ITFChars) {
                    "arcsin" -> asin(evaluateExpression(innerExpression.toString()) ?: 0.0)
                    "arccos" -> acos(evaluateExpression(innerExpression.toString()) ?: 0.0)
                    "arctan" -> atan(evaluateExpression(innerExpression.toString()) ?: 0.0)
                    else -> 0.0
                }
                tokens.add(result.toString())

                continue
            }
        }
        //计算百分比
        else {
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

    //转化π和e
    for (n in tokens.indices) {
        if (tokens[n] == "π") {
            tokens[n] = Math.PI.toString()
        } else if (tokens[n] == "e") {
            tokens[n] = Math.E.toString()
        }
    }

    return tokens.filter { it.isNotEmpty() }
}


//中缀表达式转换为后缀表达式
fun infixToPostfix(tokens: List<String>): MutableList<String>? {
    val output = mutableListOf<String>()
    val stack = mutableListOf<String>()

    for (token in tokens) {
        if (token.isNumeric()) {
            output.add(token)
        } else if (token in OPERATORS) {
            while (stack.isNotEmpty() && stack.last() in OPERATORS &&
                (PRECEDENCE[stack.last()] ?: 0) >= (PRECEDENCE[token] ?: 0)
            ) {
                output.add(stack.removeAt(stack.size - 1))
            }
            stack.add(token)
        } else if (token == "(") {
            stack.add(token)
        } else if (token == ")") {
            while (stack.isNotEmpty() && stack.last() != "(") {
                output.add(stack.removeAt(stack.size - 1))
            }
            if (stack.isNotEmpty() && stack.last() == "(") {
                stack.removeAt(stack.size - 1)
            } else {
                return null
            }
        }
    }

    while (stack.isNotEmpty()) {
        if (stack.last() == "(" || stack.last() == ")") {
            return null
        }
        output.add(stack.removeAt(stack.size - 1))
    }

    return output
}

//计算后缀表达式
fun evaluatePostfix(postfix: List<String>): Double? {
    val stack = mutableListOf<Double>()

    for (token in postfix) {
        if (token.isNumeric()) {
            stack.add(token.toDouble())
        }
        //四则运算
        else if (token in OPERATORS && stack.size >= 2) {
            val b = stack.removeAt(stack.size - 1)
            val a = stack.removeAt(stack.size - 1)
            val result = when (token) {
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                "/" -> if (b != 0.0) a / b else return null
                "^" -> a.pow(b) // 幂运算
                else -> return null
            }
            stack.add(result)
        } else {
            return null
        }
    }

    return if (stack.size == 1) stack[0] else null
}

//判断是否能转化为double类型
fun String.isNumeric(): Boolean {
    return this.toDoubleOrNull() != null
}
