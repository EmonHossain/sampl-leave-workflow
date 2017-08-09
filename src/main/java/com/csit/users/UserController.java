package com.csit.users;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.csit.dtos.Leave;

@Controller
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private DataLoader loader;

	@GetMapping(value = "/")
	public String showLoginPage() {
		try {
			if (workflowService.userCount() < 1)
				loader.loadUserData();
		} catch (Exception ex) {
			logger.debug(ex.getMessage().toString());
			ex.printStackTrace();
		}

		return "login";
	}

	@GetMapping(value = "/task")
	public String showTask() {
		return "task";
	}

	@GetMapping(value = "/leave-form")
	public String showLeaveForm() {
		return "leave-form";
	}

	@GetMapping(value = "/home")
	public String showHome(Model model/*
										 * , @RequestParam(name = "msg",
										 * required = false) String message
										 */, Principal principal) {
		String status = null;
		logger.debug(principal.getName());
		/*
		 * if (message != null) model.addAttribute("msg", message); else
		 */
		model.addAttribute("msg", null);
		try {
			/*
			 * status = userDao.findLeaveStatusByUsername(principal.getName());
			 * if(status!=null){ model.addAttribute("status", status); }
			 */
			UserEntity user = workflowService.getUser(principal.getName());
			if (user != null)
				model.addAttribute("name", user.getName());
			List<Leave> leaves = workflowService.getLeaveApplications(principal.getName());
			model.addAttribute("leaves", leaves);
		} catch (Exception ex) {
			logger.debug(ex.getMessage());
			ex.printStackTrace();
		}

		return "home";
	}

	@PostMapping(value = "/apply")
	public String leaveApplication(LeaveApplicationEntity leaveApplicationEntity, BindingResult result,
			Principal principal) {
		if (result.hasErrors()) {
			return "leave-form";
		} else {
			try {
				long userId = workflowService.getUserId(principal.getName());
				logger.debug(String.valueOf(userId));
				leaveApplicationEntity.setUserEntity(new UserEntity(userId));
				String insId = workflowService.startFlow(principal.getName());
				leaveApplicationEntity.setProId(insId);
				workflowService.saveApplication(leaveApplicationEntity);
				workflowService.completeTask(insId, principal.getName());
				logger.debug("Application saved");
			} catch (Exception ex) {
				logger.debug(ex.getMessage());
				ex.printStackTrace();
			}

		}

		return "redirect:/home?msg=success";
	}
	
	@GetMapping(value="/approve")
	public String doApprove(@RequestParam(name="pi",required=true)String proId,Principal principal){
		workflowService.completeTask(proId, principal.getName());
		return "redirect:/home";
	}

}
