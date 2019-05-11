package id.ac.umn.projectuts_00000012802;

import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    TextView dataTitle, dataAuthor, dataPublisher, dataAsin, dataGroup, dataFormat;

    BookDbHelper dbHelper;
    Book book;
    FloatingActionButton favButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        START CONFIGURE ACTION BAR
        setTitle("Star Book Detail");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(android.R.drawable.star_big_on);
        actionBar.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.color.ThemeSilverBlue, null));
//        END CONFIGURE ACTION BAR

//        PREPARING DATABASE
        dbHelper = new BookDbHelper(getApplicationContext());
        dbHelper.createDatabase();

//        START GET EXTRA DATA
        Bundle bundle = getIntent().getExtras();

        String asin = bundle.getString("asin");
        dataAsin = findViewById(R.id.dataAsin);
        dataAsin.setText(asin);
//        END GET EXTRA DATA

//        START SET DATA
        book = dbHelper.getBook(dataAsin.getText().toString());

        dataFormat = findViewById(R.id.dataFormat);
        dataFormat.setText(book.getFormat());

        dataGroup = findViewById(R.id.dataGroup);
        dataGroup.setText(book.getGroup());

        dataTitle = findViewById(R.id.dataTitle);
        dataTitle.setText(book.getTitle());

        dataAuthor = findViewById(R.id.dataAuthor);
        dataAuthor.setText(book.getAuthor());

        dataPublisher = findViewById(R.id.dataPublisher);
        dataPublisher.setText(book.getPublisher());
//        END SET DATA

//        START FLOATING ACTION BUTTON CONFIG
        favButton = findViewById(R.id.fabFavorite);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                if(book.getFavorite() == 0){
                    favButton.setImageResource(android.R.drawable.star_big_on);
                    cv.put("FAVORITE", 1);
                    book.setFavorite(1);
                    Toast.makeText(getApplicationContext(), "This book is now added to My Favorites", Toast.LENGTH_SHORT).show();
                }
                else{
                    favButton.setImageResource(android.R.drawable.star_big_off);
                    cv.put("FAVORITE", 0);
                    book.setFavorite(0);
                    Toast.makeText(getApplicationContext(), "This book is now removed from My Favorites", Toast.LENGTH_SHORT).show();
                }
                dbHelper.updateDatabase(cv, book.getAsin());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(book.getFavorite() == 0){
            favButton.setImageResource(android.R.drawable.star_big_off);
        }
        else{
            favButton.setImageResource(android.R.drawable.star_big_on);
        }
    }
//    END FLOATING BUTTON CONFIG

//    ACTION BAR MENU CONFIG
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.dashboard:
                Intent intent = new Intent(DetailActivity.this, DataActivity.class);
                startActivity(intent);
                return true;

            case R.id.myfavorites:
                intent = new Intent(DetailActivity.this, FavoritesActivity.class);
                startActivity(intent);
                return true;

            case R.id.aboutme:
                intent = new Intent(DetailActivity.this, AboutMeActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simplified_menu, menu);

        return true;
    }
}
