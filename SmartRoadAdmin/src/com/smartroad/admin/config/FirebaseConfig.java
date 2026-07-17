package com.smartroad.admin.config;

import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

/**
 * Initialises Firebase Admin SDK and provides
 * the Cloud Firestore connection.
 */
public final class FirebaseConfig {

    private static final String PROJECT_ID =
            "ict602-fc4bc";

    private FirebaseConfig() {
        // Prevent this utility class from being instantiated.
    }

    public static synchronized void initialize()
            throws IOException {

        if (FirebaseApp.getApps().isEmpty()) {

            FirebaseOptions options =
                    FirebaseOptions.builder()
                            .setCredentials(
                                    GoogleCredentials
                                            .getApplicationDefault()
                            )
                            .setProjectId(PROJECT_ID)
                            .build();

            FirebaseApp.initializeApp(options);

            System.out.println(
                    "SmartRoad Firebase initialised successfully."
            );
        }
    }

    public static Firestore getFirestore()
            throws IOException {

        initialize();

        return FirestoreClient.getFirestore();
    }
}