package hibernate;

import WeightingModel.AHPM;
import model.BusinessEntity;
import model.RatingsEntity;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;

public class HibernateMain {

    public void insert(RatingsEntity ratingsEntity) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(ratingsEntity);
        session.getTransaction().commit();
        HibernateUtil.getSessionFactory().close();
    }

    public List getRatings(String restaurantID) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("from RatingsEntity where restaurantId = :id ");
        query.setParameter("id", restaurantID);
        List<?> list = query.list();
        return list;
    }

    public List getWeights(String parentAspect) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("from WeightsEntity where parentAspect = :id ");
        query.setParameter("id", parentAspect);
        List<?> list = query.list();
        return list;
    }

    public List getWeights()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("from WeightsEntity ");
        List<?> list = query.list();
        return list;
    }

    public static void main(String[] args) {
        HibernateMain hibernateMain = new HibernateMain();
        //hibernateMain.updateWeightsTable();
        List list=hibernateMain.getRatings("DDD");
        System.out.println(list.size());

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

//    public void aspectTest(){
//        //Get Session
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        //start transaction
//        session.beginTransaction();
//        //Save the Model object
//        //session.save(businessEntity);
//        AspectEntity aspectEntity = (AspectEntity)session.get(AspectEntity.class, "Food");
//
//        BusinessEntity businessEntity = (BusinessEntity)session.get(BusinessEntity.class,"2e2e7WgqU1BnpxmQL5jbfw");
//
//        RatingsEntity ratingsEntity = new RatingsEntity();
//
//        ratingsEntity.setAspect(aspectEntity);
//        ratingsEntity.setRestaurant(businessEntity);
//        ratingsEntity.setNoOfoccurance(1);
//        ratingsEntity.setScore(4.0);
//
//        session.save(ratingsEntity);
//                //Commit transaction
//        session.getTransaction().commit();
//
//        //terminate session factory, otherwise program won't end
//        HibernateUtil.getSessionFactory().close();
//
//    }

    public void testRating(){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //start transaction
        session.beginTransaction();

        String hql = "FROM RatingsEntity R WHERE R.restaurant.businessId = :restaurantId AND R.aspect.aspectName = :aspectName";
        Query query = session.createQuery(hql);
        query.setParameter("restaurantId", "2e2e7WgqU1BnpxmQL5jbfw");
        query.setParameter("aspectName", "Food");
        List results = query.list();

        RatingsEntity ratingsEntity;
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
