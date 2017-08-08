package com.csit.users;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

	@GetMapping(value = "/")
	public String showLoginPage() {
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
	public String showLeaveForm(Model model, @RequestParam(name = "msg", required = false) String message) {
		if (message != null)
			model.addAttribute("msg", message);
		else
			model.addAttribute("msg", null);
		return "leave-form";
	}

	@PostMapping(value = "/apply")
	public String leaveApplication(LeaveApplicationEntity leaveApplicationEntity, BindingResult result,
			Principal principal) {
		if (result.hasErrors()) {
			return "leave-form";
		}

		return "redirect:/home?msg=success";
	}

}
