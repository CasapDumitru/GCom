package ro.utcluj.gcom.services;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import ro.utcluj.gcom.model.ChatGroup;
import ro.utcluj.gcom.model.Reply;
import ro.utcluj.gcom.serviceInterfaces.IGroupService;

public class GroupService  implements IGroupService{
	
	private SessionFactory factory;
	
	public GroupService() {
		Configuration configuration = new Configuration();
		this.factory = configuration.configure().buildSessionFactory();
	}

	public List<Reply> getAllMessages(String groupName) {
		
		Session session = factory.openSession();
		Transaction tx = null;
		List<Reply> messages = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM Reply WHERE groupName = :groupName order by date");
			query.setParameter("groupName", groupName);
			messages = query.list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return messages;	
		
	}
	
	public void addMessage(Reply message) {
		Date date = new Date();
		message.setDate(date);
		System.out.println(message.getDate());
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(message);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
	}
	
	public List<ChatGroup> getAllGroups() {
		Session session = factory.openSession();
		Transaction tx = null;
		List<ChatGroup> chatGroups = null;
		try {
			tx = session.beginTransaction();
			chatGroups = session.createQuery(" FROM ChatGroup").list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return chatGroups;
	}

	public ChatGroup getGroupByName(String groupName) {
		Session session = factory.openSession();
		Transaction tx = null;
		ChatGroup c=null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM ChatGroup WHERE groupName = :name");
			query.setParameter("name", groupName );
			List<ChatGroup> result = query.getResultList();
			if(result!=null && result.size()>0)
				c = result.get(0);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return c;
	}

	public void deleteGroup(ChatGroup chatGroup) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("Delete Reply WHERE groupName = :groupName");
			query.setParameter("groupName", chatGroup.getGroupName() );
			query.executeUpdate();
			session.delete(chatGroup);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}

	}
	
	public void addGroup(ChatGroup chatGroup) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(chatGroup);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
	}

	public static void main(String args[]){

		try {
			// Instantiating the implementation class
			GroupService groupService = new GroupService();

			LocateRegistry.createRegistry(1099);
			// Exporting the object of implementation class
			// (here we are exporting the remote object to the stub)
			IGroupService groupStub = (IGroupService) UnicastRemoteObject.exportObject(groupService, 0);

			// Binding the remote object (stub) in the registry
			Registry registry = LocateRegistry.getRegistry();

			registry.bind("IGroupService", groupStub );
			System.out.println("Server ready");
		} catch (Exception e) {
			System.out.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
