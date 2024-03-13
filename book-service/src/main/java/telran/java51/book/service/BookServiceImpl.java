package telran.java51.book.service;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java51.book.dao.AuthorRepository;
import telran.java51.book.dao.BookRepository;
import telran.java51.book.dao.PublisherRepository;
import telran.java51.book.dto.AuthorDto;
import telran.java51.book.dto.BookDto;
import telran.java51.book.dto.exceptions.EntityNotFoundException;
import telran.java51.book.model.Author;
import telran.java51.book.model.Book;
import telran.java51.book.model.Publisher;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
	
	final BookRepository bookRepository;
	final AuthorRepository authorRepository;
	final PublisherRepository publisherRepository;
	final ModelMapper modelMapper;

	
	@Transactional //Используем вариант Spring, в остальных случаях c другими аннотациями - джакарта
	@Override
	public boolean addBook(BookDto bookDto) {
		
		if(bookRepository.existsById(bookDto.getIsbn())) {
			
			return false;
		}
		/*
		 * Этот код не сработает, потому что мы должны соблюдать историчность
		 * Начала добавить автора, потом уже книгу
		 */
	//bookRepository.save(modelMapper.map(bookDto,Book.class));
		
		
		/*
		 * Это два сложных метода. В них мы ищем авторов и издателей и проверяем, есть ли они уже в существующих базах
		 * 
		 * Если их нет, то через orElse мы их туда добавляем
		 * 
		 * Особенно важно, как во втором методе мы их тут же преобразовываем
		 */
		
		Publisher publisher = publisherRepository.findById(bookDto.getPublisher())
				.orElse(publisherRepository
						.save(new Publisher(bookDto.getPublisher())));
		
		Set <Author> authors = bookDto.getAuthors().stream()
				.map(x-> authorRepository.findById(x.getName())
						.orElse(authorRepository.save(new Author(x.getName(), x.getBirthDate()))))
				.collect(Collectors.toSet());
		

		Book book= new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);
	
		bookRepository.save(book);
		
	return true;
	
	}

	/*
	 * Не ставим тут аннотацию @Transactional, так как у нас все в одно действие.
	 * 
	 * Плюс нет стримов. То есть даже формат read-only нам тут не нужен
	 */
	@Override
	public BookDto findBookByIsbn(String isbn) {
	
		Book book=bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		
		return modelMapper.map(book, BookDto.class);
	}

	@Transactional
	@Override
	public BookDto deleteBookByIsbn(String isbn) {
		
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		
		bookRepository.delete(book);
		
		return modelMapper.map(book, BookDto.class);
		
	}
	@Transactional
	@Override
	public BookDto updateBookTitle(String isbn, String newTitle) {
		
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);

		book.setTitle(newTitle);
		
		return modelMapper.map(book, BookDto.class);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Iterable <BookDto> findAllBooksByAuthor(String author) {
		
		Author auth = authorRepository.findById(author).orElseThrow(EntityNotFoundException::new);
		
		return auth.getBooks().stream().map(x-> modelMapper.map(x, BookDto.class)).toList();
		
		//return bookRepository.findAllBooksByAuthorsName(author).map(x->modelMapper.map(x,BookDto.class)).toList();
	}

	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	@Override
	public Iterable<BookDto> findAllBooksByPublisher(String publisher) {
		
		Publisher publisher2 = publisherRepository.findById(publisher).orElseThrow(EntityNotFoundException::new);
		
		//return bookRepository.findAllBooksByPublisherPublisherName(publisher).map(x->modelMapper.map(x,BookDto.class)).toList();

		return  publisher2.getBooks().stream().map(x-> modelMapper.map(x, BookDto.class)).toList();
	}

	@Override
	public Iterable<AuthorDto> findAllAuthorsByBook(String isbn) {
		
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);

		
		return book.getAuthors().stream().map(x-> modelMapper.map(x, AuthorDto.class)).collect(Collectors.toSet());

	}


	
	@Override
	public Iterable<String> findPublisherByAuthor(String author) {
		
		
		//return publisherRepository.findByPublishersByAuthor(author);
		
		return publisherRepository.findDistinctByBooksAuthorsName(author).map(x->x.getPublisherName()).toList();
	}
	

	
	@Transactional
	@Override
	public AuthorDto deleteAuthor(String author) {
	
		
//		Author auth = authorRepository.findById(author).orElseThrow(EntityNotFoundException::new);
//		
//		bookRepository.deleteBooksByAuthorsName(author);
//		
//		authorRepository.delete(auth);
//		
//		return modelMapper.map(auth, AuthorDto.class);
		
		Author auth = authorRepository.findById(author).orElseThrow(EntityNotFoundException::new);
		
		authorRepository.deleteById(author);
		
		return modelMapper.map(auth, AuthorDto.class);
		
	}

	

}
