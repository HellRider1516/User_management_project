package in.mahesh.service;

import java.util.Map;

import in.mahesh.Dto.LoginDto;
import in.mahesh.Dto.RegisterDto;
import in.mahesh.Dto.ResetPasswordDto;
import in.mahesh.Dto.UserDto;

public interface UserService {
	
	public Map<Integer, String> getCountries();
	
	public Map<Integer, String> getStates(Integer countryId);
	
	public Map<Integer, String> getcities(Integer stateId);
	
	public UserDto getuser(String mail);
	
	public boolean registerUser(RegisterDto regDto);
	
	public UserDto getuser(LoginDto loginDto);
	
	public boolean resetPassword(ResetPasswordDto resetPasswordDto);
	
	public String getQuote();
	

}
