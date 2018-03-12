package cn.f_ms.formguidelib.widget.show;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.f_ms.formguidelib.Context;
import cn.f_ms.formguidelib.R;
import cn.f_ms.formguidelib.widget.entity.DateSelect0Entity;
import cn.f_ms.library.logic.IsRight;

public class DateSelect0ShowHandler extends BaseWidgetShowWithShowOrResultBeanHandler<DateSelect0Entity, Long> {

    private TextView tvDateDisplay;

    public DateSelect0ShowHandler(Activity activity, ViewGroup parent, Context context) {
        super(activity, parent, context);
    }

    @Override
    protected View generateView(Activity mActivity, LayoutInflater layoutInflater, ViewGroup parent) {

        View view = layoutInflater.inflate(R.layout.holder_show_date_select_0, parent, false);

        tvDateDisplay = (TextView) view.findViewById(R.id.tv_date_display);

        return view;
    }

    @Override
    public IsRight checkWidgetDesc(DateSelect0Entity descBean) {
        return DateSelect0Entity.checkRight(descBean);
    }

    @Override
    public void fillWidgetDesc(DateSelect0Entity showDescBean) {
        fillData(showDescBean);
    }

    @Override
    public IsRight checkResultData(Long resultDescBean) {
        return IsRight.yes();
    }

    private void fillData(DateSelect0Entity descBean) {

        // is_fill_local_time
        if (descBean.is_fill_local_time) {
            fillResultDataWithJava(System.currentTimeMillis());
        }
    }

    @Override
    public void fillResultData(Long resultDescBean) {
        fillResultDataWithJava(resultDescBean);
    }

    private void fillResultDataWithJava(Long resultDescBean) {
        if (resultDescBean == null
                || resultDescBean < 0) {
            tvDateDisplay.setText(null);
            return;
        }

        // 兼容php时间戳没有毫秒
        resultDescBean *= 1000;

        String dateStr = getDateStr(resultDescBean);

        tvDateDisplay.setText(dateStr);
    }

    private String getDateStr(Long resultDescBean) {
        return new SimpleDateFormat("yyyy 年 MM 月 dd 日")
                .format(new Date(resultDescBean));
    }

    @Override
    protected Class<DateSelect0Entity> getShowDescType() {
        return DateSelect0Entity.class;
    }

    @Override
    protected Class<Long> getResultDescType() {
        return Long.class;
    }
}
