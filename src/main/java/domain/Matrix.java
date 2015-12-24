package domain;

import java.util.AbstractList;

/**
 * Created by bruntha on 12/8/15.
 */
public class Matrix {
    String heading;
    AbstractList<String> variables;
    Jama.Matrix matrix;
    static double factor = 1e4; // = 1 * 10^5 = 100000.


    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public AbstractList<String> getVariables() {
        return variables;
    }

    public void setVariables(AbstractList<String> variables) {
        this.variables = variables;
    }

    public Jama.Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Jama.Matrix matrix) {
        this.matrix = matrix;
    }

    public void print() {
        System.out.println("************** "+heading+" **************");

        for (int i = -1; i < matrix.getRowDimension(); i++) {
            for (int j = -1; j < matrix.getRowDimension(); j++) {
                if (i == -1 && j == -1) {
                    System.out.printf("%-12s ","");
                } else if(i==-1){
                    System.out.printf("%-12s ",variables.get(j) );
                } else if(j==-1){
                    System.out.printf("%-12s ",variables.get(i) );
                } if (i != -1 && j != -1){
                    System.out.printf("%-12s ", Math.round(matrix.get(i,j)* factor )/ factor);
                }
            }
            System.out.println();
            ;
        }
    }
}
