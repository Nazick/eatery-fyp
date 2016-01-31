package WeightingModel;

import Jama.Matrix;
import domain.Pair;
import edu.umbc.cs.maple.utils.JamaUtils;
import excel.Excel;
import model.WeightsEntity;
import util.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by bruntha on 12/8/15.
 */
public class AHPM {
    static double factor = 1e4; // = 1 * 10^5 = 100000.
    String[] filePathToAnnFileArray = {"refinedReviews/u_1.ann",
            "refinedReviews/u_2.ann",
            "refinedReviews/u_3.ann",
            "refinedReviews/u_4.ann",
            "refinedReviews/u_5.ann",
            "refinedReviews/u_6.ann",
            "refinedReviews/u_7.ann",
            "refinedReviews/u_8.ann",
            "refinedReviews/u_9.ann",
            "refinedReviews/u_11.ann",
            "refinedReviews/u_12.ann",
            "refinedReviews/u_13.ann",
            "refinedReviews/u_14.ann",
    };
    Hashtable<String, Integer> aspectHashtable = new Hashtable<>();
    double[] ri = {0, 0, 0, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.51, 1.51, 1.51, 1.51};

    public List getWeights() {
        List listFinal = new ArrayList<>();
        try {
            getAspectCount(filePathToAnnFileArray);
            Utility.print(aspectHashtable);
            List list;
            list = calculateWeightsAHP2("Restaurant", "", 0, aspectHashtable);
            for (int i = 0; i < list.size(); i++) {
                listFinal.add(list.get(i));
            }
//            list = calculateWeightsAHP("Service", "S", 1, aspectHashtable);
//            for (int i = 0; i < list.size(); i++) {
//                listFinal.add(list.get(i));
//            }
//            list = calculateWeightsAHP("Worthiness", "W", 1, aspectHashtable);
//            for (int i = 0; i < list.size(); i++) {
//                listFinal.add(list.get(i));
//            }
//            list = calculateWeightsAHP("Ambience", "A", 1, aspectHashtable);
//            for (int i = 0; i < list.size(); i++) {
//                listFinal.add(list.get(i));
//            }
//            list = calculateWeightsAHP("Food", "F", 1, aspectHashtable);
//            for (int i = 0; i < list.size(); i++) {
//                listFinal.add(list.get(i));
//            }
//            list = calculateWeightsAHP("Others", "O", 1, aspectHashtable);
//            for (int i = 0; i < list.size(); i++) {
//                listFinal.add(list.get(i));
//            }
//            list = calculateWeightsAHP("S_Staff", "S_Stf", 2, aspectHashtable);
//            for (int i = 0; i < list.size(); i++) {
//                listFinal.add(list.get(i));
//            }
//            list = calculateWeightsAHP("S_Delivery", "S_Del", 2, aspectHashtable);
//            for (int i = 0; i < list.size(); i++) {
//                listFinal.add(list.get(i));
//            }
//            list = calculateWeightsAHP("A_Entertainment", "A_Ent", 2, aspectHashtable);
//            for (int i = 0; i < list.size(); i++) {
//                listFinal.add(list.get(i));
//            }
//            list = calculateWeightsAHP("A_Furniture", "A_Fur", 2, aspectHashtable);
//            for (int i = 0; i < list.size(); i++) {
//                listFinal.add(list.get(i));
//            }
//            list = calculateWeightsAHP("A_Places", "A_Plc", 2, aspectHashtable);
//            for (int i = 0; i < list.size(); i++) {
//                listFinal.add(list.get(i));
//            }
//            list = calculateWeightsAHP("F_FoodItem", "F_FI", 2, aspectHashtable);
//            for (int i = 0; i < list.size(); i++) {
//                listFinal.add(list.get(i));
//            }
//            list = calculateWeightsAHP("O_Payment", "O_Pay", 2, aspectHashtable);
//            for (int i = 0; i < list.size(); i++) {
//                listFinal.add(list.get(i));
//            }
//            list = calculateWeightsAHP("O_Experience", "O_Exp", 2, aspectHashtable);
//            for (int i = 0; i < list.size(); i++) {
//                listFinal.add(list.get(i));
//            }
            list = calculateWeightsAHP2("A_Environment", "A_Env", 2, aspectHashtable);
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
        ahpm.getWeights();

    }


    private void calculateWeights(String heading, String prefix, int level, Hashtable<String, Integer> aspectHashtable) {
        System.out.println("************************************************************************************************************************************");
        ArrayList<Pair> arrayList = getVariableCount(aspectHashtable, heading);
        domain.Matrix matrix = buildMatrix(arrayList, heading);
        matrix.print();

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

    private List calculateWeightsAHP(String heading, String prefix, int level, Hashtable<String, Integer> aspectHashtable) {
        System.out.println(heading + " " + "************************************************************************************************************************************");

        List list = new ArrayList<>();

        ArrayList<Pair> arrayList = getVariableCount(aspectHashtable, heading);
        domain.Matrix matrix = buildMatrix(arrayList, heading);
        System.out.println("Initial Matrix");
        matrix.getMatrix().print(5, 5);

        double eigenMax = EigenValues.getMaxEigenValue(matrix.getMatrix());
        double ci = (eigenMax - arrayList.size()) / (arrayList.size() - 1);
        double cr = ci * 100 / ri[arrayList.size()];
        System.out.println("Consistency = " + cr);

        Matrix matrixJama = getNormalizedMatrix(matrix.getMatrix());
        System.out.println("Normalized Matrix");
        matrixJama.print(5, 5);
        matrixJama = getRowSum(matrixJama);

        System.out.println("Result Matrix");
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println(arrayList.get(i).getKey() + "\t" + Math.round(matrixJama.get(i, 0) * factor) / factor);
            list.add(new WeightsEntity(arrayList.get(i).getKey(), level, heading, Math.round(matrixJama.get(i, 0) * factor) / factor));
        }

        System.out.println("************************************************************************************************************************************");
        return list;
    }

    private List calculateWeightsAHP3(String heading, String prefix, int level, Hashtable<String, Integer> aspectHashtable) {
        System.out.println(heading + " " + "************************************************************************************************************************************");

        List list = new ArrayList<>();

        ArrayList<Pair> arrayList = getVariableCount(aspectHashtable, heading);
        domain.Matrix matrix = buildMatrix(arrayList, heading);
        System.out.println("Initial Matrix");
        matrix.getMatrix().print(5, 5);

        double eigenMax = EigenValues.getMaxEigenValue(matrix.getMatrix());
        double ci = (eigenMax - arrayList.size()) / (arrayList.size() - 1);
        double cr = ci * 100 / ri[arrayList.size()];
        System.out.println("Consistency = " + cr);

        Matrix matrixJama = getNormalizedMatrix(matrix.getMatrix());
        System.out.println("Normalized Matrix");
        matrixJama.print(5, 5);
        matrixJama = getRowMulSqrt(matrixJama);

        System.out.println("Result Matrix");
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println(arrayList.get(i).getKey() + "\t" + Math.round(matrixJama.get(i, 0) * factor) / factor);
            list.add(new WeightsEntity(arrayList.get(i).getKey(), level, heading, Math.round(matrixJama.get(i, 0) * factor) / factor));
        }

        System.out.println("************************************************************************************************************************************");
        return list;
    }

