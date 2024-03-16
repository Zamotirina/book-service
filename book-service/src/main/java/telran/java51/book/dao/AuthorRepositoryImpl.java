package telran.java51.book.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import telran.java51.book.model.Author;

@Repository //Так добавляем в аппликационный контекст
public class AuthorRepositoryImpl implements AuthorRepository{

	@PersistenceContext //Будет создавать EntityManager ля каждого контекста персестности (то есть пока мы удержаем одну сессию), обычно создается только один
	EntityManager em; //Так мы получаем дсотуп к объекту, который обеспечивает связь с persistence context
	
	@Override
	public Optional<Author> findById(String name) {
	
		return Optional.ofNullable(em.find(Author.class, name));
	}

	@Override
	public Author save(Author author) {
		
		em.persist(author);
		return author;
	}

	@Override
	public Author deleteById(String author) {
		
		Author auth=findById(author).get();
	
		em.remove(auth);
		return auth;
	}

}
