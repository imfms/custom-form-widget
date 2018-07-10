package cn.f_ms.formguidelib.widget.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.f_ms.library.check.CheckCollection;
import cn.f_ms.library.logic.IsRight;

public class InputText0Entity {

    public static final String INPUT_TYPE_TEXT = "text";
    public static final String INPUT_TYPE_NUMBER = "number";
    public static final String INPUT_TYPE_NUMBER_DECIMAL = "number_decimal";
    public static final String INPUT_TYPE_NUMBER_SIGNED = "number_signed";
    public static final String INPUT_TYPE_TEXT_PASSWORD = "text_password";
    public static final String INPUT_TYPE_NUMBER_PASSWORD = "number_password";
    public static final String INPUT_TYPE_NUMBER_DECIMAL_SIGNED = "number_decimal_signed";

    public static final String CHECK_REGULAR_NUMBER = "^\\d+$";
    public static final String CHECK_REGULAR_NUMBER_DECIMAL = "^\\d+\\.{0,1}\\d{0,}$";
    public static final String CHECK_REGULAR_NUMBER_SIGNED = "^[-\\+]{0,1}\\d+$";
    public static final String CHECK_REGULAR_NUMBER_PASSWORD = CHECK_REGULAR_NUMBER;
    public static final String CHECK_REGULAR_NUMBER_DECIMAL_SIGNED = "^[-\\+]{0,1}\\d+\\.{0,1}\\d{0,}$";

    public static final String ERROR_MESSAGE_CHECK_REGULAR_NUMBER = "只能包含数字";
    public static final String ERROR_MESSAGE_CHECK_REGULAR_NUMBER_DECIMAL = "只能包含非负小数";
    public static final String ERROR_MESSAGE_CHECK_REGULAR_NUMBER_SIGNED = "只能包含整数";
    public static final String ERROR_MESSAGE_CHECK_REGULAR_NUMBER_PASSWORD = ERROR_MESSAGE_CHECK_REGULAR_NUMBER;
    public static final String ERROR_MESSAGE_CHECK_REGULAR_NUMBER_DECIMAL_SIGNED = "只能包含小数";

    public static final String ERROR_MESSAGE_VERIFY_REGULAR_DEFAULT = "校验错误，请检查后重试";

    /**
     * hint : ${hint}
     * text : ${text}
     * input_type : ${input_type}
     * max_length : ${max_length}
     * max_lines : ${max_lines}
     * verify : [{"regular_expression":"${regular_expression}","error_tip":"${error_tip}"}]
     */
    @SerializedName("hint")
    public String hint;
    @SerializedName("text")
    public String text;
    @SerializedName("input_type")
    public String input_type = INPUT_TYPE_TEXT;
    @SerializedName("max_length")
    public Integer max_length;
    @SerializedName("max_lines")
    public Integer max_lines;
    @SerializedName("verify")
    public List<Verify> verify;

    public static class Verify {
        /**
         * regular_expression : ${regular_expression}
         * error_tip : ${error_tip}
         */
        @SerializedName("regular_expression")
        public String regular_expression;
        @SerializedName("error_tip")
        public String error_tip;
    }

    public static IsRight checkRight(InputText0Entity descBean) {

        if (descBean == null) {
            return IsRight.yes();
        }

        if (descBean.max_length == null) {
            descBean.max_length = Integer.MAX_VALUE;
        } else {
            if (descBean.max_length < 0) {
                return IsRight.no("descBean's max_length can't less 0, but found '" + descBean.max_length + "'");
            }
        }

        if (descBean.max_lines == null) {
            descBean.max_lines = -1;
        }

        if (!CheckCollection.isEmptyWithNull(descBean.verify)) {
            int nullIndex = descBean.verify.indexOf(null);
            if (nullIndex >= 0) {
                return IsRight.no("descBean.verify can't contain null, but found on '" + nullIndex + "'");
            }

            for (int i = 0; i < descBean.verify.size(); i++) {
                InputText0Entity.Verify verify = descBean.verify.get(i);

                if (verify.regular_expression == null) {
                    return IsRight.no("descBean.verify's element's regular_expression can't be null, but found on '" + i + "'");
                }

                try {
                    Pattern.compile(verify.regular_expression);
                } catch (PatternSyntaxException e) {
                    return IsRight.no("descBean.verify's element '" + i + "' regular expression parse error: " + e.getMessage());
                }
            }
        }

        return IsRight.yes();
    }

}