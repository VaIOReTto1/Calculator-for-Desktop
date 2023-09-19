package CalculateService

import org.mariuszgromada.math.mxparser.Expression
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

val OPERATORS = arrayOf('+', '-', '*', '/')

//计算
fun calculate(input: String): String? {
    val expression = input.replace('x', '*').replace('÷', '/')
    if (!OPERATORS.any { expression.contains(it) } || OPERATORS.any { expression.endsWith(it) }) {
        return null
    }
    return try {
        //保留小数点后10位
        val result = BigDecimal(Expression(expression).calculate())
            .round(MathContext(10, RoundingMode.HALF_UP))
            .setScale(10, RoundingMode.HALF_UP)
            .stripTrailingZeros()
        result.toPlainString()
    } catch (e: Exception) {
        null
    }
}