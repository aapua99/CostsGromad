package com.example.andriy.dehack;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andriy on 09.02.2018.
 */

public class SuggestInformation {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public void uploadInformation(View view) {

        TextView nameSuggest = (TextView) view.findViewById(R.id.suggestName);
        final TextView voted = (TextView) view.findViewById(R.id.voted);
        String path = "/data/user/0/com.example.andriy.dehack/databases/Gromad";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, 0);
        Cursor c = db.query("Gromad", null, null, null, null, null, null);
        c.moveToFirst();
        final DocumentReference suggest = firestore.collection(c.getString(c.getColumnIndex("name"))).document("Пропозиції витрат").collection("Пропозиції").document(nameSuggest.getText().toString());
        firestore.runTransaction(new Transaction.Function<Double>() {
            @Override
            public Double apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(suggest);
                double newVoted = snapshot.getDouble("voted") + 1;
                transaction.update(suggest, "voted", newVoted);

                return newVoted;

            }
        }).addOnSuccessListener(new OnSuccessListener<Double>() {
            @Override
            public void onSuccess(Double result) {
                Log.d("ok", "Transaction success: " + result);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ok", "Transaction failure.", e);
                    }
                });
        final DocumentReference docRef = firestore.collection("Користувачі").document(user.getEmail());
        firestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(docRef);
                double newPopulation = snapshot.getDouble("canSuggest") - 1;
                transaction.update(docRef, "canSuggest", newPopulation);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("ok", "Transaction success!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ok", "Transaction failure.", e);
                    }
                });

        voted.setText(String.valueOf(Integer.valueOf(voted.getText().toString()) + 1));
        CollectionReference collectionReference = firestore.collection("Користувачі").document(user.getEmail()).collection("Підтримані");
        Map<String, Object> map = new HashMap<>();
        map.put("suggest", "true");
        collectionReference.document(nameSuggest.getText().toString()).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ok", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ok", "Error writing document", e);
                    }
                });
        TextView buttonItem = (TextView) view.findViewById(R.id.buttomItemSuggest);
        buttonItem.setText("Підтримано");
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.suggestExpence);
        linearLayout.setClickable(false);
        ImageView logoItem = (ImageView) view.findViewById(R.id.logoItem);
        logoItem.setImageResource(R.drawable.checked_item);
        linearLayout.setBackgroundResource(R.drawable.buttom_clicked_item_suggest);
    }

}
