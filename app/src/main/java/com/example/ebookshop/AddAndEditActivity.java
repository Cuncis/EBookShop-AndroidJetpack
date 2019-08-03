package com.example.ebookshop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ebookshop.model.Book;

public class AddAndEditActivity extends AppCompatActivity {

    private EditText etBookName, etUnitPrice;
    private Button btnAddAndEdit;

    private Book book;
    public static final String BOOK_ID = "bookId";
    public static final String BOOK_NAME = "bookName";
    public static final String BOOK_PRICE = "unitPrice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_edit);

        book = new Book();

        etBookName = findViewById(R.id.et_bookName);
        etUnitPrice = findViewById(R.id.et_unitPrice);
        btnAddAndEdit = findViewById(R.id.btn_addAndEdit);

        Intent intent = getIntent();

        if (intent.hasExtra(BOOK_ID)) {
            setTitle("Edit Book");
            book.setBookName(intent.getStringExtra(BOOK_NAME));
            book.setUnitPrice(intent.getStringExtra(BOOK_PRICE));

            etBookName.setText(intent.getStringExtra(BOOK_NAME));
            etUnitPrice.setText(intent.getStringExtra(BOOK_PRICE));
        } else {
            setTitle("Add New Book");
        }

        btnAddAndEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book.setBookName(etBookName.getText().toString().trim());
                book.setUnitPrice(etUnitPrice.getText().toString().trim());
                if (TextUtils.isEmpty(etBookName.getText().toString()) || TextUtils.isEmpty(etUnitPrice.getText().toString())) {
                    Toast.makeText(AddAndEditActivity.this, "The Field can't be null", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(BOOK_NAME, book.getBookName());
                    intent.putExtra(BOOK_PRICE, book.getUnitPrice());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }



}
