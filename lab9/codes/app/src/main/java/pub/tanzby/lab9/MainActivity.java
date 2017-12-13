package pub.tanzby.lab9;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pub.tanzby.lab9.adapter.RC_Adapter;
import pub.tanzby.lab9.factory.ServiceFactory;
import pub.tanzby.lab9.factory.StatusBarFactory;
import pub.tanzby.lab9.model.GitHub;
import pub.tanzby.lab9.other.Permission;
import retrofit2.Retrofit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends Activity {

    EditText ed_search;
    Button bnt_clear;
    Button bnt_fetch;
    ProgressBar loading;

    RecyclerView rv_main_list;
    RC_Adapter rv_main_list_adapter;

    List<GitHub> listGithubUser = new ArrayList<>();

    Observer<GitHub> github_obs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Permission.verifyInternetPermissions(this);

        init();
        evenbinding();
    }

    private void init()
    {
        StatusBarFactory.immerseStatusBar(this);



        ed_search   = findViewById(R.id.ed_search);
        bnt_clear   = findViewById(R.id.bnt_clear);
        bnt_fetch   = findViewById(R.id.bnt_fetch);
        loading     = findViewById(R.id.progressBar_load_github);

        rv_main_list= findViewById(R.id.rv_main_list);


        loading.setVisibility(View.INVISIBLE);

        rv_main_list_adapter = new RC_Adapter(this);
        rv_main_list_adapter.setNewList(listGithubUser);
        rv_main_list.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        rv_main_list.setAdapter(rv_main_list_adapter);

    }


    private void evenbinding()
    {
        bnt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_search.setText("");
            }
        });

        bnt_fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String username = ed_search.getText().toString();
            if (username.isEmpty())
            {
                Toast.makeText(MainActivity.this,
                        "用户名不可为空",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                //TODO : 调用服务
                getName(username);

            }
            }
        });



    github_obs = new Observer<GitHub>() {

        @Override
        public void onSubscribe(Disposable d) {

            Log.i("github_obs","Subscribe");
            loading.setVisibility(View.VISIBLE);
            rv_main_list.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onNext(GitHub gitHub) {
            Log.i("github_obs","onNext");
            rv_main_list_adapter.addiItem(gitHub);

        }

        @Override
        public void onError(Throwable t) {
            Log.i("github_obs","onError thowable"+t);
            Toast.makeText(MainActivity.this,"没有此用户",Toast.LENGTH_SHORT).show();;
        }

        @Override
        public void onComplete() {
            System.out.println("完成传输");
            loading.setVisibility(View.INVISIBLE);
            rv_main_list.setVisibility(View.VISIBLE);

        }
    };


        rv_main_list_adapter.setOnItemClickLitener(new RC_Adapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent newIntent = new Intent(MainActivity.this,ReposActivity.class);
                newIntent.putExtra("username",
                        rv_main_list_adapter.getItem(position).getTitle());
                startActivity(newIntent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                rv_main_list_adapter.deleteItem(position);
            }
        });


    }

private void  getName(String name)
{
    Retrofit retrofit = ServiceFactory.createRetrofit();
    ServiceFactory.GitHubService github =  retrofit.create(ServiceFactory
                                                   .GitHubService.class);
    Observable<GitHub> observable = github.getUser(name);
    observable.subscribeOn(Schedulers.io())
              .unsubscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(github_obs);

}
}
