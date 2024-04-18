package in.mahesh.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.mahesh.entity.StateEntity;

public interface StateRepo extends JpaRepository<StateEntity, Integer> {
	
	@Query(value="select *from state_master where country_id=:stateId" , nativeQuery = true)
	public List<StateEntity> getStates(Integer stateId);

}
