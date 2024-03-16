package telran.java51.book.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.java51.book.model.Author;

/*
 * Чтобы понять, как работает Hibernate, мы решили реаализовать все руками и
 * убрать интерфейсы Spting-а JpaRepository
 */

//public interface AuthorRepository extends JpaRepository<Author, String> {
//
//}

public interface AuthorRepository  {
	
	/*
	 * Методы ниже мы написали после удаления интерфейса JpaRepository<Book, String>,
	 * 
	 * чтобы BoookServiceImpl снова начал работать
	 */
	
	Optional <Author> findById(String name);
	Author save(Author author);
	Author deleteById(String author);

}
