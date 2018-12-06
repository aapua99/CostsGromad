package com.example.andriy.dehack;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser user;
    LinearLayout signIn;
    TextView signUp;
    EditText editEmail, editPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();
        editEmail=(EditText) findViewById(R.id.editEmail);
        editPassword=(EditText) findViewById(R.id.editPassword);
        user = mAuth.getCurrentUser();
        Log.d("ok",String.valueOf(user));
        signIn=(LinearLayout) findViewById(R.id.buttonSignIn);
        signUp=(TextView) findViewById(R.id.signUp) ;

    }
    public  void signIn(View view){
        signIn.setClickable(false);
        signUp.setClickable(false);
        final DialogFragment loadFragment = new Load();
        loadFragment.show(getFragmentManager(), "Comment");
        if (user==null){
            if(checkEditText(editEmail)&&checkEditText(editPassword)) {
                mAuth.signInWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        loadFragment.dismiss();
                                        Intent intent= new Intent();
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }
                                else {
                                    loadFragment.dismiss();
                                    Toast.makeText(getBaseContext(), "Не вірний логін або пароль", Toast.LENGTH_SHORT).show();
                                    signIn.setClickable(true);
                                    signUp.setClickable(true);
                                }

                            }



                        });
            }
        }
        else{
            Toast.makeText(getBaseContext(),"Ви вже увійшли", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkEditText(EditText editText) {
        if (editText.getText().length() == 0) {
            Toast.makeText(getBaseContext(), "Заповніть усі поля", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    public void Click(View view){
        Intent intent=new Intent(getApplicationContext(),SignUp.class);
        startActivity(intent);
    }

}
