package pub.tanzby.lab5;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;


public class productRecommand extends BroadcastReceiver {

    private static final String SA = "StaticFilter";
    private static final String DA = "DynamicFilter";
    private String  receiveType;
    private Product curProduct;
    private NotificationManager mNotifyMgr;
    private PendingIntent contentIntent;
    private static Integer notifyNo = 0x123;
    private  RemoteViews remoteView;
    @Override
    public void onReceive(Context context, Intent intent) {

        mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        receiveType = intent.getAction();
        ReceiveDataAndHandle(context,intent);

        assert receiveType!=null;

        switch (receiveType)
        {
            case DA:/*去往购物车*/
                sendProductAddToListInfo(context);
                break;
            case SA:/*去往详情*/
                sendRecommand(context);
                break;
        }

    }

    private void ReceiveDataAndHandle(Context context,Intent intent)
    {
        curProduct = (Product) intent.getParcelableExtra("PRODUCT");
        Integer curProductPos = intent.getIntExtra("PRODUCT_Position", -1);

        assert intent.getAction()!=null;

        Intent sendIntend;
        switch (receiveType)
        {
            case DA:/*去往购物车*/
                sendIntend = new Intent().setClass(context, MainActivity.class );
                sendIntend.putExtra("OPENLIST",true);
                break;
            case SA:/*去往详情*/
                sendIntend = new Intent().setClass(context, ProductDetailActivity.class);
                sendIntend.putExtra("PRODUCT",curProduct);//发送数据
                sendIntend.putExtra("PRODUCT_Position", curProductPos);
                break;
            default:
                sendIntend = new Intent().setClass(context, MainActivity.class );
                sendIntend.putExtra("OPENLIST",false);
        }
        /*创建对应的intent的PendingIntent*/
        contentIntent = PendingIntent.getActivity(
                context, 0, sendIntend, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView = new RemoteViews(context.getPackageName(), R.layout.remote_view);
        remoteView.setImageViewResource(R.id.notiimage, curProduct.getImgObject());
    }

    private void  sendRecommand(Context context)
    {
        remoteView.setTextViewText(R.id.notititle,curProduct.getName());
        remoteView.setTextViewText(R.id.notitext,curProduct.getSpecialInfo()+"!");
        remoteView.setTextViewText(R.id.notitext2,"现仅需"+curProduct.getPrice()+"!");

        Notification notification = new Notification.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(curProduct.getImgObject())
                .setTicker("清仓大甩卖")
                .setAutoCancel(true)
                .build();// getNotification()
        notification.bigContentView = remoteView; //API22 专用 要不然设置不了图片
        mNotifyMgr.notify(notifyNo+=1, notification);
    }

    private void sendProductAddToListInfo(Context context)
    {
        remoteView.setTextViewText(R.id.notititle,"马上下单");
        remoteView.setTextViewText(R.id.notitext2,curProduct.getName()+" 已添加到购物车");
        remoteView.setTextViewText(R.id.notitext,"");

        Notification notification = new Notification.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.arla)
                .setTicker("您有新物品已添加购物车")
                .setAutoCancel(true)
                .build();// getNotification()
        notification.bigContentView = remoteView; //API22 专用 要不然设置不了图片
        mNotifyMgr.notify(notifyNo+=1, notification);
    }


}
