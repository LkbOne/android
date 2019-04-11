package com.example.life;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.life.entity.UserEntity;
import com.example.life.net.service.UserService;
import com.example.life.result.ModelResult;
import com.example.life.util.OKHttpUitls;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RegisterDialog extends Dialog {
    private TextInputEditText userNameTIET, phoneTIET, accountTIET, passwordTIET, confirmPasswordTIET, emailTIET, descTIET;
    private Button registerBtn;
    private UserService userService;
    private Gson gson = new Gson();
    private Boolean passwordFlag = false;
    public RegisterDialog(@NonNull Context context) {
        super(context);
        initDialog();
        initRegisterView();
    }
    void initDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_register);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        initViewId();
        userService = new UserService();
    }
    void initViewId(){
        userNameTIET = (TextInputEditText)findViewById(R.id.UserNameTIET);
        phoneTIET = (TextInputEditText)findViewById(R.id.PasswordTIET);
        accountTIET = (TextInputEditText)findViewById(R.id.AccountTIET);
        passwordTIET = (TextInputEditText)findViewById(R.id.PasswordTIET);
        confirmPasswordTIET = (TextInputEditText)findViewById(R.id.ComfirmTIET);
        emailTIET = (TextInputEditText)findViewById(R.id.EmailTIET);
        descTIET = (TextInputEditText)findViewById(R.id.DescTIET);

        registerBtn = (Button)findViewById(R.id.RegisterBtn);
    }
    void initRegisterView(){
        confirmPasswordTIET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String password = passwordTIET.getText().toString();
                String confirmPassword = confirmPasswordTIET.getText().toString();
                if(!password.equals(confirmPassword) && !confirmPassword.isEmpty()){

                    Toast.makeText(getContext(), "俩次密码输入不相同，请重新输入", Toast.LENGTH_SHORT).show();
                    passwordFlag = false;
                }else if(password.equals(confirmPassword) && !confirmPassword.isEmpty()){
                    passwordFlag = true;
                }else {
                    passwordFlag = false;
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameTIET.getText().toString();
                String phone = phoneTIET.getText().toString();
                String account = accountTIET.getText().toString();
                String password = passwordTIET.getText().toString();
                String confirmPassword = confirmPasswordTIET.getText().toString();
                String email = emailTIET.getText().toString();
                String desc = descTIET.getText().toString();
                if(userName.isEmpty() || account.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()){
                    Toast.makeText(getContext(), "有必填项为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!passwordFlag){
                    Toast.makeText(getContext(), "俩次密码输入不相同，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserEntity user = new UserEntity();
                user.setName(userName);
                user.setPhone(phone);
                user.setAccount(account);
                user.setPassword(password);
                user.setEmail(email);
                user.setDesc(desc);

                userService.register(user);
                userService.registerHttp.setOnOKHttpGetListener(new OKHttpUitls.OKHttpGetListener() {
                    @Override
                    public void error(String error) {

                    }
                    @Override
                    public void success(String json) {
                        ModelResult verifyResult = gson.fromJson(json,new TypeToken<ModelResult>(){}.getType());
                        if(verifyResult.getErrCode() == 0){
                            Toast.makeText(getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }else {
                            Toast.makeText(getContext(), verifyResult.getErrMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
