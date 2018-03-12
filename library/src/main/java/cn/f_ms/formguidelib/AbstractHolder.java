package cn.f_ms.formguidelib;

import android.app.Activity;
import android.view.View;

/**
 * 视图逻辑模块抽象基类
 *
 * @author f_ms
 */
public abstract class AbstractHolder {

    protected final Activity mActivity;
    private final View mParentView;
    protected View mContentView;

    public AbstractHolder(Activity activity, View parent) {
        mActivity = activity;
        mParentView = parent;
    }

    public View getContentView() {

        if (mContentView == null) {
            mContentView = generateView(mActivity, mParentView);
        }

        return mContentView;
    }

    /**
     * 生成view
     *
     * @param activity activity
     * @param parent   父视图
     * @return 本视图
     */
    protected abstract View generateView(Activity activity, View parent);
}
