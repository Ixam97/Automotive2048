package com.ixam97.automotive2048.utils

import java.io.File
import kotlin.math.abs
import kotlin.math.log10

/** Returns the number of digits of an integer in base 10. */
fun Int.length() = when(this) {
    0 -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}
fun fetchCpuName(): String {
    return try {
        File("/proc/cpuinfo").readLines().last() // Cpu name
    } catch (e: Exception) {
        "Unavailable"
    }
}


fun fetchRamInfo(): String {
    return try {
        File("/proc/meminfo").readLines().first() // MemTotal
    } catch (e: Exception) {
        "Unavailable"
    }
}