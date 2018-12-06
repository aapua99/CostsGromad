package com.example.andriy.dehack;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {
    LinearLayout next;
    int phoneNumber;
    boolean mVerificationInProgress;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    EditText numberMobile, email, firstName, lastName, password;
    CircleImageView avatarImage;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        if(auth.getCurrentUser()!=null){
            Intent intent=new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        getSupportActionBar().hide();
        TextView skip = (TextView) findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        avatarImage=(CircleImageView) findViewById(R.id.avatarImage);
        next = (LinearLayout) findViewById(R.id.next);
        numberMobile = (EditText) findViewById(R.id.numberMobile);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!isEmpty(firstName) && !isEmpty(lastName) && !isEmpty(email) && !isEmpty(password) && !isEmpty(numberMobile)) {
                    auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                setUserInformation();
                                uploadImageToDataBase();
                                Intent intent=new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),"Помилка реєстрації",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }


        });


    }


    public boolean isEmpty(EditText editText) {
        if (editText.length() == 0) {
            Toast.makeText(getApplicationContext(),"Заповніть усі поля",Toast.LENGTH_SHORT).show();
            return true;
        } else {

            return false;
        }
    }


    public  void setUserInformation(){
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", firstName.getText().toString());
        data.put("lastName", lastName.getText().toString());
        data.put("numberMobile",numberMobile.getText().toString());
        data.put("canSuggest",5);
        db.collection("Користувачі")
                .document(email.getText().toString())
                .set(data) .addOnSuccessListener(new OnSuccessListener<Void>() {
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



    }

    public  void  setImage(View view){
        Intent getImage = new Intent(Intent.ACTION_PICK);
        getImage.setType("image/*");
        startActivityForResult(getImage, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent getImage) {
        super.onActivityResult(requestCode, resultCode, getImage);
        Bitmap logoImage = null;
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri SelectedImage = getImage.getData();
                try {
                    logoImage = MediaStore.Images.Media.getBitmap(getContentResolver(), SelectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                avatarImage.setImageBitmap(logoImage);
            }
        }
    }
    public void uploadImageToDataBase() {

        StorageReference storageRef = storage.getReference();


        StorageReference mountainsRef = storageRef.child(email.getText().toString() + ".jpg");


        StorageReference mountainImagesRef = storageRef.child("user/" + email.getText().toString() + ".jpg");

        mountainsRef.getName().equals(mountainImagesRef.getName());
        mountainsRef.getPath().equals(mountainImagesRef.getPath());
        avatarImage.setDrawingCacheEnabled(true);
        avatarImage.buildDrawingCache();
        Bitmap bitmap = avatarImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }

}
