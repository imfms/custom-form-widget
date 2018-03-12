package cn.f_ms.formguidelib.widget.write;

import android.app.Activity;
import android.view.ViewGroup;

import java.lang.reflect.Type;

import cn.f_ms.formguidelib.Context;
import cn.f_ms.formguidelib.FormHandler;
import cn.f_ms.library.logic.IsRight;

/**
 * 单个规范实例
 *
 * @author f-ms
 * @time 2017/5/23
 */
public abstract class BaseWidgetWriteWithShowOrResultBeanHandler<SHOW_DESC_TYPE, RESULT_TYPE> extends BaseWidgetWriteWithShowBeanHandler<SHOW_DESC_TYPE> {

    public static final class Result<T> {
        public final boolean isSuccess;
        public final String errorMessage;
        public final T result;

        public Result(boolean isSuccess, String errorMessage, T result) {
            this.isSuccess = isSuccess;
            this.errorMessage = errorMessage;
            this.result = result;
        }
    }

    public BaseWidgetWriteWithShowOrResultBeanHandler(Activity activity, ViewGroup parent, Context context) {
        super(activity, parent, context);
    }

    @Override
    public final IsRight checkResultJson(String resultJson) {

        RESULT_TYPE resultDescBean = mGson.fromJson(
                resultJson,
                getResultDescType()
        );

        return checkResultData(resultDescBean);
    }

    @Override
    public final void fillResult(String resultDescJson) {
        RESULT_TYPE resultDescBean = mGson.fromJson(
                resultDescJson,
                getResultDescType()
        );

        IsRight isRight = checkResultData(resultDescBean);
        if (!isRight.isRight()) {
            throw new IllegalArgumentException(isRight.errorMessage());
        }

        fillResultData(resultDescBean);
    }

    @Override
    public FormHandler.FinalResult getFinalResult() {

        Result<RESULT_TYPE> result = getResult();

        if (result == null) {
            return new FormHandler.FinalResult(
                    false,
                    "控件返回结果解析错误，请检查升级~",
                    null
            );
        }

        return new FormHandler.FinalResult(
                result.isSuccess,
                result.errorMessage,
                mGson.toJson(
                        result.result
                )
        );
    }

    public abstract Result<RESULT_TYPE> getResult();

    /**
     * 检查result是否合规
     * @param resultDescBean desc bean
     * @return is it right?
     */
    public abstract IsRight checkResultData(RESULT_TYPE resultDescBean);

    /**
     * 填充result
     * @param resultDescBean desc bean
     */
    public abstract void fillResultData(RESULT_TYPE resultDescBean);

    protected abstract Type getResultDescType();
}

