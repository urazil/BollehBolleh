package com.example.kotlinnodejsauth.data

class ChatModel {
    private var message: String?
    private var userName: String?
    private var sender: Int

    constructor(message: String? = null, userName: String? = null, sender: Int = 0) {
        this.message = message
        this.userName = userName
        this.sender = sender
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message!!
    }

    fun getUserName(): String? {
        return userName
    }

    fun setUserName(userName: String?) {
        this.userName = userName!!
    }

    fun getSender(): Int {
        return sender
    }

    fun setSender(sender: Int) {
        this.sender = sender
    }
}