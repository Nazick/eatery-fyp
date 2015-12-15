package domain;

/**
 * Created by bruntha on 8/14/15.
 */
public class Syn {
    private int tagCount;
    private String word;

    public Syn(int tagCount, String word) {
        this.tagCount = tagCount;
        this.word = word;
    }

    public int getTagCount() {
        return tagCount;
    }

    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
