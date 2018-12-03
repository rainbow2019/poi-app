package xin.zhaohong.countDownLatchDemo;

import java.util.ArrayList;
import java.util.List;

public class JConsoleTest {
    static class JConsoleMemo {
        private int[] bytes = new int[1 * 64 * 1024];
    }

    public static void main(String[] args) {
        fillHeap();
    }

    public static void fillHeap(){
        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }
        final List<JConsoleTest> list = new ArrayList<>();

        int loop = 1000;
        for (int i = 0; i < loop; i++) {
            try {
                Thread.sleep(50);
                list.add(new JConsoleTest());
            } catch (Exception e) {

            }

        }
        try {
            Thread.sleep(300000000);
        } catch (Exception e) {

        }
        System.gc();
    }
}
