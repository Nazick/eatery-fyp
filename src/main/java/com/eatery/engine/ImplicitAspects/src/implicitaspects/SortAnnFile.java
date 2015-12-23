/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implicitaspects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Ruba
 */
public class SortAnnFile {
    public static void main(String args[]) throws FileNotFoundException{
        
    HashMap<Integer, String> hmap = new HashMap<Integer, String>();
    Scanner in = new Scanner(new File("src/reviews/A Ann.txt"));
    int count=0;
        while (in.hasNext()) {  
            String tagData = in.nextLine();
            count++;
            try{
            int key = Integer.parseInt(tagData.split("\t")[1].split(" ")[1]);
            hmap.put(key, tagData);
            }catch(Exception e){
                System.out.println(count+" "+tagData);
            }
        }
    
    Map<Integer, String> map = new TreeMap<Integer, String>(hmap);
    
    PrintWriter outF = new PrintWriter(new File("src/reviews/A Ann_Sorted.txt"));
    Set set = map.entrySet();
         Iterator iterator = set.iterator();
         while(iterator.hasNext()) {
              Map.Entry me = (Map.Entry)iterator.next();
              String tag = (String) me.getValue();
              outF.write(tag+"\n");
         }
    outF.close();
    }
}
