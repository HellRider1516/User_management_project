package in.mahesh.Repo;


import org.springframework.data.jpa.repository.JpaRepository;

import in.mahesh.entity.UserEntity;


public interface UserDetailsRepo extends JpaRepository<UserEntity, Integer> {
	
	public UserEntity findByEmail(String email);
	
	public UserEntity findByEmailAndPassword(String email, String password);
	
	

}
