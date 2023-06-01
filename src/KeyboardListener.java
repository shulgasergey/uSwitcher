import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyboardListener implements NativeKeyListener { // Класс регистрирует написанные слова
    DictionaryChecker dictionaryChecker = new DictionaryChecker();
    public String currentWord = ""; // Переменная текущего слова
    List<Integer> currentKeyCodes = new ArrayList<>(); // Список кодов нажатых клавиш (=коды символов текущего слова)
    List<Integer> previousKeyCodes = new ArrayList<>(); // Список кодов предыдщего слова
    int keyCode; // Кей-код текущей нажатой клавиши
    char[] extrasymbols = {'[', ']', ';', '\'', '\\', ','}; // Массив с символами, которые не являются буквами на английском раскладке


    public void nativeKeyPressed(NativeKeyEvent e) { // Метод, который вызывается при нажатии клавиши на клавиатуре
        keyCode = e.getKeyCode();

        // Если нажат Backspace - удаляем последний символ слова
        if (e.getKeyCode() == NativeKeyEvent.VC_BACKSPACE) {
            if (currentWord.length() > 0) {
                currentWord = currentWord.substring(0, currentWord.length() - 1);
                currentKeyCodes.remove(currentKeyCodes.size() - 1);
            }
        }
        //System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    // Метод, который вызывается при отпускании клавиши на клавиатуре
    public void nativeKeyReleased(NativeKeyEvent e) {

        //System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    // Метод, который вызывается при наборе символа на клавиатуре
    public void nativeKeyTyped(NativeKeyEvent e) {
        if (Frame.runningProgramm) {
            char actualSymbolTyped = e.getKeyChar();


            if (actualSymbolTyped == ' ') { // Если нажат пробел
                if (!currentKeyCodes.equals(previousKeyCodes)) { // Если последних два слова имеют разные кей-коды (разные слова)
                    if (!currentWord.isEmpty()) { // Если переменная "currentWord" не пустая

                        System.out.println("Actual word typed -> " + currentWord);
                        System.out.println("Actual key codes -> " + currentKeyCodes);

                        dictionaryChecker.checkerWorker(currentWord, currentKeyCodes);

                        currentWord = "";
                        previousKeyCodes.clear();
                        previousKeyCodes.addAll(currentKeyCodes);
                        currentKeyCodes.clear();

                        System.out.println("------------------------------");
                        System.out.println(" ");
                    }
                } else { // Если предыдущее слово = текущее слово
                    System.out.println("Word has been processed already");
                    currentWord = "";
                    currentKeyCodes.clear();
                    System.out.println("------------------------------");
                }
            } else if (Character.isLetter(actualSymbolTyped) || containsSymbol(actualSymbolTyped, extrasymbols)) {
                currentWord += actualSymbolTyped;
                currentKeyCodes.add(keyCode);
            }

        }
    }

    // Метод для проверки наличия extra-символа в массиве
    public static boolean containsSymbol(char symbol, char[] symbols) {
        for (char s : symbols) {
            if (s == symbol) {
                return true;
            }
        }
        return false;
    }

    public void mainListener() {

        // Получаем логгер для библиотеки jnativehook и выключаем вывод логов
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        try {
            // Регистрируем глобальный слушатель клавиатуры
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            // Обработка исключения при ошибке регистрации слушателя
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            // Выходим из программы с кодом 1
            System.exit(1);
        }
        // Добавляем экземпляр класса KeyboardListener в список слушателей
        GlobalScreen.addNativeKeyListener(new KeyboardListener());
    }

}