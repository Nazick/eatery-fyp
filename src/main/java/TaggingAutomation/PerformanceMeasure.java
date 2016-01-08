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
    Hashtable<String,String> autoTagged=new Hashtable<>();
    Hashtable<String,String> manuallyTagged=new Hashtable<>();

    final String filePathAnnAuto = "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/Test/" +
            "u_14.ann";  //manually tagged ann file

    final String filePathAnnManual = "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/" +
            "u_14.ann";  //manually tagged ann file

    int taggedWords=0;
    int correctTaggs=0;
    int totalTaggs=0;

    public static void main(String args[]) {
        PerformanceMeasure performanceMeasure=new PerformanceMeasure();
        performanceMeasure.measurePerformance();
    }

    public void measurePerformance()
    {
        try {
            readAnnFile(filePathAnnAuto,autoTagged);
            readAnnFile(filePathAnnManual,manuallyTagged);
            totalTaggs=manuallyTagged.size();
            taggedWords=autoTagged.size();
            compare(autoTagged,manuallyTagged);

            System.out.println("Total tags = "+totalTaggs);
            System.out.println("Automatically tagged words = "+taggedWords);
            System.out.println("Correct tags = "+correctTaggs);
            System.out.println("Precision = "+correctTaggs/(1.00*taggedWords));
            System.out.println("Recall = "+correctTaggs/(1.00*totalTaggs));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAnnFile(String file,Hashtable<String,String> hashtable) throws IOException {
        File fileAnnotation = new File(file);
        FileReader fr = new FileReader(fileAnnotation);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            String[] annotations = line.split("[ \t]");

            hashtable.put(annotations[2]+" "+annotations[3]+" "+annotations[4],annotations[1]);


        }
        br.close();
        fr.close();
    }

    private void compare(Hashtable<String,String> hashtable,Hashtable<String,String> hashtable2) throws IOException {

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
