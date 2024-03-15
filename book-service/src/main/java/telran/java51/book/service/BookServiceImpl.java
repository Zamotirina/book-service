package telran.java51.book.service;


import java.util.List;
import java.util.Optional;
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
	
	//@Transactional(readOnly = true) Убрали при переходе на Bi-directional
	@Override
	public Iterable <BookDto> findAllBooksByAuthor(String author) {
		
		/*
		 * Вариант 1. Uni-Uni-directional
		 */
		
		//return bookRepository.findAllBooksByAuthorsName(author).map(x->modelMapper.map(x,BookDto.class)).toList();

		
		/*
		 * Вариант 2. Bi-directional
		 * 
		 * Транзакционность нам тут уже не нужна, потому что мы не возвращаем стрим из метода
		 * 
		 * И находим автора в одно действие
		 */
		
		Author auth = authorRepository.findById(author).orElseThrow(EntityNotFoundException::new);
		
		return auth.getBooks().stream().map(x-> modelMapper.map(x, BookDto.class)).toList();
		
	}

	//@Transactional(readOnly = true) Убрали при переходе на Bi-directional
	@Override
	public Iterable<BookDto> findAllBooksByPublisher(String publisher) {
		
		/*
		 * Вариант 1. Uni-Uni-directional
		 */
	
		
		//return bookRepository.findAllBooksByPublisherPublisherName(publisher).map(x->modelMapper.map(x,BookDto.class)).toList();
		
		/*
		 * Вариант 2. Bi-directional
		 * 
		 * Транзакционность можно убрать, так как все происходит в одно действие
		 */
		
		Publisher pub = publisherRepository.findById(publisher).get();

		return  pub.getBooks().stream().map(x-> modelMapper.map(x, BookDto.class)).toList();
	}

	@Override
	public Iterable<AuthorDto> findAllAuthorsByBook(String isbn) {
		
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		
		return book.getAuthors().stream().map(x-> modelMapper.map(x, AuthorDto.class)).collect(Collectors.toSet());

	}


	@Transactional(readOnly = true) //Добавили при Bi-directional
	@Override
	public Iterable<String> findPublisherByAuthor(String author) {
		
		/*
		 * Вариант 1. Uni-directional
		 * 
		 * Реализуем через длинный Query запрос с join-ами
		 */
		
		//return publisherRepository.findPublishersByAuthor(author);
		
		/*
		 * Вариант 2. bi-directional
		 * 
		 * Добавляем транзакционность
		 */
		
		return publisherRepository.findDistinctByBooksAuthorsName(author).map(x->x.getPublisherName()).toList();
	}
	
	/*
	 * В этом методе можно выбрать сталинский и гитлеровский подход.
	 * 
	 * Сталинский - при удалении автора оставлять книгу, но убирать у нее автора
	 * Гитлеровский - вместе с автором удалять всю книгу, даже если книгу написал коллектив авторов
	 * 
	 * В домашке я делала сталинский вариант, а в классе мы работали только с гитлеровским
	 */

	
	@Transactional
	@Override
	public AuthorDto deleteAuthor(String author) {
		
		/*
		 * Вариант 1. Uni-directional
		 * 
		 * У нас однонаправленная связь, поэтому мы сначала удаляли все книги, а потом удалили автора
		 */
	
		
//		Author auth = authorRepository.findById(author).orElseThrow(EntityNotFoundException::new);
//		
//		bookRepository.deleteBooksByAuthorsName(author);
//		
//		authorRepository.delete(auth);
//		
//		return modelMapper.map(auth, AuthorDto.class);
		
		/*
		 * Вариант 2. Bi-directional
		 * 
		 * Мы добавили двунаправленную связь и каскадное удаление в классе Author
		 * 
		 * Поэтому теперь мы можем удалять все одним действием. Книги удалятся сами
		 */
		
		Author auth = authorRepository.findById(author).orElseThrow(EntityNotFoundException::new);
		
		authorRepository.deleteById(author);
		
		return modelMapper.map(auth, AuthorDto.class);
		
	}

	

}
