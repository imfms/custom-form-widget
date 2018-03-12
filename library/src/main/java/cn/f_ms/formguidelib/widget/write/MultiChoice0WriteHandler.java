package cn.f_ms.formguidelib.widget.write;

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

import cn.f_ms.formguidelib.Context;
import cn.f_ms.formguidelib.R;
import cn.f_ms.formguidelib.widget.entity.MultiChoice0Entity;
import cn.f_ms.library.logic.IsRight;

/**
 * 多选控件,版本0
 */
public class MultiChoice0WriteHandler extends BaseWidgetWriteWithShowOrResultBeanHandler<MultiChoice0Entity, ArrayList<Integer>> {

    private LinearLayout llContainer;

    public MultiChoice0WriteHandler(Activity activity, ViewGroup parent, Context context) {
        super(activity, parent, context);
    }

    @Override
    protected View generateView(Activity mActivity, LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.holder_write_multi_choice_0, parent, false);

        llContainer = (LinearLayout) view.findViewById(R.id.ll_container);

        return view;
    }

    @Override
    public IsRight checkWidgetDesc(MultiChoice0Entity descBean) {
        return MultiChoice0Entity.checkRight(descBean);
    }

    @Override
    public void fillWidgetDesc(MultiChoice0Entity showDescBean) {
        fillData(mActivity, showDescBean);
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

    private void changeSelect(LinearLayout llContainer, List<Integer> selectIds) {

        if (selectIds == null) {
            selectIds = new ArrayList<>(0);
        }

        for (int x = 0; x < llContainer.getChildCount(); x++) {

            CheckBox checkBox = (CheckBox) llContainer.getChildAt(x);

            MultiChoice0Entity.Option option = (MultiChoice0Entity.Option) checkBox.getTag();

            checkBox.setChecked(
                    selectIds.contains(option.choice_id)
            );
        }
    }

    private CheckBox generateCheckBox(Activity mActivity, MultiChoice0Entity.Option option) {

        CheckBox checkBox = new CheckBox(mActivity);
        checkBox.setTag(option);

        checkBox.setText(option.choice_title);

        return checkBox;
    }

    @Override
    public Result<ArrayList<Integer>> getResult() {

        ArrayList<Integer> checkedIds = getCheckedIds();

        if (mShowDescBean.min_select_num >= 0) {
            if (checkedIds.size() < mShowDescBean.min_select_num) {

                String errorMsg = String.format("最少选择 '%s' 个选项", mShowDescBean.min_select_num);

                return new Result<>(
                        false,
                        errorMsg,
                        null
                );
            }
        }

        if (mShowDescBean.max_select_num > 0) {

            if (checkedIds.size() > mShowDescBean.max_select_num) {

                String errorMsg = String.format("最多选择 '%s' 个选项", mShowDescBean.max_select_num);

                return new Result<>(
                        false,
                        errorMsg,
                        null
                );
            }
        }

        return new Result<>(
                true,
                null,
                checkedIds
        );
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

    private ArrayList<Integer> getCheckedIds() {

        ArrayList<Integer> ids = new ArrayList<>();

        for (int x = 0; x < llContainer.getChildCount(); x++) {
            CheckBox cb = (CheckBox) llContainer.getChildAt(x);

            if (cb.isChecked()) {
                MultiChoice0Entity.Option option = (MultiChoice0Entity.Option) cb.getTag();
                ids.add(option.choice_id);
            }
        }

        return ids;
    }

    @Override
    public void fillResultData(ArrayList<Integer> resultDescBean) {
        changeSelect(llContainer, resultDescBean);
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
