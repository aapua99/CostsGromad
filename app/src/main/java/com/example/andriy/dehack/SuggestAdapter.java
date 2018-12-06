package com.example.andriy.dehack;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Created by Andriy on 08.02.2018.
 */

public class SuggestAdapter extends ArrayAdapter<Suggest> {
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user;
    int size;
    public SuggestAdapter(Activity context, ArrayList<Suggest> suggests,FirebaseUser user) {
        super(context, 0, suggests);
        size=suggests.size();
        this.user=user;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View listItemView = LayoutInflater.from(getContext()).inflate(
                R.layout.item_suggest, parent, false);


        final Suggest currentPlace = getItem(position);
        TextView idItem=(TextView) listItemView.findViewById(R.id.idItemView);
        idItem.setText(String.valueOf(position+1)+"/"+String.valueOf(size));
        TextView voted=(TextView) listItemView.findViewById(R.id.voted);
        TextView nameSuggest = (TextView) listItemView.findViewById(R.id.suggestName);
        TextView describeSuggest=(TextView)  listItemView.findViewById(R.id.suggestDescribe);
        TextView priceSuggest=(TextView) listItemView.findViewById(R.id.suggestPrice);
        double k=Double.valueOf(currentPlace.getVoted());
        voted.setText(String .valueOf((int)k));
        nameSuggest.setText(currentPlace.getName());
        describeSuggest.setText(currentPlace.getDescribe());
        priceSuggest.setText(currentPlace.getPrice()+" грн");
        if(user!=null){
            db.collection("Користувачі")
                    .document(user.getEmail()).collection("Підтримані")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                  if(document.getId().equals(currentPlace.getName())){
                                        TextView buttonItem=(TextView) listItemView.findViewById(R.id.buttomItemSuggest);
                                        buttonItem.setText("Підтримано");
                                        LinearLayout linearLayout=(LinearLayout) listItemView.findViewById(R.id.suggestExpence);
                                        linearLayout.setClickable(false);
                                        ImageView logoItem=(ImageView) listItemView.findViewById(R.id.logoItem);
                                        logoItem.setImageResource(R.drawable.checked_item);
                                        linearLayout.setBackgroundResource(R.drawable.buttom_clicked_item_suggest);
                                    }
                                }
                            } else {
                                Log.d("bad", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }


        return listItemView;
    }
}
