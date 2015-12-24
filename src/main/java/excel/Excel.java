package excel;

import Jama.Matrix;
import domain.Pair;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by anirudh on 23/10/14.
 */
public class Excel {
    private static final String FILE_PATH = "Data/weights.xlsx";
    //We are making use of a single instance to prevent multiple write access to same file.
    private static final Excel INSTANCE = new Excel();
    static double factor = 1e4; // = 1 * 10^5 = 100000.

    public static Excel getInstance() {
        return INSTANCE;
    }

    private Excel() {
    }


//    public static void writeStudentsListToExcel(List studentList){
//
//        // Using XSSF for xlsx format, for xls use HSSF
//        Workbook workbook = new XSSFWorkbook();
//
//        Sheet studentsSheet = workbook.createSheet("Students");
//
//        int rowIndex = 0;
//        for(Student student : studentList){
//            Row row = studentsSheet.createRow(rowIndex++);
//            int cellIndex = 0;
//            //first place in row is name
//            row.createCell(cellIndex++).setCellValue(student.getName());
//
//            //second place in row is marks in maths
//            row.createCell(cellIndex++).setCellValue(student.getMaths());
//
//            //third place in row is marks in Science
//            row.createCell(cellIndex++).setCellValue(student.getScience());
//
//            //fourth place in row is marks in English
//            row.createCell(cellIndex++).setCellValue(student.getEnglish());
//
//        }
//
//        //write this workbook in excel file.
//        try {
//            FileOutputStream fos = new FileOutputStream(FILE_PATH);
//            workbook.write(fos);
//            fos.close();
//
//            System.out.println(FILE_PATH + " is successfully written");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }

    public static void writeWeights(ArrayList<Pair> pairs, Matrix matrix,String heading){

        // Using XSSF for xlsx format, for xls use HSSF
        Workbook workbook = new XSSFWorkbook();

        Sheet studentsSheet = workbook.createSheet(heading);


        for (int i = 0; i <pairs.size(); i++) {

            Row row = studentsSheet.createRow(i);
            int cellIndex = 0;
            row.createCell(cellIndex++).setCellValue(pairs.get(i).getKey());

            row.createCell(cellIndex++).setCellValue(Math.round(matrix.get(i,0) * factor) / factor);

        }


        //write this workbook in excel file.
        try {
            FileOutputStream fos = new FileOutputStream("Data/"+heading+".xlsx",true);
            workbook.write(fos);
            fos.close();

            System.out.println(FILE_PATH + " is successfully written");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
