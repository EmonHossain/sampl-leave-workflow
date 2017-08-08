package com.csit.users;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

	@Autowired
	private SessionFactory _sessionFactory;

	private Session getSession() {
		return _sessionFactory.getCurrentSession();
	}

	public UserEntity getUserByUsername(String username) {
		return (UserEntity) getSession().createCriteria(UserEntity.class).add(Restrictions.eq("username", username))
				.uniqueResult();
	}

}
