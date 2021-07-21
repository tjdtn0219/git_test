package com.example.sns_project_sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.SignUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoLoginButton).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed() {//뒤로가기 누르면 바로꺼지게 함
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.SignUpButton:
                    Log.e("클릭","클릭");
                    signUp();
                    break;
                case R.id.gotoLoginButton:
                    myStartActivity(LoginActivity.class);
                    break;
            }
        }
    };

    private void signUp(){
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();
        String passwordCheck = ((EditText)findViewById(R.id.passwordcheckEditText)).getText().toString();

        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            if(password.equals(passwordCheck)){
                mAuth.createUserWithEmailAndPassword(email, password)//회원가입 로직
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startToast("회원가입에 성공하였습니다.");
                                    myStartActivity(MainActivity.class);
                                } else {
                                    if(task.getException() != null){
                                        startToast(task.getException().toString());
                                    }
                                }
                            }
                        });
            }//if_password_equal
            else{
                startToast("비밀번호가 일치하지 않습니다.");
            }//else_password_equal
        }//if_length
        else {
            startToast("이메일 또는 비밀번호를 입력해 주세요.");
        }//else_length

    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c){
        Intent intent=new Intent(this, c);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Login화면에서 Main화면으로 갈때 스택에는 Main->회원가입->Login와 같이 있는데 회원가입,Login 기록에서 제거
        startActivity(intent);
    }

}