package cn.f_ms.formguidelib.widget.show;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.f_ms.formguidelib.FormContext;
import cn.f_ms.formguidelib.R;
import cn.f_ms.formguidelib.widget.entity.SingleChoice0Entity;
import cn.f_ms.library.logic.IsRight;

public class SingleChoice0Handler extends BaseWidgetShowWithShowOrResultBeanHandler<SingleChoice0Entity, Integer> {

    private RadioGroup rgRadioGroup;

    public SingleChoice0Handler(Activity activity, ViewGroup parent, FormContext formContext) {
        super(activity, parent, formContext);
    }

    @Override
    public IsRight checkResultData(Integer resultDescBean) {
        if (resultDescBean == null) {
            return IsRight.no("resultDescBean can't be null");
        }

        return IsRight.yes();
    }

    @Override
    protected View generateView(Activity mActivity, LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.holder_show_single_choice_0, parent, false);

        rgRadioGroup = (RadioGroup) view.findViewById(R.id.rg_radio);

        return view;
    }

    private void fillData(Activity activity, SingleChoice0Entity descBean) {

        if (descBean.options == null
                || descBean.options.isEmpty()) {
            return;
        }

        // options
        for (SingleChoice0Entity.Option option : descBean.options) {

            rgRadioGroup.addView(
                    generateRadioButton(option, activity)
            );
        }

        // defaultSelect
        changeSelect(descBean.default_select);
    }

    // change radiogroup's check button from optionId
    private void changeSelect(Integer selectOptionId) {

        if (selectOptionId == null) {
            return;
        }

        int ChildId = -1;

        for (int x = 0; x < rgRadioGroup.getChildCount(); x++) {

            RadioButton radioButton = (RadioButton) rgRadioGroup.getChildAt(x);

            if (selectOptionId.equals(
                    ((SingleChoice0Entity.Option) radioButton.getTag()).choice_id)) {
                ChildId = radioButton.getId();
                break;
            }
        }

        if (ChildId == -1) {
            return;
        }

        rgRadioGroup.check(ChildId);
    }

    private RadioButton generateRadioButton(SingleChoice0Entity.Option option, Activity activity) {

        RadioButton radioButton = new RadioButton(activity);

        radioButton.setText(option.choice_title);
        radioButton.setTag(option);
        radioButton.setClickable(false);

        return radioButton;
    }

    @Override
    public void fillResultData(Integer resultDescBean) {
        changeSelect(resultDescBean);
    }

    @Override
    public IsRight checkWidgetDesc(SingleChoice0Entity descBean) {
        return SingleChoice0Entity.isRight(descBean);
    }

    @Override
    public void fillWidgetDesc(SingleChoice0Entity showDescBean) {
        fillData(mActivity, showDescBean);
    }

    @Override
    protected Class<SingleChoice0Entity> getShowDescType() {
        return SingleChoice0Entity.class;
    }


    @Override
    protected Class<Integer> getResultDescType() {
        return Integer.class;
    }
}
