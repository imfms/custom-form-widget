package cn.f_ms.formguidelib.widget.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.f_ms.formguidelib.entity.WidgetEntity;
import cn.f_ms.library.logic.IsRight;

/**
 * Desc: 列表控件描述
 *
 * @author f_ms
 * @time 18-3-9
 */

public class List0ShowEntity {

    @SerializedName("min_item_num")
    public Integer minItemNum;

    @SerializedName("max_item_num")
    public Integer maxItemNum;

    @SerializedName("default_item_num")
    public Integer defaultItemNum;

    @SerializedName("item_elements")
    public List<WidgetEntity> itemElements;

    public static IsRight checkRight(List0ShowEntity descBean) {

        if (descBean == null) {
            return IsRight.no("descBean can't be null");
        }

        if (descBean.minItemNum == null) {
            descBean.minItemNum = 0;
        }
        if (descBean.minItemNum < 0) {
            return IsRight.no("descBean's minItemNum can't less 0, but found '" + descBean.minItemNum + "'");
        }

        if (descBean.maxItemNum == null) {
            descBean.maxItemNum = Integer.MAX_VALUE;
        }
        if (descBean.maxItemNum < descBean.minItemNum) {
            return IsRight.no("descBean's maxItemNum can't less minItemNum but found '" + descBean.maxItemNum + " < " + descBean.minItemNum + "'");
        }

        if (descBean.defaultItemNum == null
                || descBean.defaultItemNum < 0) {
            descBean.defaultItemNum = 0;
        }

        if (descBean.defaultItemNum > descBean.maxItemNum) {
            descBean.defaultItemNum = descBean.maxItemNum;
        }

        if (descBean.itemElements == null) {
            return IsRight.no("descBean's itemElements can't be null");
        }

        int nullIndex = descBean.itemElements.indexOf(null);
        if (nullIndex >= 0) {
            return IsRight.no("descBean's itemElements can't contain null value, but found on '" + nullIndex + "'");
        }

        for (int i = 0; i < descBean.itemElements.size(); i++) {
            IsRight isRight = WidgetEntity.checkIsRight(descBean.itemElements.get(i));
            if (!isRight.isRight()) {
                return IsRight.no("descBean's itemElements's element found error on '" + i + "' :" + isRight.errorMessage());
            }
        }

        return IsRight.yes();
    }

}
