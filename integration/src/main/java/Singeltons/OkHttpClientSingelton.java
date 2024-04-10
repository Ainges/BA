package Singeltons;

import jakarta.enterprise.context.ApplicationScoped;
import okhttp3.OkHttpClient;

/**
 * We are using the same OkHttpClient instance in multiple classes, we are using the Singleton pattern to ensure that only one instance of OkHttpClient is created.
 * This is done to avoid creating multiple instances of OkHttpClient, which would be a waste of resources as mentioned in the OkHttpClient documentation.
 * */
@ApplicationScoped
public class OkHttpClientSingelton {
    private static final OkHttpClient client = new OkHttpClient();

    // private constructor to avoid client applications to use constructor
    private OkHttpClientSingelton() {
    }

    // static method to create instance of Singleton class
    public static OkHttpClient getInstance() {
        return client;
    }
}