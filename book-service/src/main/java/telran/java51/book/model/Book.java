package telran.java51.book.model;

import java.io.Serializable;
import java.util.Set;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of="isbn")
@Entity
public class Book implements Serializable{

	private static final long serialVersionUID = 8840647348644778936L;

	@Id
	String isbn;
	@Setter
	String title;
	
	/*
	 * Добавляем аннотации, которые отражают отношения между нашими Entity
	 * Это не встроенные сущености (как Adress у Persoт), а три равнозначные сущности, которые мы сопоставляем
	 */
	
	@ManyToMany //У нас много авторов у одной книги, и 1 автор мог написать несколько книг
	Set <Author> authors;
	
	@ManyToOne//Каждую книгу издал только 1 издатель. Важно, что мы тут на стороне книг. То есть мы НЕ должны выбирать @OnetoMany
	Publisher publisher;
}
