package cn.f_ms.formguidelib.widget.show;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.f_ms.formguidelib.FormContext;
import cn.f_ms.formguidelib.R;
import cn.f_ms.formguidelib.widget.entity.MultiChoice0Entity;
import cn.f_ms.library.logic.IsRight;

public class MultiChoice0ShowHandler extends BaseWidgetShowWithShowOrResultBeanHandler<MultiChoice0Entity, ArrayList<Integer>> {

    private LinearLayout llContainer;

    public MultiChoice0ShowHandler(Activity activity, ViewGroup parent, FormContext formContext) {
        super(activity, parent, formContext);
    }

    @Override
    public IsRight checkResultData(ArrayList<Integer> resultDescBean) {

        if (resultDescBean == null) {
            return IsRight.yes();
        }

        int nullIndex = resultDescBean.indexOf(null);
        if (nullIndex >= 0) {
            return IsRight.no("resultDescBean can't contain null value, but found on '" + nullIndex + "'");
        }

        return IsRight.yes();
    }

    @Override
    protected View generateView(Activity mActivity, LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.holder_show_multi_choice_0, parent, false);

        llContainer = (LinearLayout) view.findViewById(R.id.ll_container);

        return view;
    }

    private void fillData(Activity mActivity, MultiChoice0Entity descBean) {

        if (descBean.options == null
                || descBean.options.isEmpty()) {

            throw new IllegalArgumentException("服务器返回数据异常");
        }

        // options
        for (MultiChoice0Entity.Option option : descBean.options) {

            llContainer.addView(
                    generateCheckBox(mActivity, option)
            );
        }

        // defaultSelect
        changeSelect(llContainer, mShowDescBean.default_select);
    }

    private void changeSelect(LinearLayout llContainer, List<Integer> default_select_ids) {

        if (default_select_ids == null) {
            default_select_ids = new ArrayList<>(0);
        }

        for (int x = 0; x < llContainer.getChildCount(); x++) {

            CheckBox checkBox = (CheckBox) llContainer.getChildAt(x);

            MultiChoice0Entity.Option option = (MultiChoice0Entity.Option) checkBox.getTag();

            checkBox.setChecked(
                    default_select_ids.contains(option.choice_id)
            );
        }
    }

    private CheckBox generateCheckBox(Activity mActivity, MultiChoice0Entity.Option option) {

        CheckBox checkBox = new CheckBox(mActivity);
        checkBox.setTag(option);

        checkBox.setText(option.choice_title);
        checkBox.setClickable(false);

        return checkBox;
    }

    @Override
    public void fillResultData(ArrayList<Integer> resultDescBean) {
        changeSelect(llContainer, resultDescBean);
    }

    @Override
    public IsRight checkWidgetDesc(MultiChoice0Entity descBean) {
        return MultiChoice0Entity.checkRight(descBean);
    }

    @Override
    public void fillWidgetDesc(MultiChoice0Entity showDescBean) {
        fillData(mActivity, showDescBean);
    }

    @Override
    protected Class<MultiChoice0Entity> getShowDescType() {
        return MultiChoice0Entity.class;
    }

    @Override
    protected Type getResultDescType() {
        return new TypeToken<ArrayList<Integer>>() {
        }.getType();
    }
}
