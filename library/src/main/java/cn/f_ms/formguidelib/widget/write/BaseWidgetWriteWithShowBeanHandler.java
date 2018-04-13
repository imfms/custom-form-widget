package cn.f_ms.formguidelib.widget.write;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import cn.f_ms.formguidelib.Context;
import cn.f_ms.formguidelib.FormHandler;
import cn.f_ms.library.check.CheckNull;
import cn.f_ms.library.logic.IsRight;

/**
 * 单个规范实例
 *
 * @author f-ms
 * @time 2017/5/23
 */
public abstract class BaseWidgetWriteWithShowBeanHandler<SHOW_DESC_TYPE> implements FormHandler {

    private ViewGroup mParent;

    protected final Activity mActivity;
    private final Context mContext;
    protected SHOW_DESC_TYPE mShowDescBean;
    protected final Gson mGson;
    private View mContentView;

    public BaseWidgetWriteWithShowBeanHandler(Activity activity, ViewGroup parent, Context context) {

        mParent = parent;
        mActivity = CheckNull.ifNullThrowArgException(activity);
        mContext = CheckNull.ifNullThrowArgException(context);

        mGson = new GsonBuilder()
                .registerTypeAdapter(Void.class, new JsonDeserializer<Void>() {
                    @Override
                    public Void deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return null;
                    }
                })
                .create();
    }

    @Override
    public View getContentView() {
        if (mContentView == null) {
            mContentView = generateView(mActivity, LayoutInflater.from(mActivity), mParent);
        }
        return mContentView;
    }

    @Override
    public IsRight checkWidgetDescJson(String widgetDescJson) {
        try {
            SHOW_DESC_TYPE descBean = mGson.fromJson(widgetDescJson, getShowDescType());
            return checkWidgetDesc(descBean);
        } catch (Exception e) {
            return IsRight.no(e.getMessage());
        }
    }

    @Override
    public void fillWidgetDescJson(String widgetDescJson) {
        mShowDescBean = mGson.fromJson(
                widgetDescJson,
                getShowDescType()
        );

        IsRight isRight = checkWidgetDesc(mShowDescBean);
        if (!isRight.isRight()) {
            throw new IllegalArgumentException(isRight.errorMessage());
        }
        fillWidgetDesc(mShowDescBean);
    }

    protected final Context getContext() {
        return mContext;
    }

    protected abstract View generateView(Activity activity, LayoutInflater layoutInflater, ViewGroup parent);

    /**
     * 检查部件描述是否存在问题
     * @param descBean 部件描述
     * @return 是否存在问题
     */
    public abstract IsRight checkWidgetDesc(SHOW_DESC_TYPE descBean);

    /**
     * 填充部件
     * @param showDescBean 部件描述数据
     */
    public abstract void fillWidgetDesc(SHOW_DESC_TYPE showDescBean);

    protected final Activity getActivity() {
        return mActivity;
    }

    protected abstract Type getShowDescType();
}

