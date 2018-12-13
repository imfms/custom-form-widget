package cn.f_ms.formguidelib.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.f_ms.formguidelib.FormContext;
import cn.f_ms.formguidelib.entity.ResultEntity;
import cn.f_ms.formguidelib.entity.WidgetEntity;
import cn.f_ms.formguidelib.widget.write.BaseWidgetWriteWithShowOrResultBeanHandler;
import cn.f_ms.library.logic.IsRight;

/**
 * Desc: 容器控件，容纳一组控件
 *
 * @author f_ms
 * @time 18-4-12
 */
public class Container0Holder extends BaseWidgetWriteWithShowOrResultBeanHandler<List<WidgetEntity>, List<ResultEntity>> {


    private final List<WidgetEntity> mDatas = new ArrayList<>();
    private final Map<WidgetEntity, SingleWidgetHandler> mWidgetEntityHandlerMap = new LinkedHashMap<>();

    private ViewGroup mContainView;

    public Container0Holder(Activity activity, ViewGroup parent, FormContext formContext) {
        super(activity, parent, formContext);
    }

    @Override
    protected View generateView(Activity activity, LayoutInflater layoutInflater, ViewGroup parent) {
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        mContainView = linearLayout;

        return linearLayout;
    }

    @Override
    public IsRight checkWidgetDesc(List<WidgetEntity> descBean) {
        if (descBean == null) {
            return IsRight.no("descBean can't be null");
        }

        int nullIndex = descBean.indexOf(null);
        if (nullIndex >= 0) {
            return IsRight.no("descBean can't contain null value, but found on '" + nullIndex + "'");
        }

        for (WidgetEntity widgetEntity : descBean) {
            IsRight isRight = WidgetEntity.checkIsRight(widgetEntity);
            if (!isRight.isRight()) {
                return isRight;
            }
        }

        return IsRight.yes();
    }

    @Override
    public void fillWidgetDesc(List<WidgetEntity> showDescBean) {
        mDatas.clear();
        mWidgetEntityHandlerMap.clear();
        mContainView.removeAllViews();

        mDatas.addAll(showDescBean);

        for (WidgetEntity widgetEntity : showDescBean) {

            SingleWidgetHandler widgetHandler = new SingleWidgetHandler(mActivity, mContainView, getContext());

            mContainView.addView(widgetHandler.getContentView());
            widgetHandler.fillWidgetDesc(widgetEntity);

            mWidgetEntityHandlerMap.put(widgetEntity, widgetHandler);
        }
    }

    @Override
    public IsRight checkResultData(List<ResultEntity> resultDescBean) {
        if (resultDescBean == null) {
            return IsRight.no("resultDescBean can't be null");
        }

        int nullIndex = resultDescBean.indexOf(null);
        if (nullIndex >= 0) {
            return IsRight.no("resultDescBean can't contain null value, but found on '" + nullIndex + "'");
        }

        for (ResultEntity resultEntity : resultDescBean) {
            IsRight isRight = ResultEntity.checkRight(resultEntity);
            if (!isRight.isRight()) {
                return isRight;
            }
        }

        return IsRight.yes();
    }

    @Override
    public void fillResultData(List<ResultEntity> resultDescBean) {
        for (ResultEntity resultEntity : resultDescBean) {
            SingleWidgetHandler widgetHandler = getWidgetHandler(resultEntity);
            if (widgetHandler == null) {
                continue;
            }
            widgetHandler.fillResultData(resultEntity);
        }
    }

    @Override
    public Result<List<ResultEntity>> getResult() {

        List<ResultEntity> resultList = new ArrayList<>(mDatas.size());

        for (Map.Entry<WidgetEntity, SingleWidgetHandler> entry : mWidgetEntityHandlerMap.entrySet()) {

            BaseWidgetWriteWithShowOrResultBeanHandler.Result<ResultEntity> result = entry.getValue().getResult();
            if (!result.isSuccess) {
                return new Result<>(false, result.errorMessage, null);
            }

            resultList.add(result.result);
        }

        return new Result<>(true, null, resultList);
    }

    @Override
    protected Type getShowDescType() {
        return new TypeToken<List<WidgetEntity>>() {
        }.getType();
    }

    @Override
    protected Type getResultDescType() {
        return new TypeToken<List<ResultEntity>>() {
        }.getType();
    }

    private SingleWidgetHandler getWidgetHandler(ResultEntity resultEntity) {

        for (Map.Entry<WidgetEntity, SingleWidgetHandler> entry : mWidgetEntityHandlerMap.entrySet()) {
            WidgetEntity widgetEntity = entry.getKey();
            SingleWidgetHandler widgetHandler = entry.getValue();

            if (widgetEntity.elementId.equals(resultEntity.elementId)) {
                return widgetHandler;
            }
        }

        return null;
    }
}
