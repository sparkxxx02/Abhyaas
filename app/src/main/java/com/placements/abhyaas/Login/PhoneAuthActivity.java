package com.placements.abhyaas.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.placements.abhyaas.MainActivity;
import com.placements.abhyaas.databinding.ActivityPhoneauthBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class PhoneAuthActivity extends AppCompatActivity {

    ActivityPhoneauthBinding binding;
    final String[] str =new String[1];
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneauthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.buttonVerifyPhone.setOnClickListener(v -> {
            //checking for empty fields
            if (!TextUtils.isEmpty(binding.fieldemail.getText().toString()) &&
                    !TextUtils.isEmpty(binding.fieldpwd.getText().toString()) )
            {
                //Toast.makeText(PhoneAuthActivity.this,"clicked",Toast.LENGTH_SHORT).show();
                dologin(binding.fieldemail.getText().toString(), binding.fieldpwd.getText().toString());

            }


            else
                Toast.makeText(PhoneAuthActivity.this,"Please fill all the details",Toast.LENGTH_SHORT).show();
        });

    }

    public void catergory_type(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            str[0]= document.getString("Category");
                            Intent intent=new Intent(PhoneAuthActivity.this, MainActivity.class);
                            intent.putExtra("Category",str[0]);
                            if (str[0].equals("Student"))
                                startActivity(intent);
                            else
                                startActivity(intent);

                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        }
                        else
                        {
                            Toast.makeText(PhoneAuthActivity.this,"unsuccess",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void dologin(String email, String password)
    {

        FirebaseAuth  mAuth= FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            catergory_type();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(PhoneAuthActivity.this,"Enter correct username/password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}