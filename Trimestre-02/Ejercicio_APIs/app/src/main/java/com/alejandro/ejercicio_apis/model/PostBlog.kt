package com.alejandro.ejercicio_apis.model

data class PostBlog(
    val userId: Int,
    val id: Int? = null,
    val title: String,
    val body: String
) {
    override fun toString(): String {
        return "ID: $id - UserID: $userId"
    }
}