    private List calculateWeightsAHP2(String heading, String prefix, int level, Hashtable<String, Integer> aspectHashtable) {
        System.out.println(heading + " " + "************************************************************************************************************************************");

        List list = new ArrayList<>();

        ArrayList<Pair> arrayList = getVariableCount(aspectHashtable, heading);
        domain.Matrix matrix = buildMatrix(arrayList, heading);
        System.out.println("Initial Matrix");
        matrix.getMatrix().print(5, 5);

        double eigenMax = EigenValues.getMaxEigenValue(matrix.getMatrix());
        double ci = (eigenMax - arrayList.size()) / (arrayList.size() - 1);
        double cr = ci * 100 / ri[arrayList.size()];
        System.out.println("Consistency = " + cr);

//        Matrix matrixJama = getNormalizedMatrix(matrix.getMatrix());
//        System.out.println("Normalized Matrix");
//        matrixJama.print(5, 5);
        Matrix matrixJama = EigenValues.getMaxEigenVector(matrix.getMatrix());
        matrixJama=getNormalizedMatrix(matrixJama);

        System.out.println("Result Matrix");
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println(arrayList.get(i).getKey() + "\t" + Math.round(matrixJama.get(i, 0) * factor) / factor);
            list.add(new WeightsEntity(arrayList.get(i).getKey(), level, heading, Math.round(matrixJama.get(i, 0) * factor) / factor));
        }

