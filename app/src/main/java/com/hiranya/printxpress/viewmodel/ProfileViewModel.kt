package com.hiranya.printxpress.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hiranya.printxpress.data.PrintXpressDatabase
import com.hiranya.printxpress.data.entity.Address
import com.hiranya.printxpress.data.entity.SavedDesign
import com.hiranya.printxpress.data.entity.User
import com.hiranya.printxpress.data.repository.AddressRepository
import com.hiranya.printxpress.data.repository.UserRepository
import com.hiranya.printxpress.data.util.SessionManager
import com.hiranya.printxpress.data.util.checkPassword
import com.hiranya.printxpress.data.util.hashPassword
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepo: UserRepository
    private val addressRepo: AddressRepository

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _addresses = mutableStateOf<List<Address>>(emptyList())
    val addresses: State<List<Address>> = _addresses

    private val _savedDesigns = mutableStateOf<List<SavedDesign>>(emptyList())
    val savedDesigns: State<List<SavedDesign>> = _savedDesigns

    val updateError: MutableState<String?> = mutableStateOf(null)
    val updateSuccess: MutableState<Boolean> = mutableStateOf(false)

    init {
        val db = PrintXpressDatabase.getDatabase(application)
        userRepo = UserRepository(db.userDao())
        addressRepo = AddressRepository(db.addressDao())
        load()
    }

    fun load() {
        val userId = SessionManager.getUserId() ?: return
        viewModelScope.launch {
            _user.value = userRepo.findById(userId)
            _addresses.value = addressRepo.getByUser(userId)
        }
    }

    fun updateProfile(fullName: String, email: String?, phone: String?) {
        val currentUser = _user.value ?: return
        updateError.value = null
        if (fullName.trim().length < 2) {
            updateError.value = "Full name must be at least 2 characters."
            return
        }
        viewModelScope.launch {
            // Check for duplicate email/phone (excluding the current user).
            if (!email.isNullOrBlank()) {
                val existing = userRepo.findByEmail(email.trim())
                if (existing != null && existing.userId != currentUser.userId) {
                    updateError.value = "That email is already used by another account."
                    return@launch
                }
            }
            if (!phone.isNullOrBlank()) {
                val existing = userRepo.findByPhone(phone.trim())
                if (existing != null && existing.userId != currentUser.userId) {
                    updateError.value = "That phone is already used by another account."
                    return@launch
                }
            }
            userRepo.updateProfile(
                currentUser.copy(
                    fullName = fullName.trim(),
                    email = email?.trim()?.ifBlank { null },
                    phone = phone?.trim()?.ifBlank { null }
                )
            )
            _user.value = userRepo.findById(currentUser.userId)
            updateSuccess.value = true
        }
    }

    fun changePassword(current: String, new: String, confirm: String) {
        val currentUser = _user.value ?: return
        updateError.value = null
        if (!checkPassword(current, currentUser.passwordHash)) {
            updateError.value = "Current password is incorrect."
            return
        }
        if (new.length < 6) {
            updateError.value = "New password must be at least 6 characters."
            return
        }
        if (new != confirm) {
            updateError.value = "Passwords do not match."
            return
        }
        viewModelScope.launch {
            userRepo.updateProfile(currentUser.copy(passwordHash = hashPassword(new)))
            _user.value = userRepo.findById(currentUser.userId)
            updateSuccess.value = true
        }
    }

    fun addAddress(label: String, line1: String, city: String, postalCode: String) {
        val userId = SessionManager.getUserId() ?: return
        viewModelScope.launch {
            addressRepo.add(Address(userId = userId, label = label, line1 = line1, city = city, postalCode = postalCode))
            _addresses.value = addressRepo.getByUser(userId)
        }
    }

    fun deleteAddress(addressId: Long) {
        val userId = SessionManager.getUserId() ?: return
        viewModelScope.launch {
            val address = addressRepo.getById(addressId) ?: return@launch
            addressRepo.delete(address)
            _addresses.value = addressRepo.getByUser(userId)
        }
    }

    fun setDefaultAddress(addressId: Long) {
        val userId = SessionManager.getUserId() ?: return
        viewModelScope.launch {
            addressRepo.setDefault(userId, addressId)
            _addresses.value = addressRepo.getByUser(userId)
        }
    }

    fun logout() {
        SessionManager.logout()
    }
}
