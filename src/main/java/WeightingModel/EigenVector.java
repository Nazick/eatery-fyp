package WeightingModel;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * Created by bruntha on 1/30/16.
 */
public class EigenVector {
    public static void main(String[] argv) {
//        double[][] I = { {2./3,-1./4,-1./4}, {-1./4,2./3,-1./4}, {-1./4,-1./4,2./3} };
//        double[][] I = { {1,2,1}, {6,-1,0}, {-1,-2,-1} };
        double[][] I = {{0, 1}, {-2, -3}};
        Matrix MatI = new Matrix(I);                                       //  Form Matrix from 2D arrays
        System.out.print("Input Matrix");
        MatI.print(10, 5);                                      //  Jama's Matrix print
        //  Jama eigenvalue finder
        EigenvalueDecomposition E = new EigenvalueDecomposition(MatI);
        double[] lambdaRe = E.getRealEigenvalues();                                      //  Real eigens
        double[] lambdaIm = E.getImagEigenvalues();                                      //  Imag eigens
        System.out.println("Eigenvalues: \t lambda.Re[] = " + lambdaRe[0] + ", " + lambdaRe[1]);
        Matrix V = E.getV();                                      //  Get matrix of eigenvectors
        System.out.print("\n Matrix with column eigenvectors ");
        V.print(10, 5);
        Matrix Vec = new Matrix(3, 1);                                      //  Extract single eigenvector
        Vec.set(0, 0, V.get(0, 0));
        Vec.set(1, 0, V.get(1, 0));
        Vec.set(2, 0, V.get(2, 0));
        System.out.print("First Eigenvector, Vec");
        Vec.print(10, 5);
        Matrix LHS = MatI.times(Vec);                                     //  Should get Vec as answer
        Matrix RHS = Vec.times(lambdaRe[0]);
        System.out.print("Does LHS = RHS?");
        LHS.print(18, 12);
        RHS.print(18, 12);
    }

    public Matrix getMaxEigenVector(Matrix matrix) {
return null;
    }
}
