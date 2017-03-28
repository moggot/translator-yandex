/*
 * Copyright 2013 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.moggot.mytranslator;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Makes the generic Yandex API calls. Different service classes can then
 * extend this to make the specific service calls.
 */
public abstract class YandexTranslatorAPI {
    //Encoding type
    protected static final String ENCODING = "UTF-8";

    private static final String LOG_TAG = "YandexTranslatorAPI";

    protected static String apiKey;

    protected static final String PARAM_API_KEY = "key=",
            PARAM_LANG_PAIR = "&lang=",
            PARAM_TEXT = "&text=";

    /**
     * Sets the API key.
     *
     * @param pKey The API key.
     */
    public static void setKey(final String pKey) {
        apiKey = pKey;
    }

    /**
     * Forms an HTTPS request, sends it using GET method and returns the result of the request as a String.
     *
     * @param url The URL to query for a String response.
     * @return The translated String.
     * @throws Exception on error.
     */
    private static String retrieveResponse(final URL url) throws Exception {
        final HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();

        try {
            final int responseCode = uc.getResponseCode();
            final String result = inputStreamToString(uc.getInputStream());
            if (responseCode != 200) {
                throw new Exception("Error from Yandex API: " + result);
            }
            return result;
        } finally {
            if (uc != null) {
                uc.disconnect();
            }
        }
    }

    /**
     * Forms a request, sends it using the GET method and returns the value with the given label from the
     * resulting JSON response.
     */
    protected static String retrievePropString(final URL url, final String jsonValProperty) throws Exception {
        final String response = retrieveResponse(url);
        JSONObject jsonObj = new JSONObject(response);
        return jsonObj.get(jsonValProperty).toString();
    }

    /**
     * Forms a request, sends it using the GET method and returns the contents of the array of strings
     * with the given label, with multiple strings concatenated.
     */
    protected static String retrievePropArrString(final URL url, final String jsonValProperty) throws Exception {
        final String response = retrieveResponse(url);
        return jsonObjValToStringArr(response, jsonValProperty);
    }

    // Helper method to parse a JSONObject containing an array of Strings with the given label.
    private static String jsonObjValToStringArr(final String inputString, final String subObjPropertyName) throws Exception {
        JSONObject jsonObj = new JSONObject(inputString);
        String translatedStr = jsonObj.getString(subObjPropertyName);
        Log.v(LOG_TAG, "var = " + translatedStr);
        int start = translatedStr.indexOf("[");
        int end = translatedStr.indexOf("]");
        return translatedStr.substring(start + 2, end - 1);
    }

    /**
     * Reads an InputStream and returns its contents as a String.
     * Also effects rate control.
     *
     * @param inputStream The InputStream to read from.
     * @return The contents of the InputStream as a String.
     * @throws Exception on error.
     */
    private static String inputStreamToString(final InputStream inputStream) throws Exception {
        final StringBuilder outputBuilder = new StringBuilder();

        try {
            String string;
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
                while (null != (string = reader.readLine())) {
                    // TODO Can we remove this?
                    // Need to strip the Unicode Zero-width Non-breaking Space. For some reason, the Microsoft AJAX
                    // services prepend this to every response
                    outputBuilder.append(string.replaceAll("\uFEFF", ""));
                }
            }
        } catch (Exception ex) {
            throw new Exception("[yandex-translator-api] Error reading translation stream.", ex);
        }
        return outputBuilder.toString();
    }

    //Check if ready to make request, if not, throw a RuntimeException
    protected static void validateServiceState() throws Exception {
        if (apiKey == null || apiKey.length() < 27) {
            throw new RuntimeException("INVALID_API_KEY - Please set the API Key with your Yandex API Key");
        }
    }

}
