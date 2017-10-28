package pub.tanzby.lab4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends  AppCompatActivity {
    @BindView(R.id.id_listview)     ListView      mShoppingCarList;
    @BindView(R.id.id_recyclerview) RecyclerView  mAllProductList;
    @BindView(R.id.floatingActionButton) FloatingActionButton mfab;
    List<Product> productList;
    RCAdapter     mAdapter;
    LVAdapter     mAdapter_ShoppingList;
    productRecommand productRecommand_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        productRecommand();
        setView();
    }

    private void init()
    {
        EventBus.getDefault().register(this);

        /*注册动态广播*/
        productRecommand_ = new productRecommand();
        IntentFilter intentFilter = new IntentFilter("DynamicFilter");
        registerReceiver(productRecommand_,intentFilter);

        ButterKnife.bind(this);
        /*data initialization*/
        productList = Product.getDataFromXMLSource(this,R.xml.product_resource);
        /*visibility for two list*/
        mAllProductList.setVisibility(View.VISIBLE);
        mShoppingCarList.setVisibility(View.INVISIBLE);
    }

    private void productRecommand()
    {
        Integer randomID = new Random().nextInt(productList.size());
        Intent BroadcastIntent =  new Intent("StaticFilter");
        BroadcastIntent.putExtra("PRODUCT",productList.get(randomID));
        BroadcastIntent.putExtra("PRODUCT_Position",randomID);
        sendBroadcast(BroadcastIntent);
    }

    private  void setView()
    {
        /*set FloatingActionButton*/
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (mAllProductList.getVisibility()==View.INVISIBLE) /*当前处在购物车界面*/
            {
                mAllProductList.setVisibility(View.VISIBLE);
                mShoppingCarList.setVisibility(View.INVISIBLE);
                mfab.setImageResource(R.drawable.ic_shopping_cart_white_48dp);
            }
            else/*当前处在Home界面*/
            {
                updateShoppingList();
                mAllProductList.setVisibility(View.INVISIBLE);
                mShoppingCarList.setVisibility(View.VISIBLE);
                mfab.setImageResource(R.drawable.ic_home_white_48dp);
            }
            }
        });

        /*set RecyclerView*/
        mAllProductList.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new RCAdapter(this,productList);
        mAdapter.setOnItemClickLitener(
                new RCAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent newInten = new Intent().setClass(MainActivity.this,ProductDetailActivity.class);
                        newInten.putExtra("PRODUCT",mAdapter.getItems(position));//发送数据
                        newInten.putExtra("PRODUCT_Position",position);
                        startActivityForResult(newInten,1);//启动intent
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {
                        Toast.makeText(MainActivity.this,"移除第"+(position+1)+"个商品",Toast.LENGTH_SHORT).show();
                        mAdapter.removeData(position);
                    }
                }
        );
        mAllProductList.setAdapter(mAdapter);

        /*set ListView*/
        mAdapter_ShoppingList = new LVAdapter(MainActivity.this);
        mAdapter_ShoppingList.setOnItemClickLitener(
            new LVAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent newInten = new Intent().setClass(MainActivity.this,ProductDetailActivity.class);
                    //发送数据
                    newInten.putExtra("PRODUCT",mAdapter_ShoppingList.getItem(position));
                    newInten.putExtra("PRODUCT_Position",searchProductPosition(position));
//                    startActivityForResult(newInten,1);//启动intent
                    startActivity(newInten);
                }

                @Override
                public void onItemLongClick(View view, final int position) {
                    AlertDialog.Builder AB = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("移除商品")
                        .setMessage("从购物车中移除"+mAdapter_ShoppingList.getItem(position).getName()+"?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.getItems(searchProductPosition(position))
                                        .setPurchaseNumber(0);
                                mAdapter_ShoppingList.removeData(position);
                            }
                        })
                        .setNegativeButton("取消",null);
                    AB.create().show();

                }
            }
        );
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headView = inflater.inflate(R.layout.shoppinglist_head, null);
        mShoppingCarList.addHeaderView(headView);
        mShoppingCarList.setAdapter(mAdapter_ShoppingList);
    }
    private void updateShoppingList()
    {
        mAdapter_ShoppingList.clearData();
        for (int i = 0 ;i < mAdapter.getItemCount(); i++)
        {
            if (mAdapter.getItems(i).getIs_add_to_shopList())
            {
                mAdapter_ShoppingList.addData(mAdapter.getItems(i));
            }
        }
    }

    /**
     * Use the position of product in shopping car to find the position of product in shopping car in main-List
     * @param pos_in_ShopList position of product in shopping car
     * @return the position of product in shopping car in main-List
     */
    private Integer searchProductPosition(Integer pos_in_ShopList)
    {
        int i = 0;
        for ( ;i < mAdapter.getItemCount();i++)
        {
            String s1 = mAdapter.getItems(i).getName();
            String s2 = mAdapter_ShoppingList.getItem(pos_in_ShopList).getName();
            if ( s1.equals(s2)) {
                break;
            }
        }
        return i;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(productMessageEvent event){
        mAdapter.getItems(event.productIndex).setPurchaseNumber(event.productPurchaseNum);
        mAdapter.getItems(event.productIndex).setIs_favorite(event.isProductFavor);
        updateShoppingList();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        boolean openList =  intent.getBooleanExtra("OPENLIST",false);
        if (openList) /*判断是需要主界面还是购物车*/
        {
            updateShoppingList();
            mAllProductList.setVisibility(View.INVISIBLE);
            mShoppingCarList.setVisibility(View.VISIBLE);
            mfab.setImageResource(R.drawable.ic_home_white_48dp);
        }
        else/*当前处在Home界面*/
        {
            mAllProductList.setVisibility(View.VISIBLE);
            mShoppingCarList.setVisibility(View.INVISIBLE);
            mfab.setImageResource(R.drawable.ic_shopping_cart_white_48dp);
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        /*解除广播注册*/
        if(productRecommand_ != null)
        {
            unregisterReceiver(productRecommand_);
            productRecommand_ = null;
        }
        EventBus.getDefault().unregister(this);
    }
}
