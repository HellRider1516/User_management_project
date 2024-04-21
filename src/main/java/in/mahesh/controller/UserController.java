package in.mahesh.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import in.mahesh.Dto.LoginDto;
import in.mahesh.Dto.RegisterDto;
import in.mahesh.Dto.ResetPasswordDto;
import in.mahesh.Dto.UserDto;
import in.mahesh.service.UserServiceImp;
import in.mahesh.utils.AppConstant;
import in.mahesh.utils.AppProperties;

@Controller
public class UserController {
	 
	@Autowired
	private UserServiceImp service;
	
	@Autowired
	private AppProperties appProp;
	
	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute(AppConstant.REG_OBJ, new RegisterDto());
		Map<Integer, String> countryMap = service.getCountries();
		model.addAttribute(AppConstant.COUNTRY_OBJ, countryMap);
		return AppConstant.REG_VIEW;
	}
	
	@GetMapping("/state/{countryId}")
	@ResponseBody
	public Map<Integer, String> getStates(@PathVariable("countryId") Integer countryId){
		return service.getStates(countryId);
		
	}

	@GetMapping("/cities/{stateId}")
	@ResponseBody
	public Map<Integer, String> getCities(@PathVariable("stateId") Integer stateId){
		return service.getcities(stateId);
	}
	
	@PostMapping("/register")
	public String register(Model model , RegisterDto registerDto) {
		Map<Integer, String> countryMap = service.getCountries();
		model.addAttribute(AppConstant.COUNTRY_OBJ, countryMap);
		Map<String, String> message = appProp.getMessage();
		
		UserDto getuser = service.getuser(registerDto.getEmail());
		if(getuser != null) {
			model.addAttribute(AppConstant.ERROR_MSG,message.get("dupEmail") );
			model.addAttribute(AppConstant.REG_OBJ, new RegisterDto());
			return AppConstant.REG_VIEW;
		}
		
		boolean status = service.registerUser(registerDto);
		if(status) {
			model.addAttribute(AppConstant.SUCESS_MSG, message.get("regSucess"));
			model.addAttribute(AppConstant.REG_OBJ, new RegisterDto());
		}else {
			model.addAttribute(AppConstant.ERROR_MSG, message.get("regError")); 
			model.addAttribute(AppConstant.REG_OBJ, new RegisterDto());
		}
		return AppConstant.REG_VIEW;
	}
	
	@GetMapping("/")
	public String loginPage(Model model) {
		model.addAttribute(AppConstant.LOGIN_OBJ, new LoginDto());
		return AppConstant.LOGIN_VIEW;
	}
	
	@PostMapping("/login")
	public String login(LoginDto loginDto , Model model) {
		Map<String, String> message = appProp.getMessage();
		UserDto user = service.getuser(loginDto);
		if(user == null) {
			model.addAttribute(AppConstant.ERROR_MSG,message.get("invaildCreditals") );
			model.addAttribute(AppConstant.LOGIN_OBJ, new LoginDto());
			return AppConstant.LOGIN_VIEW;
		}
		String status = user.getPasswordStatus();
		if(status!=null && status.equals("yes")) {
			return "redirect:dashboard";
		}else {
			ResetPasswordDto rtd= new ResetPasswordDto(); 
			rtd.setEmail(user.getEmail());
			model.addAttribute(AppConstant.RESPWD_OBJ, rtd);
			return "resetPassword";
		}
		
	}
	
	@PostMapping("/resetPassword")
	public String resetpassword(Model model , ResetPasswordDto resetPasswordDto) {
		Map<String, String> message = appProp.getMessage();
		if(resetPasswordDto.getPassword() == null) {
			model.addAttribute(AppConstant.ERROR_MSG, message.get("passwordNull"));
			return AppConstant.RESET_VIEW;
		}
		if(!(resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword()))) {
			model.addAttribute(AppConstant.ERROR_MSG, message.get("passwordMatch"));
			return AppConstant.RESET_VIEW;
		}
		
		
		if(resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())) {
			boolean status = service.resetPassword(resetPasswordDto);
			if(status) {
				return "redirect:dashboard";
			}else {
				model.addAttribute(AppConstant.ERROR_MSG, message.get("passwordUpdated"));
				return AppConstant.RESET_VIEW;
			}
		}else {
			model.addAttribute(AppConstant.ERROR_MSG, message.get("oldPasswordError"));
			return AppConstant.RESET_VIEW;
		}
		
	}
	
	@GetMapping("/dashboard")
	public String dashBoard(Model model) {
		String quote = service.getQuote();
		model.addAttribute(AppConstant.DASHBOARD_OBJ, quote);
		return "dashboard";
	}
	
	@GetMapping("/logout")
	public String logout(Model model) {
		return "redirect:/";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
