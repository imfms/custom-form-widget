package cn.f_ms.formguidelib.widget.write;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.f_ms.formguidelib.Context;
import cn.f_ms.formguidelib.R;
import cn.f_ms.formguidelib.entity.ResultEntity;
import cn.f_ms.formguidelib.entity.WidgetEntity;
import cn.f_ms.formguidelib.widget.entity.List0ShowEntity;
import cn.f_ms.formguidelib.MultiWidgetHolder;
import cn.f_ms.library.check.CheckNull;
import cn.f_ms.library.logic.IsRight;

/**
 * Desc: 列表控件, 容纳不定数量的一组控件
 *
 * @author f_ms
 * @time 18-3-9
 */
public class List0WriteHandler extends BaseWidgetWriteWithShowOrResultBeanHandler<List0ShowEntity, List<List<ResultEntity>>> {

    private LinearLayout llItemContainer;
    private ImageView ivAddItem;

    private Map<View,ItemInfo> mCustomFormInfoMap = new LinkedHashMap<>();
    private List0ShowEntity mShowDescBean;

    public List0WriteHandler(Activity activity, ViewGroup parent, Context context) {
        super(activity, parent, context);
    }

    @Override
    protected View generateView(Activity activity, LayoutInflater layoutInflater, ViewGroup parent) {
        View contentView = layoutInflater.inflate(R.layout.holder_write_list_0, parent, false);

        llItemContainer = (LinearLayout) contentView.findViewById(R.id.ll_item_container);
        ivAddItem = (ImageView) contentView.findViewById(R.id.iv_add_item);
        ivAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOneItem(mShowDescBean.itemElements);
            }
        });

        return contentView;
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
            customFormContainer = mCustomFormInfoMap.get(llItemContainer.getChildAt(0)).customFormContainer;
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
            ItemInfo itemInfo = mCustomFormInfoMap.get(llItemContainer.getChildAt(i));
            List<ResultEntity> resultEntities = resultDescBean.get(i);
            itemInfo.customFormContainer.fillResultData(resultEntities);
        }
    }

    @Override
    public Result<List<List<ResultEntity>>> getResult() {

        int childCount = llItemContainer.getChildCount();

        if (childCount < mShowDescBean.minItemNum) {
            return new Result<>(false, "至少需要填写 '" + mShowDescBean.minItemNum + "' 个条目",null);
        }

        if (childCount > mShowDescBean.maxItemNum) {
            return new Result<>(false, "最多只能填写 '" + mShowDescBean.maxItemNum + "' 个条目", null);
        }

        List<List<ResultEntity>> results = new ArrayList<>(childCount);

        for (int i = 0; i < childCount; i++) {
            Result<List<ResultEntity>> result = mCustomFormInfoMap.get(llItemContainer.getChildAt(i))
                    .customFormContainer.getResult();
            if (!result.isSuccess) {
                return new Result<>(false, "条目 '" + (i + 1) + "' 发现错误: " + result.errorMessage, null);
            }
            results.add(result.result);
        }

        return new Result<>(true, null, results);
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

        final View itemContainer = View.inflate(mActivity, R.layout.holder_write_list_0_item_container, null);
        ImageView ivDeleteItem = (ImageView) itemContainer.findViewById(R.id.iv_delete_item);
        TextView tvOrderNumber = (TextView) itemContainer.findViewById(R.id.tv_order_number);
        LinearLayout llCustomFormContainer = (LinearLayout) itemContainer.findViewById(R.id.ll_custom_form_container);

        MultiWidgetHolder customFormContainer = new MultiWidgetHolder(mActivity, llCustomFormContainer, getContext());
        llCustomFormContainer.addView(customFormContainer.getContentView());

        // 顺序
        tvOrderNumber.setText(String.valueOf(llItemContainer.getChildCount() + 1) + ". ");
        ivDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOneItem(itemContainer);
            }
        });

        customFormContainer.fillWidgetDesc(itemElements);

        llItemContainer.addView(itemContainer);
        mCustomFormInfoMap.put(itemContainer, new ItemInfo(customFormContainer,tvOrderNumber));

        // 条目数量大于最大数量时隐藏添加按钮
        if (llItemContainer.getChildCount() >= mShowDescBean.maxItemNum) {
            if (ivAddItem.getVisibility() != View.GONE) {
                ivAddItem.setVisibility(View.GONE);
            }
        }
    }

    private void deleteOneItem(View itemContainer) {

        int itemPosition = -1;
        for (int i = 0; i < llItemContainer.getChildCount(); i++) {
            if (llItemContainer.getChildAt(i) == itemContainer) {
                itemPosition = i;
            }
        }

        if (itemPosition == -1) {
            return;
        }

        llItemContainer.removeView(itemContainer);
        mCustomFormInfoMap.remove(itemContainer);

        for (int i = itemPosition; i < llItemContainer.getChildCount(); i++) {
            TextView tvOrderNumber = mCustomFormInfoMap.get(llItemContainer.getChildAt(i)).tvOrderNumber;

            tvOrderNumber.setText(String.valueOf(i + 1) + ". ");
        }

        // 条目数量小于最大数量时展示添加按钮
        if (llItemContainer.getChildCount() < mShowDescBean.maxItemNum) {
            if (ivAddItem.getVisibility() != View.VISIBLE) {
                ivAddItem.setVisibility(View.VISIBLE);
            }
        }
    }

    private class ItemInfo {

        public ItemInfo(MultiWidgetHolder customFormContainer, TextView tvOrderNumber) {
            this.customFormContainer = customFormContainer;
            this.tvOrderNumber = tvOrderNumber;
        }

        MultiWidgetHolder customFormContainer;
        TextView tvOrderNumber;
    }

}
