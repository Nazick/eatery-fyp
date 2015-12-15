package domain;

import java.util.ArrayList;

/**
 * Created by bruntha on 11/4/15.
 */
public class TwoDimentionalArrayList<Integer> extends ArrayList<ArrayList<Integer>> {
    public void addToInnerArray(int index, Integer element) {
        while (index >= this.size()) {
            this.add(new ArrayList<Integer>());
        }
        this.get(index).add(element);
    }

    public void addToInnerArray(int index, int index2, Integer element) {
        while (index >= this.size()) {
            this.add(new ArrayList<Integer>());
        }

        ArrayList<Integer> inner = this.get(index);
        while (index2 >= inner.size()) {
            inner.add(null);
        }

        inner.set(index2, element);
    }
}