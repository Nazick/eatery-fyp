package com.eatery.engine.utils;

import domain.Pair;

import java.util.*;

/**
 * Created by bruntha on 9/7/15.
 */
 public class Utility {
    static double factor = 1e4; // = 1 * 10^5 = 100000.

    public static void printArrayList(ArrayList<String> strings) {
        for (int i = 0; i < strings.size(); i++) {
            System.out.println(strings.get(i));
        }
    }

    public static void printHashTable(Hashtable<String, Integer> hashtable) {
        Set set = hashtable.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    public static void printHashTableSD(Hashtable<String, Double> hashtable) {
        Set set = hashtable.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            System.out.printf("%-20s %-20s \n",entry.getKey(),Math.round(Double.parseDouble(entry.getValue().toString()) * factor) / factor);
//            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

//    public static void printMatrix(ArrayList<String> stringArrayList,double[][] doubles) {
//        for (int i = 0; i <= stringArrayList.size(); i++) {
//            for (int j = 0; j <= stringArrayList.size(); j++) {
//                if (i == 0 && j == 0) {
//                    System.out.print("\t\t\t");
//                }else if(j==0 && i!=0){
//                    System.out.print(stringArrayList.get(i - 1) + "\t\t\t");
//                }else if (j!=0 && i!=0){
//                    System.out.print(doubles[i-1][j-1]+"\t\t\t");
//                }
//            }
//            System.out.println();
//        }
//    }

    public static void printMatrix(ArrayList<Pair> stringArrayList,double[][] doubles) {
        for (int i = 0; i <= stringArrayList.size(); i++) {
            for (int j = 0; j <= stringArrayList.size(); j++) {
                if (i == 0 && j == 0) {
                   System.out.printf("%-15s","");
                }else if(j==0 && i!=0){
                    System.out.printf("%-15s",stringArrayList.get(i - 1).getKey());
                }else if(j!=0 && i==0){
                    System.out.printf("%-15s",stringArrayList.get(j - 1).getKey());
                } else if (j!=0 && i!=0){
                    System.out.printf("%-15s",Math.round(doubles[i-1][j-1] * factor) / factor);
                }
            }
            System.out.println();
        }
    }

    public static void printMatrix(Hashtable<Character, ArrayList<Pair>> inputHashtableFL,Hashtable<String, double[][]> outputHashtableFL) {

        Set set = inputHashtableFL.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            printMatrix((ArrayList<Pair>)entry.getValue(),outputHashtableFL.get(entry.getKey().toString()));
        }

    }

    public static void printHashTableSD(Hashtable<Character, ArrayList<Pair>> inputHashTable, Hashtable<String, Double> weightsL2) {
        Set set1=inputHashTable.entrySet();
        Iterator it1=set1.iterator();

        while (it1.hasNext()) {
            Map.Entry entry1= (Map.Entry) it1.next();

            Set set = weightsL2.entrySet();
            Iterator it = set.iterator();
            System.out.println();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();

                if (entry1.getKey().toString().matches(entry.getKey().toString().substring(0, 1))) {
                    System.out.printf("%-20s %-20s \n",entry.getKey(),Math.round(Double.parseDouble(entry.getValue().toString()) * factor) / factor);
                }
//            System.out.println(entry.getKey() + " : " + entry.getValue());
            }

        }


    }
}
