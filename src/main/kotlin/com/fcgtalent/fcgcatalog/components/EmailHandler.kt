package com.fcgtalent.fcgcatalog.components

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class EmailHandler(val emailSender: JavaMailSender) {

    fun sendPasswordReset(
        to: String,
        token: String
    ) {
        val message = SimpleMailMessage()
        message.setTo(to)
        // TODO add better title
        message.setSubject("Reset your password")
        message.setText("Hello\n" +
            "\n" +
            // TODO add correct, address thigny
            "Please go to https://localhost/ \n" +
            "To reset your password")
        emailSender.send(message)
    }

    fun sendActivateAccount(
        to: String,
        token: String
    ) {
        val message = SimpleMailMessage()
        message.setTo(to)
        // TODO add better title
        message.setSubject("Activate your account")
        message.setText("Hello" +
            "\n" +
            // TODO add correct, address thigny
            "Please go to https://localhost/ \n " +
            "To Activate your account")
        emailSender.send(message)
    }
}