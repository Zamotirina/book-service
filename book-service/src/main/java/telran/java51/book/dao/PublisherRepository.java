package telran.java51.book.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.java51.book.model.Publisher;

/*
 * Чтобы понять, как работает Hibernate, мы решили реаализовать все руками и
 * убрать интерфейсы Spting-а JpaRepository
 */
//public interface PublisherRepository extends JpaRepository<Publisher, String>{

public interface PublisherRepository {

	@Query("select distinct p.publisherName from Book b join b.authors a join b.publisher p where a.name=?1")
	List <String> findPublishersByAuthor(String author); //Можно сдлеать Set, чтобы издатели были уникальными. Но можно в запросе указать distinct
	
	Stream <Publisher> findDistinctByBooksAuthorsName (String authorName);
	
	/*
	 * Методы ниже мы написали после удаления интерфейса JpaRepository, иначе ничего не работало
	 */

	Optional <Publisher> findById(String publisher);

	Publisher save(Publisher publisher);
}
