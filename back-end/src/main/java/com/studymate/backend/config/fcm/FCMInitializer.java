package com.studymate.backend.config.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Collections;

@Configuration
@Slf4j
public class FCMInitializer {
    @Value("${firebase.firebaseConfigPath}")
    private String firebaseConfigPath;

    @Value("${firebase.scope}")
    private String scope;

    @PostConstruct
    public void initialize(){

        try {
            ClassPathResource serviceAccount = new ClassPathResource(firebaseConfigPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream())
                            .createScoped(Collections.singleton(scope)))
                            .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        } catch (IOException e) {
            log.info(">>>>>>>>>FCM error");
            log.info(">>>>>>>>>FCM error message : " + e.getMessage());
        }
    }
}
