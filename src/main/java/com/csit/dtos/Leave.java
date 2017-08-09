package com.csit.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Leave {

	private String empName;
	private String empId;
	private String reason;
	private String proId;

}
