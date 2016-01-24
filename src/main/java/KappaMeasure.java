import excel.Excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by bruntha on 1/6/16.
 */
public class KappaMeasure {
    Hashtable<String,String> autoTagged=new Hashtable<>();
    Hashtable<String,String> manuallyTagged=new Hashtable<>();
    int taggedWords=0;
    int correctTags =0;
    int nonCorrectTags =0;
    int totalTags =0;

    public static void main(String args[]) {
        KappaMeasure kappaMeasure=new KappaMeasure();
//        kappaMeasure.generateKappaInputFile();
        kappaMeasure.measurePerformance();
    }

    public void generateKappaInputFile(){
        try {
            readAnnFile("Kappa/u_11.ann",autoTagged);
            readAnnFile("Kappa/u_11_2.txt",manuallyTagged);

            Excel.writeKappa(autoTagged,manuallyTagged,"kappa");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void measurePerformance()
    {
        try {
            readAnnFile("Kappa/u_13-Ann_Sorted.txt",autoTagged);
            readAnnFile("Kappa/u_13-new-Ann_Sorted.txt",manuallyTagged);
            totalTags =manuallyTagged.size();
            taggedWords=autoTagged.size();
            compare(autoTagged,manuallyTagged);

            System.out.println("Total tags = "+ totalTags);
            System.out.println("Matched tags = "+ correctTags);
            System.out.println("Non Matched tags = "+ nonCorrectTags);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAnnFile(String filePath,Hashtable<String,String> hashtable) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filePath).getFile());
        FileReader fr = new FileReader(file);
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
                    correctTags++;
                }else {
                    nonCorrectTags++;
                }
            }
        }
    }
}
