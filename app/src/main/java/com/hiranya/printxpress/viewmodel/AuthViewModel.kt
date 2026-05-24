package com.hiranya.printxpress.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hiranya.printxpress.data.PrintXpressDatabase
import com.hiranya.printxpress.data.repository.UserRepository
import com.hiranya.printxpress.data.util.SessionManager
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: UserRepository

    val loginError: MutableState<String?> = mutableStateOf(null)
    val registerError: MutableState<String?> = mutableStateOf(null)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    init {
        val db = PrintXpressDatabase.getDatabase(application)
        repo = UserRepository(db.userDao())
    }

    fun login(emailOrPhone: String, password: String, onSuccess: () -> Unit) {
        if (emailOrPhone.isBlank() || password.isBlank()) {
            loginError.value = "Please fill in all fields."
            return
        }
        viewModelScope.launch {
            isLoading.value = true
            loginError.value = null
            val user = repo.login(emailOrPhone.trim(), password)
            if (user != null) {
                SessionManager.login(user.userId)
                onSuccess()
            } else {
                loginError.value = "Incorrect email, phone, or password."
            }
            isLoading.value = false
        }
    }

    fun register(
        fullName: String,
        emailOrPhone: String,
        isEmail: Boolean,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        // Validate inputs before hitting the database.
        if (fullName.trim().length < 2) {
            registerError.value = "Full name must be at least 2 characters."
            return
        }
        if (emailOrPhone.isBlank()) {
            registerError.value = if (isEmail) "Email is required." else "Phone is required."
            return
        }
        if (password.length < 6) {
            registerError.value = "Password must be at least 6 characters."
            return
        }
        if (password != confirmPassword) {
            registerError.value = "Passwords do not match."
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            registerError.value = null

            // Check for duplicates before inserting.
            val existing = if (isEmail) {
                repo.findByEmail(emailOrPhone.trim())
            } else {
                repo.findByPhone(emailOrPhone.trim())
            }
            if (existing != null) {
                registerError.value = if (isEmail) "An account with this email already exists." else "An account with this phone already exists."
                isLoading.value = false
                return@launch
            }

            val userId = repo.register(
                fullName = fullName.trim(),
                email = if (isEmail) emailOrPhone.trim() else null,
                phone = if (!isEmail) emailOrPhone.trim() else null,
                password = password
            )
            SessionManager.login(userId)
            onSuccess()
            isLoading.value = false
        }
    }
}
