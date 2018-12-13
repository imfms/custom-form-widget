package cn.f_ms.formguidelib;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import cn.f_ms.library.logic.IsRight;

public interface FormHandler {

    final class FinalResult {
        public final boolean isSuccess;
        public final String errorMessage;
        public final String json;

        public FinalResult(boolean isSuccess, String errorMessage, String json) {
            this.isSuccess = isSuccess;
            this.errorMessage = errorMessage;
            this.json = json;
        }
    }

    View getContentView();

    /**
     * 检查widgetEntity是否存在异常
     *
     * @param widgetDescJson widget desc json
     * @return isRight?
     */
    IsRight checkWidgetDescJson(String widgetDescJson);

    /**
     * 填充部件描述json
     * @param widgetDescJson 部件描述json
     */
    void fillWidgetDescJson(String widgetDescJson);

    /**
     * 检查widgetEntity是否存在异常
     *
     * @param resultJson result json
     * @return isRight?
     */
    IsRight checkResultJson(String resultJson);

    /**
     * 填充结果
     * @param resultJson 结果描述json
     */
    void fillResult(String resultJson);

    /**
     * 获取控件结果
     * @return 控件结果
     */
    FinalResult getFinalResult();

    interface FormMatcher {
        /**
         * 是否处理
         *
         * @param type    自定义控件类型
         * @param version 自定义控件版本
         * @param formContext 自定义控件上下文
         * @return 自定义控件处理器
         */
        FormHandler getConvertHolder(String type, int version, Activity activity, ViewGroup parent, FormContext formContext);
    }
}
