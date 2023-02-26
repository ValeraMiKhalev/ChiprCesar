import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final int BUFFER_SIZE = 1000;
    private static final HashMap<Character, Character> digitAndPunctuation;
    static {
        digitAndPunctuation = new HashMap<>();
        digitAndPunctuation.put('0', '9');
        digitAndPunctuation.put('1', '8');
        digitAndPunctuation.put('2', '7');
        digitAndPunctuation.put('3', '6');
        digitAndPunctuation.put('4', '5');
        digitAndPunctuation.put('5', '4');
        digitAndPunctuation.put('6', '3');
        digitAndPunctuation.put('7', '2');
        digitAndPunctuation.put('8', '1');
        digitAndPunctuation.put('9', '0');
        digitAndPunctuation.put('.', ';');
        digitAndPunctuation.put(',', ':');
        digitAndPunctuation.put(':', ',');
        digitAndPunctuation.put(';', '.');
        digitAndPunctuation.put('?', '-');
        digitAndPunctuation.put('!', '"');
        digitAndPunctuation.put('-', '?');
        digitAndPunctuation.put('"', '!');
    }
    public static void main(String[] args) {
        System.out.println("""
                Hello, this program implements Caesar encryption and decryption.
                The shift is implemented only for letters, while the letter "Ё" is not taken into account.
                Numbers and punctuation marks are reversed.""");
        System.out.println("""
                If you want to encrypt the file, enter: ENCRYPT.
                If you want to decrypt the file, enter: DECRYPT.
                If you want to decrypt a file using the brut_force method, enter: DECRYPT_BRUT_FORCE.""");
        Scanner console = new Scanner(System.in);
        String choice = console.nextLine();
        if (choice.equalsIgnoreCase("ENCRYPT")) {// Определяем, равны ли переменные s1 и A, и игнорируем размер
            System.out.println("Specify the path to the file to be encrypted:");
            String pathReaderFile = console.nextLine();
            System.out.println("Specify the path to save the encrypted file:");
            String pathEncryptFile = console.nextLine();
            System.out.println("Specify the encryption key:");
            int key = console.nextInt();
            Encrypt(pathReaderFile, pathEncryptFile, key);// Вызываем метод шифрования*/
        } else if (choice.equalsIgnoreCase("DECRYPT")) {
            System.out.println("Specify the path to the file to be decrypted:");
            String pathReaderEncryptFile = console.nextLine();
            System.out.println("Specify the path to save the decrypted file:");
            String pathDecryptFile = console.nextLine();
            System.out.println("Specify the decryption key:");
            int key = console.nextInt();// ключ для сдвига
            Decrypt(pathReaderEncryptFile, pathDecryptFile, key);// Вызываем метод шифрования
        } else if (choice.equalsIgnoreCase("DECRYPT_BRUT_FORCE")) {
            System.out.println("Specify the path to the file to be decrypted:");
            String pathReaderEncryptFile = console.nextLine();
            /*System.out.println("Specify the path to save the decrypted file:");
            String pathDecryptFile = console.nextLine();*/
            Decrypt_brut_Force(pathReaderEncryptFile);
        }
    }
    public static void Encrypt(String pathReaderFile,String pathEncryptFile,int key){
        try (FileReader readerFile = new FileReader(pathReaderFile);
             FileWriter encryptFile = new FileWriter(pathEncryptFile)) {
            while (readerFile.ready()) {
                char[] readBuffer = new char[BUFFER_SIZE];
                int readTextLenght = readerFile.read(readBuffer);
                for (int bufferIndex = 0; bufferIndex < readTextLenght; bufferIndex++) {
                    if (readBuffer[bufferIndex] == 'ё') {
                        readBuffer[bufferIndex] = 'е';
                    }
                    if (readBuffer[bufferIndex] == 'Ё') {
                        readBuffer[bufferIndex] = 'Е';
                    }
                    if (digitAndPunctuation.containsKey(readBuffer[bufferIndex])) {
                        readBuffer[bufferIndex] = digitAndPunctuation.get(readBuffer[bufferIndex]);
                    }
                    if (Character.isLetter(readBuffer[bufferIndex]) && Character.isUpperCase(readBuffer[bufferIndex])) {
                        readBuffer[bufferIndex] = (char) ((((int) (readBuffer[bufferIndex]) - 'А' + key) % 32) + 'А');
                    } else if (Character.isLetter(readBuffer[bufferIndex]) && Character.isLowerCase(readBuffer[bufferIndex])) {
                        readBuffer[bufferIndex] = (char) ((((int) (readBuffer[bufferIndex]) - 'а' + key) % 32) + 'а');
                    }
                }
                encryptFile.write(readBuffer, 0 , readTextLenght);
                encryptFile.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void Decrypt(String pathReaderEncryptFile,String pathDecryptFile,int key){
        try (FileReader readerEncryptFile = new FileReader(pathReaderEncryptFile);
             FileWriter decryptFile = new FileWriter(pathDecryptFile)) {
            while (readerEncryptFile.ready()) {
                char[] readBuffer = new char[BUFFER_SIZE];
                int readTextLenght = readerEncryptFile.read(readBuffer);
                for (int bufferIndex = 0; bufferIndex < readTextLenght; bufferIndex++) {
                    if (digitAndPunctuation.containsValue(readBuffer[bufferIndex])) {
                        readBuffer[bufferIndex] = digitAndPunctuation.get(readBuffer[bufferIndex]);
                    }
                    if (Character.isLetter(readBuffer[bufferIndex]) && Character.isUpperCase(readBuffer[bufferIndex])) {
                        readBuffer[bufferIndex] = (char) ((((int) (readBuffer[bufferIndex]) + 'А' - key) % 32) + 'А');
                    } else if (Character.isLetter(readBuffer[bufferIndex]) && Character.isLowerCase(readBuffer[bufferIndex])) {
                        readBuffer[bufferIndex] = (char) ((((int) (readBuffer[bufferIndex]) + 'а' - key) % 32) + 'а');
                    }
                }
                decryptFile.write(readBuffer, 0 , readTextLenght);
                decryptFile.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void Decrypt_brut_Force(String pathReaderEncryptFile){
        for (int key = 0;  key <= 32; key++) {
            HashMap<Integer, String> keyMatches = new HashMap<>();
            try (FileReader readerEncryptFile = new FileReader(pathReaderEncryptFile)) {
                StringBuilder checkBuffer = new StringBuilder();
                while (readerEncryptFile.ready()) {
                    char[] readBuffer = new char[BUFFER_SIZE];
                    int readTextLenght = readerEncryptFile.read(readBuffer);
                    for (int bufferIndex = 0; bufferIndex < readTextLenght; bufferIndex++) {
                        if (digitAndPunctuation.containsValue(readBuffer[bufferIndex])) {
                            readBuffer[bufferIndex] = digitAndPunctuation.get(readBuffer[bufferIndex]);
                        }
                        if (Character.isLetter(readBuffer[bufferIndex]) && Character.isUpperCase(readBuffer[bufferIndex])) {
                            readBuffer[bufferIndex] = (char) ((((int) (readBuffer[bufferIndex]) + 'А' - key) % 32) + 'А');
                        } else if (Character.isLetter(readBuffer[bufferIndex]) && Character.isLowerCase(readBuffer[bufferIndex])) {
                            readBuffer[bufferIndex] = (char) ((((int) (readBuffer[bufferIndex]) + 'а' - key) % 32) + 'а');
                        }
                    }
                    checkBuffer.append(readBuffer);
                }
                keyMatches.put(key, String.valueOf(checkBuffer));
                System.out.println("Print matches by key:");
                for (Map.Entry<Integer, String> entry : keyMatches.entrySet()) {
                    System.out.println("Key: " + entry.getKey() + ".\n" +
                            " Matches: " + entry.getValue());
                }
                System.out.println("-------------------------------------------");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Choose the right key and complete the decryption.");
        System.out.println("Specify the path to save the decrypted file:");
        Scanner console = new Scanner(System.in);
        String pathDecryptFile = console.nextLine();
        System.out.println("Specify the decryption right key:");
        int key = console.nextInt();
        Decrypt(pathReaderEncryptFile, pathDecryptFile, key);
    }
}

