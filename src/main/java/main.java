import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class main {
    static AtomicInteger counterOne = new AtomicInteger();
    static AtomicInteger counterTwo = new AtomicInteger();
    static AtomicInteger counterThree = new AtomicInteger();

    public static void main(String args[]) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        List<Thread> threads = new ArrayList<>();

        threads.add(new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                int flag = 1;
                int forward = 0;
                int backward = texts[i].length() - 1;
                while (backward > forward) {
                    char forwardChar = texts[i].charAt(forward++);
                    char backwardChar = texts[i].charAt(backward--);
                    if (forwardChar != backwardChar) {
                        flag = 0;
                    }
                }
                if (flag == 1) {
                    setCount(texts[i]);
                }
            }
        }));

        threads.add(new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                int flag = 1;
                for (int j = 0; j < texts[i].length(); j++) {
                    if (texts[i].charAt(0) != texts[i].charAt(j)) {
                        flag = 0;
                    }
                }
                if (flag == 1) {
                    setCount(texts[i]);
                }
            }
        }));

        threads.add(new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                int flag = 1;
                for (int j = 0; j < texts[i].length() - 1; j++) {
                    if (texts[i].charAt(j) > texts[i].charAt(j + 1)) {
                        flag = 0;
                    }
                }
                if (flag == 1) {
                    setCount(texts[i]);
                }
            }
        }));

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Красивых слов с длиной 3: " + counterOne);
        System.out.println("Красивых слов с длиной 4: " + counterTwo);
        System.out.println("Красивых слов с длиной 5: " + counterThree);
    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    private static void setCount(String text) {
        switch (text.length()) {
            case 3:
                counterOne.getAndAdd(1);
                break;
            case 4:
                counterTwo.getAndAdd(1);
                break;
            case 5:
                counterThree.getAndAdd(1);
                break;
            default:
                break;
        }
    }
}