package tosamara.classifiers;

import tosamara.classifiers.parsers.StopParser;

public class Main {
    public static void main(String[] args) {
        try{
            /*Updater.update(false);
            StopParser parser = new StopParser();
            long start = System.currentTimeMillis();
            parser.parse();
            System.out.println("Parse: " + (System.currentTimeMillis() - start));*/

            for (int i = 0; i < 1000; i++) {
                int finalI = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int s = 0;

                        for (int j = 0; j < finalI * 10000; j++) {
                            s += j;
                        }
                        System.out.println(s);
                    }
                }).start();
            }
            Thread.sleep(20000);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
