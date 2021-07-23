package com.example.sns_project_sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemberActivity extends AppCompatActivity {

    private static final String TAG = "MemberInitActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        // Initialize Firebase Auth

        findViewById(R.id.checkButton).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //출처: https://dsnight.tistory.com/14 [Development Assemble]
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.checkButton:
                    profileUpdate();
                    break;
            }
        }
    };

    private void profileUpdate(){
        String name = ((EditText)findViewById(R.id.nameEditText)).getText().toString();
        String phoneNumber = ((EditText)findViewById(R.id.phoneNumberEditText)).getText().toString();
        String birthDay = ((EditText)findViewById(R.id.birthdayEditText)).getText().toString();
        String address = ((EditText)findViewById(R.id.addressEditText)).getText().toString();

        if(name.length() > 0 && phoneNumber.length() > 9 && birthDay.length() > 5 && address.length() > 0) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            MemberInfo memberInfo = new MemberInfo(name, phoneNumber, birthDay, address);//데이터 추가->커스텀객체
            if(user != null){
                db.collection("users").document(user.getUid()).set(memberInfo)//데이터 추가->커스텀객체
                        .addOnSuccessListener(new OnSuccessListener<Void>() {//~
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("회원정보 등록에 성공하였습니다.");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원정보 등록에 실패패하였습다.");
                                Log.w(TAG, "Error writing document", e);
                            }
                        });//~데이터 추가 -> 데이터 유형
            }

        }//if_length
        else {
            startToast("회원정보를 입력해 주세요.");
        }//else_length
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}