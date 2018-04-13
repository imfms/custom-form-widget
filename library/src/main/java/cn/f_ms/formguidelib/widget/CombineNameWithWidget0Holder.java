package cn.f_ms.formguidelib.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.f_ms.formguidelib.Context;
import cn.f_ms.formguidelib.entity.ResultEntity;
import cn.f_ms.formguidelib.widget.entity.CombineNameWithWidget0Entity;
import cn.f_ms.formguidelib.widget.write.BaseWidgetWriteWithShowOrResultBeanHandler;
import cn.f_ms.library.logic.IsRight;

/**
 * 可以将指定不定数量个名称与指定控件一一组合的控件
 * 例如指定名称张三，李四，王五；指定控件文本框。结果为：张三文本框，李四文本框，王五文本框
 *
 * @author f_ms
 * @date 18-4-13
 */
public class CombineNameWithWidget0Holder extends BaseWidgetWriteWithShowOrResultBeanHandler<CombineNameWithWidget0Entity, List<ResultEntity>> {

    private LinearLayout llItemContainer;
    private List<SingleWidgetHandler> mWidgetHandlerList = new ArrayList<>();
    private String[] mNames;

    public CombineNameWithWidget0Holder(Activity activity, ViewGroup parent, Context context) {
        super(activity, parent, context);
    }

    @Override
    protected View generateView(Activity activity, LayoutInflater layoutInflater, ViewGroup parent) {

        llItemContainer = new LinearLayout(activity);
        llItemContainer.setOrientation(LinearLayout.VERTICAL);

        return llItemContainer;
    }

    @Override
    public IsRight checkWidgetDesc(CombineNameWithWidget0Entity descBean) {
        return CombineNameWithWidget0Entity.checkRight(descBean);
    }

    @Override
    public void fillWidgetDesc(CombineNameWithWidget0Entity showDescBean) {

        List<SingleWidgetHandler> singleWidgetHandlers = new ArrayList<>(showDescBean.names.length);

        for (String name : showDescBean.names) {
            SingleWidgetHandler handlers = new SingleWidgetHandler(mActivity, llItemContainer, getContext());

            handlers.getContentView();
            handlers.fillWidgetDesc(new SingleWidgetHandler.MyWidgetEntity(showDescBean.widgetEntityJson, name));

            singleWidgetHandlers.add(handlers);
        }

        llItemContainer.removeAllViews();
        mWidgetHandlerList.clear();

        mNames = Arrays.copyOf(showDescBean.names, showDescBean.names.length);
        mWidgetHandlerList.addAll(singleWidgetHandlers);
        for (SingleWidgetHandler singleWidgetHandler : mWidgetHandlerList) {
            llItemContainer.addView(singleWidgetHandler.getContentView());
        }
    }

    @Override
    public Result<List<ResultEntity>> getResult() {

        List<ResultEntity> results = new ArrayList<>(mWidgetHandlerList.size());
        Gson gson = new Gson();

        for (int i = 0; i < mWidgetHandlerList.size(); i++) {
            SingleWidgetHandler singleWidgetHandler = mWidgetHandlerList.get(i);

            Result<ResultEntity> result = singleWidgetHandler.getResult();
            if (!result.isSuccess) {
                return new Result<>(false, result.errorMessage, null);
            }

            results.add(gson.fromJson(result.result.result, ResultEntity.class));
        }

        return new Result<>(true, null, results);
    }

    @Override
    public IsRight checkResultData(List<ResultEntity> resultDescBean) {

        Gson gson = new Gson();

        for (int i = 0; i < mWidgetHandlerList.size(); i++) {
            SingleWidgetHandler singleWidgetHandler = mWidgetHandlerList.get(i);
            ResultEntity resultEntity = resultDescBean.get(i);
            ResultEntity finalResultEntity = new ResultEntity(resultEntity.elementId, gson.toJson(resultEntity));

            IsRight isRight = singleWidgetHandler.checkResultData(finalResultEntity);
            if (!isRight.isRight()) {
                return isRight;
            }
        }

        return IsRight.yes();
    }

    @Override
    public void fillResultData(List<ResultEntity> resultDescBean) {
        Gson gson = new Gson();

        for (int i = 0; i < mWidgetHandlerList.size(); i++) {
            SingleWidgetHandler singleWidgetHandler = mWidgetHandlerList.get(i);
            ResultEntity resultEntity = resultDescBean.get(i);
            ResultEntity finalResultEntity = new ResultEntity(resultEntity.elementId, gson.toJson(resultEntity));

            singleWidgetHandler.fillResultData(finalResultEntity);
        }
    }

    @Override
    protected Type getResultDescType() {
        return new TypeToken<List<ResultEntity>>(){}.getType();
    }

    @Override
    protected Type getShowDescType() {
        return CombineNameWithWidget0Entity.class;
    }
}
