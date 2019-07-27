package com.example.ebookshop.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.ebookshop.data.EbookShopRepository;
import com.example.ebookshop.model.Book;
import com.example.ebookshop.model.Category;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private EbookShopRepository repository;
    private LiveData<List<Category>> allCategories;
    private LiveData<List<Book>> booksOfSelectedCategory;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new EbookShopRepository(application);
    }

    public LiveData<List<Category>> getAllCategories() {
        allCategories = repository.getCategories();
        return allCategories;
    }

    public LiveData<List<Book>> getBooksOfSelectedCategory(int categoryId) {
        booksOfSelectedCategory = repository.getBooks(categoryId);
        return booksOfSelectedCategory;
    }

    public void addNewBook(Book book) {
        repository.insertBook(book);
    }

    public void updateBook(Book book) {
        repository.updateBook(book);
    }

    public void deleteBook(Book book) {
        repository.deleteBook(book);
    }

}


















