package telran.java51.book.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.java51.book.model.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, String>{

	@Query("select distinct p.publisherName from Book b join b.authors a join b.publisher p where a.name=?1")
	List <String> findByPublishersByAuthor(String author); //Можно сдлеать Set, чтобы издатели были уникальными. Но можно в запросе указать distinct
}
