package model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.BotHeart;
import model.entity.User;

public class UserJpaDAO {

	private static UserJpaDAO instance;
	protected EntityManager entityManager;

	public static UserJpaDAO getInstance(){
		if (instance == null){
			instance = new UserJpaDAO();
		}

		return instance;
	}

	private UserJpaDAO() {
		entityManager = getEntityManager();
	}

	private EntityManager getEntityManager() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("botInfo");
		if (entityManager == null) {
			entityManager = factory.createEntityManager();
		}

		return entityManager;
	}

	public User getById(final int id) {
		return entityManager.find(User.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<User> findAll() {
		return entityManager.createQuery("FROM " + User.class.getName()).getResultList();
	}
	
	public User findUser(String nome, BotHeart.Sites site){
		List<User> all = findAll();
		User user = null;
		
		for(User u : all){
			if(u.getNome().equals(nome) && u.getSite() == site.ordinal()){
				user = u;
			}
		}
		
		return user;		
	}

	public void persist(User User) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(User);
			entityManager.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			entityManager.getTransaction().rollback();
		}
	}

	public void merge(User User) {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(User);
			entityManager.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			entityManager.getTransaction().rollback();
		}
	}

	public void remove(User User) {
		try {
			entityManager.getTransaction().begin();
			User = entityManager.find(User.class, User.getId());
			entityManager.remove(User);
			entityManager.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			entityManager.getTransaction().rollback();
		}
	}

	public void removeById(final int id) {
		try {
			User User = getById(id);
			remove(User);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}