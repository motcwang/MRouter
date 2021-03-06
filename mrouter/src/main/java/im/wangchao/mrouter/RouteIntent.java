package im.wangchao.mrouter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.Set;

import im.wangchao.mrouter.annotations.Constants;

/**
 * <p>Description  : Route.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 2017/11/10.</p>
 * <p>Time         : 上午10:41.</p>
 */
public final class RouteIntent {
    public static final String DEFAULT_POP_PATH = "/finish";
    public static final String DEFAULT_POP_URI = Constants.ROUTER_SERVICE_NAME.concat("://").concat(DEFAULT_POP_PATH);

    public static final int FLAG_ACTIVITY_PUSH_AND_POP = -2;
    public static final int FLAG_CLEAR_CURRENT_TASK_AND_NEW = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK;

    // uri
    private final Uri mUri;
    // params
    private final Bundle mBundle;
    // start/pop flags
    private final int mFlags;
    // target class, maybe null.
    private final String mTargetClass;

    public static RouteIntent of(String uri){
        return new Builder().uri(uri).build();
    }

    public static RouteIntent of(String uri, Bundle bundle){
        return new Builder().uri(uri).bundle(bundle).build();
    }

    public static RouteIntent of(String uri, int flags){
        return new Builder().uri(uri).flags(flags).build();
    }

    public static RouteIntent of(String uri, Bundle bundle, int flags){
        return new Builder().uri(uri).bundle(bundle).flags(flags).build();
    }

    public static RouteIntent popIntent(){
        return popIntent(null);
    }

    public static RouteIntent popIntent(Bundle bundle){
        return new Builder().uri(DEFAULT_POP_URI).bundle(bundle).build();
    }

    private RouteIntent(Builder builder){
        mUri = builder.mUri;
        mBundle = builder.mBundle;
        mFlags = builder.mFlags;
        mTargetClass = builder.mTargetClass;
    }

    public Uri uri(){
        return mUri;
    }

    public Bundle bundle(){
        return mBundle;
    }

    public int flags(){
        return mFlags;
    }

    public String targetClass(){
        return mTargetClass;
    }

    public Intent getPushIntent(Context context){
        Intent intent = new Intent();
        if (TextUtils.isEmpty(mTargetClass)){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(mUri);
        } else {
            ComponentName component = new ComponentName(context, mTargetClass);
            intent.setComponent(component);
        }
        intent.putExtras(mBundle);
        // Set flags.
        if (-1 != mFlags && FLAG_ACTIVITY_PUSH_AND_POP != mFlags) {
            intent.setFlags(mFlags);
        } else if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        return intent;
    }

    public Intent getPopIntent(){
        Intent intent = new Intent();
        intent.putExtras(mBundle);
        if (-1 != mFlags){
            intent.addFlags(mFlags);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        return intent;
    }

    public Builder newBuilder(){
        return new Builder(this);
    }

    public static final class Builder {
        Uri mUri;
        Bundle mBundle;
        int mFlags;
        String mTargetClass;
        boolean mIsUriChanged;

        public Builder(){
            mBundle = new Bundle();
            mFlags = -1;
            mTargetClass = null;
            mIsUriChanged = false;
        }

        private Builder(RouteIntent route){
            mUri = route.mUri;
            mBundle = route.mBundle;
            mFlags = route.mFlags;
            mTargetClass = route.mTargetClass;
            mIsUriChanged = false;
        }

        public Builder uri(String uri){
            mUri = Uri.parse(uri);
            mIsUriChanged = true;
            return this;
        }

        public Builder uri(Uri uri){
            mUri = uri;
            mIsUriChanged = true;
            return this;
        }

        public Builder bundle(Bundle bundle){
            if (bundle != null){
                mBundle = bundle;
            }
            return this;
        }

        public Builder flags(int flags){
            mFlags = flags;
            return this;
        }

        public Builder targetClass(String targetClass){
            mTargetClass = targetClass;
            return this;
        }

        public Builder addAll(Bundle bundle){
            if (bundle != null){
                mBundle.putAll(bundle);
            }
            return this;
        }

        public Builder addParameter(String key, Bundle value){
            mBundle.putBundle(key, value);
            return this;
        }

        public Builder addParameter(String key, String value){
            mBundle.putString(key, value);
            return this;
        }

        public Builder addParameter(String key, float value){
            mBundle.putFloat(key, value);
            return this;
        }

        public Builder addParameter(String key, int value){
            mBundle.putInt(key, value);
            return this;
        }

        public Builder addParameter(String key, double value){
            mBundle.putDouble(key, value);
            return this;
        }

        public Builder addParameter(String key, long value){
            mBundle.putLong(key, value);
            return this;
        }

        public RouteIntent build(){
            if (mUri == null) throw new NullPointerException("Route.Builder mUri can not be null.");
            if (mBundle == null) throw new NullPointerException("Route.Builder mBundle can not be null.");

            if (mIsUriChanged){
                Set<String> keys = mUri.getQueryParameterNames();
                for (String key : keys) {
                    mBundle.putString(key, mUri.getQueryParameter(key));
                }
            }

            return new RouteIntent(this);
        }
    }
}
