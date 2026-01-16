package com.example.p6minda_230104040082.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(ts: Long): String {
    val sdf = SimpleDateFormat("dd MMM, yyyy, hh:mma", Locale.getDefault())
    return sdf.format(Date(ts))
}