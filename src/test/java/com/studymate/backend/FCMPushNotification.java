package com.studymate.backend;

import com.google.firebase.FirebaseApp;
import com.studymate.backend.commons.firebase.PushNotificationService;
import com.studymate.backend.config.fcm.FCMInitializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FCMPushNotification {

    @Mock
    private FCMInitializer fcmInitializer;

    @Mock
    private PushNotificationService pushNotificationService;

    @Test
    public void contextLoads() {

    }

    @Test
    @DisplayName("firebase init 이 되는지")
    public void firebaseAppInitializationTest(){
        fcmInitializer.initialize();
        System.out.println(FirebaseApp.getApps());
        Assertions.assertFalse(FirebaseApp.getApps().isEmpty());
    }
}
