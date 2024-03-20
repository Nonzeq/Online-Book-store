package com.kobylchak.bookstore.service;

import com.kobylchak.bookstore.dto.BookDto;
import com.kobylchak.bookstore.dto.CreateBookRequestDto;
import com.kobylchak.bookstore.mapper.BookMapper;
import com.kobylchak.bookstore.model.Book;
import com.kobylchak.bookstore.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookRepository.save(bookMapper.toModel(requestDto));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Optional<Book> bookById = bookRepository.findBookById(id);
        return bookMapper.toDto(bookById.orElseThrow(
                () -> new EntityNotFoundException("Book by id: " + id + "not found")));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBookById(Long id, CreateBookRequestDto bookRequestDto) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book by id: " + id + " not found");
        }
        Book updatedBook = bookMapper.toModel(bookRequestDto);
        updatedBook.setId(id);
        return bookMapper.toDto(bookRepository.save(updatedBook));
    }
}
