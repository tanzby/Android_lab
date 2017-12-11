package pub.tanzby.lab8;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class permission {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE ,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA,
    };
    private static String[] PERMISSIONS_CONTACTS = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
    };

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }
    public static void verifyCameraPermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    PERMISSIONS_CAMERA, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public static void verifyContactPermissions(Activity activity)
    {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CONTACTS);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    PERMISSIONS_CONTACTS, REQUEST_EXTERNAL_STORAGE);
        }
    }
}