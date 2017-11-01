package pub.tanzby.lab5;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.nio.channels.NonWritableChannelException;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    private static final String SA = "StaticFilter";
    private static final String DA = "DynamicFilter";
    private static String  receiveType;
    private static Product curProduct;
    private static RemoteViews remoteView;

static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                            int appWidgetId) {
    if(remoteView==null)
    {
        remoteView = new RemoteViews(context.getPackageName(), R.layout.remote_view);
        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        remoteView.setOnClickPendingIntent(R.id.notification_layout,pendingIntent);
        remoteView.setTextViewText(R.id.notititle, "还没有任何商品推荐");
        remoteView.setTextViewText(R.id.notitext2,"请打开轻触打开应用以获取更多信息");
        remoteView.setImageViewResource(R.id.notiimage,R.drawable.shoplist);
    }
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, remoteView);
}

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context,Intent intent)
    {
        if(intent.getAction().equals(SA) || intent.getAction().equals(DA))
        {
            receiveType = intent.getAction();
            ReceiveDataAndHandle(context,intent);
            assert receiveType!=null;
            switch (receiveType)
            {
                case DA:/*intend去往购物车*/
                    sendProductAddToListInfo(context);
                    break;
                case SA:/*intend去往详情*/
                    sendRecommand(context);
                    break;
            }
        }
        super.onReceive(context,intent);
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
        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 0, sendIntend, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView = new RemoteViews(context.getPackageName(), R.layout.remote_view);
        remoteView.setImageViewResource(R.id.notiimage, curProduct.getImgObject());
        remoteView.setOnClickPendingIntent(R.id.notification_layout, contentIntent);
    }

private void  sendRecommand(Context context)
{
    remoteView.setTextViewText(R.id.notititle, curProduct.getName());
    remoteView.setTextViewText(R.id.notitext, curProduct.getSpecialInfo()+"!");
    remoteView.setTextViewText(R.id.notitext2,  "现仅需"+curProduct.getPrice()+"!");
    AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
    ComponentName CN = new ComponentName(context,NewAppWidget.class);
    appWidgetManager.updateAppWidget(CN,remoteView);
}

private void sendProductAddToListInfo(Context context)
{
    remoteView.setTextViewText(R.id.notititle,"马上下单");
    remoteView.setTextViewText(R.id.notitext2,curProduct.getName()+" 已添加到购物车");
    remoteView.setTextViewText(R.id.notitext,"");
    AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
    ComponentName CN = new ComponentName(context,NewAppWidget.class);
    appWidgetManager.updateAppWidget(CN,remoteView);
}

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

