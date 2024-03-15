package telran.java51.book.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of="name")
@Entity


/*
 * Мы превращаем этот проект в Bi-directional связь, то есть сообщаем родительским классам о детях
 * 
 * 1. Добавляем поле Set<Book> books;
 * 2. Добавляем конструктор с аргументами String name, LocalDate birthDate, иначе система ругается
 * 3. Добавляем в родмтельском классе аннотацию 	@ManyToMany(mappedBy = "authors", cascade = CascadeType.REMOVE)
 * 
 * В аннотации:
 * mappedBy - обязательный атрибут для родительского класса
 * authors- указание на поле дочернего класса, по которому связываем
 * cascade - указание на то, что мы делаем каскадное удаление,обновление и остальное.
 * То есть мы говорим системе, чтобы она удалила всех авторов и все книги, которые эти авторы напсиали
 */
public class Author implements Serializable{

	private static final long serialVersionUID = -8428841473039448125L;
	
	@Id
	String name;
	LocalDate birthDate;
	
	@ManyToMany(mappedBy = "authors", cascade = CascadeType.REMOVE)
	Set<Book> books;//Добавляем, чтобы реализовать Bi-directional
	
	public Author(String name, LocalDate birthDate) {
		super();
		this.name = name;
		this.birthDate = birthDate;
	}

	
}
