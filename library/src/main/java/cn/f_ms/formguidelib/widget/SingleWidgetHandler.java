package cn.f_ms.formguidelib.widget;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Type;

import cn.f_ms.formguidelib.FormContext;
import cn.f_ms.formguidelib.FormHandler;
import cn.f_ms.formguidelib.ViewStructureBuilder;
import cn.f_ms.formguidelib.entity.ResultEntity;
import cn.f_ms.formguidelib.entity.WidgetEntity;
import cn.f_ms.formguidelib.widget.write.BaseWidgetWriteWithShowOrResultBeanHandler;
import cn.f_ms.library.logic.IsRight;

/**
 * Desc: 单控件容器,用于包装单控件
 *
 * @author f_ms
 * @time 18-3-9
 */

public class SingleWidgetHandler extends BaseWidgetWriteWithShowOrResultBeanHandler<WidgetEntity, ResultEntity> {

    /**
     * 自类型
     */
    public static class MyWidgetEntity extends WidgetEntity {

        public MyWidgetEntity(String show_desc_data, String title) {
            super(null, null, show_desc_data, null, title);
        }
    }

    private TextView tvTitle;
    private ViewGroup itemContainer;
    private WidgetEntity mWidgetEntity;
    private FormHandler mConvertHolder;

    public SingleWidgetHandler(Activity activity, ViewGroup parent, FormContext formContext) {
        super(activity, parent, formContext);
    }

    @Override
    protected View generateView(Activity activity, LayoutInflater layoutInflater, ViewGroup parent) {
        ViewStructureBuilder.ViewStructureWrapper viewStructureWrapper = getContext().getViewStructureBuilder().create(activity, parent);

        tvTitle = viewStructureWrapper.titleView();
        itemContainer = viewStructureWrapper.contentContainer();

        return viewStructureWrapper.rootView();
    }

    @Override
    public IsRight checkWidgetDesc(WidgetEntity descBean) {
        IsRight entityIsRight = WidgetEntity.checkIsRight(descBean);
        if (!entityIsRight.isRight()) {
            return entityIsRight;
        }

        return IsRight.yes();
        // TODO: 18-4-13 bugfix: Will NPE? Maybe I have no mConvertHolder now.
        // return mConvertHolder.checkWidgetDescJson(descBean.show_desc_data);
    }

    @Override
    public void fillWidgetDesc(WidgetEntity showDescBean) {
        mConvertHolder = getConvertHolder(showDescBean, itemContainer);
        mWidgetEntity = showDescBean;
        itemContainer.removeAllViews();
        itemContainer.addView(mConvertHolder.getContentView());
        mConvertHolder.fillWidgetDescJson(showDescBean.show_desc_data);

        if (showDescBean.title == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(showDescBean.title);
        }
    }

    @Override
    public IsRight checkResultData(ResultEntity resultDescBean) {
        IsRight entityIsRight = ResultEntity.checkRight(resultDescBean);
        if (!entityIsRight.isRight()) {
            return entityIsRight;
        }
        return mConvertHolder.checkResultJson(resultDescBean.result);
    }

    @Override
    public void fillResultData(ResultEntity resultDescBean) {
        mConvertHolder.fillResult(resultDescBean.result);
    }

    @Override
    public Result<ResultEntity> getResult() {
        FormHandler.FinalResult holderResult = mConvertHolder.getFinalResult();

        if (!holderResult.isSuccess) {
            String errorMessage = String.format(
                    "%s:%s",
                    mWidgetEntity.title != null
                            ? mWidgetEntity.title
                            : "",
                    holderResult.errorMessage
            );
            return new Result<>(false, errorMessage, null);
        }

        return new Result<>(true, null, new ResultEntity(mWidgetEntity.elementId, holderResult.json));
    }

    @Override
    protected Type getShowDescType() {
        return WidgetEntity.class;
    }

    @Override
    protected Type getResultDescType() {
        return ResultEntity.class;
    }

    private FormHandler getConvertHolder(final WidgetEntity guide, final ViewGroup containView) {

        // 自类型
        if (guide instanceof MyWidgetEntity) {
            return new SingleWidgetHandler(mActivity, containView, getContext());
        }

        return getContext().getConvertHolder(guide.type, guide.version, mActivity, containView);
    }
}
