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

    //Generate random string containing uppercase, lowercase, number and special character
    public static String generateRandomStringWithSpecialChars(int length) {
        if (length < 4) {
            throw new IllegalArgumentException("Length must be at least 4 to include required character types.");
        }

        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = uppercase.toLowerCase();
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()";
        String allChars = uppercase + lowercase + digits + specialChars;

        StringBuilder randomString = new StringBuilder();

        randomString.append(uppercase.charAt(RANDOM.nextInt(uppercase.length())));
        randomString.append(lowercase.charAt(RANDOM.nextInt(lowercase.length())));
        randomString.append(digits.charAt(RANDOM.nextInt(digits.length())));
        randomString.append(specialChars.charAt(RANDOM.nextInt(specialChars.length())));

        for (int i = 4; i < length; i++) {
            randomString.append(allChars.charAt(RANDOM.nextInt(allChars.length())));
        }

        return randomString.toString();
    }

    //Generate random string with only spaces
    public static String generateStringWithOnlySpaces() {
        return "   ";
    }

    //generate random number
    public static String generateNumber(int length) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    //generate string with leading space
    public static String generateStringWithLeadingSpaces(int length){
        String randomString = generateRandomString(length-1);
        return " " + randomString;
    }

    //generate string with trailing space
    public static String generateStringWithTrailingSpaces(int length){
        String randomString = generateRandomString(length-1);
        return randomString + " " ;
    }

    //generate string with leading and trailing space
    public static String generateStringWithLeadingTrailingSpaces(int length){
        String randomString = generateRandomString(length-2);
        return " " + randomString + " " ;
    }
}
