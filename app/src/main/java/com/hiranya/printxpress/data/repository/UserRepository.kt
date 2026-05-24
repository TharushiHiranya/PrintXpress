package com.hiranya.printxpress.data.repository

import com.hiranya.printxpress.data.dao.UserDao
import com.hiranya.printxpress.data.entity.User
import com.hiranya.printxpress.data.util.checkPassword
import com.hiranya.printxpress.data.util.hashPassword

class UserRepository(private val userDao: UserDao) {

    // Register a new user and return the generated user id.
    suspend fun register(fullName: String, email: String?, phone: String?, password: String): Long {
        val user = User(
            fullName = fullName,
            email = email,
            phone = phone,
            passwordHash = hashPassword(password),
            createdAt = System.currentTimeMillis()
        )
        return userDao.insert(user)
    }

    // Return the user if credentials match, null otherwise.
    suspend fun login(emailOrPhone: String, password: String): User? {
        val user = userDao.findByEmail(emailOrPhone) ?: userDao.findByPhone(emailOrPhone)
        return if (user != null && checkPassword(password, user.passwordHash)) user else null
    }

    suspend fun updateProfile(user: User) {
        userDao.update(user)
    }

    suspend fun findByEmail(email: String): User? = userDao.findByEmail(email)

    suspend fun findByPhone(phone: String): User? = userDao.findByPhone(phone)

    suspend fun findById(id: Long): User? = userDao.findById(id)
}
