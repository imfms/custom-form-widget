package cn.f_ms.formguidelib.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.f_ms.formguidelib.FormContext;
import cn.f_ms.formguidelib.R;
import cn.f_ms.formguidelib.widget.write.BaseWidgetWriteWithShowOrResultBeanHandler;
import cn.f_ms.library.logic.IsRight;

public class NoConvertHolder extends BaseWidgetWriteWithShowOrResultBeanHandler {

    public NoConvertHolder(Activity activity, ViewGroup parent, FormContext formContext) {
        super(activity, parent, formContext);
    }

    @Override
    protected View generateView(final Activity mActivity, LayoutInflater layoutInflater, ViewGroup parent) {

        View view = layoutInflater.inflate(R.layout.holder_no_convert, parent, false);
        view.findViewById(R.id.tv_text)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mActivity, "不支持的格式，请升级最新版本~", Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }

    @Override
    public IsRight checkWidgetDesc(Object descBean) {
        return IsRight.yes();
    }

    @Override
    public void fillWidgetDesc(Object showDescBean) {
        // do nothing
    }

    @Override
    public Result getResult() {
        return new Result<Object>(
                false,
                "不支持的格式，请升级最新版本~",
                null
        );
    }

    @Override
    public IsRight checkResultData(Object resultDescBean) {
        return IsRight.yes();
    }

    @Override
    public void fillResultData(Object resultDescBean) {

    }

    @Override
    protected Class getShowDescType() {
        return Void.class;
    }

    @Override
    protected Class getResultDescType() {
        return Void.class;
    }
}