import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

public class LastWrongWordDeleter { // Класс удаляет последнее неправильно введенное слово

    public void mainDeleter(String currentWord, List<Integer> currentKeyCodes) throws AWTException {

        int numberOfBackspaces = currentWord.length() + 1; // Количество нажатий клавиши Backspace (=длинна последнего слова)

        try {
            // AppleScript-код для нажатия клавиши Backspace заданное количество раз
            StringBuilder scriptBuilder = new StringBuilder();
            scriptBuilder.append("tell application \"System Events\"\n");
            for (int i = 0; i < numberOfBackspaces; i++) {
                scriptBuilder.append("    key code 51\n");
            }
            scriptBuilder.append("end tell");

            // Выполнение команды osascript для выполнения скрипта AppleScript
            String[] cmd = {"osascript", "-e", scriptBuilder.toString()};
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Word " + currentWord + " has been deleted.");
        printTransform(currentKeyCodes);
    }

    // Печатаем трансформированное слово
    public void printTransform(List<Integer> currentKeyCodes) {

        try {
            Robot robot = new Robot();
            robot.delay(215);
            for (int keyCode : currentKeyCodes) {
                int vkCode = getKeyEventKeyCode(keyCode);
                robot.keyPress(vkCode);
                robot.keyRelease(vkCode);
            }

            // Нажатие и отпускание клавиши пробела
            int spaceVkCode = KeyEvent.VK_SPACE;
            robot.keyPress(spaceVkCode);
            robot.keyRelease(spaceVkCode);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    // Преобразовываем полученные кей-коды в коды для класса Robot
    private static int getKeyEventKeyCode(int keyCode) {
        int vkCode = -1;
        int[][] keycodeMapping = LastWrongWordDeleter.KEYCODES;

        for (int[] mapping : keycodeMapping) {
            if (mapping[1] == keyCode) {
                vkCode = mapping[0];
                break;
            }
        }
        return vkCode;
    }

    // Таблица соответсвия кей-кодов между классом Robot и jnativehook
    public static final int[][] KEYCODES = {
            {KeyEvent.VK_Q, 16},
            {KeyEvent.VK_W, 17},
            {KeyEvent.VK_E, 18},
            {KeyEvent.VK_R, 19},
            {KeyEvent.VK_T, 20},
            {KeyEvent.VK_Y, 21},
            {KeyEvent.VK_U, 22},
            {KeyEvent.VK_I, 23},
            {KeyEvent.VK_O, 24},
            {KeyEvent.VK_P, 25},
            {KeyEvent.VK_OPEN_BRACKET, 27},
            {KeyEvent.VK_CLOSE_BRACKET, 28},
            {KeyEvent.VK_A, 30},
            {KeyEvent.VK_S, 31},
            {KeyEvent.VK_D, 32},
            {KeyEvent.VK_F, 33},
            {KeyEvent.VK_G, 34},
            {KeyEvent.VK_H, 35},
            {KeyEvent.VK_J, 36},
            {KeyEvent.VK_K, 37},
            {KeyEvent.VK_L, 38},
            {KeyEvent.VK_SEMICOLON, 39},
            {KeyEvent.VK_QUOTE, 40},
            {KeyEvent.VK_BACK_SLASH, 43},
            {KeyEvent.VK_Z, 44},
            {KeyEvent.VK_X, 45},
            {KeyEvent.VK_C, 46},
            {KeyEvent.VK_V, 47},
            {KeyEvent.VK_B, 48},
            {KeyEvent.VK_N, 49},
            {KeyEvent.VK_M, 50},
            {KeyEvent.VK_COMMA, 51},
            {KeyEvent.VK_PERIOD, 52}
    };
}