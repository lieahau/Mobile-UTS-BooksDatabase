package id.ac.umn.projectuts_00000012802;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataActivity extends AppCompatActivity {
    List<Book> listBook = new ArrayList<>();
    BookDbHelper dbHelper;
    LinearLayout container;
    TextView dataTitle, dataAuthor;
    ImageButton btnFavorite;
    Button searchButton;
    EditText searchEdit;
    String filterText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

//        START CONFIGURE ACTION BAR
        setTitle("Star Book Dashboard");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(android.R.drawable.star_big_on);
        actionBar.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.color.ThemeSilverBlue, null));
//        END CONFIGURE ACTION BAR

//        UNFOCUS EDITTEXT
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        START PREPARING
        Toast.makeText(getApplicationContext(), "Welcome to Star Books App!", Toast.LENGTH_SHORT).show();
        container = findViewById(R.id.container);
        dbHelper = new BookDbHelper(getApplicationContext());
        dbHelper.createDatabase();

//        SEARCH FEATURE
        searchButton = findViewById(R.id.search_button);
        searchEdit = findViewById(R.id.search_bar);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterText = searchEdit.getText().toString();
                List<Book> filteredData = getFilteredData(filterText);
                showData(filteredData);
            }
        });
//        END PREPARING
    }

//    GET ALL BOOKS THEN SHOW IT
    @Override
    protected void onResume() {
        listBook = dbHelper.getAllBooks();
        showData(listBook);
        super.onResume();
    }

//    SHOW DATA BASED ON LIST<BOOK> ON PARAMETER
    public void showData(List<Book> listData){
        if(container.getChildCount() > 0)
            container.removeAllViews(); // Remove all views first so will have no duplicates data

//        START ITERATE THROUGH LIST
        for (Book book : listData) {
            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View addView = inflater.inflate(R.layout.row_data, null);
            dataTitle = addView.findViewById(R.id.dataTitle);
            dataAuthor = addView.findViewById(R.id.dataAuthor);
            btnFavorite = addView.findViewById(R.id.favorite);

            dataTitle.setText(book.getTitle());
            dataAuthor.setText(book.getAuthor());

            container.addView(addView);
//            START FAVORITE CONFIG
            final Book obj = book;
            final ImageButton imgButton = btnFavorite;
            final ContentValues cv = new ContentValues();

            if(book.getFavorite() == 0)
                imgButton.setImageResource(android.R.drawable.star_big_off);
            else
                imgButton.setImageResource(android.R.drawable.star_big_on);

            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(obj.getFavorite() == 0) {
                        imgButton.setImageResource(android.R.drawable.star_big_on);
                        cv.put("FAVORITE", 1);
                        obj.setFavorite(1);
                        Toast.makeText(getApplicationContext(), "This book is now added to My Favorites", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        imgButton.setImageResource(android.R.drawable.star_big_off);
                        cv.put("FAVORITE", 0);
                        obj.setFavorite(0);
                        Toast.makeText(getApplicationContext(), "This book is now removed from My Favorite", Toast.LENGTH_SHORT).show();
                    }
                    dbHelper.updateDatabase(cv, obj.getAsin()); // Update database after clicked favorite button
                }
            });
//            END FAVORITE CONFIG

//            START CONFIG EACH CARDVIEW FOR DETAILS DATA
            final View details = addView;
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DataActivity.this, DetailActivity.class);
                    intent.putExtra("asin", obj.getAsin());
                    startActivity(intent);
                }
            });
//            END CONFIG EACH CARDVIEW FOR DETAILS DAA
        }
//        END ITERATE THROUGH LIST
    }

//    SORT TITLE
//    PARAMETER SORTTYPE: 0 = ASCENDING, 1 = DESCENDING
    public void sortTitle(final int sortType){
        List<Book> filteredData = getFilteredData(filterText);

        Collections.sort(filteredData, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return sortType==1? o1.getTitle().compareToIgnoreCase(o2.getTitle()) * -1 : o1.getTitle().compareToIgnoreCase(o2.getTitle());
            }
        });
        showData(filteredData); // SHOW DATA AFTER SORT
    }

//    SORT AUTHOR
//    PARAMETER SORTTYPE: 0 = ASCENDING, 1 = DESCENDING
    public void sortAuthor(final int sortType){
        List<Book> filteredData = getFilteredData(filterText);

        Collections.sort(filteredData, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return sortType==1? o1.getAuthor().compareToIgnoreCase(o2.getAuthor()) * -1 : o1.getAuthor().compareToIgnoreCase(o2.getAuthor());
            }
        });
        showData(filteredData); // SHOW DATA AFTER SORT
    }

//    SEARCH DATA BASED ON FILTER STRING
    public List<Book> getFilteredData(String filter){
        List<Book> filteredData = new ArrayList<>();

        if(!filter.isEmpty()){
            for(Book book : listBook){
                if(book.getTitle().toLowerCase().contains(filter.toLowerCase()))
                    filteredData.add(book);
            }
        }
        else
            filteredData = listBook;

        return filteredData;
    }

//    ACTION BAR MENU CONFIG
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sortTitleAsc:
                sortTitle(0);
                Toast.makeText(getApplicationContext(), "Sort books by title ascending", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.sortTitleDesc:
                sortTitle(1);
                Toast.makeText(getApplicationContext(), "Sort books by title descending", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.sortAuthorAsc:
                sortAuthor(0);
                Toast.makeText(getApplicationContext(), "Sort books by author ascending", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.sortAuthorDesc:
                sortAuthor(1);
                Toast.makeText(getApplicationContext(), "Sort books by author descending", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.myfavorites:
                Intent intent = new Intent(DataActivity.this, FavoritesActivity.class);
                startActivity(intent);
                return true;

            case R.id.aboutme:
                intent = new Intent(DataActivity.this, AboutMeActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }
}
