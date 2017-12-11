package pub.tanzby.lab8;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Button bnt_add_item;
    private ListView contactList;
    private Cursor cursor;
    private View alertDialogView;
    private mydb mdb;
    private contactAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permission.verifyContactPermissions(MainActivity.this);

        init();
        evenBinding();
    }

    private void init()
    {
        mdb = new mydb(MainActivity.this);
        bnt_add_item = (Button) findViewById(R.id.bnt_main_add_item);
        contactList = (ListView) findViewById(R.id.list_contact);
        View headerView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.simple_list_header_style,null);
        contactList.addHeaderView(headerView);
        mAdapter = new contactAdapter(MainActivity.this);
    }

    private void evenBinding()
    {
        bnt_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity.this,AddItemActivity.class);
                startActivityForResult(newIntent,0x1234);
            }
        }) ;

        mAdapter.setOnItemClickLitener(new contactAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                contactItem c = mAdapter.getItem(position);
                alertDialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialoglayout,null);
                ((TextView)alertDialogView.findViewById(R.id.dialog_ed_birthday)).setText(c.birthday);
                ((TextView)alertDialogView.findViewById(R.id.dialog_ed_gift)).setText(c.gift);
                ((TextView)alertDialogView.findViewById(R.id.dialog_name)).setText(c.name);
                ((TextView)alertDialogView.findViewById(R.id.dialog_ed_phone_num_show)).setText(getContactNumber(c.name));

                new AlertDialog.Builder(MainActivity.this)
                        .setView(alertDialogView)
                        .setPositiveButton("确认修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mdb.dbOpe(
                                ((TextView)alertDialogView.findViewById(R.id.dialog_name)).getText().toString(),
                                ((TextView)alertDialogView.findViewById(R.id.dialog_ed_birthday)).getText().toString(),
                                ((TextView)alertDialogView.findViewById(R.id.dialog_ed_gift)).getText().toString(),1);
                                updateList();
                            }
                        }).setNegativeButton("放弃修改",null)
                        .show();

            }

            @Override
            public void onItemLongClick(View view, int position) {
                final int pos = position;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("警告")
                        .setMessage("是否要删除当前联系人？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                contactItem c = mAdapter.getItem(pos);
                                mdb.dbOpe(
                                        c.name,"","",2);
                                updateList();
                            }
                        })
                        .setNegativeButton("算了算了",null)
                        .show();

            }
        });


        contactList.setAdapter(mAdapter);
        updateList();
    }

    private void updateList()
    {
        if (mdb != null && mAdapter!=null)
        {
            mAdapter.UpdateData(mdb.selectAll());
        }
    }

    private String getContactNumber(String name) {
        try
        {
            Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "= " + name,
                    null,
                    null);
            String Number = "";
            while(phone.moveToNext())
            {
                Number += phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();

            return Number.isEmpty()?"无此人联系方式":Number;
        }
        catch (Exception e)
        {
            return  "无此人联系方式";
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x1234 && data!=null)
        {
            contactItem newContact = (contactItem) data.getSerializableExtra("newContact");
            mdb.dbOpe(newContact.name,newContact.birthday,newContact.gift,0);
            updateList();
        }
    }

}
