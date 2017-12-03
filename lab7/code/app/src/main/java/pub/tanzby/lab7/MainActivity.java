package pub.tanzby.lab7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mPassSp;
    private SharedPreferences.Editor mSpEditor;


    private Button bnt_ok_reg;
    private Button bnt_clear_reg;
    private Button bnt_regis_reg;

    private EditText ed_pw1,ed_pw2;

    private boolean has_register=false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        evenBinding();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPassSp = getApplicationContext()
                .getSharedPreferences("PASSWORD", Context.MODE_PRIVATE);
        mSpEditor = mPassSp.edit();

        if (mPassSp.getString("PASSWORD",null)!=null) {
            has_register = true;
            ed_pw2.setVisibility(View.INVISIBLE);
            ed_pw1.setHint("Enter your Password");
            ed_pw1.setText("");
        }
    }

    public void init()
    {

        bnt_ok_reg  =(Button) findViewById(R.id.bnt_ok_reg);
        bnt_clear_reg=(Button) findViewById(R.id.bnt_clear_reg);
        bnt_regis_reg = (Button) findViewById(R.id.bnt_exit_reg);
        ed_pw1 = (EditText) findViewById(R.id.ed_pw1);
        ed_pw2 = (EditText) findViewById(R.id.ed_pw2);
    }

    private void evenBinding()
    {
        bnt_ok_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "";
                String pw1_str = ed_pw1.getText().toString();
                if (!has_register)
                {
                    String pw2_str = ed_pw2.getText().toString();
                    if (pw1_str.isEmpty()){
                        msg = "Password cannot be empty!";
                    }
                    else if (pw2_str.isEmpty() || !pw2_str.equals(pw1_str)) {
                        msg = "Confirm password Miss match!";
                    }
                    else {
                        msg = "Register successful";
                        mSpEditor.putString("PASSWORD", pw1_str);
                        mSpEditor.commit();
                        Intent editorIntend = new Intent(MainActivity.this, EditActivity.class);
                        startActivity(editorIntend);
                    }
                }
                else
                {
                    String rpw = mPassSp.getString("PASSWORD", null);
                    if (rpw.equals(pw1_str))
                    {
                        Intent editorIntend = new Intent(MainActivity.this, EditActivity.class);
                        startActivity(editorIntend);
                        msg = "Logging successful";
                    }
                    else {
                        msg = "Wrong Password";
                    }
                }
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });

        bnt_clear_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_pw1.setText("");
                ed_pw2.setText("");
                Toast.makeText(getApplicationContext(),"clear",Toast.LENGTH_SHORT).show();
            }
        });
        bnt_regis_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                has_register = false;
                ed_pw2.setVisibility(View.VISIBLE);
                ed_pw1.setHint("New Password");
                ed_pw2.setHint("Confirm Password");
                Toast.makeText(getApplicationContext(),"exit",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
