package cn.f_ms.formguidelib.widget.show;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.f_ms.formguidelib.Context;
import cn.f_ms.formguidelib.R;
import cn.f_ms.formguidelib.widget.entity.InputText0Entity;
import cn.f_ms.library.logic.IsRight;

public class InputText0ShowHandler extends BaseWidgetShowWithShowBeanHandler<InputText0Entity> {

    private TextView tvText;

    public InputText0ShowHandler(Activity activity, ViewGroup parent, Context context) {
        super(activity, parent, context);
    }

    @Override
    protected View generateView(Activity mActivity, LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.holder_show_input_text_0, parent, false);

        tvText = (TextView) view.findViewById(R.id.tv_text);

        return view;
    }

    @Override
    public IsRight checkResultJson(String resultJson) {
        return IsRight.yes();
    }

    @Override
    public void fillResult(String resultDescJson) {
        tvText.setText(resultDescJson);
    }

    @Override
    public IsRight checkWidgetDesc(InputText0Entity descBean) {
        return InputText0Entity.checkRight(descBean);
    }

    @Override
    public void fillWidgetDesc(InputText0Entity showDescBean) {
        // don't need show desc
    }

    @Override
    protected Class<InputText0Entity> getShowDescType() {
        return InputText0Entity.class;
    }
}
