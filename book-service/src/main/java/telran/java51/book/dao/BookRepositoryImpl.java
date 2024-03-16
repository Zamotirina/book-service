package telran.java51.book.dao;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import telran.java51.book.model.Book;

@Repository //Так добавляем в аппликационный контекст
public class BookRepositoryImpl implements BookRepository {

	@PersistenceContext //Будет создавать EntityManager ля каждого контекста персестности (то есть пока мы удержаем одну сессию), обычно создается только один
	EntityManager em; //Так мы получаем дсотуп к объекту, который обеспечивает связь с persistence context
	
	@Override
	public Stream<Book> findAllBooksByAuthorsName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<Book> findAllBooksByPublisherPublisherName(String publisherName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteBooksByAuthorsName(String author) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean existsById(String isbn) {
	
		return  em.find(Book.class, isbn) != null;
	}

	@Override
	public Book save(Book book) {
		
		em.persist(book);
		return book;
	}

	@Override
	public Optional<Book> findById(String isbn) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Book delete(Book book) {
		// TODO Auto-generated method stub
		return null;
	}

}
