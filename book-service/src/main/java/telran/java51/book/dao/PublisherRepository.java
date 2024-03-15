package telran.java51.book.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.java51.book.model.Author;
import telran.java51.book.model.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, String>{

	@Query("select distinct p.publisherName from Book b join b.authors a join b.publisher p where a.name=?1")
	List <String> findPublishersByAuthor(String author); //Можно сдлеать Set, чтобы издатели были уникальными. Но можно в запросе указать distinct
	
	/*
	 *  Это аналог метода выше, но из-за  Bi-directional мы смогли его написать без Query
	 */
	
	Stream <Publisher> findDistinctByBooksAuthorsName (String authorName);

}
