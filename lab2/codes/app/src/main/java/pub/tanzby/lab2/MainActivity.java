package pub.tanzby.lab2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button signInBnt;
    private Button registBnt;
    private EditText stuId;
    private EditText stuPw;
    private TextInputLayout TIL_Id;
    private TextInputLayout TIL_Pw;
    private RadioGroup IndentifySel;
    private Boolean isStudent;
    private ImageView Gravatar;

    private String dialogSelectStr[] = new String[]{"拍摄", "从相册选择"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();  // 初始化控件选择
        evenBind(); // 事件绑定
    }

    private void showInfo_Snackbar(View v,String  info)
    {
        Snackbar.make(v.getRootView(), info,Snackbar.LENGTH_SHORT)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {/*TO-DO*/}
                })
                .setDuration(5000)
                .setActionTextColor(Color.WHITE)
                .show();
    }
    /**** 监听输入框变化 ****/
    private TextWatcher update()
    {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TIL_Id.setErrorEnabled(false);
                TIL_Pw.setErrorEnabled(false);
            }
        };
    }
    /**** 绑定控件 ****/
    private void init()
    {
        signInBnt=(Button) findViewById(R.id.bnt_sign_in);
        registBnt=(Button) findViewById(R.id.bnt_register);
        TIL_Id=(TextInputLayout) findViewById(R.id.TIL_studentnumber);
        TIL_Pw=(TextInputLayout) findViewById(R.id.TIL_studentpassword);
        stuId=TIL_Id.getEditText();
        stuPw=TIL_Pw.getEditText();
        IndentifySel = (RadioGroup) findViewById(R.id.radioGroup);
        Gravatar=(ImageView) findViewById(R.id.gravatarIageView);
        isStudent=true;
    }
    /**** 绑定控件事件 ****/
    private void evenBind()
    {
        Gravatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder  dialogBuilder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("上传头像")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"“您选择了[取消]",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setItems(dialogSelectStr, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String showStr ="您选择了["+dialogSelectStr[which]+"]";
                                Toast.makeText(getApplicationContext(),showStr,Toast.LENGTH_SHORT).show();
                            }
                        });
                dialogBuilder.create().show();

            }
        });
        signInBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = stuId.getText().toString();
                String PW = stuPw.getText().toString();
                if (ID.isEmpty())
                {
                    TIL_Id.setError("学号不能为空");
                }
                else if (PW.isEmpty())
                {
                    TIL_Pw.setError("密码不能为空");
                }
                else if ( Objects.equals(ID, "123456") && Objects.equals(PW, "6666") )
                {
                    TIL_Pw.setErrorEnabled(false);
                    showInfo_Snackbar(v,"登陆成功");
                }
                else
                {
                    TIL_Pw.setErrorEnabled(false);
                    showInfo_Snackbar(v,"账号或密码错误");
                }
            }
        });
        registBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String showStr = (isStudent ? "学生" : "教职工")+"注册功能尚未启用";
                if(isStudent)  showInfo_Snackbar(v,showStr);
                else Toast.makeText(getApplicationContext(),showStr,Toast.LENGTH_SHORT).show();
            }
        });
        IndentifySel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                isStudent = checkedId == R.id.radioButton1;
                showInfo_Snackbar(group.getRootView(),
                        "您选择了"+ (isStudent ? "学生" : "教职工") );
            }
        });
        stuPw.addTextChangedListener(update());
        stuId.addTextChangedListener(update());
    }
}
