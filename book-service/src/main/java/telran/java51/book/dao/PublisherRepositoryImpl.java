package telran.java51.book.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import telran.java51.book.model.Publisher;

@Repository //Так добавляем в аппликационный контекст
public class PublisherRepositoryImpl implements PublisherRepository {
	
	@PersistenceContext //Будет создавать EntityManager ля каждого контекста персестности (то есть пока мы удержаем одну сессию), обычно создается только один
	EntityManager em; //Так мы получаем дсотуп к объекту, который обеспечивает связь с persistence context
	

	@Override
	public List<String> findByPublishersByAuthor(String author) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<Publisher> findDistinctByBooksAuthorsName(String authorName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Publisher> findById(String publisher) {
		
		return Optional.ofNullable(em.find(Publisher.class, publisher));
	}

	@Override
	public Publisher save(Publisher publisher) {
		
		em.persist(publisher);
		
		return publisher;
	}

}
