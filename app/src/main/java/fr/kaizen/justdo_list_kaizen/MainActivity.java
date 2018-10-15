package fr.kaizen.justdo_list_kaizen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add Date to Main Layout
        TextView viewCurrentDate = (TextView) findViewById(R.id.textCurrentDate);
        Date dateCurrentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        String stringCurrentDate = dateFormat.format(dateCurrentDate);
        viewCurrentDate.setText(stringCurrentDate);

        displayTasksForDay(R.id.layout_tasks, stringCurrentDate);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Ajoute le bouton rose avec le logo de mail
        // TODO: modifier logo mail par logo "+";
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: ajouter une fonction permmetant de créer une tache à ce jour (idée : checkbox demandant l'ajout à la liste de tâches quotidiennes);
            }
        });

        // Ajoute la menu de navigation sur la gauche
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/


    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            // Put some meat on the sandwich
        }
        else {
            // Remove the meat
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Ajoute les tâches d'une journée
    // param layoutId : identifiant du layout au format R.id.etc
    // param day : string de la journée au format 13/10/2018)
    public void displayTasksForDay(@IdRes int layoutId, String day) {
        final String jsonString = IOHelper.stringFromAsset(this, "tasks.json");
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray tasks = object.getJSONArray("daily_tasks");
            LinearLayout layoutTasks = (LinearLayout) findViewById(layoutId);

            for (int i = 0; i < tasks.length(); i++) {
                JSONObject task = tasks.getJSONObject(i);
                String taskName = task.getString("name");
                Integer taskId = task.getInt("id");
                JSONArray days = task.getJSONArray("days");

                // Boucle effectivement sur tous les jours de la tâche
                for (int j = 0; j < days.length(); j++) {
                    JSONObject jsonDay = days.getJSONObject(j);
                    String jsonDate = jsonDay.getString("date").replace("\\/","/");
                    Boolean isChecked = jsonDay.getBoolean("is_checked");
                    jsonDay.put("is_checked", true);
                    // IOHelper.writeToFile(this, "tasks.json", );

                    // Mais ne rajoute la CheckBox uniquement si la date est présente
                    if (jsonDate.equals(day)) {
                        final CheckBox checkboxTask = new CheckBox(this);
                        ViewGroup.LayoutParams params = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        checkboxTask.setLayoutParams(params);
                        checkboxTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CheckBox cb = (CheckBox)v;
                                Integer cbId = cb.getId();
                                if (cb.isChecked())
                                {
                                    Integer test = 0;
                                }
                            }
                        });
                        checkboxTask.setTextSize(26);
                        checkboxTask.setId(taskId);
                        checkboxTask.setText(taskName);
                        checkboxTask.setChecked(isChecked);
                        layoutTasks.addView(checkboxTask);
                    }
                }
                // String cookie = Cookie.toString(object);
            }
        } catch (Exception e) {
            Log.d("ReadReplaceFeedTask", e.getLocalizedMessage());
        }
    }
}