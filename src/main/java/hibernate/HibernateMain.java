package hibernate;

import WeightingModel.AHPM;
import model.BusinessEntity;
import model.RatingsEntity;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

import java.util.List;

public class HibernateMain {

    SessionFactory sessionFactory;

    public HibernateMain() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public static void main(String[] args) {
        HibernateMain hibernateMain = new HibernateMain();
        //hibernateMain.updateWeightsTable();
        List list = hibernateMain.searchByAspect("Food",5);
        //List list2 = hibernateMain.searchAspectByRestaurant("Restaurant_11","Food");
        System.out.println();

    }

    public void insertRatings(RatingsEntity ratingsEntity) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(ratingsEntity);
            session.getTransaction().commit();
            session.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insertRestaurant(BusinessEntity restaurent) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(restaurent);
        session.getTransaction().commit();
        session.close();

    }

    public List getRatings(String restaurantID) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from RatingsEntity where restaurantId = :id ");
        query.setParameter("id", restaurantID);
        List<?> list = query.list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    public List getRestaurants() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from BusinessEntity ");
        List<?> list = query.list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    public List getWeights(String parentAspect) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from WeightsEntity where parentAspect = :id ");
        query.setParameter("id", parentAspect);
        List<?> list = query.list();

        session.getTransaction().commit();
        session.close();
        return list;
    }

    public List getRating(String restaurantId, String aspectTag) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "FROM RatingsEntity R WHERE R.restaurantId = :restaurantId AND R.aspectTag = :aspectTag";
        Query query = session.createQuery(hql);
        query.setParameter("restaurantId", restaurantId);
        query.setParameter("aspectTag", aspectTag);
        List list = query.list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    public BusinessEntity getRestaurant(String restaurantId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        BusinessEntity businessEntity = (BusinessEntity) session.get(BusinessEntity.class, restaurantId);
        session.getTransaction().commit();
        session.close();
        return businessEntity;
    }

    public List getRestaurantByName(String restaurantName) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "FROM BusinessEntity B WHERE B.name = :restaurantName";
        Query query = session.createQuery(hql);
        query.setParameter("restaurantName", restaurantName);
        List list = query.list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    public List getWeights() {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from WeightsEntity ");
        List<?> list = query.list();
        return list;
    }

    public List searchByAspect(String aspectName, int noOfResults){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "select b.name, r.score from RatingsEntity r, BusinessEntity b, AspectEntity a where r.restaurantId = b.businessId and r.aspectTag = a.aspectTag and a.aspectName = :aspectName order by r.score desc";
        Query query = session.createQuery(hql);
        query.setParameter("aspectName", aspectName);
        query.setMaxResults(noOfResults);
        List list = query.list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    public List searchAspectByRestaurant(String restaurantName, String aspectName){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "select r from RatingsEntity r, AspectEntity a, BusinessEntity b where r.restaurantId = b.businessId and b.name = :restaurantName and r.aspectTag = a.aspectTag and a.aspectName = :aspectName";
        Query query = session.createQuery(hql);
        query.setParameter("restaurantName", restaurantName);
        query.setParameter("aspectName",aspectName);
        List list = query.list();
        session.getTransaction().commit();
        session.close();
        return list;
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



    public void updateWeightsTable() {
        AHPM ahpm = new AHPM();
        List list = ahpm.getWeights();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        for (int i = 0; i < list.size(); i++) {


            try {
                session.saveOrUpdate((list.get(i)));
            } catch (NonUniqueObjectException e) {
                System.out.println("ERROR " + list.get(i).toString());
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
