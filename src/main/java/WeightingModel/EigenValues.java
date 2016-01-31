package WeightingModel;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * Created by bruntha on 12/8/15.
 */
public class EigenValues {
    public static void main(String args[]) {
        double[][] vals = {{1, -2}, {-2, 0}};
        Matrix m = new Matrix(vals);
//        getMaxEigenValue(m);
        getMaxEigenVector(m);
    }

    public void test() {
        final int M = 2;                // Matrix size (square)

        /*Matrix m = new Matrix(M,M);   // Create matrix


        //           Fill with random numbers using Math.random()
        //           making a symmetric matrix matrix
        for(int i = 0; i < M; i++) {
            for(int j = 0; j <= i; j++) {
                double val = Math.random();
                m.set(i,j,val);
                m.set(j,i,val);
            }
        }*/

        double[][] vals = {{1, -2}, {-2, 0}};
        Matrix m = new Matrix(vals);

        //              Print out initial matrix to System.out on
        //              cole of with 8 with 4 sig figs
        System.out.println("Initial Radom Matrix is:");
        m.print(8, 4);


        //                  Get the Eigen value decomposition
        EigenvalueDecomposition eigen = m.eig();

        double[] realPart = eigen.getRealEigenvalues();
        double[] imagPart = eigen.getImagEigenvalues();

        for (int i = 0; i < realPart.length; i++) {
            System.out.println("Eigen Value " + i + " is " +
                    "[" + realPart[i] + ", " +
                    +imagPart[i] + "]");
        }


        //                 Get the block diagonal matrix of
        //                 Eigen values
        Matrix d = eigen.getD();
        System.out.println("Diagonal matrix of Eigen values is:");
        d.print(8, 4);


        Matrix evectors = eigen.getV();

        System.out.println("Matrix of Eigen Vectors is:");
        evectors.print(8, 4);

        //           Get transpose of evectors
        Matrix trans = evectors.transpose();

        //           Form trans*evectors (which should be unit matrix)
        Matrix u = evectors.times(trans);

        System.out.println("Matrix of trans * evectors is :");
        u.print(8, 4);
    }

    public static double getMaxEigenValue(Matrix matrix) {
        double max = 0;
        EigenvalueDecomposition eigen = matrix.eig();

        double[] realPart = eigen.getRealEigenvalues();
        double[] imagPart = eigen.getImagEigenvalues();

        for (int i = 0; i < realPart.length; i++) {
            if (max < realPart[i])
                max = realPart[i];
        }
        System.out.println("Max Eigen Value = " + max);
        return max;
    }

    private static int getMaxEigenIndex(Matrix matrix) {
        int result = 0;
        double max = 0;

        EigenvalueDecomposition eigen = matrix.eig();

        double[] realPart = eigen.getRealEigenvalues();
        double[] imagPart = eigen.getImagEigenvalues();

        for (int i = 0; i < realPart.length; i++) {
            if (max < realPart[i])
                max = realPart[i];
        }

        for (int i = 0; i < realPart.length; i++) {
            if (max == realPart[i])
                result = i;
        }
        System.out.println("Max Eigen Value = " + max);
        return result;
    }

    public static Matrix getMaxEigenVector(Matrix matrix) {
        EigenvalueDecomposition E = new EigenvalueDecomposition(matrix);
        double[] lambdaRe = E.getRealEigenvalues();                                      //  Real eigens
        double[] lambdaIm = E.getImagEigenvalues();                                      //  Imag eigens
        System.out.println("Eigenvalues: \t lambda.Re[] = " + lambdaRe[0] + ", " + lambdaRe[1]);
        Matrix V = E.getV();                                      //  Get matrix of eigenvectors
        System.out.print("\n Matrix with column eigenvectors ");
        V.print(10, 5);

        int index=getMaxEigenIndex(matrix);
        Matrix Vec = new Matrix(matrix.getColumnDimension(), 1);                                      //  Extract single eigenvector
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            Vec.set(i,0,V.get(i,index));
        }

        System.out.print("Eigen Vector with max Eigen value");
        Vec.print(10, 5);

        return Vec;
    }
}
