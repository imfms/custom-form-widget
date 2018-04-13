package cn.f_ms.formguidelib.widget.entity;

import com.google.gson.annotations.SerializedName;

import cn.f_ms.library.logic.IsRight;

public class CombineNameWithWidget0Entity {

    /**
     * 名称集
     */
    @SerializedName("names")
    public String[] names;

    /**
     * 控件展示数据格式
     */
    @SerializedName("widget")
    public String widgetEntityJson;

    public static IsRight checkRight(CombineNameWithWidget0Entity showDesc) {

        if (showDesc == null) {
            return IsRight.no("showDesc can't be null");
        }

        if (showDesc.names == null) {
            return IsRight.no("showDesc's names can't be null");
        }

        for (int i = 0; i < showDesc.names.length; i++) {
            if (showDesc.names[i] == null) {
                return IsRight.no("showDesc's names can't container null value, but found on index '" + i + "'");
            }
        }

        return IsRight.yes();
    }

}