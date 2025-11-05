package Aud2;

import java.util.stream.IntStream;

public class StringPrefix {

    public static boolean isPrefix(String str1, String str2) {
        if(str1.length() > str2.length()) {
            return false;
        }
        for(int i = 0; i < str1.length(); i++) {
            if(str1.charAt(i) != str2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPrefixStream(String str1, String str2) {
        if(str1.length() > str2.length()) {
            return false;
        }
        return IntStream.range(0, str1.length()).allMatch(i -> str1.charAt(i) == str2.charAt(i));
    }

    public static void main(String[] args) {
        System.out.println(isPrefix("pre", "prefix"));       // true
        System.out.println(isPrefix("test", "testing"));     // true
        System.out.println(isPrefix("hello", "hi"));         // false
        System.out.println(isPrefix("java", "javascript"));  // true
        System.out.println(isPrefix("world", "word"));       // false
    }
}
