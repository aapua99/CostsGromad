package com.example.andriy.dehack;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.sephiroth.android.library.widget.HListView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment costsGromad, suggest, revunesGromad, mapFragment, searchByObjectFragment;
    FragmentTransaction fragmentTransaction;
    View anonim, users;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser user;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Витрати громади");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        anonim=getLayoutInflater().inflate(R.layout.nav_header_anonim, null);
        users = getLayoutInflater().inflate(R.layout.nav_header, null);
        user=mAuth.getCurrentUser();
        updateUI(user);
        mapFragment=new MapCosts();
        searchByObjectFragment=new SearchByObject();
        revunesGromad=new RevunesGromad();
        suggest=new SuggestExpense();
        costsGromad=new CostsGromad();
        fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentPlace,costsGromad);
        fragmentTransaction.commit();
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
            Intent intent=new Intent(getBaseContext(),ChooseGromada.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent=new Intent();
        int id=item.getItemId();
        if (id == R.id.costs) {
            openCosts();
        } else if (id == R.id.income) {
            openRevunes();
        } else if (id == R.id.suggest) {
            openSuggest();
        }else if(id==R.id.exit){
            exit();
        }else  if(id==R.id.searchObject){
            openSearch();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openSearch() {
        setTitle("Пошук по об'єктах");
        fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlace,searchByObjectFragment);
        fragmentTransaction.commit();
    }

    private void openRevunes() {
        setTitle("Доходи громади");
        fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlace,revunesGromad);
        fragmentTransaction.commit();
    }

    private void openCosts() {
        setTitle("Витрати громади");
        fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlace,costsGromad);
        fragmentTransaction.commit();
    }

    private void openSuggest() {
        setTitle("Запропонувати витрати");
        fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlace,suggest);
        fragmentTransaction.commit();
    }

    public void updateUI(FirebaseUser user){
        if(user!=null) {
            navigationView.removeHeaderView(anonim);
            navigationView.addHeaderView(users);
            GetUserInformation getInformation = new GetUserInformation();
            TextView nameText = (TextView) users.findViewById(R.id.firstName);
            ImageView userAvatar=(ImageView) users.findViewById(R.id.avatarImage);
            getInformation.getInformation(user.getEmail(), nameText, userAvatar);
            ((TextView) users.findViewById(R.id.email)).setText(user.getEmail());
        }else{
            navigationView.removeHeaderView(users);
            navigationView.addHeaderView(anonim);
        }

    }
    public  void Click(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.signUp:
                intent=new Intent(this.getBaseContext(),SignUp.class);
                startActivityForResult(intent,0);
                break;
            case R.id.signIn:
              intent=new Intent(this.getBaseContext(),SignIn.class);
                startActivityForResult(intent,0);

                break;
            case R.id.suggestExpence:
               suggestExpense(view);
               break;
        }
    }
    public void suggestExpense(View view){
        if(user!=null) {
            SuggestInformation suggestInformation = new SuggestInformation();
            View parent = (View) view.getParent();
            View parent2 = (View) parent.getParent();
            HListView lv = (HListView) parent2.getParent();
            View linear = (View) lv.getParent();
            int position = lv.getPositionForView(parent);
            TextView numberSuggest = (TextView) linear.findViewById(R.id.numberSuggest);
            if (Integer.valueOf(numberSuggest.getText().toString()) > 0) {
                int number = Integer.valueOf(numberSuggest.getText().toString()) - 1;
                suggestInformation.uploadInformation(parent2);
                numberSuggest.setText(String.valueOf(number));
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Повідомлення")
                        .setMessage("У вас більше нема змоги підтримувати на цей місяць.\nДочекайтесь наступного місяця.")
                        .setCancelable(false)
                        .setNegativeButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Повідомлення")
                    .setMessage("Аби підтримувати авторизуйтесь.")
                    .setCancelable(false)
                    .setNegativeButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    public  void exit(){
        if(user!=null){
            mAuth.signOut();
            updateUI(null);
        }
    }


}
