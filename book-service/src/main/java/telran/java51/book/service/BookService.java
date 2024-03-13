package telran.java51.book.service;

import telran.java51.book.dto.AuthorDto;
import telran.java51.book.dto.BookDto;

public interface BookService {

	boolean addBook(BookDto bookDto);
	BookDto findBookByIsbn(String isbn);
	BookDto deleteBookByIsbn(String isbn);
	BookDto updateBookTitle(String isbn, String newTitle);
	Iterable <BookDto> findAllBooksByAuthor (String author);
	Iterable <BookDto> findAllBooksByPublisher (String publisher);
	Iterable<AuthorDto> findAllAuthorsByBook(String isbn);
	Iterable<String> findPublisherByAuthor(String author);
	AuthorDto deleteAuthor(String author);
}
