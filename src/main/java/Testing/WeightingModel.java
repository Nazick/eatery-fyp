package Testing;

import AggregatingModel.LBNCI;
import hibernate.HibernateMain;
import model.RatingsEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by bruntha on 1/18/16.
 */
public class WeightingModel {
    HibernateMain hibernateMain = new HibernateMain();
    LBNCI lbnci=new LBNCI();

    public static void main(String[] args) {
        WeightingModel weightingModel = new WeightingModel();
        weightingModel.testWM();
        //testWM1 2e2e7WgqU1BnpxmQL5jbfw 2.7021932082523903 3.075951856439008
        //testWM2 zt1TpTuJ6y9n551sw9TaEg 2.5163013983414766 2.6067257311433227
    }

    public void testWM() {
        try {
            readFile("WeightingModel/R2.txt","testWM1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile(String filePath,String restaurantID) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filePath).getFile());
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int lineNo=1;
        double restaurant=0.0;
        double service=0.0;
        double staff=0.0;
        double staff_bahavior=0.0;

        int noOfR=0;
        int noOfS=0;
        int noOfSt=0;
        int noOfSb=0;


        while ((line = br.readLine()) != null) {
            System.out.println(lineNo++);
//            while (line.length()>3) {
//                line = br.readLine();
//            }
            line=br.readLine();
            String[] r = line.split(" ");
            if (r.length >= 2) {
                restaurant=lbnci.calculateLBNCI(restaurant,noOfR++,Double.parseDouble(r[1]));
//                insertRatings(restaurantID, "Restaurant", Integer.parseInt(r[1]));
            }
            line = br.readLine();
            String[] s = line.split(" ");
            if (s.length >= 2) {
                service=lbnci.calculateLBNCI(service,noOfS++,Double.parseDouble(s[1]));

//                insertRatings(restaurantID, "Service", Integer.parseInt(s[1]));

            }
            line = br.readLine();
            String[] sf = line.split(" ");
            if (sf.length >= 2) {
                staff=lbnci.calculateLBNCI(staff,noOfSt++,Double.parseDouble(sf[1]));

//                insertRatings(restaurantID, "S_Staff", Integer.parseInt(sf[1]));

            }
            line = br.readLine();
            String[] sb = line.split(" ");
            if (sb.length >= 2) {
                staff_bahavior=lbnci.calculateLBNCI(staff_bahavior,noOfSb++,Double.parseDouble(sb[1]));

//                insertRatings(restaurantID, "S_Stf_Behavior", Integer.parseInt(sb[1]));

            }
        }
        br.close();
        fr.close();
        System.out.println("Done...");
        System.out.println("Restaurant "+restaurant+" "+noOfR);
        System.out.println("Service "+service+" "+noOfS);
        System.out.println("Staff "+staff+" "+noOfSt);
        System.out.println("Staff Behavior "+staff_bahavior+" "+noOfSb);
    }


    private void insertRatings(String restaurantId, String aspect, int score) {
        System.out.println(restaurantId+" "+aspect+" "+score);
        List results = hibernateMain.getRating(restaurantId, aspect);

        RatingsEntity ratingsEntity;
        if (results.size() != 0) {
            ratingsEntity = (RatingsEntity) results.get(0);
            ratingsEntity.addScore(score);
            hibernateMain.insertRatings(ratingsEntity);
        } else {
            ratingsEntity = new RatingsEntity();
            ratingsEntity.addScore(score);
            ratingsEntity.setAspectTag(aspect);
            ratingsEntity.setRestaurantId(restaurantId);
            hibernateMain.insertRatings(ratingsEntity);
        }
    }
}
