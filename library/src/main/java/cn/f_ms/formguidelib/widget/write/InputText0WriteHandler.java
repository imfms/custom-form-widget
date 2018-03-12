package cn.f_ms.formguidelib.widget.write;

import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import cn.f_ms.formguidelib.Context;
import cn.f_ms.formguidelib.FormHandler;
import cn.f_ms.formguidelib.R;
import cn.f_ms.formguidelib.widget.entity.InputText0Entity;
import cn.f_ms.library.logic.IsRight;

public class InputText0WriteHandler extends BaseWidgetWriteWithShowBeanHandler<InputText0Entity> {

    private EditText etEditText;

    public InputText0WriteHandler(Activity activity, ViewGroup parent, Context context) {
        super(activity, parent, context);
    }

    @Override
    protected View generateView(Activity mActivity, LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.holder_write_input_text_0, parent, false);

        etEditText = (EditText) view.findViewById(R.id.et_edit_text);

        return view;
    }

    @Override
    public IsRight checkWidgetDesc(InputText0Entity descBean) {
        return InputText0Entity.checkRight(descBean);
    }

    @Override
    public void fillWidgetDesc(InputText0Entity showDescBean) {
        fillData(showDescBean);
    }

    @Override
    public IsRight checkResultJson(String resultJson) {
        // do nothing
        return IsRight.yes();
    }

    @Override
    public void fillResult(String resultDescJson) {
        etEditText.setText(resultDescJson);
    }

    @Override
    public FormHandler.FinalResult getFinalResult() {

        String etStr = etEditText.getText().toString();

        // input type check
        if (!etStr.isEmpty()) {
            switch (mShowDescBean.input_type) {
                case InputText0Entity.INPUT_TYPE_NUMBER_PASSWORD:
                    if (!etStr.matches(InputText0Entity.CHECK_REGULAR_NUMBER_PASSWORD)) {
                        return obtResult(false, InputText0Entity.ERROR_MESSAGE_CHECK_REGULAR_NUMBER_PASSWORD, null);
                    }
                case InputText0Entity.INPUT_TYPE_NUMBER:
                    if (!etStr.matches(InputText0Entity.CHECK_REGULAR_NUMBER)) {
                        return obtResult(false, InputText0Entity.ERROR_MESSAGE_CHECK_REGULAR_NUMBER, null);
                    }
                    break;
                case InputText0Entity.INPUT_TYPE_NUMBER_DECIMAL:
                    if (!etStr.matches(InputText0Entity.CHECK_REGULAR_NUMBER_DECIMAL)) {
                        return obtResult(false, InputText0Entity.ERROR_MESSAGE_CHECK_REGULAR_NUMBER_DECIMAL, null);
                    }
                    break;
                case InputText0Entity.INPUT_TYPE_NUMBER_SIGNED:
                    if (!etStr.matches(InputText0Entity.CHECK_REGULAR_NUMBER_SIGNED)) {
                        return obtResult(false, InputText0Entity.ERROR_MESSAGE_CHECK_REGULAR_NUMBER_SIGNED, null);
                    }
                    break;
                case InputText0Entity.INPUT_TYPE_TEXT_PASSWORD:
                case InputText0Entity.INPUT_TYPE_TEXT:
                default:
                    break;
            }
        }

        // maxLength check
        if (mShowDescBean.max_length > 0) {

            if (etStr.length() > mShowDescBean.max_length) {
                return obtResult(false, String.format("输入不得超过 '%s' 个字符", mShowDescBean.max_length), null);
            }
        }

        // maxLines
        if (mShowDescBean.max_lines > 0) {
            if (isMoreThanLines(etStr, mShowDescBean.max_lines)) {

                return obtResult(false, String.format("不能超过 '%s' 行", mShowDescBean.max_lines), null);
            }
        }

        // regular check
        if (mShowDescBean.verify != null
                && !mShowDescBean.verify.isEmpty()) {

            for (InputText0Entity.Verify regular : mShowDescBean.verify) {

                if (regular != null
                        && regular.regular_expression != null) {

                    boolean isUnMatch;

                    try {

                        isUnMatch = !etStr.matches(regular.regular_expression);
                    } catch (Exception e) {

                        e.printStackTrace();

                        return obtResult(
                                false,
                                "服务器数据错误，请稍后重试~",
                                null
                        );
                    }

                    if (isUnMatch) {

                        return obtResult(
                                false,
                                regular.error_tip != null
                                        ? regular.error_tip : InputText0Entity.ERROR_MESSAGE_VERIFY_REGULAR_DEFAULT,
                                null
                        );
                    }
                }
            }
        }

        return obtResult(true, null, etStr);
    }

    private boolean isMoreThanLines(String etStr, int max_lines) {

        return etStr.matches(
                "(.{0,}\\n.{0,}){" + max_lines + ",}"
        );
    }

    private FormHandler.FinalResult obtResult(boolean isTrue, String errorMessage, String result) {

        return new FormHandler.FinalResult(
                isTrue, errorMessage, result
        );
    }

    @Override
    protected Class<InputText0Entity> getShowDescType() {
        return InputText0Entity.class;
    }

    private void fillData(InputText0Entity bean) {

        // hint
        etEditText.setHint(bean.hint);

        // text
        etEditText.setText(bean.text);

        /*
        inputType
         */
        bean.input_type = bean.input_type != null
                ? bean.input_type : InputText0Entity.INPUT_TYPE_TEXT;

        int inputType;

        switch (bean.input_type) {
            case InputText0Entity.INPUT_TYPE_NUMBER:
                inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;
                break;
            case InputText0Entity.INPUT_TYPE_NUMBER_DECIMAL:
                inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
                break;
            case InputText0Entity.INPUT_TYPE_NUMBER_PASSWORD:
                inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
                break;
            case InputText0Entity.INPUT_TYPE_NUMBER_SIGNED:
                inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;
                break;
            case InputText0Entity.INPUT_TYPE_TEXT_PASSWORD:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;
            case InputText0Entity.INPUT_TYPE_TEXT:
            default:
                inputType = etEditText.getInputType();
                break;
        }

        etEditText.setInputType(inputType);

        // maxLength
        if (bean.max_length > 0) {

            etEditText.setFilters(
                    new InputFilter[]{
                            new InputFilter.LengthFilter(bean.max_length)
                    }
            );
        }

        // maxLinex
        setMaxLinesStr(etEditText, bean.max_lines);
    }

    private void setMaxLinesStr(final EditText etEditText, final int maxLines) {

        if (maxLines <= 0) {
            return;
        }

        if (maxLines == 1) {
            etEditText.setSingleLine();
        }

        etEditText.addTextChangedListener(new TextWatcher() {

            private boolean isCode;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                if (isCode) {
                    isCode = false;
                    return;
                }

                StringBuilder resultStr = handleStrMoreNumCharReplace(s.toString(), maxLines, '\n', ' ');

                isCode = true;
                etEditText.setText(resultStr);
                etEditText.setSelection(resultStr.length());
            }


        });

    }

    private static StringBuilder handleStrMoreNumCharReplace(String sourceStr, int moreNum, char beforeReplace, char afterReplace) {

        moreNum--;

        StringBuilder sb = new StringBuilder();

        int count = 0;

        int resultIndex;
        int lastIndex = 0;

        while ((resultIndex = sourceStr.indexOf(beforeReplace, lastIndex)) != -1) {

            count++;

            sb.append(sourceStr, lastIndex, resultIndex);

            lastIndex = resultIndex + 1;

            if (count > moreNum) {
                sb.append(afterReplace);
            } else {
                sb.append(beforeReplace);
            }
        }

        sb.append(sourceStr, lastIndex, sourceStr.length());

        return sb;
    }
}