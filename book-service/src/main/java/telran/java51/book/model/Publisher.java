package telran.java51.book.model;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity

/*
 * Мы преращаем этот проект в Bi-directional связь, то есть сообщаем родительским классам о детях
 * 
 * 1. Добавляем поле Set<Book> books;
 * 2. Добавляем конструктор с аргументами String publisherName иначе система ругается
 * 3. Добавляем в родмтельском классе аннотацию 	@OneToMany(mappedBy = "publisher"). все поля должны быть аннотированы. Мы их аннотируем с точки зрения класса, где находится. То есть у одного издетеля много книг, но у каждой книги один издатель
 * Соответственно в скобках мы указываем поле в классе Book, на основе которого мы сопоставлям классы.
 */
public class Publisher implements Serializable{

	private static final long serialVersionUID = -232655132943852677L;
	
	@Id
	String publisherName;
	
	@OneToMany(mappedBy = "publisher")// все поля должны быть аннотированы. Мы их аннотируем с точки зрения класса, где находится. То есть у одного издетеля много книг, но у каждой книги один издатель
	Set <Book> books; //Добавляем, чтобы реализовать Bi-directional

	@Override
	public String toString() {
		return publisherName;
	}

	public Publisher(String publisherName) {
		super();
		this.publisherName = publisherName;
	}
	
	
	
}
