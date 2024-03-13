package telran.java51.book.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Publisher implements Serializable{

	private static final long serialVersionUID = -232655132943852677L;
	
	@Id
	String publisherName;

	/*
	 * Добавляем, иначе мы будем получать вместо имени результат непереопрееленного String, то есть ссылку на объект,
	 * потому что modelMapper мапит объект в стринг через непрепределенный string
	 */
	@Override
	public String toString() {
		return publisherName;
	}
	
	
}
