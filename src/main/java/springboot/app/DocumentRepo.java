package springboot.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepo extends JpaRepository<Document,Long>{
		 
		@Query(value = "SELECT * FROM document WHERE name like %?1", nativeQuery = true)
		List<Document> findByTitle(String title);
		

		
}
