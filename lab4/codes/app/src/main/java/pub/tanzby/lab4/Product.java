package pub.tanzby.lab4;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Parcel;
import android.os.Parcelable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanzb on 2017/10/17 0017.
 */

public class Product implements Parcelable {
    private String  name;
    private Boolean is_favorite;
    private Integer purchaseNumber;
    private String   price;
    private Integer imgObject;
    private String  SpecialInfo;
    Product()
    {
        purchaseNumber=0;
        is_favorite = Boolean.FALSE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIs_add_to_shopList() {
        return purchaseNumber>0;
    }

    public void setPurchaseNumber(Integer purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }
    public Integer getPurchaseNumber() {
        return purchaseNumber;
    }

    public Boolean getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(Boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    public Integer getImgObject() {
        return imgObject;
    }

    public void setImgObject(Integer imgObject) {
        this.imgObject = imgObject;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setSpecialInfo(String SpecialInfo) {
        this.SpecialInfo = SpecialInfo;
    }

    public String getSpecialInfo() {
        return SpecialInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imgObject);
        dest.writeInt(purchaseNumber);
        dest.writeInt(is_favorite ? 1 : 0);
        dest.writeString(SpecialInfo);
        dest.writeString(name);
        dest.writeString(price);
    }

    public static final Parcelable.Creator<Product> CREATOR
            = new Parcelable.Creator<Product>(){
        @Override
        public Product createFromParcel(Parcel source) {
            //反序列化的属性的顺序必须和之前写入的顺序一致
            Product product = new Product();
            product.imgObject=source.readInt();
            product.purchaseNumber=source.readInt();
            product.is_favorite=source.readInt()==1;
            product.SpecialInfo=source.readString();
            product.name=source.readString();
            product.price=source.readString();
            return product;
        }
        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public static final  List<Product> getDataFromXMLSource(Context context,int xmlFilePath)
    {
        Product p = null;
        List<Product> productList=null;
        XmlResourceParser xrp = context.getResources().getXml(xmlFilePath);
        try {
            while(xrp.getEventType() != XmlPullParser.END_DOCUMENT)
            {
                switch (xrp.getEventType())
                {
                    case XmlPullParser.START_DOCUMENT:
                        productList = new ArrayList<Product>();
                        break;
                    case  XmlPullParser.START_TAG:
                        String tagName = xrp.getName();
                        if (tagName.equals("Product"))
                        {
                            // 创建新Product item
                            p = new Product();
                            // 获取价格
                            p.setPrice(xrp.getAttributeValue(null,"price"));
                            // 获取商品名字
                            p.setName(xrp.getAttributeValue(null,"name"));
                            // 获取商品对应的图片
                            String imgName = xrp.getAttributeValue(null, "imgURL");
                            p.setImgObject(
                                    R.drawable.class.getField(imgName).getInt(null));
                        }
                        else if (tagName.equals("info"))
                        {
                            assert p != null;
                            String typeName = xrp.getAttributeValue(null,"name");
                            xrp.next();
                            String typeInfo = xrp.getText();
                            p.setSpecialInfo(typeName+" "+typeInfo);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xrp.getName().equals("Product")) {
                            assert productList != null;
                            productList.add(p);
                            p = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                }
                xrp.next();// 获取解析下一个
            }
        } catch (XmlPullParserException
                | IOException
                | IllegalAccessException
                | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return productList;
    }
}
