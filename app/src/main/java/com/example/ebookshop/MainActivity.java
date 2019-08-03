package com.example.ebookshop;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ebookshop.adapter.BookAdapter;
import com.example.ebookshop.model.Book;
import com.example.ebookshop.model.Category;
import com.example.ebookshop.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private ArrayList<Category> categoryList;
    private ArrayList<Book> bookList;
    private Category selectedCategory;

    private Spinner spinner;
    private RecyclerView rvBooks;

    private BookAdapter bookAdapter;

    public static final int ADD_BOOK_REQUEST_CODE = 1;
    public static final int EDIT_BOOK_REQUEST_CODE = 2;

    private int selectedBookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = findViewById(R.id.spinner);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mainViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                categoryList = (ArrayList<Category>) categories;
                for (Category c: categories) {
                    Log.e("_logCategory", "onChanged: " + c.getCategoryName() );
                }
                showOnSpinner();
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddAndEditActivity.class);
                startActivityForResult(intent, ADD_BOOK_REQUEST_CODE);
            }
        });
    }

    private void showOnSpinner() {
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<Category>(this, R.layout.item_spinner, categoryList);
        categoryArrayAdapter.setDropDownViewResource(R.layout.item_spinner);
        spinner.setAdapter(categoryArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = (Category) parent.getItemAtPosition(position);
                String message = " id is " + selectedCategory.getId() + "\n name is " + selectedCategory.getCategoryName() + "\n email is " + selectedCategory.getCategoryDescription();

                Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                loadBooksArrayList(selectedCategory.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do your magic
            }
        });
    }

    private void loadBooksArrayList(int categoryId) {
        mainViewModel.getBooksOfSelectedCategory(categoryId).observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable List<Book> books) {
                bookList = (ArrayList<Book>) books;
                loadRecyclerView();
            }
        });
    }

    private void loadRecyclerView() {
        rvBooks = findViewById(R.id.rv_books);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        rvBooks.setHasFixedSize(true);
        bookAdapter = new BookAdapter();
        rvBooks.setAdapter(bookAdapter);

        bookAdapter.setBooks(bookList);
        bookAdapter.setListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                selectedBookId = book.getBookId();
                Intent intent = new Intent(MainActivity.this, AddAndEditActivity.class);
                intent.putExtra(AddAndEditActivity.BOOK_ID, selectedBookId);
                intent.putExtra(AddAndEditActivity.BOOK_NAME, book.getBookName());
                intent.putExtra(AddAndEditActivity.BOOK_PRICE, book.getUnitPrice());
                startActivityForResult(intent, EDIT_BOOK_REQUEST_CODE);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Book bookToDelete = bookList.get(viewHolder.getAdapterPosition());
                mainViewModel.deleteBook(bookToDelete);
            }
        }).attachToRecyclerView(rvBooks);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int selectedCategoryId = selectedCategory.getId();
        if (requestCode==ADD_BOOK_REQUEST_CODE && resultCode==RESULT_OK) {
            Book book = new Book();
            book.setCategoryId(selectedCategoryId);
            book.setBookName(data.getStringExtra(AddAndEditActivity.BOOK_NAME));
            book.setUnitPrice(data.getStringExtra(AddAndEditActivity.BOOK_PRICE));
            mainViewModel.addNewBook(book);
        } else if (requestCode==EDIT_BOOK_REQUEST_CODE && resultCode==RESULT_OK) {
            Book book = new Book();
            book.setCategoryId(selectedCategoryId);
            book.setBookName(data.getStringExtra(AddAndEditActivity.BOOK_NAME));
            book.setUnitPrice(data.getStringExtra(AddAndEditActivity.BOOK_PRICE));
            book.setBookId(selectedBookId);
            mainViewModel.updateBook(book);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
