package WeightingModel;

import Jama.Matrix;
import domain.Pair;
import edu.umbc.cs.maple.utils.JamaUtils;
import excel.Excel;
import model.WeightsEntity;
import org.apache.commons.lang.StringUtils;
import util.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by bruntha on 12/8/15.
 */
public class AHPM {
    static double factor = 1e4; // = 1 * 10^5 = 100000.

    String[] filePathToAnnFileArray = {"/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/u_1.ann",
            "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/u_2.ann",
            "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/u_3.ann",
            "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/u_4.ann",
            "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/u_5.ann",
            "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/u_9.ann",
            "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/u_11.ann",
            "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/u_12.ann",
            "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/u_13.ann",
            "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/u_14.ann",
    };
    Hashtable<String, Integer> aspectHashtable = new Hashtable<>();
    double[] ri = {0, 0, 0, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.51, 1.51, 1.51, 1.51};


    public List getWeights() {
        List listFinal = new ArrayList<>();
        try {
            getAspectCount(filePathToAnnFileArray);

            List list = calculateWeightsAPI("Restaurant", "", 0, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("Service", "S", 1, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("Worthiness", "W", 1, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("Ambience", "A", 1, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("Food", "F", 1, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("Offers", "O", 1, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("S_Staff", "S_Stf", 2, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("S_Delivery", "S_Del", 2, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("A_Entertainment", "A_Ent", 2, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("A_Furniture", "A_Fur", 2, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("A_Places", "A_Plc", 2, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("A_LocatedArea", "S_Stf", 2, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("F_FoodItem", "F_FI", 2, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("O_Payment", "O_Pay", 2, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("O_Reservation", "A_Env", 2, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("O_Experience", "O_Exp", 2, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("A_Environment", "A_Env", 2, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
            list = calculateWeightsAPI("A_Environment", "A_Env", 2, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listFinal;
    }

    public static void main(String args[]) {
        AHPM ahpm = new AHPM();
        ahpm.calculateWeights();

    }

    public void calculateWeights() {
        try {
            getAspectCount(filePathToAnnFileArray);
//            Utility.printHashTable(aspectHashtable);


            calculateWeights("Restaurant", "", 0, aspectHashtable);
            calculateWeights("Service", "S", 1, aspectHashtable);
            calculateWeights("Worthiness", "W", 1, aspectHashtable);
            calculateWeights("Ambience", "A", 1, aspectHashtable);
            calculateWeights("Food", "F", 1, aspectHashtable);
            calculateWeights("Offers", "O", 1, aspectHashtable);
            calculateWeights("S_Staff", "S_Stf", 2, aspectHashtable);
            calculateWeights("S_Delivery", "S_Del", 2, aspectHashtable);
            calculateWeights("A_Entertainment", "A_Ent", 2, aspectHashtable);
            calculateWeights("A_Furniture", "A_Fur", 2, aspectHashtable);
            calculateWeights("A_Places", "A_Plc", 2, aspectHashtable);
            calculateWeights("A_LocatedArea", "S_Stf", 2, aspectHashtable);
            calculateWeights("F_FoodItem", "F_FI", 2, aspectHashtable);
            calculateWeights("O_Payment", "O_Pay", 2, aspectHashtable);
            calculateWeights("O_Reservation", "A_Env", 2, aspectHashtable);
            calculateWeights("O_Experience", "O_Exp", 2, aspectHashtable);
            calculateWeights("A_Environment", "A_Env", 2, aspectHashtable);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateWeights(String heading, String prefix, int level, Hashtable<String, Integer> aspectHashtable) {
        System.out.println("************************************************************************************************************************************");
        ArrayList<Pair> arrayList = getVariableCount(aspectHashtable, level, heading, prefix);
        domain.Matrix matrix = buildMatrix(arrayList, heading);
        matrix.print();

//        double[][] vals = {{1, 3, 1 / 3}, {1 / 3, 1, 3}, {3, 1 / 3, 1}};
////        double[][] val = {{1, 2, 3, 9, 9}, {1 / 2.0, 1, 2, 3, 7}, {1 / 3.0, 1 / 2.0, 1, 2, 4}, {1 / 9.0, 1 / 3.0, 1 / 2.0, 1, 2}, {1 / 9.0, 1 / 7.0, 1 / 4.0, 1 / 2.0, 1}};
////        double[][] val = {{1,2,3,4,5}, {1 / 2.0, 1, 2, 2,2}, {1 / 3.0, 1 / 2.0, 1, 1,2}, {1/4.0,1/2.0,1,1,1}, {1/5.0,1/2.0,1/2.0,1,1}};
//        double[][] val = {{1,1,2,3,5}, {1,1,1,2,4}, {1/2.0,1,1,2,3}, {1/3.0,1/2.0,1/2.0,1,2}, {1/5.0,1/4.0,1/3.0,1/2.0,1}};
//        Matrix m = new Matrix(val);
//
//        m.print(5, 5);
//        double eigenMax = EigenValues.getMaxEigenValue(matrix);


        double eigenMax = EigenValues.getMaxEigenValue(matrix.getMatrix());
        double ci = (eigenMax - arrayList.size()) / (arrayList.size() - 1);
        double cr = ci * 100 / ri[arrayList.size()];
        System.out.println("Consistency = " + cr);

        Matrix matrixJama = getNormalizedMatrix(matrix.getMatrix());
        matrixJama = getRowSum(matrixJama);
        System.out.println("*********** Weights **************");
        Utility.print(arrayList, matrixJama);
        Excel.writeWeights(arrayList, matrixJama, heading);
        System.out.println("************************************************************************************************************************************");

    }

    private List calculateWeightsAPI(String heading, String prefix, int level, Hashtable<String, Integer> aspectHashtable) {
        List list = new ArrayList<>();

        ArrayList<Pair> arrayList = getVariableCount(aspectHashtable, level, heading, prefix);
        domain.Matrix matrix = buildMatrix(arrayList, heading);

        Matrix matrixJama = getNormalizedMatrix(matrix.getMatrix());
        matrixJama = getRowSum(matrixJama);

        for (int i = 0; i < arrayList.size(); i++) {
            list.add(new WeightsEntity(arrayList.get(i).getKey(), level, heading, Math.round(matrixJama.get(i, 0) * factor) / factor));
        }

        return list;
    }

    private ArrayList<Pair> getVariableCount(Hashtable<String, Integer> count, int level, String heading, String prefix) {
        ArrayList<Pair> arrayList = new ArrayList<>();

        if (heading.matches("Restaurant")) {
            Set set = count.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (!(entry.getKey().toString().matches("Restaurants")) && StringUtils.countMatches(entry.getKey().toString(), "_") == 0) {
                    if (!(entry.getKey().toString().matches("Opinion")))
                        arrayList.add(new Pair(entry.getKey().toString(), Integer.parseInt(entry.getValue().toString())));
                }
            }
        } else {
            Set set = count.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if ((StringUtils.countMatches(entry.getKey().toString(), "_") == level && prefix.matches(entry.getKey().toString().substring(0, prefix.length()))) || entry.getKey().toString().matches(heading)) {
                    arrayList.add(new Pair(entry.getKey().toString(), Integer.parseInt(entry.getValue().toString())));
                }
            }
        }

        if (heading.matches("Restaurant")) {
            Set set = count.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();

                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getKey().charAt(0) == entry.getKey().toString().charAt(0)) {
                        arrayList.get(i).addValue(Integer.parseInt(entry.getValue().toString()));
                    }
                }
            }
        } else if (level != 2) {
            Set set = count.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();

                if (!heading.matches(entry.getKey().toString())) {

                    int index = 0;
                    if (level == 1)
                        index = entry.getKey().toString().indexOf('_');


                    for (int i = 0; i < arrayList.size(); i++) {

                        if (!entry.getKey().toString().matches(arrayList.get(i).getKey())) {

                            int indexA = 0;
                            if (level == 1)
                                indexA = entry.getKey().toString().indexOf('_');

                            if ((entry.getKey().toString().substring(0, index + 2)).matches(arrayList.get(i).getKey().substring(0, indexA + 2))) {
//                                System.out.println( arrayList.get(i).getKey()+ " " + entry.getKey());
                                arrayList.get(i).addValue(Integer.parseInt(entry.getValue().toString()));
                            }
                        }
                    }
                }
            }
        }


//        Utility.print(arrayList);
        return arrayList;
    }

    private domain.Matrix buildMatrix(ArrayList<Pair> arrayList, String heading) {
        domain.Matrix matrixResult = new domain.Matrix();

        ArrayList<String> variables = new ArrayList<>();
        Matrix matrixLocal = new Matrix(arrayList.size(), arrayList.size());


        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 0; j < arrayList.size(); j++) {
                matrixLocal.set(i, j, arrayList.get(i).getValue() / (1.00 * arrayList.get(j).getValue()));
                if (i == 0)
                    variables.add(arrayList.get(j).getKey());
            }
        }

        matrixResult.setHeading(heading);
        matrixResult.setVariables(variables);
        matrixResult.setMatrix(matrixLocal);
        return matrixResult;
    }


    private void getAspectCount(String[] filePath) throws IOException {
        for (int i = 0; i < filePath.length; i++) {
            getAspectCount(filePath[i]);
        }
    }

    private void getAspectCount(String filePath) throws IOException {
        File fileAnnotation = new File(filePath);
        FileReader fr = new FileReader(fileAnnotation);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            String[] dic = line.split("[ \t]");
            if (dic[1].charAt(0) != 'P') {
                if (!aspectHashtable.containsKey(dic[1])) {
                    aspectHashtable.put(dic[1], 1);
                } else {
                    aspectHashtable.put(dic[1], aspectHashtable.get(dic[1]) + 1);
                }
            }

        }
        br.close();
        fr.close();

    }

    private Matrix getNormalizedMatrix(Matrix matrix) {

        Matrix colsum = JamaUtils.colsum(matrix);

        for (int i = 0; i < matrix.getColumnDimension(); i++) {
            for (int j = 0; j < matrix.getRowDimension(); j++) {
                matrix.set(i, j, matrix.get(i, j) / colsum.get(0, j));
            }
        }
        return matrix;
    }

    private Matrix getRowSum(Matrix matrix) {
        Matrix matrixResult = JamaUtils.rowsum(matrix);

        Double sum = JamaUtils.colsum(matrixResult).get(0, 0);


        for (int i = 0; i < matrixResult.getRowDimension(); i++) {
            double temp = matrixResult.get(i, 0);
            matrixResult.set(i, 0, temp / sum);
        }
        return matrixResult;
    }


    public void test() {
        double[][] vals = {{1, -2}, {-2, 0}};
        Matrix m = new Matrix(vals);
        m.print(5, 5);

        m = getNormalizedMatrix(m);
        m.print(5, 5);
        getRowSum(m).print(5, 5);
    }

    private void separateFirstLevelAspects() {
        Set set = aspectHashtable.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (!(entry.getKey().toString().matches("Restaurant") || entry.getKey().toString().matches("Restaurants")) && StringUtils.countMatches(entry.getKey().toString(), "_") == 0) {

            }
        }
    }
}
