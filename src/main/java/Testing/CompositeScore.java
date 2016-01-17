package Testing;

import com.eatery.system.EateryMain;

/**
 * Created by bruntha on 1/17/16.
 */
public class CompositeScore {
    public static void main(String[] args) {
        EateryMain eateryMain = new EateryMain();
        System.out.println(eateryMain.getCompositeRating("test"));
    }

}
