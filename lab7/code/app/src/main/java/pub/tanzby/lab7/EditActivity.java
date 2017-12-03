package pub.tanzby.lab7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditActivity extends AppCompatActivity {

    private Button bnt_save_ed;
    private Button bnt_clear_ed;
    private Button bnt_load_ed;
    private Button bnt_delete_ed;

    private EditText ed_title;
    private EditText ed_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        evenBinding();
    }

    private void init()
    {
        bnt_save_ed = (Button) findViewById(R.id.bnt_save_editor);
        bnt_clear_ed = (Button) findViewById(R.id.bnt_clear_editor);
        bnt_load_ed = (Button) findViewById(R.id.bnt_load_editor);
        bnt_delete_ed = (Button) findViewById(R.id.bnt_delete_editor);
        ed_title = (EditText) findViewById(R.id.ed_title_editor);
        ed_content = (EditText) findViewById(R.id.ed_content_editor);
    }
    private void  evenBinding()
    {
        bnt_save_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        bnt_clear_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_content.setText("");
            }
        });
        bnt_load_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });
        bnt_delete_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg="";
                String title = ed_title.getText().toString();
                if (title.isEmpty())
                {
                    msg = "Please enter your file name first";
                }
                else
                {
                    deleteFile(title);
                    ed_title.setText("");
                    ed_content.setText("");
                    msg = "Delete successfully";
                }
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void save()
    {
        String msg = "";
        String title = ed_title.getText().toString();
        String content = ed_content.getText().toString();
        if (title.isEmpty())
        {
            msg = "Title should not be empty";
        }
        else
        {
            try {
                FileOutputStream localFileOutputStream =
                        getApplicationContext().openFileOutput(title, 0);
                localFileOutputStream.write(content.getBytes()); 
                localFileOutputStream.close();

                msg  = "save successfully";
            }
            catch (IOException e) {
                msg = "save fail";
                e.printStackTrace();
            }
        }
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void load()
    {
        String msg = "";
        String title = ed_title.getText().toString();
        if (title.isEmpty())
        {
            msg = "Title should not be empty";
        }
        else
        {
            try {
                FileInputStream localFile = getApplicationContext().openFileInput(title);
                byte[] content = new byte[localFile.available()];
                localFile.read(content);
                localFile.close();
                msg  = "load from file successfully";
                
            }
            catch (IOException e) {
                msg = "No such file";
                ed_content.setText("");
                e.printStackTrace();
            }
        }

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);

        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(mHomeIntent);
    }


}
