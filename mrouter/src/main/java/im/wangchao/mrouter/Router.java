package im.wangchao.mrouter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import static im.wangchao.mrouter.RouteIntent.DEFAULT_POP_URI;

/**
 * <p>Description  : Router.
 *                   app://module/path</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 2017/11/10.</p>
 * <p>Time         : 上午9:26.</p>
 */
public final class Router {
    private static boolean sInit;

    private Router(){}

    public static void init(){
        RouterRepository.init();
        sInit = true;
    }

    public static void push(Context context, String uri){
        push(context, uri, null);
    }

    public static void push(Context context, String uri, Bundle bundle){
        push(context, uri, bundle, -1);
    }

    public static void push(Context context, String uri, int flags){
        push(context, uri, null, flags);
    }

    public static void push(Context context, String uri, Bundle bundle, int flags){
        pushForResult(context, uri, bundle, flags, -1);
    }

    public static void pushForResult(Context context, String uri, int requestCode){
        pushForResult(context, uri, null, -1, requestCode);
    }

    public static void pushForResult(Context context, String uri, Bundle bundle, int requestCode){
        pushForResult(context, uri, bundle, -1, requestCode);
    }

    public static void pushForResult(Context context, String uri, int flags, int requestCode){
        pushForResult(context, uri, null, flags, requestCode);
    }

    public static void pushForResult(Context context, String uri, Bundle bundle, int flags, int requestCode){
        push(context, RouteIntent.of(uri, bundle, flags), requestCode);
    }

    public static void pop(Context context){
        pop(context, DEFAULT_POP_URI, null, Activity.RESULT_OK);
    }

    public static void pop(Context context, String uri, Bundle bundle){
        pop(context, uri, bundle, Activity.RESULT_OK);
    }

    public static void pop(Context context, Bundle bundle, int resultCode){
        pop(context, DEFAULT_POP_URI, bundle, resultCode);
    }

    public static void pop(Context context, String uri, Bundle bundle, int resultCode){
        pop(context, RouteIntent.of(uri, bundle), resultCode);
    }

    public static void request(String uri){
        request(uri, null);
    }

    public static void request(String uri, Bundle bundle){
        request(uri, bundle, null);
    }

    public static void request(String uri, Bundle bundle, RouterCallback callback){
        request(RouteIntent.of(uri, bundle), callback);
    }

    public static void push(Context context, RouteIntent route, int requestCode){
        check();
        RouterRepository.getRouterServiceCenter().push(context, route, requestCode);
    }

    public static void pop(Context context, RouteIntent route, int resultCode){
        check();
        RouterRepository.getRouterServiceCenter().pop(context, route, resultCode);
    }

    public static void request(RouteIntent route, RouterCallback callback){
        check();
        RouterRepository.getRouterServiceCenter().onReceiver(route, callback);
    }

    private static void check(){
        if (!sInit){
            throw new RuntimeException("You must invoke Router.init() first.");
        }
    }
}
