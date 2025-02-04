package com.emirkanmaz.androidnotification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {

//    kullanıcı tokeni değiştiğinde bunu serverde o kullanıcıyla her seferinde eşleştirmelisin
//    FirebaseMessaging.getToken() ile token dönüşü aynı
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

    }


}
