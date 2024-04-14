package in.mahesh.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.mahesh.Dto.UserDto;
import in.mahesh.entity.UserEntity;


public interface UserDetailsRepo extends JpaRepository<UserEntity, Integer> {
	
	public List<UserEntity> findByEmail(String email);
	
	public UserEntity findByemailAndPassword(String email, String password);

}
