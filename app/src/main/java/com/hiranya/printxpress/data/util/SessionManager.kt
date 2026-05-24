package com.hiranya.printxpress.data.util

// Holds the logged-in user id in memory for the lifetime of the app process.
object SessionManager {
    private var loggedInUserId: Long? = null

    fun login(userId: Long) {
        loggedInUserId = userId
    }

    fun logout() {
        loggedInUserId = null
    }

    fun isLoggedIn(): Boolean = loggedInUserId != null

    fun getUserId(): Long? = loggedInUserId
}
