package com.hiranya.printxpress.data.util

import java.security.MessageDigest

// SHA-256 password hashing. Never store plain text passwords.
fun hashPassword(password: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}

fun checkPassword(input: String, hash: String): Boolean {
    return hashPassword(input) == hash
}
