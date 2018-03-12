package cn.f_ms.formguidelib.entity;

import com.google.gson.annotations.SerializedName;

import cn.f_ms.library.logic.IsRight;

public final class ResultEntity {

    @SerializedName("form_element_id")
    public final Integer elementId;
    @SerializedName("result")
    public final String result;

    public ResultEntity(Integer elementId, String result) {
        this.elementId = elementId;
        this.result = result;
    }

    public static IsRight checkRight(ResultEntity resultEntity) {
        if (resultEntity == null) {
            return IsRight.no("resultEntity can't be null");
        }

        if (resultEntity.elementId == null) {
            return IsRight.no("resultEntity's elementId can't be null");
        }

        return IsRight.yes();
    }

}
