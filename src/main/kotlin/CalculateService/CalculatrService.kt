package CalculateService

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