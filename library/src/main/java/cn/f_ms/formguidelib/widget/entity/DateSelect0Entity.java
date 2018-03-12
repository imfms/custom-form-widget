package cn.f_ms.formguidelib.widget.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.f_ms.library.logic.IsRight;

public class DateSelect0Entity {

    /**
     * is_fill_local_time : ${is_fill_local_time}
     * more_than_check : [{"timestamp":"${timestamp}","error_tips":"${error_tips}"}]
     * less_than_check : [{"timestamp":"${timestamp}","error_tips":"${error_tips}"}]
     */

    @SerializedName("is_fill_local_time")
    public boolean is_fill_local_time;
    @SerializedName("more_than_check")
    public List<Verify> more_than_check;
    @SerializedName("less_than_check")
    public List<Verify> less_than_check;

    public static class Verify {

        /**
         * timestamp : ${timestamp}
         * error_tips : ${error_tips}
         */
        public long timestamp;
        public String error_tips;
    }

    public static IsRight checkRight(DateSelect0Entity showDesc) {
        if (showDesc == null) {
            return IsRight.yes();
        }

        if (showDesc.less_than_check == null) {
            return IsRight.yes();
        }

        return IsRight.yes();
    }
}