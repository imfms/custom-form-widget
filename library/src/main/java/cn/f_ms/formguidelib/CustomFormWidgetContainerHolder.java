package cn.f_ms.formguidelib;

import android.app.Activity;
import android.view.ViewGroup;

import cn.f_ms.formguidelib.widget.Container0Holder;
import cn.f_ms.formguidelib.widget.NoConvertHolder;
import cn.f_ms.library.check.CheckNull;

/**
 * 自定义表单 - 入口holder
 */
public class CustomFormWidgetContainerHolder extends Container0Holder {

    public CustomFormWidgetContainerHolder(Activity activity, ViewGroup parent, FormMatcher formMatcher) {
        super(activity, parent, makeContext(formMatcher));
    }

    private static Context makeContext(final FormMatcher formMatcher) {
        CheckNull.ifNullThrowArgException(formMatcher, "formMatcher can't be null");

        return new Context(
                new FormMatcher() {

                    @Override
                    public FormHandler getConvertHolder(String type, int version, Activity activity, ViewGroup parent, Context context) {
                        FormHandler convertHolder = formMatcher.getConvertHolder(type, version, activity, parent, context);
                        if (convertHolder == null) {
                            convertHolder = new NoConvertHolder(activity, parent, context);
                        }
                        return convertHolder;
                    }
                }
        );
    }
}
