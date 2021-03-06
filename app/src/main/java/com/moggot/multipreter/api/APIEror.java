package com.moggot.multipreter.api;

/**
 * Класс обработки кодов ответов API Yandex Translator
 */
public class APIEror {

    /**
     * Обработка ошибок
     *
     * @param errorCode - код ошибки
     */
    public static void parseError(int errorCode) throws Exception {
        if (errorCode != 200) {
            switch (errorCode) {
                case 401:
                    throw new Exception("Wrong API key");
                case 402:
                    throw new Exception("API key is locked");
                case 404:
                    throw new Exception("Daily text limit exceeded");
                case 413:
                    throw new Exception("Maximum text size exceeded");
                case 422:
                    throw new Exception("Text can not be translated");
                case 501:
                    throw new Exception("This translation direction is not supported");
                default:
                    throw new Exception("Unknown error");
            }
        }
    }
}
