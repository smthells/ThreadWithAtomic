import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static AtomicInteger counterForLengthEqualThree = new AtomicInteger(0);
    private static AtomicInteger counterForLengthEqualFour = new AtomicInteger(0);
    private static AtomicInteger counterForLengthEqualFive = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread thread1 = new Thread(() -> {
            for (String nicknames : texts) {
                if (nicknames.contentEquals(new StringBuilder(nicknames).reverse())) {
                    incrementCounter(nicknames.length());
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (String nicknames : texts) {
                if (isSingleLetterWord(nicknames)) {
                    incrementCounter(nicknames.length());
                }
            }
        });

        Thread thread3 = new Thread(() -> {
            for (String nicknames : texts) {
                if (alphabeticalWord(nicknames)) {
                    incrementCounter(nicknames.length());
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        thread3.join();
        thread2.join();
        thread1.join();

        System.out.println("Красивых слов с длиной 3: " + counterForLengthEqualThree.get() + " шт.");
        System.out.println("Красивых слов с длиной 4: " + counterForLengthEqualFour.get() + " шт.");
        System.out.println("Красивых слов с длиной 5: " + counterForLengthEqualFive.get() + " шт.");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void incrementCounter(int length) {
        switch (length) {
            case 3 -> counterForLengthEqualThree.getAndIncrement();
            case 4 -> counterForLengthEqualFour.getAndIncrement();
            case 5 -> counterForLengthEqualFive.getAndIncrement();
        }
    }

    public static boolean isSingleLetterWord(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != text.charAt(0)) {
                return false;
            }
        }
        return true;
    }

    public static boolean alphabeticalWord(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }
}