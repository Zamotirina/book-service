package telran.java51.book.service;

import telran.java51.book.dto.AuthorDto;
import telran.java51.book.dto.BookDto;

public interface BookService {

	boolean addBook(BookDto bookDto);
	BookDto findBookByIsbn(String isbn);

}
