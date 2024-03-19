package telran.java51.book.dao;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.java51.book.model.Book;
import telran.java51.book.model.Publisher;

/*
 * Чтобы понять, как работает Hibernate, мы решили реаализовать все руками и
 * убрать интерфейсы Spting-а JpaRepository
 */

//public interface BookRepository extends JpaRepository<Book, String> {

public interface BookRepository {

	Stream <Book> findAllBooksByAuthorsName(String name);
	Stream <Book> findAllBooksByPublisherPublisherName(String publisherName);
	void deleteBooksByAuthorsName(String author);
	
	/*
	 * Методы ниже мы написали после удаления интерфейса JpaRepository<Book, String>,
	 * 
	 * чтобы BoookServiceImpl снова начал работать
	 */
	boolean existsById(String isbn);
	Book save(Book book);
	Optional<Book> findById(String isbn);
	Book delete(Book book);
	
	void deleteById (String isbn) ;
}
