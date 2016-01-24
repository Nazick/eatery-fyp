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

//        Restaurant 3.0497235530137847 179 2.183277736452741
//        Service 3.1064089709104508 94 2.9345653108076557
//        Staff 2.8798108737752 10 2.504896258067787
//        Staff Behavior 2.099416522936422 4 3.5312220305818958
//
//        Restaurant 3.2449633133013394 162 2.146817278544596
//        Service 3.0967014728855253 77 2.8830093099268925
//        Staff 3.4624120638767653 24 2.221563577624397
//        Staff Behavior 3.9252514935560634 8 2.9581122215081685
//
//        2.183277736452741
//        2.9345653108076557
//        2.504896258067787
//        3.5312220305818958
//
//        2.146817278544596
//        2.8830093099268925
//        2.221563577624397
//        2.9581122215081685
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
