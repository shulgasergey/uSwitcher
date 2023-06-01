import java.awt.im.InputContext;
import java.io.IOException;

public class LanguageSwitcher { // Класс меняет язык на правильный
    public void mainSwitcher(String correctlanguage, String currentLanguage) throws InterruptedException {

        // Код AppleScript для переключения языка
        while (!correctlanguage.equals(currentLanguage)) {
            try {
                String script = "tell application \"System Events\"\n" +
                        "   keystroke tab using {control down}\n" +
                        "end tell";

                String[] cmd = {"osascript", "-e", script};

                Process process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
                Thread.sleep(70);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            //Обновляем показания текущего языка клавиатуры
            InputContext inputContext = InputContext.getInstance();
            if ("_US_UserDefined_252".equals(String.valueOf(inputContext.getLocale()))) {
                currentLanguage = "en";
            } else {
                currentLanguage = String.valueOf(inputContext.getLocale());
            }

        }
        System.out.println("Language chosen to -> " + currentLanguage);
        Thread.sleep(10);
    }
}