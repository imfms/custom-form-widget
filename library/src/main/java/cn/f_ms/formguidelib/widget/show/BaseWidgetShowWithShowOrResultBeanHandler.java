package cn.f_ms.formguidelib.widget.show;

import android.app.Activity;
import android.view.ViewGroup;

import cn.f_ms.formguidelib.Context;
import cn.f_ms.formguidelib.widget.write.BaseWidgetWriteWithShowOrResultBeanHandler;

/**
 * 单个规范实例
 *
 * @author f-ms
 * @time 2017/5/23
 */
public abstract class BaseWidgetShowWithShowOrResultBeanHandler<SHOW_DESC_TYPE, RESULT_TYPE> extends BaseWidgetWriteWithShowOrResultBeanHandler<SHOW_DESC_TYPE, RESULT_TYPE> {
    public BaseWidgetShowWithShowOrResultBeanHandler(Activity activity, ViewGroup parent, Context context) {
        super(activity, parent, context);
    }

    @Override
    public final Result<RESULT_TYPE> getResult() {
        // ignore
        return new Result<>(false, "unaccept getResult", null);
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

