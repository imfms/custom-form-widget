package cn.f_ms.formguidelib.widget.show;

import android.app.Activity;
import android.view.ViewGroup;

import cn.f_ms.formguidelib.FormContext;
import cn.f_ms.formguidelib.widget.write.BaseWidgetWriteWithShowBeanHandler;

/**
 * 单个规范实例
 *
 * @author f-ms
 * @time 2017/5/23
 */
public abstract class BaseWidgetShowWithShowBeanHandler<SHOW_DESC_TYPE> extends BaseWidgetWriteWithShowBeanHandler<SHOW_DESC_TYPE> {

    public BaseWidgetShowWithShowBeanHandler(Activity activity, ViewGroup parent, FormContext formContext) {
        super(activity, parent, formContext);
    }

    @Override
    public final FinalResult getFinalResult() {
        return new FinalResult(
                false,
                "I don't accept getResult feature",
                null
        );
    }
}

