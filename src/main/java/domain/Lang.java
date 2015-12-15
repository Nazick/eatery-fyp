package domain;

/**
 * Created by bruntha on 9/15/15.
 */
public class Lang {
    private String language;
    private boolean reliable;
    private double confident;

    public Lang() {
    }

    public Lang(String language, boolean reliable, double confident) {
        this.language = language;
        this.reliable = reliable;
        this.confident = confident;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isReliable() {
        return reliable;
    }

    public void setReliable(boolean reliable) {
        this.reliable = reliable;
    }

    public double getConfident() {
        return confident;
    }

    public void setConfident(double confident) {
        this.confident = confident;
    }
}
