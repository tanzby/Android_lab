package pub.tanzby.lab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailActivity extends AppCompatActivity {
    @BindView(R.id.favoriteBnt)        ImageButton Fav;
    @BindView(R.id.addShoppingListBnt) ImageButton Add;
    @BindView(R.id.returnBnt)          ImageButton Ret;
    @BindView(R.id.productName)        TextView    Name;
    @BindView(R.id.productWeight)      TextView    PrioInfo;
    @BindView(R.id.productPrice)       TextView    Price;
    @BindView(R.id.productView)        ImageView   proImg;
    @BindView(R.id.moreOption)         ListView    moreOption;
    private Product curProduct;
    private Integer curProductPos;
    private Boolean is_Favor;
    private Boolean is_Add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        init();
        getDataFromIntent();
        setEven();
    }

    private void init()
    {
        moreOption.setAdapter(
                new ArrayAdapter<String>(
                        ProductDetailActivity.this,
                        R.layout.more_opt_item_view,
                        new String[]{"一键下单","分享商品","不感兴趣","查看更多商品促销信息"}
                ));
    }

    private void getDataFromIntent() {
        curProduct = (Product)  this.getIntent().getParcelableExtra("PRODUCT");
        curProductPos = this.getIntent().getIntExtra("PRODUCT_Position",-1);
        Name.setText(curProduct.getName());
        proImg.setImageResource(curProduct.getImgObject());
        PrioInfo.setText(curProduct.getSpecialInfo());
        Price.setText(curProduct.getPrice());
        is_Favor = curProduct.getIs_favorite();
        is_Add   = curProduct.getIs_add_to_shopList();


        setOptionBnt(Fav,is_Favor,
                R.drawable.ic_star_pink_a400_48dp,
                R.drawable.ic_star_border_grey_500_48dp);
        setOptionBnt(Add,is_Add,
                R.drawable.ic_remove_shopping_cart_black_24dp,
                R.drawable.ic_add_shopping_cart_black_24dp);
    }

    private void setOptionBnt(ImageButton bnt, Boolean value, int trueOpt, int falseOpt)
    {
        if (value) bnt.setBackgroundResource(trueOpt);
        else bnt.setBackgroundResource(falseOpt);
    }

    private void setEven()
    {
        Ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("backInfo",new boolean[]{is_Add,is_Favor});
                intent.putExtra("PRODUCT_Position",curProductPos);
                setResult(1,intent);
                finish();
            }
        });
        Fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_Favor=!is_Favor;
                setOptionBnt(Fav,is_Favor,
                   R.drawable.ic_star_pink_a400_48dp,
                   R.drawable.ic_star_border_grey_500_48dp);
            }
        });
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_Add=!is_Add;
                setOptionBnt(Add,is_Add,
                    R.drawable.ic_remove_shopping_cart_black_24dp,
                    R.drawable.ic_add_shopping_cart_black_24dp);
                if(is_Add) {
                    Toast.makeText(ProductDetailActivity.this,
                            "商品已添加到购物车",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
