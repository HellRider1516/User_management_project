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

@Controller
public class UserController {
	 
	@Autowired
	private UserServiceImp service;
	
	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("register", new RegisterDto());
		Map<Integer, String> countryMap = service.getCountries();
		model.addAttribute("countires", countryMap);
		return"register";
	}
	
	@GetMapping("/state/{countryId}")
	@ResponseBody
	public Map<Integer, String> getStates(@PathVariable("countryId") Integer countryId){
		Map<Integer, String> statesMap = service.getStates(countryId);
		return statesMap;
		
	}

	@GetMapping("/cities/{stateId}")
	@ResponseBody
	public Map<Integer, String> getCities(@PathVariable("stateId") Integer stateId){
		Map<Integer, String> citiesMap = service.getcities(stateId);
		return citiesMap;
	}
	
	@PostMapping("/register")
	public String register(Model model , RegisterDto registerDto) {
		UserDto getuser = service.getuser(registerDto.getEmail());
		if(getuser != null) {
			model.addAttribute("error", "User Already Exists...");
			model.addAttribute("register", new RegisterDto());
			return "register";
		}
		
		boolean status = service.registerUser(registerDto);
		if(status) {
			model.addAttribute("sucess", "Account has been Created...");
			model.addAttribute("register", new RegisterDto());
		}else {
			model.addAttribute("error", "Plz Try again..."); 
			model.addAttribute("register", new RegisterDto());
		}
		return "register";
	}
	
	@GetMapping("/")
	public String loginPage(Model model) {
		model.addAttribute("login", new LoginDto());
		return "login";
	}
	
	@PostMapping("/login")
	public String login(LoginDto loginDto , Model model) {
		UserDto user = service.getUser(loginDto);
		if(user==null) {
			model.addAttribute("error", "Invaild Creditals...");
			model.addAttribute("login", new LoginDto());
			return "login";
		}
		String status = user.getPasswordStatus();
		if(status.equals("yes")) {
			return "redirect:dashboard";
		}else {
			model.addAttribute("resetPassword", new ResetPasswordDto());
			return "resetPassword";
		}
		
	}
	
	@PostMapping("/resetPassword")
	public String resetpassword(Model model , ResetPasswordDto resetPasswordDto) {
		if(resetPasswordDto.getOldPassword()==null) {
			model.addAttribute("error", "password is null");
			return "resetPassword";
		}
		if(!(resetPasswordDto.getNewPassword().equals(resetPasswordDto.getNewPassword()))) {
			model.addAttribute("error", "Password must Match.....");
			return "resetPassword";
		}
		
		
		UserDto user = service.getuser(resetPasswordDto.getEmail());
		if(user.getOldPassword().equals(resetPasswordDto.getNewPassword())) {
			boolean status = service.resetPassword(resetPasswordDto);
			if(status) {
				return "redirect:dashboard";
			}else {
				model.addAttribute("error", "Password upadte failed....");
				return "resetPassword";
			}
		}else {
			model.addAttribute("error", "Given Old Password is Wrong...");
			return "resetPassword";
		}
		
	}
	
	@GetMapping("/dashboard")
	public String dashBoard(Model model) {
		String quote = service.getQuote();
		model.addAttribute("dashboard", quote);
		return "dashboard";
	}
	
	@GetMapping("/logout")
	public String logout(Model model) {
		return "redirect:/";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
