package telran.java51.book.dto;

import java.util.Set;

import lombok.Getter;
import telran.java51.book.model.Author;
import telran.java51.book.model.Publisher;

@Getter
public class BookDto {

	String isbn;
	String title;
	Set <AuthorDto> authors;
	String publisher;//Делаем его String, так как именно это мы видим в API документации в Postman.Они не должны совпадать на 100%
}
