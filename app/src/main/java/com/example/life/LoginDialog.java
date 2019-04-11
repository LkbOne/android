package com.example.life;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.life.entity.ClockEntity;
import com.example.life.net.service.UserService;
import com.example.life.result.ModelResult;
import com.example.life.result.user.LoginResult;
import com.example.life.result.user.UserResult;
import com.example.life.util.OKHttpUitls;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import static android.text.InputType.TYPE_CLASS_NUMBER;


public class LoginDialog extends Dialog  {
    private Button loginBtn, verificationBtn, registerBtn;
    private TextView vCodeTX;
    private EditText vCodeETX, contentETX;
    private UserService userService;


    public RegisterDialog getRegisterDialog() {
        return registerDialog;
    }

    public void setRegisterDialog(RegisterDialog registerDialog) {
        this.registerDialog = registerDialog;
    }

    private RegisterDialog registerDialog;

    public View getNavHeaderView() {
        return navHeaderView;
    }

    public void setNavHeaderView(View navHeaderView) {
        this.navHeaderView = navHeaderView;
    }

    private View navHeaderView;
    public String uid;
    public LoginDialog(@NonNull Context context) {
        super(context);
        initDialog();
    }
    void initDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_login);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        initViewId();
        initLoginView();
        userService = new UserService();
    }
    void initViewId(){
        vCodeTX = (TextView)findViewById(R.id.VerificationCodeTX);

        vCodeETX = (EditText)findViewById(R.id.VerificationCodeETX);
        contentETX = (EditText)findViewById(R.id.ContentETX);

        verificationBtn = (Button)findViewById(R.id.VerificationBtn);
        loginBtn = (Button)findViewById(R.id.LoginBtn);
        registerBtn = (Button)findViewById(R.id.RegisterBtn);

    }
    void initLoginView(){

        verificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = contentETX.getText().toString();
                if(email.isEmpty()){
                    // 提示密码为空
                    Toast.makeText(getContext(), "邮箱为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 还有获取验证码事件
                userService.verification(email);
                userService.verifyHttp.setOnOKHttpGetListener(new OKHttpUitls.OKHttpGetListener() {
                    @Override
                    public void error(String error) {

                    }

                    @Override
                    public void success(String json) {
                        Gson gson = new Gson();
                        ModelResult verifyResult = gson.fromJson(json,new TypeToken<ModelResult>(){}.getType());
                        if(verifyResult.getErrCode() == 0){
                            Toast.makeText(getContext(), "请到邮箱查看验证码", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), verifyResult.getErrMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                vCodeTX.setText("验证码");
                contentETX.setInputType(TYPE_CLASS_NUMBER);

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentETX.getText().toString();
                String verificationCode = vCodeETX.getText().toString();

                if(content.isEmpty() || verificationCode.isEmpty()){
                    // 提示密码为空
                    Toast.makeText(getContext(), "账户或密码为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 登录验证
                userService.login(content, verificationCode, 0);
                userService.loginHttp.setOnOKHttpGetListener(new OKHttpUitls.OKHttpGetListener() {
                    @Override
                    public void error(String error) {
                    }
                    @Override
                    public void success(String json) {
                        Gson gson = new Gson();
                        ModelResult<LoginResult> loginResult = gson.fromJson(json,new TypeToken<ModelResult<LoginResult>>(){}.getType());
                        if(loginResult.getErrCode() == 0){
                            uid = loginResult.getData().getId();
                            Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            dismiss();
                            userService.queryUserById(uid);


                            userService.queryByIdHttp.setOnOKHttpGetListener(new OKHttpUitls.OKHttpGetListener() {
                                @Override
                                public void error(String error) {

                                }

                                @Override
                                public void success(String json) {
                                    Gson gson = new Gson();
                                    ModelResult<UserResult> queryUserResult = gson.fromJson(json,new TypeToken<ModelResult<UserResult>>(){}.getType());
                                    if(queryUserResult.getErrCode() == 0) {
                                        TextView userName = (TextView) navHeaderView.findViewById(R.id.UseNameTX);
                                        userName.setText(queryUserResult.getData().getName());
                                        TextView desc = (TextView) navHeaderView.findViewById(R.id.DescTX);
                                        desc.setText(queryUserResult.getData().getEmail());
                                    }
                                }
                            });

                        }else {
                            Toast.makeText(getContext(), loginResult.getErrMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                registerDialog.show();
            }
        });
    }
}
