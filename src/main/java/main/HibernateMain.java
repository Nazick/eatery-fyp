package main;

import WeightingModel.AHPM;
import model.BusinessEntity;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;

public class HibernateMain {

    public static void main(String[] args) {
        HibernateMain hibernateMain = new HibernateMain();
        //hibernateMain.updateWeightsTable();
        hibernateMain.test();
    }

    public void test() {
        BusinessEntity businessEntity = new BusinessEntity("nazick", "dd", "dd", "ddd", "erf", 1.02f);

        //Get Session
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //start transaction
        session.beginTransaction();
        //Save the Model object
        session.save(businessEntity);
        //Commit transaction
        session.getTransaction().commit();

        //terminate session factory, otherwise program won't end
        HibernateUtil.getSessionFactory().close();
    }

    public void updateWeightsTable() {
        AHPM ahpm = new AHPM();
        List list = ahpm.getWeights();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        for (int i = 0; i < list.size(); i++) {


            try {
                session.saveOrUpdate((list.get(i)));
            } catch (NonUniqueObjectException e) {
                System.out.println("ERROR "+list.get(i).toString());
            }


        }

        session.getTransaction().commit();

        HibernateUtil.getSessionFactory().close();
    }

    public void updateWeightsTableT() {
        AHPM ahpm = new AHPM();
        List list = ahpm.getWeights();

        for (int i = 0; i < list.size(); i++) {
//            updateWeightsTable((WeightsEntity) list.get(i));
        }
    }

//    public static void updateWeightsTable(WeightsEntity weightsEntity) {
//        //Get Session
//        //start transaction
//        session.beginTransaction();
//        //Save the Model object
//        session.saveOrUpdate(weightsEntity);
//        //Commit transaction
//        session.getTransaction().commit();
//        //terminate session factory, otherwise program won't end
//        HibernateUtil.getSessionFactory().close();
//    }
}
