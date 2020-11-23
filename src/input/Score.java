package input;

public class Score {

    private Thread thread;
    private int score=0;

    public Score(Thread thread) {
        this.thread = thread;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Score{" +
                "thread=" + thread.getName() +
                ", score=" + score +
                '}';
    }
}
