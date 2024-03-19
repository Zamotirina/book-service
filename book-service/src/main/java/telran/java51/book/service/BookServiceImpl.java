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

/*
 * В этом варианте чтобы понять, как работает Hibernate, мы решили реаализовать все руками и
 * убрать интерфейсы Spting-а JpaRepository
 * 
 * После того как мы убрали интерфейсы, тут многое отвалилось, и пришлось менять код немного
 * 
 * Также мы добавили реализацию наших репозиториев: BookRepositoryImpl, AuthorRepositoryImpl и AuthorReposotoryImpl
 */


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
	
	final BookRepository bookRepository;
	final AuthorRepository authorRepository;
	final PublisherRepository publisherRepository;
	final ModelMapper modelMapper;

	
	@Transactional 
	@Override
	public boolean addBook(BookDto bookDto) {
		
		if(bookRepository.existsById(bookDto.getIsbn())) {
			
			return false;
		}

		
//		Publisher publisher = publisherRepository.findById(bookDto.getPublisher())
//				.orElse(publisherRepository
//						.save(new Publisher(bookDto.getPublisher())));
		
		Publisher publisher = publisherRepository.findById(bookDto.getPublisher())
				.orElseGet(()->publisherRepository
						.save(new Publisher(bookDto.getPublisher())));
		
		/*
		 * Переписали метод ниже после удаления интерфейсов
		 */
		
		
//		Set <Author> authors = bookDto.getAuthors().stream()
//				.map(x-> authorRepository.findById(x.getName())
//						.orElse(authorRepository.save(new Author(x.getName(), x.getBirthDate()))))
//				.collect(Collectors.toSet());
		
		
//		Set <Author> authors=bookDto.getAuthors().stream().
//		map(x-> authorRepository.findById(x.getName())
//				.orElse(authorRepository.save(new Author(x.getName(), x.getBirthDate()))))
//		.collect(Collectors.toSet());
		
		/*
		 * Метод выше переписали, чтобы исправить orElse на orElseGet
		 * 
		 * Ранее мы неправильно использовали Optional и метод orElse(), потому что он должен получать объект, а у нас сначала происходил save, а потом уже получает объект, 
		 * соответственно в итоге мы попадаем в то, что такого автора у нас еще нет, и при новой попытке добавить автора приложение слетает, потому что мы пытаемся добавить объект, который уже есть в базе
		 * 
		 * Метод orElseGet() уже принимает функцию, а не объект и возвращает либо значение, если оно есть, либо результат выполнения функции
		 */
		
		Set <Author> authors=bookDto.getAuthors().stream().
		map(x-> authorRepository.findById(x.getName())
				.orElseGet(()->authorRepository.save(new Author(x.getName(), x.getBirthDate()))))
		.collect(Collectors.toSet());

		Book book= new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);
	
		bookRepository.save(book);
		
	return true;
	
	}

	@Override
	public BookDto findBookByIsbn(String isbn) {
	
		Book book=bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		
		return modelMapper.map(book, BookDto.class);
	}


	
	/*
	 * Ошибка 1. Комменитируем @Transactional и получаем, что изменение НЕ фиксируется в базе данных
	 * 
	 *  Даже если добавляем bookRepository.save(book); все равно не срабатывает
	 *  
	 *  Метод сработает только если мы еще в BookReposotoryImpl поставим @Transactional над методом save(), то есть все равно добавим транзакционность, только не в сервисе, а в репозитории
	 *  
	 *  Отсюда мы понимаем, что у Spring Data все операции на изменение (в том числе добавление и удаление) транзакционные 
	 * 
	 *  Потому что чтобы выполнился метод persist() соединение должно оставаться открытым, иначе метод не сработает
	 *  
	 *  Соответственно мы можем ставить @@Transactional  в двух местах. Но если его ставим здесь, то строчка bookRepository.save(book); уже не нужна. Если там, то нужна
	 *  
	 *  Эдуард советует мыслить критическими секциями, как в мальти-трединге
	 */
	@Transactional
	@Override
	public BookDto updateBookTitle(String isbn, String newTitle) {
		
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);

		book.setTitle(newTitle);
		
		//bookRepository.save(book);
		
		return modelMapper.map(book, BookDto.class);
	}
	
	/*
	 * Здесь точно атк же можно перенести @Transactional в репозиторий
	 */
	@Transactional
	@Override
	public BookDto deleteBookByIsbn(String isbn) {
		
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
//		
//		bookRepository.delete(book);
		
		BookDto dto=modelMapper.map(book, BookDto.class);
		
		bookRepository.deleteById(isbn);
		
		return dto;
		
	}
	
	
	//@Transactional(readOnly = true) Все эти методы у нас перестали требовать эту аннотацию, так как стримы которые возвращает Spring- это прокси, поэтому он требовал поддерживать соединение пока не выполнит терминальную операцию, а EntityManager возвращает уже настоящий стрим. То есть мы его поулчили и дальше работаем 
	@Override
	public Iterable <BookDto> findAllBooksByAuthor(String author) {
		
//		Author auth = authorRepository.findById(author).orElseThrow(EntityNotFoundException::new);
//		
//		return auth.getBooks().stream().map(x-> modelMapper.map(x, BookDto.class)).toList();
		
		return bookRepository.findAllBooksByAuthorsName(author).map(x->modelMapper.map(x,BookDto.class)).toList();
		
		
	}

	//@Transactional(readOnly = true) Все эти методы у нас перестали требовать эту аннотацию, так как стримы которые возвращает Spring- это прокси, поэтому он требовал поддерживать соединение пока не выполнит терминальную операцию, а EntityManager возвращает уже настоящий стрим. То есть мы его поулчили и дальше работаем 

	@Override
	public Iterable<BookDto> findAllBooksByPublisher(String publisher) {
		
		//Publisher publisher2 = publisherRepository.findById(publisher).orElseThrow(EntityNotFoundException::new);
		
		return bookRepository.findAllBooksByPublisherPublisherName(publisher).map(x->modelMapper.map(x,BookDto.class)).toList();

		//return  publisher2.getBooks().stream().map(x-> modelMapper.map(x, BookDto.class)).toList();
	}

	@Override
	public Iterable<AuthorDto> findAllAuthorsByBook(String isbn) {
		
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);

		return book.getAuthors().stream().map(x-> modelMapper.map(x, AuthorDto.class)).collect(Collectors.toSet());

	}


	//@Transactional(readOnly = true) Все эти методы у нас перестали требовать эту аннотацию, так как стримы которые возвращает Spring- это прокси, поэтому он требовал поддерживать соединение пока не выполнит терминальную операцию, а EntityManager возвращает уже настоящий стрим. То есть мы его поулчили и дальше работаем 
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
//		authorRepository.deleteById(auth.getName());
//		
//		return modelMapper.map(auth, AuthorDto.class);
		
		Author auth = authorRepository.findById(author).orElseThrow(EntityNotFoundException::new);
		
		authorRepository.deleteById(author);
		
		return modelMapper.map(auth, AuthorDto.class);
		
	}

	

}
