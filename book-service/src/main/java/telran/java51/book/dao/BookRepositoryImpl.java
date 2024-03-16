package telran.java51.book.dao;

import java.util.Optional;
import java.util.stream.Stream;

import org.hibernate.boot.model.internal.EmbeddableBinder;
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
		
		jakarta.persistence.Query query = em.createQuery("select b from Book b join b.authors a where a.name=:name")
				.setParameter("name",name);
						
				return query.getResultStream();
		
	}

	@Override
	public Stream<Book> findAllBooksByPublisherPublisherName(String publisherName) {
	
		
		jakarta.persistence.Query query = em.createQuery("select b from Book b join b.publisher p where p.publisherName=:publisherName")
				.setParameter("publisherName",publisherName);
						
				return query.getResultStream();
	}

	@Override
	public void deleteBooksByAuthorsName(String author) {
		
		
		jakarta.persistence.Query query = em.createQuery("select b from Book b join b.authors a where a.name=:author")
				.setParameter("author",author);
	
		em.clear();
	
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

		return Optional.ofNullable(em.find(Book.class,isbn));
	}

	@Override
	public Book delete(Book book) {
		
		em.remove(book);
		
		return book;
	}

}
