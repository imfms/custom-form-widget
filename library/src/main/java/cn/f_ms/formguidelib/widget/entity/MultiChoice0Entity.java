package cn.f_ms.formguidelib.widget.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.f_ms.library.logic.IsRight;

public class MultiChoice0Entity {

    /**
     * options : [{"choice_id":"${choice_id}","choice_title":"${choice_title}"}]
     * default_select : ["${default_select_id1}"]
     * min_select_num : ${min_select_num}
     * max_select_num : ${max_select_num}
     */
    @SerializedName("min_select_num")
    public Integer min_select_num;
    @SerializedName("max_select_num")
    public Integer max_select_num;
    @SerializedName("options")
    public List<Option> options;
    @SerializedName("default_select")
    public List<Integer> default_select;

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

    public static IsRight checkRight(MultiChoice0Entity showDesc) {

        if (showDesc == null) {
            return IsRight.no("descBean can't be null");
        }

        if (showDesc.options == null) {
            return IsRight.no("descBean's options can't be null");
        }

        if (showDesc.options.isEmpty()) {
            return IsRight.no("descBean's options can't be empty");
        }

        for (int i = 0; i < showDesc.options.size(); i++) {

            MultiChoice0Entity.Option option = showDesc.options.get(i);

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

        if (showDesc.min_select_num == null) {
            showDesc.min_select_num = 0;
        } else {
            if (showDesc.min_select_num < 0) {
                return IsRight.no("descBean's min_select_num can't less 0, but found '" + showDesc.min_select_num + "'");
            }
        }

        if (showDesc.max_select_num == null) {
            showDesc.max_select_num = showDesc.options.size();
        } else {

            if (showDesc.max_select_num > showDesc.options.size()) {
                showDesc.max_select_num = showDesc.options.size();
            }
            if (showDesc.max_select_num < showDesc.min_select_num) {
                return IsRight.no("descBean's max_select_num can't less min_select_num, but found '"
                        + showDesc.max_select_num + " < " + showDesc.min_select_num + "'");
            }
        }

        return IsRight.yes();
    }

}