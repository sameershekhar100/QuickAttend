package com.bugslayers.quickattend.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bugslayers.quickattend.R;
import com.bugslayers.quickattend.students.StudentLoginActivity;
import com.bugslayers.quickattend.teachers.TeacherLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class teacherRegisterActivity extends AppCompatActivity {
    EditText teacherName;
    EditText teacherEmail;
    EditText teacherPassword;
    Button addTeacher;
    String name;
   public String email;
    public String password;
    public FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);
        mAuth = FirebaseAuth.getInstance();
        teacherName = findViewById(R.id.add_newTeacher);
        teacherEmail = findViewById(R.id.teacherEmail);
        teacherPassword = findViewById(R.id.teacherPassword);
        addTeacher = findViewById(R.id.registerTeacher);
        addTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = teacherName.getText().toString().trim();
                email = teacherEmail.getText().toString().trim();
                password = teacherPassword.getText().toString().trim();
                registerTeacher();

            }
        });

    }

    private void registerTeacher() {

        if(name.isEmpty()){
            Toast.makeText(this, "Enter your name!", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Enter your email!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address!", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "okk", Toast.LENGTH_SHORT).show();

            final ProgressDialog progressDialog = new ProgressDialog(teacherRegisterActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Registering the teacher...");
            progressDialog.show();


            mAuth.createUserWithEmailAndPassword(email, password)

                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override

                        public void onComplete(@NonNull Task<AuthResult> task) {

                            Log.d("abcd","problem"+task.isSuccessful());
//                            if(task.isSuccessful()){
//                            Toast.makeText(teacherRegisterActivity.this, "problem", Toast.LENGTH_SHORT).show();

                                Map<String,String> student= new HashMap<>();
                                student.put("name",name);

                                FirebaseDatabase.getInstance().getReference("Teachers")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(student).addOnCompleteListener(new OnCompleteListener<Void>(){

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(teacherRegisterActivity.this,"User has been successfully registered",Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                            Intent intent=new Intent(teacherRegisterActivity.this, TeacherLoginActivity.class);
                                            startActivity(intent);
                                            
                                        }else{
                                            Toast.makeText(teacherRegisterActivity.this,"Failed to register! Try Again!!",Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });

//                         }   else{
//                                Toast.makeText(teacherRegisterActivity.this,"Failed to register! Try Again!!",Toast.LENGTH_LONG).show();
//                                progressDialog.dismiss();
//                            }
                        }
                    });
        }
    }
}