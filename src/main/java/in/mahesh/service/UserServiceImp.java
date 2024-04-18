package in.mahesh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.mahesh.Dto.LoginDto;
import in.mahesh.Dto.QuoteDto;
import in.mahesh.Dto.RegisterDto;
import in.mahesh.Dto.ResetPasswordDto;
import in.mahesh.Dto.UserDto;
import in.mahesh.Repo.CityRepo;
import in.mahesh.Repo.CountryRepo;
import in.mahesh.Repo.StateRepo;
import in.mahesh.Repo.UserDetailsRepo;
import in.mahesh.entity.CityEntity;
import in.mahesh.entity.CountryEntity;
import in.mahesh.entity.StateEntity;
import in.mahesh.entity.UserEntity;
import in.mahesh.utils.Emailutils;
import in.mahesh.utils.PasswordGenerator;
@Service
public class UserServiceImp implements UserService {
	
	@Autowired
	private UserDetailsRepo userRepo;
	
	@Autowired
	private CountryRepo countryRepo;
	
	@Autowired
	private StateRepo stateRepo;
	
	@Autowired
	private CityRepo cityRepo;
	
	@Autowired
	private Emailutils emailUtils;
	
	@Autowired
	private PasswordGenerator passwordGenerator; 
	
	QuoteDto[] value = null;
	

	@Override
	public Map<Integer, String> getCountries() {
		Map<Integer, String> countryMap = new HashMap<>();
		List<CountryEntity> all = countryRepo.findAll();
		all.forEach(e -> {
			countryMap.put(e.getCountryId(), e.getCountryName());
		});
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		Map<Integer, String> mapStates = new HashMap<>();
		List<StateEntity> states = stateRepo.getStates(countryId);
		states.forEach(e -> {
			mapStates.put(e.getStateId(),e.getStateName());
		});
		
		return mapStates;
	}

	@Override
	public Map<Integer, String> getcities(Integer stateId) {
		Map<Integer, String> mapcity = new HashMap<>();
		List<CityEntity> cities = cityRepo.getCities(stateId);
		cities.forEach(e -> {
			mapcity.put(e.getCityId(), e.getCityName());
		});
		
		return mapcity;
	}

	@Override
	public UserDto getuser(String mail) {
		ModelMapper mapper = new ModelMapper();
		UserEntity list = userRepo.findByEmail(mail);
		if(list  == null) {
			return null;
		}
		UserDto userDto = mapper.map(list, UserDto.class);
		
		return userDto;
		
	}

	@Override
	public boolean registerUser(RegisterDto regDto) {
		ModelMapper mapper = new ModelMapper();
		UserEntity user = mapper.map(regDto, UserEntity.class);
		CountryEntity country = countryRepo.findById(regDto.getCountryId()).orElseThrow();
		StateEntity state = stateRepo.findById(regDto.getStateId()).orElseThrow();
		CityEntity city = cityRepo.findById(regDto.getCityId()).orElseThrow();
		user.setCountryEntity(country);
		user.setStateEntity(state);
		user.setCityEntity(city);
		user.setPassword(passwordGenerator.getRandomString());
		user.setPasswordStatus("no");
		UserEntity userSaved = userRepo.save(user);
		String body="your Account has been Created and your password is"+passwordGenerator.getRandomString();
		emailUtils.mailSent(user.getEmail(), body, "Account Details");
		return userSaved.getUserId()!=null;
		
		
	}

	@Override
	public UserDto getUser(LoginDto loginDto) {
		ModelMapper mapper = new ModelMapper();
		  UserEntity list = userRepo.findByEmailAndPassword(loginDto.getEmail() , loginDto.getPassword());
		 if(list == null) {
			 return null;
		 }
		 UserDto dto = mapper.map(list, UserDto.class);
		 
		 return dto;
		
	}

	@Override
	public boolean resetPassword(ResetPasswordDto resetPasswordDto ) {
		 UserEntity user = userRepo.findByEmailAndPassword(resetPasswordDto.getEmail(), resetPasswordDto.getPassword());
		if(user != null) {
			user.setPassword(resetPasswordDto.getNewPassword());
			user.setPasswordStatus("yes");
			userRepo.save(user);
			return true;
		}
		return false;
		
	}

	@Override
	public String getQuote() {
		
		
		 String url="https://type.fit/api/quotes";
		 
		 RestTemplate rt= new RestTemplate();
		 ResponseEntity<String> forEntity = rt.getForEntity(url, String.class);
		 String body = forEntity.getBody();
		 ObjectMapper mapper=new ObjectMapper();
		 try {
			  value = mapper.readValue(body, QuoteDto[].class);
			 
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 Random r = new Random();
		 int index = r.nextInt(value.length-1);
			return value[index].getText(); 
		 
		 
	}

}
