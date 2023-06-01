import java.awt.*;
import java.awt.im.InputContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class DictionaryChecker extends Frame{ // Класс проверяет правильность написанного слова

    LanguageSwitcher languageSwitcher = new LanguageSwitcher();
    LastWrongWordDeleter lastWrongWordDeleter = new LastWrongWordDeleter();

    static String currentLanguage = ""; // Эта переменная показывает текущий язык раскладки клавиатуры

    String correctlanguage = " ";  // Переменная, которая показывает правильный язык раскладки клавиатуры

    // Запрос в Google API для проверки слова
    public static boolean isWordInDictionary(String word, String apiKey) throws IOException {
        String apiUrl = "https://translation.googleapis.com/language/translate/v2/detect?key=" + apiKey + "&q=";
        String urlEncodedWord = URLEncoder.encode(word, StandardCharsets.UTF_8);

        // Формирование запроса к API
        URL url = new URL(apiUrl + urlEncodedWord);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Обработка ответа от API
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();
            // Проверка, содержится ли слово в ответе API
            return response.contains("\"language\": \"" + currentLanguage + "\""); //Выставляем язык словаря с помощью нашей переменной

        } else {
            throw new IOException("Не удалось выполнить запрос к API, код ошибки: " + responseCode);
        }
    }

    // Обработко ответа от Google API
    public void checkerWorker(String currentWord, List<Integer> currentKeyCodes) {

        // Узнаем какой сейчас установлен язык ввода в системе
        InputContext inputContext = InputContext.getInstance();
        if ("_US_UserDefined_252".equals(String.valueOf(inputContext.getLocale()))) {
            currentLanguage = "en";
        } else {
            currentLanguage = String.valueOf(inputContext.getLocale());
        }

        String apiKey = "AIzaSyDISFdioPgs9MR2DXVuQT9sscjpTYYVjr8";

        //Проверка есть ли слово в словаре
        try {

            if (isWordInDictionary(currentWord, apiKey)) {
                System.out.println("Word '" + currentWord + "' found in dictionary");
            } else {

                System.out.println("Word '" + currentWord + "' don't found in dictionary");

                //Определяем правильный язык
                if (Objects.equals(currentLanguage, availableLanguage1)) {
                    correctlanguage = availableLanguage2;
                } else {
                    correctlanguage = availableLanguage1;
                }

                languageSwitcher.mainSwitcher(correctlanguage, currentLanguage);
                lastWrongWordDeleter.mainDeleter(currentWord, currentKeyCodes);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при выполнении запроса к API: " + e.getMessage());
        } catch (InterruptedException | AWTException e) {
            throw new RuntimeException(e);
        }
    }
}