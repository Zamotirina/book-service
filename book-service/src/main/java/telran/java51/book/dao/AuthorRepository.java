package telran.java51.book.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.java51.book.model.Author;
import telran.java51.book.model.Publisher;

public interface AuthorRepository extends JpaRepository<Author, String> {

	Optional<Author> findById(String author);

	void deleteById(String author);

}
