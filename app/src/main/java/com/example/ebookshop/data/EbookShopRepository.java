package com.example.ebookshop.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.example.ebookshop.data.local.BookDAO;
import com.example.ebookshop.data.local.BookDatabase;
import com.example.ebookshop.data.local.CategoryDAO;
import com.example.ebookshop.model.Book;
import com.example.ebookshop.model.Category;

import java.util.List;

public class EbookShopRepository {
    private CategoryDAO categoryDAO;
    private BookDAO bookDAO;
    private LiveData<List<Category>> categories;
    private LiveData<List<Book>> books;

    public EbookShopRepository(Application application) {
        BookDatabase bookDatabase = BookDatabase.getInstance(application);
        categoryDAO = bookDatabase.categoryDAO();
        bookDAO = bookDatabase.bookDAO();
    }

    public LiveData<List<Category>> getCategories() {
        return categoryDAO.getAllCategories();
    }

    public LiveData<List<Book>> getBooks(int categoryId) {
        return bookDAO.getBooks(categoryId);
    }

    // CATEGORY
    public void insertCategory(Category category) {
        new InsertCategoryAsyncTask(categoryDAO).execute(category);
    }

    public void updatecategory(Category category) {
        new UpdateCategoryAsyncTask(categoryDAO).execute(category);
    }

    public void deleteCategory(Category category) {
        new DeleteCategoryAsyncTask(categoryDAO).execute(category);
    }

    // BOOK
    public void insertBook(Book book) {
        new InsertBookAsyncTask(bookDAO).execute(book);
    }

    public void updateBook(Book book) {
        new UpdateBookAsyncTask(bookDAO).execute(book);
    }

    public void deleteBook(Book book) {
        new DeleteBookAsyncTask(bookDAO).execute(book);
    }

    // Async Category
    private static class InsertCategoryAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDAO categoryDAO;

        public InsertCategoryAsyncTask(CategoryDAO categoryDAO) {
            this.categoryDAO = categoryDAO;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDAO.insert(categories[0]);
            return null;
        }
    }

    private static class UpdateCategoryAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDAO categoryDAO;

        public UpdateCategoryAsyncTask(CategoryDAO categoryDAO) {
            this.categoryDAO = categoryDAO;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDAO.update(categories[0]);
            return null;
        }
    }

    private static class DeleteCategoryAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDAO categoryDAO;

        public DeleteCategoryAsyncTask(CategoryDAO categoryDAO) {
            this.categoryDAO = categoryDAO;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDAO.delete(categories[0]);
            return null;
        }
    }

    // Async Book
    private static class InsertBookAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDAO bookDAO;

        public InsertBookAsyncTask(BookDAO bookDAO) {
            this.bookDAO = bookDAO;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDAO.insert(books[0]);
            return null;
        }
    }

    private static class UpdateBookAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDAO bookDAO;

        public UpdateBookAsyncTask(BookDAO bookDAO) {
            this.bookDAO = bookDAO;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDAO.update(books[0]);
            return null;
        }
    }

    private static class DeleteBookAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDAO bookDAO;

        public DeleteBookAsyncTask(BookDAO bookDAO) {
            this.bookDAO = bookDAO;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDAO.delete(books[0]);
            return null;
        }
    }

}


















