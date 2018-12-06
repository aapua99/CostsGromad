package com.example.andriy.dehack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Andriy on 09.02.2018.
 */
public class GetUserInformation {
    User user;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    Bitmap bmp;

    GetUserInformation() {
    }

    public void getInformation(String email, final TextView textView, ImageView imageView) {
        user = new User();
        dataBase.collection("Користувачі")
                .document(email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        textView.setText(documentSnapshot.getString("firstName")+" "+documentSnapshot.getString("lastName"));
                    }
                });
        getUserAvatar(email,imageView);
    }

    private void getUserAvatar(String email, final ImageView imageView) {
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(email + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }
}
