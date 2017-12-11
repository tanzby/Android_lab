package pub.tanzby.lab8;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddItemActivity extends AppCompatActivity {

    private Button bnt_sure_to_add;
    private EditText ed_name;
    private  EditText ed_birthday;
    private EditText ed_gift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        init();
        evenBinding();
    }

    private void init()
    {
        bnt_sure_to_add = (Button) findViewById(R.id.bnt_sure);
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_birthday=(EditText)findViewById(R.id.ed_birthday);
        ed_gift =(EditText) findViewById(R.id.ed_gift);

    }

    private void evenBinding()
    {
        bnt_sure_to_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ed_name.getText().toString();

                String birthday = ed_birthday.getText().toString();

                String gift = ed_gift.getText().toString();

                if (name.isEmpty())
                {
                    new AlertDialog.Builder(AddItemActivity.this)
                            .setTitle("警告")
                            .setMessage("请输入名字！")
                            .show();
                }
                else
                {
                    Intent mIntent = new Intent(AddItemActivity.this,
                            MainActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("newContact", new contactItem(name,birthday,gift));
                    mIntent.putExtras(mBundle);
                    setResult(0x1234, mIntent);
                    finish();
                }
            }
        });
    }
}