        System.out.println("************************************************************************************************************************************");
        return list;
    }

    private Matrix getRowMulSqrt(Matrix matrixJama) {
        Matrix matrixResult = JamaUtils.rowsum(matrixJama);

        double colMul = 1;

        for (int i = 0; i < matrixResult.getRowDimension(); i++) {
            for (int j = 0; j < matrixResult.getColumnDimension(); j++) {
                colMul = colMul * matrixJama.get(i, j);
            }
            matrixResult.set(i, 0, colMul / Math.pow(colMul, 1 / matrixJama.getRowDimension()));
        }
        return matrixResult;
    }


    private ArrayList<Pair> getVariableCount(Hashtable<String, Integer> count, String heading) {
        ArrayList<Pair> arrayList = new ArrayList<>();

        int total_F_FoodItem = 0;
        total_F_FoodItem += aspectHashtable.get("F_FI_Taste");
        total_F_FoodItem += aspectHashtable.get("F_FI_Price");
        total_F_FoodItem += aspectHashtable.get("F_FI_Quality");
        total_F_FoodItem += aspectHashtable.get("F_FI_Healthy");
        total_F_FoodItem += aspectHashtable.get("F_FI_CookingLevel");
        total_F_FoodItem += aspectHashtable.get("F_FI_Size");
        total_F_FoodItem += aspectHashtable.get("F_FI_Religious");
        total_F_FoodItem += aspectHashtable.get("F_FoodItem");
        if (heading.matches("F_FoodItem")) {
            arrayList.add(new Pair("F_FI_Taste", aspectHashtable.get("F_FI_Taste")));
            arrayList.add(new Pair("F_FI_Price", aspectHashtable.get("F_FI_Price")));
            arrayList.add(new Pair("F_FI_Quality", aspectHashtable.get("F_FI_Quality")));
            arrayList.add(new Pair("F_FI_Healthy", aspectHashtable.get("F_FI_Healthy")));
            arrayList.add(new Pair("F_FI_CookingLevel", aspectHashtable.get("F_FI_CookingLevel")));
            arrayList.add(new Pair("F_FI_Size", aspectHashtable.get("F_FI_Size")));
            arrayList.add(new Pair("F_FI_Religious", aspectHashtable.get("F_FI_Religious")));
            arrayList.add(new Pair("F_FoodItem", aspectHashtable.get("F_FoodItem")));
        }

        int total_S_Staff = 0;
        total_S_Staff += aspectHashtable.get("S_Staff");
        total_S_Staff += aspectHashtable.get("S_Stf_Behavior");
        total_S_Staff += aspectHashtable.get("S_Stf_Experience");
        total_S_Staff += aspectHashtable.get("S_Stf_Availability");
        total_S_Staff += aspectHashtable.get("S_Stf_Appearance");
        if (heading.matches("S_Staff")) {
            arrayList.add(new Pair("S_Staff", aspectHashtable.get("S_Staff")));
            arrayList.add(new Pair("S_Stf_Behavior", aspectHashtable.get("S_Stf_Behavior")));
            arrayList.add(new Pair("S_Stf_Experience", aspectHashtable.get("S_Stf_Experience")));
            arrayList.add(new Pair("S_Stf_Availability", aspectHashtable.get("S_Stf_Availability")));
            arrayList.add(new Pair("S_Stf_Appearance", aspectHashtable.get("S_Stf_Appearance")));
        }

        int total_S_Delivery = 0;
        total_S_Delivery += aspectHashtable.get("S_Delivery");
        total_S_Delivery += aspectHashtable.get("S_Del_Time");
        total_S_Delivery += aspectHashtable.get("S_Del_OrderingMethod");
        if (heading.matches("S_Delivery")) {
            arrayList.add(new Pair("S_Delivery", aspectHashtable.get("S_Delivery")));
            arrayList.add(new Pair("S_Del_Time", aspectHashtable.get("S_Del_Time")));
            arrayList.add(new Pair("S_Del_OrderingMethod", aspectHashtable.get("S_Del_OrderingMethod")));
        }

        int total_A_Furniture = 0;
        total_A_Furniture += aspectHashtable.get("A_Furniture");
        total_A_Furniture += aspectHashtable.get("A_Fur_Table");
        total_A_Furniture += aspectHashtable.get("A_Fur_Door");
        if (heading.matches("A_Furniture")) {
            arrayList.add(new Pair("A_Furniture", aspectHashtable.get("A_Furniture")));
            arrayList.add(new Pair("A_Fur_Table", aspectHashtable.get("A_Fur_Table")));
            arrayList.add(new Pair("A_Fur_Door", aspectHashtable.get("A_Fur_Door")));
        }

        int total_A_Entertainment = 0;
        total_A_Entertainment += aspectHashtable.get("A_Entertainment");
        total_A_Entertainment += aspectHashtable.get("A_Ent_Music");
        total_A_Entertainment += aspectHashtable.get("A_Ent_LiveShow");
        total_A_Entertainment += aspectHashtable.get("A_Ent_Tv");
        if (heading.matches("A_Entertainment")) {
            arrayList.add(new Pair("A_Entertainment", aspectHashtable.get("A_Entertainment")));
            arrayList.add(new Pair("A_Ent_Music", aspectHashtable.get("A_Ent_Music")));
            arrayList.add(new Pair("A_Ent_LiveShow", aspectHashtable.get("A_Ent_LiveShow")));
            arrayList.add(new Pair("A_Ent_Tv", aspectHashtable.get("A_Ent_Tv")));
        }

        int total_A_Environment = 0;
        total_A_Environment += aspectHashtable.get("A_Environment");
        total_A_Environment += aspectHashtable.get("A_Env_Size");
        total_A_Environment += aspectHashtable.get("A_Env_Type");
        total_A_Environment += aspectHashtable.get("A_Env_AC");
        if (heading.matches("A_Environment")) {
            arrayList.add(new Pair("A_Environment", aspectHashtable.get("A_Environment")));
            arrayList.add(new Pair("A_Env_Size", aspectHashtable.get("A_Env_Size")));
            arrayList.add(new Pair("A_Env_Type", aspectHashtable.get("A_Env_Type")));
            arrayList.add(new Pair("A_Env_AC", aspectHashtable.get("A_Env_AC")));
        }

        int total_A_Places = 0;
        total_A_Places += aspectHashtable.get("A_Places");
        total_A_Places += aspectHashtable.get("A_Plc_Bathroom");
        total_A_Places += aspectHashtable.get("A_Plc_SmokingArea");
//        total_A_Places += aspectHashtable.get("A_Plc_Buffet");
        total_A_Places += aspectHashtable.get("A_Plc_Bar");
        total_A_Places += aspectHashtable.get("A_Plc_Patio");
        total_A_Places += aspectHashtable.get("A_Plc_DiningRoom");
        total_A_Places += aspectHashtable.get("A_Plc_RestRoom");
        total_A_Places += aspectHashtable.get("A_Plc_Kitchen");
        if (heading.matches("A_Places")) {
            arrayList.add(new Pair("A_Places", aspectHashtable.get("A_Places")));
            arrayList.add(new Pair("A_Plc_Bathroom", aspectHashtable.get("A_Plc_Bathroom")));
            arrayList.add(new Pair("A_Plc_SmokingArea", aspectHashtable.get("A_Plc_SmokingArea")));
//            arrayList.add(new Pair("A_Plc_Buffet", aspectHashtable.get("A_Plc_Buffet")));
            arrayList.add(new Pair("A_Plc_Bar", aspectHashtable.get("A_Plc_Bar")));
            arrayList.add(new Pair("A_Plc_Patio", aspectHashtable.get("A_Plc_Patio")));
            arrayList.add(new Pair("A_Plc_DiningRoom", aspectHashtable.get("A_Plc_DiningRoom")));
            arrayList.add(new Pair("A_Plc_RestRoom", aspectHashtable.get("A_Plc_RestRoom")));
            arrayList.add(new Pair("A_Plc_Kitchen", aspectHashtable.get("A_Plc_Kitchen")));
        }

        int total_O_Payment = 0;
        total_O_Payment += aspectHashtable.get("O_Payment");
        total_O_Payment += aspectHashtable.get("O_Pay_Method");
        total_O_Payment += aspectHashtable.get("O_Pay_Price");
        if (heading.matches("O_Payment")) {
            arrayList.add(new Pair("O_Payment", aspectHashtable.get("O_Payment")));
            arrayList.add(new Pair("O_Pay_Method", aspectHashtable.get("O_Pay_Method")));
            arrayList.add(new Pair("O_Pay_Price", aspectHashtable.get("O_Pay_Price")));
        }

        int total_O_Experience = 0;
        total_O_Experience += aspectHashtable.get("O_Experience");
        total_O_Experience += aspectHashtable.get("O_Exp_StarsByCus");
        if (heading.matches("O_Experience")) {
            arrayList.add(new Pair("O_Experience", aspectHashtable.get("O_Experience")));
            arrayList.add(new Pair("O_Exp_StarsByCus", aspectHashtable.get("O_Exp_StarsByCus")));
        }

        int total_Food = 0;
        total_Food += aspectHashtable.get("Food");
        total_Food += aspectHashtable.get("F_Appetizer");
        total_Food += aspectHashtable.get("F_Dessert");
        total_Food += aspectHashtable.get("F_Drinks");
        total_Food += total_F_FoodItem;
        total_Food += aspectHashtable.get("F_Ingredients");
        if (heading.matches("Food")) {
            arrayList.add(new Pair("Food", aspectHashtable.get("Food")));
            arrayList.add(new Pair("F_Appetizer", aspectHashtable.get("F_Appetizer")));
            arrayList.add(new Pair("F_Dessert", aspectHashtable.get("F_Dessert")));
            arrayList.add(new Pair("F_Drinks", aspectHashtable.get("F_Drinks")));
            arrayList.add(new Pair("F_FoodItem", total_F_FoodItem));
            arrayList.add(new Pair("F_Ingredients", aspectHashtable.get("F_Ingredients")));
        }

        int total_Service = 0;
        total_Service += aspectHashtable.get("Service");
        total_Service += aspectHashtable.get("S_Menu");
        total_Service += aspectHashtable.get("S_OpenHours");
        total_Service += aspectHashtable.get("S_Parking");
        total_Service += aspectHashtable.get("S_Gift");
//        total_Service += aspectHashtable.get("S_PetsAllowed");
        total_Service += aspectHashtable.get("S_Accessibility");
        total_Service += aspectHashtable.get("S_Wifi");
        total_Service += aspectHashtable.get("S_Cutlery");
        total_Service += aspectHashtable.get("S_Seating");
        total_Service += total_S_Staff;
        total_Service += total_S_Delivery;
        if (heading.matches("Service")) {
            arrayList.add(new Pair("Service", aspectHashtable.get("Service")));
            arrayList.add(new Pair("S_Menu", aspectHashtable.get("S_Menu")));
            arrayList.add(new Pair("S_OpenHours", aspectHashtable.get("S_OpenHours")));
            arrayList.add(new Pair("S_Parking", aspectHashtable.get("S_Parking")));
            arrayList.add(new Pair("S_Gift", aspectHashtable.get("S_Gift")));
//            arrayList.add(new Pair("S_PetsAllowed", aspectHashtable.get("S_PetsAllowed")));
            arrayList.add(new Pair("S_Accessibility", aspectHashtable.get("S_Accessibility")));
            arrayList.add(new Pair("S_Wifi", aspectHashtable.get("S_Wifi")));
            arrayList.add(new Pair("S_Cutlery", aspectHashtable.get("S_Cutlery")));
            arrayList.add(new Pair("S_Seating", aspectHashtable.get("S_Seating")));
            arrayList.add(new Pair("S_Staff", total_S_Staff));
            arrayList.add(new Pair("S_Delivery", total_S_Delivery));
        }

        int total_Ambience = 0;
        total_Ambience += aspectHashtable.get("Ambience");
        total_Ambience += aspectHashtable.get("A_Decor");
        total_Ambience += aspectHashtable.get("A_OutsideView");
        total_Ambience += aspectHashtable.get("A_LocatedArea");
        total_Service += total_A_Entertainment;
        total_Service += total_A_Environment;
        total_Service += total_A_Places;
        total_Service += total_A_Furniture;
        if (heading.matches("Ambience")) {
            arrayList.add(new Pair("Ambience", aspectHashtable.get("Ambience")));
            arrayList.add(new Pair("A_Decor", aspectHashtable.get("A_Decor")));
            arrayList.add(new Pair("A_OutsideView", aspectHashtable.get("A_OutsideView")));
            arrayList.add(new Pair("A_LocatedArea", aspectHashtable.get("A_LocatedArea")));
            arrayList.add(new Pair("A_Entertainment", total_A_Entertainment));
            arrayList.add(new Pair("A_Environment", total_A_Environment));
            arrayList.add(new Pair("A_Places", total_A_Places));
            arrayList.add(new Pair("A_Furniture", total_A_Furniture));
        }

        int total_Offers = 0;
        total_Offers += aspectHashtable.get("Offers");
        if (heading.matches("Offers")) {
            arrayList.add(new Pair("Offers", aspectHashtable.get("Offers")));
        }

        int total_Worthiness = 0;
        total_Worthiness += aspectHashtable.get("Worthiness");
        total_Worthiness += aspectHashtable.get("W_Price");
        total_Worthiness += aspectHashtable.get("W_Waiting");
        if (heading.matches("Worthiness")) {
            arrayList.add(new Pair("Worthiness", aspectHashtable.get("Worthiness")));
            arrayList.add(new Pair("W_Price", aspectHashtable.get("W_Price")));
            arrayList.add(new Pair("W_Waiting", aspectHashtable.get("W_Waiting")));
        }

        int total_Others = 0;
//        total_Others += aspectHashtable.get("Others");
        total_Others += aspectHashtable.get("O_Reservation");
        total_Others += total_O_Payment;
        total_Others += total_O_Experience;
        if (heading.matches("Others")) {
//            arrayList.add(new Pair("Others", aspectHashtable.get("Others")));
            arrayList.add(new Pair("O_Reservation", aspectHashtable.get("O_Reservation")));
            arrayList.add(new Pair("O_Payment", total_O_Payment));
            arrayList.add(new Pair("O_Experience", total_O_Experience));
        }

//        int total_Restaurant = 0;
//        total_Restaurant += total_Food;
//        total_Restaurant += total_Service;
//        total_Restaurant += total_Ambience;
//        total_Restaurant += total_Offers;
//        total_Restaurant += total_Worthiness;
//        total_Restaurant += total_Others;
        if (heading.matches("Restaurant")) {
            arrayList.add(new Pair("Restaurant", aspectHashtable.get("Restaurant")));
            arrayList.add(new Pair("Food", total_Food));
            arrayList.add(new Pair("Service", total_Service));
            arrayList.add(new Pair("Ambience", total_Ambience));
            arrayList.add(new Pair("Offers", total_Offers));
            arrayList.add(new Pair("Worthiness", total_Worthiness));
            arrayList.add(new Pair("Others", total_Others));
        }

        return arrayList;
    }

    private domain.Matrix buildMatrix(ArrayList<Pair> arrayList, String heading) {
        domain.Matrix matrixResult = new domain.Matrix();

        ArrayList<String> variables = new ArrayList<>();
        Matrix matrixLocal = new Matrix(arrayList.size(), arrayList.size());

//        System.out.println(heading);
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println(arrayList.get(i).getKey() + " " + arrayList.get(i).getValue());
            for (int j = 0; j < arrayList.size(); j++) {
                matrixLocal.set(i, j, arrayList.get(i).getValue() / (1.00 * arrayList.get(j).getValue()));
                if (i == 0)
                    variables.add(arrayList.get(j).getKey());
            }
        }
//        System.out.println("**************************");
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
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filePath).getFile());
        FileReader fr = new FileReader(file);
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
        Matrix resultMatrix = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension());
        Matrix colSum = JamaUtils.colsum(matrix);
        System.out.println("Column Sum");
        colSum.print(5, 5);

        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                resultMatrix.set(i, j, matrix.get(i, j) / colSum.get(0, j));
            }
        }
        return resultMatrix;
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

}
