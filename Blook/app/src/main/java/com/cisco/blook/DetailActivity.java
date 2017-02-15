package com.cisco.blook;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView_link;
    ImageView imageView_cover;
    TextView textView_title, textView_author, textView_list_price, textView_sale_price, textView_description;
    BookItem bookItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
        initModel();
        setView();
        initListener();
    }

    private void initModel() {
        bookItem = new BookItem();

        bookItem.cover_s_url = getIntent().getExtras().getString("cover_s_url");
        bookItem.title = getIntent().getExtras().getString("title");
        bookItem.author = getIntent().getExtras().getString("author");
        bookItem.author_t = getIntent().getExtras().getString("author_t");
        bookItem.list_price = getIntent().getExtras().getString("list_price");
        bookItem.sale_yn = getIntent().getExtras().getString("sale_yn");
        bookItem.sale_price = getIntent().getExtras().getString("sale_price");
        bookItem.description = getIntent().getExtras().getString("description");
        bookItem.link = getIntent().getExtras().getString("link");
    }

    private void initView() {

        imageView_link = (ImageView) findViewById(R.id.button_link);
        imageView_cover = (ImageView) findViewById(R.id.imageView_cover);
        textView_title = (TextView) findViewById(R.id.textView_title);
        textView_author = (TextView) findViewById(R.id.textView_author);
        textView_list_price = (TextView) findViewById(R.id.textView_list_price);
        textView_sale_price = (TextView) findViewById(R.id.textView_sale_price);
        textView_description = (TextView) findViewById(R.id.textView_description);
    }

    private void initListener() {
        imageView_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bookItem.link)));
            }
        });
    }

    private void setView() {

        Glide.with(getApplicationContext()).load(bookItem.cover_s_url).into(imageView_cover);
        textView_title.setText(bookItem.title);

        if (!bookItem.author_t.equals(""))
            textView_author.setText("저자 : " + bookItem.author_t);
        else
            textView_author.setText("저자 : " + bookItem.author);

        textView_list_price.setText("정가 : " + bookItem.list_price);

        if (bookItem.sale_yn.equals("Y"))
            textView_sale_price.setText("할인가 : " + bookItem.sale_price);
        else
            textView_sale_price.setText("정가제 상품입니다.");

        textView_description.setText(bookItem.description);
    }
}
