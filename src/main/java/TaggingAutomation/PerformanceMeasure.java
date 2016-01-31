package TaggingAutomation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by bruntha on 11/11/15.
 */
public class PerformanceMeasure {
    Hashtable<String, String> autoTagged;
    Hashtable<String, String> manuallyTagged;
    String filePathAnnAuto;
    String filePathAnnManual;
    int taggedWords;
    int correctTaggs;
    int totalTaggs;

    public static void main(String args[]) {
        PerformanceMeasure performanceMeasure = new PerformanceMeasure();
        performanceMeasure.measurePerformanceAll();
    }

    public void measurePerformanceAll() {
        for (int i = 0; i < 14; i++) {
            initialize();
            System.out.println((i+1) + "**************************");
            filePathAnnAuto = "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/" +
                    "u_" + (i + 1) + "_A.ann";  //manually tagged ann file

            filePathAnnManual = "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/" +
                    "u_" + (i + 1) + ".ann";  //manually tagged ann file

            measurePerformance();
        }
    }

    private void initialize() {
        autoTagged=new Hashtable<>();
        manuallyTagged=new Hashtable<>();
        taggedWords=0;
        correctTaggs=0;
        totalTaggs=0;
    }

    public void measurePerformance() {
        try {
            readAnnFile(filePathAnnAuto, autoTagged);
            readAnnFile(filePathAnnManual, manuallyTagged);
            totalTaggs = manuallyTagged.size();
            taggedWords = autoTagged.size();
            compare(autoTagged, manuallyTagged);

            double precision = correctTaggs / (1.00 * taggedWords);
            double recall = correctTaggs / (1.00 * totalTaggs);

            System.out.println("Total tags = " + totalTaggs);
            System.out.println("Automatically tagged words = " + taggedWords);
            System.out.println("Correct tags = " + correctTaggs);
            System.out.println("Precision = " + precision);
            System.out.println("Recall = " + recall);
            System.out.println("F Measure = " + 2 * precision * recall / (precision + recall));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAnnFile(String file, Hashtable<String, String> hashtable) throws IOException {
        File fileAnnotation = new File(file);
        FileReader fr = new FileReader(fileAnnotation);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            String[] annotations = line.split("[ \t]");

            hashtable.put(annotations[2] + " " + annotations[3] + " " + annotations[4], annotations[1]);


        }
        br.close();
        fr.close();
    }

    private void compare(Hashtable<String, String> hashtable, Hashtable<String, String> hashtable2) throws IOException {

        Set set = hashtable.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();

            if (hashtable2.containsKey(entry.getKey())) {
                if (entry.getValue().equals(hashtable2.get(entry.getKey()))) {
                    correctTaggs++;
                }
            }
        }
    }
}
