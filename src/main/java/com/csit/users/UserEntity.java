package com.csit.users;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
	private String empId;
	private String name;
	private String password;
	@OneToMany(mappedBy="userEntity",targetEntity=LeaveApplicationEntity.class,cascade=CascadeType.ALL)
	private List<LeaveApplicationEntity> leaveApplicationEntities = new ArrayList<LeaveApplicationEntity>();

}
