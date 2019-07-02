package com.alexthekap.a05_vkinfo.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetworkUtils {

    private static final String VK_API_BASE_URL = "https://api.vk.com";
    private static final String VK_USERS_GET = "/method/users.get";
    private static final String PARAM_USER_ID = "user_ids";
    private static final String PARAM_VERSION = "v";
    private static final String PARAM_TOKEN = "access_token";
    private static final String TOKEN_VALUE = "c8a2c431c8a2c431c8a2c4312ec8c984b0cc8a2c8a2c43195ae7702cf35d235f3f95414";

    public static URL generateURL(String userIds) {
        Uri buildUri = Uri.parse(VK_API_BASE_URL + VK_USERS_GET)
                .buildUpon()
                .appendQueryParameter(PARAM_USER_ID, userIds)
                .appendQueryParameter(PARAM_TOKEN, TOKEN_VALUE)
                .appendQueryParameter(PARAM_VERSION, "5.8")
                .build();

        try {
            return new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(5000);

        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (UnknownHostException e) {
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }

}
