package com.example.sns_project_sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) {//로그인이 안되있는 상태
            myStartActivity(SignUpActivity.class);
        }
        else{//회원가입 & 로그인이 되어있는 상태
            for (UserInfo profile : user.getProviderData()) {
                String name = profile.getDisplayName();
                Log.e("이름", "이름 : "+name);
                if(name != null){
                    if(name.length() == 0){//name 은 null처리가 안됨
                        myStartActivity(MemberActivity.class);
                    }
                }
            }
        }

        findViewById(R.id.LogoutButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.LogoutButton:
                    FirebaseAuth.getInstance().signOut();//do 로그아웃
                    myStartActivity(SignUpActivity.class);
                    break;
            }
        }
    };

    private void myStartActivity(Class c){
        Intent intent=new Intent(this, c);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Login화면에서 Main화면으로 갈때 스택에는 Main->회원가입->Login와 같이 있는데 회원가입,Login 기록에서 제거
        startActivity(intent);
    }

}