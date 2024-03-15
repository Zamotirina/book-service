package telran.java51.book.dao;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.java51.book.model.Book;
import telran.java51.book.model.Publisher;

public interface BookRepository extends JpaRepository<Book, String>{

	Stream <Book> findAllBooksByAuthorsName(String name);
	Stream <Book> findAllBooksByPublisherPublisherName(String publisherName);
	void deleteBooksByAuthorsName(String author);
	boolean existsById(String isbn);
	Book save(Book book);
	Optional<Book> findById(String isbn);
	void delete(Book book);
}
