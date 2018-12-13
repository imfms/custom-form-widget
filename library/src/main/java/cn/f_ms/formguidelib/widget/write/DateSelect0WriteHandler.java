package cn.f_ms.formguidelib.widget.write;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.f_ms.formguidelib.FormContext;
import cn.f_ms.formguidelib.R;
import cn.f_ms.formguidelib.widget.entity.DateSelect0Entity;
import cn.f_ms.library.logic.IsRight;

public class DateSelect0WriteHandler extends BaseWidgetWriteWithShowOrResultBeanHandler<DateSelect0Entity, Long> {

    private LinearLayout llDateSelect;
    private TextView tvDateDisplay;
    private long mSelectDate = -1;

    public DateSelect0WriteHandler(Activity activity, ViewGroup parent, FormContext formContext) {
        super(activity, parent, formContext);
    }

    @Override
    protected View generateView(Activity mActivity, LayoutInflater layoutInflater, ViewGroup parent) {

        View view = layoutInflater.inflate(R.layout.holder_write_date_select_0, parent, false);

        llDateSelect = (LinearLayout) view.findViewById(R.id.ll_date_select);
        tvDateDisplay = (TextView) view.findViewById(R.id.tv_date_display);

        initView();

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

    private void initView() {

        llDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateSelectDialog(getActivity(), mSelectDate);
            }
        });

    }

    private void showDateSelectDialog(Activity activity, long fillTimeStamp) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(
                new Date(fillTimeStamp < 0
                        ? System.currentTimeMillis()
                        : fillTimeStamp)
        );

        new DatePickerDialog(
                activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month++; // 默认选择月份为0-11, 需要修改为1-12

                        onDateSelect(year, month, dayOfMonth);

                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void onDateSelect(int year, int month, int dayOfMonth) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        long dateSelectTimeStamps = -1;

        try {
            dateSelectTimeStamps = simpleDateFormat.parse(String.format("%s-%s-%s", year, month, dayOfMonth)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mSelectDate = dateSelectTimeStamps;

        fillResultDataWithJava(mSelectDate);
    }

    private void fillData(DateSelect0Entity descBean) {

        // is_fill_local_time
        if (descBean.is_fill_local_time) {

            this.mSelectDate = System.currentTimeMillis();

            fillResultDataWithJava(mSelectDate);
        }
    }

    @Override
    public Result<Long> getResult() {

        long selectDate = this.mSelectDate / 1000; // php时间戳不包含毫秒

        // less_than_check
        if (mShowDescBean.less_than_check != null
                && !mShowDescBean.less_than_check.isEmpty()) {

            for (DateSelect0Entity.Verify verify : mShowDescBean.less_than_check) {

                if (selectDate > verify.timestamp) {

                    String errorMessage = verify.error_tips != null
                            ? verify.error_tips
                            : String.format("选择时间不能大于 '%s'", getDateStr(verify.timestamp));

                    return new Result<>(
                            false,
                            errorMessage,
                            null
                    );
                }
            }
        }

        // more_than_check
        if (mShowDescBean.more_than_check != null
                && !mShowDescBean.more_than_check.isEmpty()) {

            for (DateSelect0Entity.Verify verify : mShowDescBean.more_than_check) {

                if (selectDate < verify.timestamp) {

                    return new Result<>(
                            false,
                            verify.error_tips != null
                                    ? verify.error_tips
                                    : String.format("选择时间不能小于 '%s'", getDateStr(verify.timestamp)),
                            null
                    );
                }
            }
        }

        return new Result<>(
                true,
                null,
                selectDate
        );
    }

    @Override
    public IsRight checkResultData(Long resultDescBean) {
        // do nothing
        return IsRight.yes();
    }

    @Override
    public void fillResultData(Long resultDescBean) {

        if (resultDescBean != null) {
            resultDescBean *= 1000; // 兼容php时间戳没有毫秒
        }

        fillResultDataWithJava(resultDescBean);
    }

    private void fillResultDataWithJava(Long resultDescBean) {
        if (resultDescBean == null
                || resultDescBean < 0) {
            tvDateDisplay.setText(null);
            return;
        }

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
