import java.util.ArrayList;
import java.util.List;


public class AvailableLanguagesGetter {
    public static String[] availableLanguages;

    public void starter() {
        // Получение доступных языков
        availableLanguages = getAvailableLanguages();

        // Вывод заголовка
        System.out.print("Available languages: ");

        // Стирание первых 4 символов первого элемента массива
        if (availableLanguages.length > 0) {
            String firstLanguage = availableLanguages[0];
            if (firstLanguage.length() > 4) {
                availableLanguages[0] = firstLanguage.substring(4);
            }
        }

        availableLanguages = removeLanguage(availableLanguages, "ua");

        // Вывод всех языков
        for (String language : availableLanguages) {
            System.out.print(language + " ");
        }

        Frame frame = new Frame();
        frame.frameStarter(availableLanguages);
    }

    // Получение доступных языков
    private static String[] getAvailableLanguages() {
        List<String> languages = new ArrayList<>();

        // Получение списка языков в формате Apple
        String appleLanguages = getAppleLanguages();

        // Разделение списка на отдельные языковые коды
        String[] languageCodes = appleLanguages.split(",");

        // Обход каждого языкового кода
        for (int i = 0; i < languageCodes.length; i++) {
            String code = languageCodes[i].trim();
            if (!code.isEmpty()) {
                // Получение основного языка из кода
                String language = code.split("-")[0].toLowerCase();

                // Если это последний языковой код, получение второго языка и добавление обоих в список
                if (i == languageCodes.length - 1) {
                    String lastLanguage = code.split("-")[1].toLowerCase();
                    languages.add(stripQuotes(stripParentheses(language)));
                    languages.add(stripQuotes(stripParentheses(lastLanguage)));
                } else {
                    // Добавление основного языка в список
                    languages.add(stripQuotes(stripParentheses(language)));
                }
            }
        }

        // Преобразование списка в массив и возвращение
        return languages.toArray(new String[0]);
    }

    // Получение списка языков в формате Apple
    private static String getAppleLanguages() {
        StringBuilder appleLanguages = new StringBuilder();
        try {
            // Запуск команды для получения списка языков
            Process process = Runtime.getRuntime().exec("defaults read -g AppleLanguages");
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                // Чтение каждой строки вывода и добавление к строке языков
                appleLanguages.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Возвращение строки языков
        return appleLanguages.toString();
    }

    // Удаление кавычек из строки
    private static String stripQuotes(String str) {
        return str.replace("\"", "");
    }

    // Удаление скобок из строки
    private static String stripParentheses(String str) {
        return str.replace("(", "").replace(")", "");
    }

    public static String[] removeLanguage(String[] languages, String languageToRemove) {
        int index = -1;

        // Поиск индекса элемента, который нужно удалить
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].equals(languageToRemove)) {
                index = i;
                break;
            }
        }

        // Если элемент найден, удаляем его и возвращаем новый массив
        if (index != -1) {
            String[] updatedLanguages = new String[languages.length - 1];
            System.arraycopy(languages, 0, updatedLanguages, 0, index);
            System.arraycopy(languages, index + 1, updatedLanguages, index, languages.length - index - 1);
            return updatedLanguages;
        }

        // Если элемент не найден, возвращаем исходный массив без изменений
        return languages;
    }

}