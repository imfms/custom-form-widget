package cn.f_ms.formguidelib.widget.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.f_ms.library.logic.IsRight;

public class SingleChoice0Entity {

    /**
     * options : [{"choice_id":"${choice_id}","choice_title":"${choice_title}"}]
     * default_select : ${default_select_choice_id}
     */
    @SerializedName("default_select")
    public Integer default_select;
    @SerializedName("options")
    public List<Option> options;

    public static class Option {
        /**
         * choice_id : ${choice_id}
         * choice_title : ${choice_title}
         */

        @SerializedName("choice_id")
        public Integer choice_id;
        @SerializedName("choice_title")
        public String choice_title;
    }

    public static IsRight isRight(SingleChoice0Entity descBean) {
        if (descBean == null) {
            return IsRight.no("descBean can't be null");
        }

        if (descBean.options == null) {
            return IsRight.no("descBean's options can't be null");
        }

        if (descBean.options.isEmpty()) {
            return IsRight.no("descBean's options can't be empty");
        }

        for (int i = 0; i < descBean.options.size(); i++) {

            SingleChoice0Entity.Option option = descBean.options.get(i);

            if (option == null) {
                return IsRight.no("descBean's options can't contain null value, but '" + i + "' is");
            }

            if (option.choice_id == null) {
                return IsRight.no("descBean's options's element's choice_id can't be null value, but '" + i + "' is");
            }

            if (option.choice_title == null) {
                return IsRight.no("descBean's options's element's choice_title can't be null value, but '" + i + "' is");
            }
        }

        return IsRight.yes();
    }
}