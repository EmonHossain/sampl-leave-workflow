package com.csit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.csit.users.UserDao;
import com.csit.users.UserEntity;

public class InitialDataLoader implements CommandLineRunner {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserDao userDao;

	@Override
	public void run(String... args) throws Exception {
		
		UserEntity user1 = new UserEntity("CSIT-2012", "Emon", "emon", new BCryptPasswordEncoder().encode("admin"),"ROLE_USER");
		UserEntity user2 = new UserEntity("CSIT-2013", "Shuvo", "shuvo", new BCryptPasswordEncoder().encode("admin"),"ROLE_USER");
		UserEntity user3 = new UserEntity("CSIT-2014", "Tanmoy", "tanmoy", new BCryptPasswordEncoder().encode("admin"),"ROLE_USER");
		UserEntity user4 = new UserEntity("CSIT-2015", "Asad", "asad", new BCryptPasswordEncoder().encode("admin"),"ROLE_USER");
		try {
			userDao.saveUser(user1);
			userDao.saveUser(user2);
			userDao.saveUser(user3);
			userDao.saveUser(user4);
		} catch (Exception ex) {
			logger.debug(ex.getMessage().toString());
			ex.printStackTrace();
		}
		
	}

}
