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
        // TODO add env variable host address
        message.setSubject("Matsku salasanan vaihtaminen")
        message.setText("Hei,\n" +
                "Olet pyytänyt Matsku käyttäjätunnuksesi salasanan vaihtamista.\n"+
                "Mene osoitteeseen http://localhost:3000/activate?forgot=true&key=" + token + " asettaaksesi tilillesi uuden salasanan.\n\n" +
                "Linkki on kertakäyttöinen.")
        emailSender.send(message)
    }

    fun sendActivateAccount(
        to: String,
        token: String
    ) {
        val message = SimpleMailMessage()
        message.setTo(to)
        // TODO add env variable host address
        message.setSubject("Matsku käyttäjätunnuksen aktivointi")
        message.setText("Hei,\n" +
                "Sinulle on luotu FCG Talent Matsku käyttäjätunnus.\n"+
                "Mene osoitteeseen http://localhost:3000/activate?key=" + token + " aktivoidaksesi käyttäjätunnuksesi.\n\n" +
                "Linkki on kertakäyttöinen.")
        emailSender.send(message)
    }
}