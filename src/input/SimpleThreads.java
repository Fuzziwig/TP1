package input;

import java.util.ArrayList;

public class SimpleThreads {

    //private static Map<String, Integer> threadScoreCount = new TreeMap<String, Integer>();
    private static ArrayList<Score> threadScoreCount = new ArrayList<Score>();
    // Display a message, preceded by
    // the name of the current thread
    static void threadMessage(String message) {
        String threadName =
                Thread.currentThread().getName();
        System.out.format("%s: %s%n",
                threadName,
                message);
    }

    private static class MessageLoop
            implements Runnable {

        private static int messageLoopCounter = 0;
        private boolean running = true;

        public void run() {
            String importantInfo[] = {
                    "Mares eat oats",
                    "Does eat oats",
                    "Little lambs eat ivy",
                    "A kid will eat ivy too"
            };
            while (running){
                try {
                    for (int i = 0;
                         i < importantInfo.length;
                         i++) {
                        int randomDelay = (int)  (100+ Math.random() * (5000 - 100));
                        // Pause for 4 seconds
                        Thread.sleep(randomDelay);
                        // Print a message
                        threadMessage(importantInfo[i]);
                        //keep track of how much is broadcasted
                        messageLoopCounter++;
                        System.out.println("messages broadcasted "+messageLoopCounter);
                        //update individual thread score
                        updateScore(Thread.currentThread().getName());
                        System.out.println(threadScoreCount.toString());
                        //if any other thread is doing better interrupt them
                        envy(Thread.currentThread().getName());
                    }
                    return;
                } catch (InterruptedException e) {
                    threadMessage("I wasn't done!");
                    //delay thread if interrupted
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException d) {
                    }
                }
            }
        }
    }

    private static void updateScore(String threadName){
        for (Score score : threadScoreCount){
            if (score.getThread().getName().equals(threadName)){
                score.setScore(score.getScore()+1);
                return;
            }
        }
    }

    private static void envy(String threadName){
        int envyThreadScore=0;
        //get envy threads score
        for (Score score : threadScoreCount){
                if (score.getThread().getName().equals(threadName)){
                    envyThreadScore = score.getScore();
                }
        }
        //interrupt every other thread if their message score is greater
        for (Score score : threadScoreCount){
            if (!score.getThread().getName().equals(threadName)){
                if (score.getScore()>envyThreadScore){
                    score.getThread().interrupt();
                }
            }
        }
    }

    public static void main(String args[])
            throws InterruptedException {

        // Delay, in milliseconds before
        // we interrupt MessageLoop
        // thread (default one hour).
        long patience = 1000 * 60 * 60;

        threadMessage("Starting MessageLoop thread");
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new MessageLoop());
        t.start();
        Thread u = new Thread(new MessageLoop());
        u.start();
        Thread w = new Thread(new MessageLoop());
        w.start();

        threadScoreCount.add(new Score(t));
        threadScoreCount.add(new Score(u));
        threadScoreCount.add(new Score(w));

        threadMessage("Waiting for MessageLoop thread to finish");
        // loop until MessageLoop
        // thread exits
        while (t.isAlive() || u.isAlive() || w.isAlive()) {
            threadMessage("Still waiting...");
            // Wait maximum of 1 second
            // for MessageLoop thread
            // to finish.
            t.join(1000);
            u.join(1000);
            w.join(1000);
            if (((System.currentTimeMillis() - startTime) > patience)
                    && (t.isAlive() || u.isAlive() || w.isAlive())) {
                threadMessage("Tired of waiting!");
                t.interrupt();
                u.interrupt();
                w.interrupt();
                // Shouldn't be long now
                // -- wait indefinitely
                t.join();
                u.join();
                w.join();
            }
        }
        threadMessage("Finally!");
    }
}