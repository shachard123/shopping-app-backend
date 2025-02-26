package com.shopping.services

import com.mongodb.client.MongoCollection
import com.shopping.models.User
import org.litote.kmongo.eq
import at.favre.lib.crypto.bcrypt.BCrypt
import org.litote.kmongo.findOne


class UserService(private val collection: MongoCollection<User>) {
    fun createUser(username: String, password: String): String? {
        // Check if user already exists
        if (collection.findOne(User::username eq username) != null) {
            return null
        }

        // Hash password
        val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())

        val user = User(username = username, passwordHash = hashedPassword)
        collection.insertOne(user)
        return user.id
    }

    fun getUserById(userId: String): User? {
        return collection.findOne(User::id eq userId)
    }

    fun getUserByUsername(username: String): User? {
        return collection.findOne(User::username eq username)
    }

    fun validateUser(username: String, password: String): User? {
        val user = getUserByUsername(username) ?: return null
        return if (BCrypt.verifyer().verify(password.toCharArray(), user.passwordHash).verified) user else null
    }
}