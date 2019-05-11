package id.ac.umn.projectuts_00000012802;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

//        START CONFIGURE ACTION BAR
        setTitle("About Me");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(android.R.drawable.star_big_on);
//        END CONFIGURE ACTION BAR
    }

//    ACTION BAR MENU CONFIG
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dashboard:
                Intent intent = new Intent(AboutMeActivity.this, DataActivity.class);
                startActivity(intent);;
                return true;

            case R.id.aboutme:
                return true;

            case R.id.myfavorites:
                intent = new Intent(AboutMeActivity.this, FavoritesActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simplified_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
