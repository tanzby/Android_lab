package pub.tanzby.lab2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
//                    TIL_Id.setErrorEnabled(true);
                }
                else if (PW.isEmpty())
                {
                    TIL_Pw.setError("密码不能为空");
                    TIL_Id.setErrorEnabled(false);
//                    TIL_Pw.setErrorEnabled(true);
                }
                else if ( Objects.equals(ID, "123456") && Objects.equals(PW, "6666")  )
                {
                    TIL_Pw.setErrorEnabled(false);
                    Snackbar.make(v.getRootView(), "登陆成功",Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    TIL_Pw.setErrorEnabled(false);
                    Snackbar.make(v.getRootView(), "登陆失败",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        registBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String showStr = (isStudent ? "学生" : "教职工")+"注册功能尚未启用";
                if(isStudent)
                {
                    Snackbar.make(v.getRootView(), showStr,Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),showStr,Toast.LENGTH_SHORT).show();
                }
            }
        });
        IndentifySel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                isStudent = checkedId == R.id.radioButton1;
                String showStr = "您选择了"+ (isStudent ? "学生" : "教职工");
                Snackbar.make(group.getRootView(), showStr,Snackbar.LENGTH_SHORT).show();

            }
        });
    }

}
