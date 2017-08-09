package com.csit.users;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users_leave_application")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LeaveApplicationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long leaveId;
	private String description;
	private String status;
	private String proId;
	@ManyToOne(optional = false)
	@JoinColumn(name = "userId")
	private UserEntity userEntity;

}
