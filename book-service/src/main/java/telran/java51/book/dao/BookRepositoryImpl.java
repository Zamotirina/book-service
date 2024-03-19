package telran.java51.book.dao;

import java.util.Optional;
import java.util.stream.Stream;

import org.hibernate.boot.model.internal.EmbeddableBinder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
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
	
		
		TypedQuery <Book> query = em.createQuery("select b from publisher p join p.books b where p.publisherName=?1", Book.class)
				.setParameter(1,publisherName);
						
				return query.getResultStream();
	}

	@Override
	public void deleteBooksByAuthorsName(String author) {
		
		/* 
		 * Мое НЕПРАВИЛЬНОЕ решение
		 */
		
//		jakarta.persistence.Query query = em.createQuery("select b from Book b join b.authors a where a.name=:author")
//				.setParameter("author",author);
//	
//		em.clear();
		
		/*
		 * Решение Эдуарда
		 */
	
		
		TypedQuery <String> query = em.createQuery("delete from Book b join b.authors a where a.name=:author", String.class)
				.setParameter("author",author);
	
		query.executeUpdate();
	
	}

	@Override
	public boolean existsById(String isbn) {
	
		return  em.find(Book.class, isbn) != null;
	}

	//@Transactional //Добавляем когда убираем эту аннотацию у методу update() в сервисе
	@Override
	public Book save(Book book) {
		
		em.persist(book);
		return book;
	}

	@Override
	public Optional<Book> findById(String isbn) {

		return Optional.ofNullable(em.find(Book.class,isbn));
	}

	/*
	 * С двумя методами ниже у нас возникла проблема.
	 * 
	 * Когда мы передали код с delete() на deleteById() книга отлично удалялась, но в Postman она возвращалась без авторов, вместо авторов-пустой массив
	 * 
	 * Причина - потому что findById() по умолчанию возвращает нам не коллекцию, а ее прокси
	 * 
	 * В результате когда маппер потом ее мапит, он хочет получить доступ из базы к сету авторов, а его уже там нет
	 * 
	 * Получается пустой массив
	 * 
	 * Решение - использовать fetch-стратегию или сначала маппить, а потом удалять
	 */
	
	
	@Override
	public Book delete(Book book) {

		em.remove(book);
		
		return book;
		

	}
	
	public void deleteById (String isbn) {
		
		Query query = em.createQuery("delete from Book b where b.isbn=?1")
				.setParameter(1,isbn);
	
		query.executeUpdate();
	}

}
