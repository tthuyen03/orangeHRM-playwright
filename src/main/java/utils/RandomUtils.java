package utils;

import java.util.Random;

public class RandomUtils {
    private static final Random RANDOM = new Random();

    //Generate random string
    public static String generateRandomString(int length){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        for(int i = 0; i < length; i++){
            randomString.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return randomString.toString();
    }

    //Generate random string with number and special character
    public static String generateRandomStringWithSpecialChars(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return randomString.toString();
    }

    //Generate random string with only spaces
    public static String generateStringWithOnlySpaces() {
        return "   ";
    }
}
