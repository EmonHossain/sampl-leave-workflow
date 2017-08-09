package com.csit.users;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UserDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SessionFactory _sessionFactory;

	private Session getSession() {
		return _sessionFactory.getCurrentSession();
	}

	@Transactional
	public UserEntity findUserByUsername(String username) {
		UserEntity user = (UserEntity) getSession().createCriteria(UserEntity.class)
				.add(Restrictions.eq("username", username)).uniqueResult();
		user.getUserId();
		return user;
	}

	public void saveUser(UserEntity user) {
		getSession().save(user);
		logger.debug(user.toString() + " saved");
	}

	public void saveLeaveApplication(LeaveApplicationEntity leaveApplicationEntity) {
		getSession().save(leaveApplicationEntity);
	}

	/*
	 * public String findLeaveStatusByUsername(String name) { Criteria criteria
	 * = getSession().createCriteria(UserEntity.class, "u");
	 * 
	 * }
	 */

	public int countAllUsers() {
		int count = getSession().createCriteria(UserEntity.class).list().size();
		return count;
	}

	public UserEntity findNextUserRole(String role) {
		UserEntity user = (UserEntity) getSession().createCriteria(UserEntity.class).add(Restrictions.eq("role", role))
				.uniqueResult();
		user.getUserId();
		return user;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findAllAssignedApplication(List<String> proIds) {
		logger.debug("task size : "+String.valueOf(proIds.size()));
		List<Object[]> objs = new ArrayList<Object[]>();
		for(String id : proIds){
			Criteria criteria = getSession().createCriteria(LeaveApplicationEntity.class, "l");
			criteria.createAlias("l.userEntity", "u", org.hibernate.sql.JoinType.INNER_JOIN);
			criteria.setProjection(
					Projections.projectionList().add(Projections.property("u.name")).add(Projections.property("u.empId"))
							.add(Projections.property("l.proId")).add(Projections.property("l.description")));
			criteria.add(Restrictions.eq("l.proId", id));
			Object[] obj = (Object[])criteria.uniqueResult();
			objs.add(obj);
		}
		
		//List<Object[]> objs = criteria.list();
		return objs;
	}

}
