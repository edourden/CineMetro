package cinemetroproject.cinemetro;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.parse.Parse;

import java.util.Locale;

public class MainMenu extends ActionBarActivity {

    //Buttons
    private Button navigationButton;
    private Button linesButton;
    private Button aboutButton;
    private String[] menu;
    private DrawerLayout dLayout;
    private ListView dList;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.initializeDB();
        SharedPreferences sPrefs = getSharedPreferences("myAppsPreferences", 0);
        String bCheck = sPrefs.getString("lang", getResources().getString(R.string.language));

        String lang = bCheck;
        LanguageActivity.language = bCheck;

        if (lang.equals("el")) {
            Configuration c = new Configuration(getResources().getConfiguration());
            c.locale = new Locale("el", "EL");
            getResources().updateConfiguration(c, getResources().getDisplayMetrics());
            DbAdapter.getInstance().changeLanguage(Language.GREEK);
        }
        else{
            Configuration c = new Configuration(getResources().getConfiguration());
            c.locale = new Locale("en", "EN");
            getResources().updateConfiguration(c, getResources().getDisplayMetrics());
            DbAdapter.getInstance().changeLanguage(Language.ENGLISH);
        }





        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);

        LanguageActivity.language = getResources().getString(R.string.language);

        navigationButton = (Button) findViewById(R.id.navigation_button);
        navigationButton.setOnClickListener(navigationButtonOnClickListener);

        linesButton = (Button) findViewById(R.id.lines_button);
        linesButton.setOnClickListener(linesButtonOnClickListener);

        aboutButton = (Button) findViewById(R.id.about_button);
        aboutButton.setOnClickListener(aboutButtonOnClickListener);

        menu = new String[]{getResources().getString(R.string.left_scroll_item1), getResources().getString(R.string.left_scroll_item2)};
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ListViewAdapter adapter = new ListViewAdapter(MainMenu.this, menu);
        dList = (ListView) findViewById(R.id.left_drawer);
        dList.setAdapter(adapter);
        dList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                //dLayout.closeDrawers();
                if (position == 0) {
                    Intent intent;
                    if(DbAdapter.getInstance().getActiveUser() == null){
                        intent=new Intent(MainMenu.this, LogIn.class);
                        startActivity(intent);
                    }
                    else {
                        intent = new Intent(MainMenu.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                }
                if (position == 1) {
                    Intent intent = new Intent(MainMenu.this, LanguageActivity.class);
                    startActivity(intent);
                    finish();
                    onStop();
                }
            }
        });

        Parse.initialize(this, "swhW7tnXLp2qdr7ZqbQ1JRCZMuRaQE5CXY12mp7c", "lrNR1Wa2YThA7SjlkitdaCtMmEBJJM69bHcwpifD");
    }

    @Override
    public void onRestart(){
        super.onRestart();

        SharedPreferences sPrefs = getSharedPreferences("myAppsPreferences", 0);
        String bCheck = sPrefs.getString("lang", getResources().getString(R.string.language));

        String lang = bCheck;
        LanguageActivity.language = bCheck;

        if (lang.equals("el")) {
            Configuration c = new Configuration(getResources().getConfiguration());
            c.locale = new Locale("el", "EL");
            getResources().updateConfiguration(c, getResources().getDisplayMetrics());
            DbAdapter.getInstance().changeLanguage(Language.GREEK);
        }
        else{
            Configuration c = new Configuration(getResources().getConfiguration());
            c.locale = new Locale("en", "EN");
            getResources().updateConfiguration(c, getResources().getDisplayMetrics());
            DbAdapter.getInstance().changeLanguage(Language.ENGLISH);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
         int id = item.getItemId();
         if (id == R.id.action_settings) {
             return true;
         }
         //return super.onOptionsItemSelected(item);
         switch (item.getItemId()) {
             case R.id.left_scroll:
                 if (dLayout.isDrawerOpen(Gravity.LEFT))
                     dLayout.closeDrawers();
                 else
                     dLayout.openDrawer(Gravity.LEFT);
                 return true;
            default:
                return super.onOptionsItemSelected(item);
         }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }






    //Starts the map activity when the button Map is pressed
    View.OnClickListener navigationButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(MainMenu.this, MapActivity.class);
            MainMenu.this.startActivity(intent);
        }};

    //Starts the lines activity when the button Lines is pressed
    View.OnClickListener linesButtonOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(MainMenu.this, TabedLinesActivity.class);
            MainMenu.this.startActivity(intent);
        }};

    //Starts the about activity when the button About is pressed
    View.OnClickListener aboutButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(MainMenu.this, AboutActivity.class);
            MainMenu.this.startActivity(intent);
        }};

    private static DbHelper db;
    //Initialize the db and add data
    public void initializeDB()
    {
        db = new DbHelper(this);
        db.setLanguage(getResources().getString(R.string.language));
        DbAdapter.getInstance().setDB(db);
    }


}
