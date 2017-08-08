package com.csit.users;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SessionFactory _sessionFactory;

	private Session getSession() {
		return _sessionFactory.getCurrentSession();
	}

	public UserEntity getUserByUsername(String username) {
		return (UserEntity) getSession().createCriteria(UserEntity.class).add(Restrictions.eq("username", username))
				.uniqueResult();
	}

	public void saveUser(UserEntity user) {
		getSession().save(user);
		logger.debug(user.toString()+" saved");
	}

}
