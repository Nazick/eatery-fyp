package AggregatingModel;

/**
 * Created by bruntha on 12/29/15.
 */
public class LBNCI {
    double zValue=1.96;
    double variation=0.05;
    public double calculateLBNCI(double[] ratings) {
        double average;
        double total=0;
        for (int i = 0; i < ratings.length; i++) {
            total+=ratings[i];
        }
        average=total/ratings.length;

        double score=average-zValue*variation/Math.sqrt(ratings.length);

        return score;
    }

    public double calculateAvgRating(double[] ratings) {
        double average;
        double total=0;
        for (int i = 0; i < ratings.length; i++) {
            total+=ratings[i];
        }
        average=total/ratings.length;

        return average;
    }

    public static void main(String[] args) {
        LBNCI lbnci=new LBNCI();
        double[] ratings={5.0,4.0,5.0};
        System.out.println(lbnci.calculateLBNCI(ratings));
    }
}
