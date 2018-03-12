package cn.f_ms.formguidelib.widget.show;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.f_ms.formguidelib.Context;
import cn.f_ms.formguidelib.MultiWidgetHolder;
import cn.f_ms.formguidelib.R;
import cn.f_ms.formguidelib.entity.ResultEntity;
import cn.f_ms.formguidelib.entity.WidgetEntity;
import cn.f_ms.formguidelib.widget.entity.List0ShowEntity;
import cn.f_ms.library.check.CheckNull;
import cn.f_ms.library.logic.IsRight;

/**
 * Desc: 列表控件, 容纳不定数量的一组控件
 *
 * @author f_ms
 * @time 18-3-10
 */
public class List0ShowHandler extends BaseWidgetShowWithShowOrResultBeanHandler<List0ShowEntity, List<List<ResultEntity>>> {

    private LinearLayout llItemContainer;

    private Map<View,MultiWidgetHolder> mCustomFormInfoMap = new LinkedHashMap<>();
    private List0ShowEntity mShowDescBean;

    public List0ShowHandler(Activity activity, ViewGroup parent, Context context) {
        super(activity, parent, context);
    }

    @Override
    protected View generateView(Activity activity, LayoutInflater layoutInflater, ViewGroup parent) {
        llItemContainer = new LinearLayout(activity);
        llItemContainer.setOrientation(LinearLayout.VERTICAL);

        return llItemContainer;
    }

    @Override
    public IsRight checkWidgetDesc(List0ShowEntity descBean) {
        return List0ShowEntity.checkRight(descBean);
    }

    @Override
    public void fillWidgetDesc(List0ShowEntity showDescBean) {
        mShowDescBean = showDescBean;

        addItem(showDescBean.defaultItemNum, showDescBean.itemElements);
    }

    @Override
    public IsRight checkResultData(List<List<ResultEntity>> resultDescBean) {
        CheckNull.ifNullThrowArgException(resultDescBean, "resultDescBean can't be null");

        int nullIndex = resultDescBean.indexOf(null);
        if (nullIndex >= 0) {
            throw new IllegalArgumentException("resultDescBean can't contain null value, but found on '" + nullIndex + "'");
        }

        MultiWidgetHolder customFormContainer;
        if (llItemContainer.getChildCount() > 0) {
            customFormContainer = mCustomFormInfoMap.get(llItemContainer.getChildAt(0));
        } else {
            customFormContainer = new MultiWidgetHolder(mActivity, null, getContext());
            customFormContainer.getContentView();
            customFormContainer.fillWidgetDesc(mShowDescBean.itemElements);
        }

        for (int i = 0; i < resultDescBean.size(); i++) {
            IsRight isRight = customFormContainer.checkResultData(resultDescBean.get(i));
            if (!isRight.isRight()) {
                return IsRight.no("found error on index '" + i + "': " + isRight.errorMessage());
            }
        }

        return IsRight.yes();
    }

    @Override
    public void fillResultData(List<List<ResultEntity>> resultDescBean) {

        llItemContainer.removeAllViews();
        addItem(resultDescBean.size(), mShowDescBean.itemElements);

        for (int i = 0; i < llItemContainer.getChildCount(); i++) {
            MultiWidgetHolder multiWidgetHolder = mCustomFormInfoMap.get(llItemContainer.getChildAt(i));
            List<ResultEntity> resultEntities = resultDescBean.get(i);
            multiWidgetHolder.fillResultData(resultEntities);
        }
    }

    @Override
    protected Type getShowDescType() {
        return List0ShowEntity.class;
    }

    @Override
    protected Type getResultDescType() {
        return new TypeToken<List<List<ResultEntity>>>(){}.getType();
    }


    private void addItem(Integer num, List<WidgetEntity> itemElements) {

        if (num == null) {
            num = 0;
        }

        for (Integer i = 0; i < num; i++) {
            addOneItem(itemElements);
        }
    }

    private void addOneItem(List<WidgetEntity> itemElements) {

        // 条目数量大于等于最大数量则不再添加
        if (llItemContainer.getChildCount() >= mShowDescBean.maxItemNum) {
            return;
        }

        final View itemContainer = View.inflate(mActivity, R.layout.holder_show_list_0_item_container, null);
        TextView tvOrderNumber = (TextView) itemContainer.findViewById(R.id.tv_order_number);
        LinearLayout llCustomFormContainer = (LinearLayout) itemContainer.findViewById(R.id.ll_custom_form_container);

        MultiWidgetHolder customFormContainer = new MultiWidgetHolder(mActivity, llCustomFormContainer, getContext());
        llCustomFormContainer.addView(customFormContainer.getContentView());

        // 顺序
        tvOrderNumber.setText(String.valueOf(llItemContainer.getChildCount() + 1) + ". ");

        customFormContainer.fillWidgetDesc(itemElements);

        llItemContainer.addView(itemContainer);
        mCustomFormInfoMap.put(itemContainer, customFormContainer);
    }
}
