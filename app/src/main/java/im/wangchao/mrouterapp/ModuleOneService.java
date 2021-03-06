package im.wangchao.mrouterapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import im.wangchao.mrouter.IRouterService;
import im.wangchao.mrouter.RouteIntent;
import im.wangchao.mrouter.RouterCallback;
import im.wangchao.mrouter.annotations.RouterService;

/**
 * <p>Description  : ModuleOneService.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 2017/11/20.</p>
 * <p>Time         : 上午10:01.</p>
 */
@RouterService("one")
public class ModuleOneService implements IRouterService {

    @Override public void push(Context context, RouteIntent route, int requestCode, RouterCallback callback) {
        Log.e("wcwcwc", "Module one service: push() -> " + route.targetClass());
        final Intent intent = route.getPushIntent(context);

        if (requestCode > 0) {
            ActivityCompat.startActivityForResult((Activity) context, intent, requestCode, null);
        } else {
            ActivityCompat.startActivity(context, intent, null);
        }

        if (callback != null){
            callback.onSuccess(route);
        }
    }

    @Override public void pop(Context context, RouteIntent route, int resultCode, RouterCallback callback) {
        Log.e("wcwcwc", "Module one service: pop()");
    }
}
