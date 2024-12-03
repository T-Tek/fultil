package com.fultil.utils;

import java.util.Arrays;

public class TestMaskPan {
    public static void main(String[] args) {
        String save = "save";
        String vase = "vase";

        checkIfAnagram(save, vase);
    }

    public static void checkIfAnagram(String s1, String s2) {
        if (s1.length() != s2.length()) {
            System.out.println("The strings are not anagrams (different lengths).");
            return;
        }

        char[] char1 = s1.toCharArray();
        char[] char2 = s2.toCharArray();

        Arrays.sort(char1);
        Arrays.sort(char2);

        System.out.println("Sorted char1 array: " + Arrays.toString(char1));
        System.out.println("Sorted char2 array: " + Arrays.toString(char2));

        if (Arrays.equals(char1, char2)) {
            System.out.println("The strings are anagrams.");
        } else {
            System.out.println("The strings are not anagrams.");
        }
    }
}
