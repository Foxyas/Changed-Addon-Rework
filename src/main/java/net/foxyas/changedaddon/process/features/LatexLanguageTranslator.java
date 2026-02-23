package net.foxyas.changedaddon.process.features;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LatexLanguageTranslator {
    private static final Map<Character, Character> mapping = new HashMap<>();
    private static final Map<Character, Character> reverseMapping = new HashMap<>();

    static {
        // Initialize the mapping
        String keys = "abcdefghijklmnopqrstuvwxyz1234567890";
        String values = "σ£∃₳ε˩Гλ∩⌠≡Œßþ⌜ÆᖳΩФ⸸↨ǂw⋛¥√●-▲■▱◈▩▣▶◀";

        for (int i = 0; i < keys.length(); i++) {
            char k = keys.charAt(i);
            char v = values.charAt(i);
            mapping.put(k, v);
            reverseMapping.put(Character.toLowerCase(v), k);
        }
    }

    public static String translateText(String text, TranslationType translationType) {
        return translationType.getTranslated(text.toLowerCase());
    }

    // Runnable
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("\nTranslate TO or FROM? (or 'exit'): ");
            String option = scanner.nextLine().trim().toLowerCase();

            if (option.equals("exit")) break;

            if (!option.equals("to") && !option.equals("from")) {
                System.out.println("Invalid option! Please type 'to' or 'from'.");
                continue;
            }

            System.out.print("Enter text: ");
            String text = scanner.nextLine().toLowerCase();
            StringBuilder result = new StringBuilder();

            if (option.equals("to")) {
                for (char c : text.toCharArray()) {
                    result.append(mapping.getOrDefault(c, c));
                }
            } else {
                for (char c : text.toCharArray()) {
                    result.append(reverseMapping.getOrDefault(c, c));
                }
            }

            System.out.println("Result: " + result);
        }
        scanner.close();
    }

    public enum TranslationType {
        TO_LATEX_LANGUAGE(mapping),
        FROM_LATEX_LANGUAGE(reverseMapping);

        private final Map<Character, Character> translationMap;

        TranslationType(Map<Character, Character> translationMap) {
            this.translationMap = translationMap;
        }

        public String getTranslated(String text) {
            StringBuilder result = new StringBuilder();
            for (char c : text.toCharArray()) {
                result.append(this.translationMap.getOrDefault(c, c));
            }
            return result.toString();
        }
    }
}