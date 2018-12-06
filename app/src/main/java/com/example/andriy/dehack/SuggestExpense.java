package com.example.andriy.dehack;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.sephiroth.android.library.widget.HListView;


public class    SuggestExpense extends Fragment {
    CollectionReference suggest;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    HListView listSuggest;
    ConstraintLayout addExpense;
    View view;
    FirebaseUser user;
    ArrayList<Suggest> arraySuggest;
    boolean openAdd = false;
    LinearLayout openAddExpense;
    TextView numberSuggest, textButtonOpenAddExpense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_suggest_expense, container, false);
        listSuggest = (HListView) view.findViewById(R.id.listSuggest);
        addExpense = (ConstraintLayout) view.findViewById(R.id.addExpense);
        addExpense.setVisibility(View.INVISIBLE);
        loadSuggest();
        textButtonOpenAddExpense = (TextView) view.findViewById(R.id.textButtonOpenAddExpense);
        numberSuggest = (TextView) view.findViewById(R.id.numberSuggest);
        user = FirebaseAuth.getInstance().getCurrentUser();
        setNumberSuggest();
        LinearLayout buttonAddExpense = (LinearLayout) view.findViewById(R.id.addSuggestExpence);
        buttonAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDataSuggest();
            }
        });
        openAddExpense = (LinearLayout) view.findViewById(R.id.openAddExpense);
        openAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!openAdd) {
                    textButtonOpenAddExpense.setTextColor(Color.WHITE);
                    textButtonOpenAddExpense.setText("Скасувати");
                    openAddExpense.setBackgroundResource(R.drawable.cancel_add_button);
                    addExpense.setVisibility(View.VISIBLE);
                    listSuggest.setVisibility(View.INVISIBLE);
                    openAdd = true;
                } else {
                    textButtonOpenAddExpense.setTextColor(Color.BLACK);
                    textButtonOpenAddExpense.setText("Додати свій проект");
                    openAddExpense.setBackgroundResource(R.drawable.white_rounds);
                    addExpense.setVisibility(View.INVISIBLE);
                    listSuggest.setVisibility(View.VISIBLE);
                    openAdd = false;
                }
            }
        });
        return view;
    }

    private void setNumberSuggest() {
        if (user != null) {
            final DocumentReference docRef = firestore.collection("Користувачі").document(user.getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            double k = Double.valueOf(String.valueOf(document.get("canSuggest")));
                            numberSuggest.setText(String.valueOf((int) k));
                        } else {
                            Log.d("bad", "No such document");
                        }
                    } else {
                        Log.d("bad", "get failed with ", task.getException());
                    }
                }
            });
        } else {
            TextView textSuggest = (TextView) view.findViewById(R.id.textSuggest);
            textSuggest.setText("Щоб підтримувати авторизуйтесь");
        }
    }


    private void loadSuggest() {
        final DialogFragment loadFragment = new Load();
        loadFragment.show(getFragmentManager(), "Comment");
        String path = "/data/user/0/com.example.andriy.dehack/databases/Gromad";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, 0);
        Cursor c = db.query("Gromad", null, null, null, null, null, null);
        c.moveToFirst();
        suggest = firestore.collection(c.getString(c.getColumnIndex("name"))).document("Пропозиції витрат").collection("Пропозиції");
        arraySuggest = new ArrayList<>();
        suggest.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    arraySuggest.add(new Suggest(document.getString("name"), document.getString("price"), document.getString("describe"), document.get("voted").toString()));
                                }
                                SuggestAdapter suggestAdapter = new SuggestAdapter(getActivity(), arraySuggest, user);

                                listSuggest.setClickable(false);
                                listSuggest.setLongClickable(false);
                                listSuggest.setItemsCanFocus(false);
                                listSuggest.setFocusable(false);
                                listSuggest.setAdapter(suggestAdapter);
                                loadFragment.dismiss();
                            } else {


                            }
                        } else {

                            Log.d("ok", "Error getting documents: ", task.getException());
                        }
                    }
                });
        loadFragment.dismiss();

    }

    public void addDataSuggest() {
        if (openAdd) {
            final DialogFragment loadFragment = new Load();
            EditText addSuggestName = (EditText) view.findViewById(R.id.addSuggestName);
            EditText addSuggetsDescribe = (EditText) view.findViewById(R.id.addDescribeSuggest);
            EditText addSuggestPrice = (EditText) view.findViewById(R.id.addSuggestPrice);
            loadFragment.show(getFragmentManager(), "Comment");
            String path = "/data/user/0/com.example.andriy.dehack/databases/Gromad";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, 0);
            Cursor c = db.query("Gromad", null, null, null, null, null, null);
            c.moveToFirst();
            Map<String, Object> data = new HashMap<>();
            data.put("describe", addSuggetsDescribe.getText().toString());
            data.put("name", addSuggestName.getText().toString());
            data.put("price", addSuggestPrice.getText().toString());
            data.put("voted", 0);
            suggest = firestore.collection(c.getString(c.getColumnIndex("name"))).document("Пропозиції витрат").collection("Пропозиції");
            if (checkTextView(addSuggestName) && checkTextView(addSuggestPrice) && checkTextView(addSuggetsDescribe)) {
                suggest.document(addSuggestName.getText().toString()).set(data);
                textButtonOpenAddExpense.setTextColor(Color.BLACK);
                textButtonOpenAddExpense.setText("Додати свій проект");
                openAddExpense.setBackgroundResource(R.drawable.white_rounds);
                addExpense.setVisibility(View.INVISIBLE);
                listSuggest.setVisibility(View.VISIBLE);
                openAdd = false;
                Toast.makeText(getActivity(), "Витрата успіно запропунована", Toast.LENGTH_SHORT).show();
                listSuggest.setAdapter(null);

                loadSuggest();
            }
            loadFragment.dismiss();
        }
    }

    public boolean checkTextView(TextView textView) {
        if (textView.getText().length() == 0) {
            Toast.makeText(getActivity(), "Заповніть усі поля", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
