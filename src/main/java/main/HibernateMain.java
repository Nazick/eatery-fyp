package main;

import model.BusinessEntity;
import org.hibernate.Session;
import util.HibernateUtil;

public class HibernateMain {

	public static void main(String[] args) {
		BusinessEntity businessEntity=new BusinessEntity("sdg","dd","dd","ddd","erf",1.02f);
		
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

}
