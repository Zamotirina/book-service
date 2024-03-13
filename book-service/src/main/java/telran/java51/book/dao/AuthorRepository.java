package telran.java51.book.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.java51.book.model.Author;

public interface AuthorRepository extends JpaRepository<Author, String> {

}
