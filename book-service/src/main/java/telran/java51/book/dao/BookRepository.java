package telran.java51.book.dao;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.java51.book.model.Book;

public interface BookRepository extends JpaRepository<Book, String> {

	Stream <Book> findAllBooksByAuthorsName(String name);
	Stream <Book> findAllBooksByPublisherPublisherName(String publisherName);
	
}
