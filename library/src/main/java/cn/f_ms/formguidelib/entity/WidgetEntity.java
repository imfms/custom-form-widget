package cn.f_ms.formguidelib.entity;

import com.google.gson.annotations.SerializedName;

import cn.f_ms.library.logic.IsRight;

public class WidgetEntity {
    @SerializedName("type")
    public final String type;
    @SerializedName("version")
    public final Integer version;
    @SerializedName("title")
    public final String title;
    @SerializedName("form_element_id")
    public final Integer elementId;
    @SerializedName("meta_data")
    public final String show_desc_data;

    public WidgetEntity(String type, Integer version, String show_desc_data, Integer elementId, String title) {
        this.type = type;
        this.title = title;
        this.version = version;
        this.show_desc_data = show_desc_data;
        this.elementId = elementId;
    }

    public static IsRight checkIsRight(WidgetEntity widgetEntity) {
        if (widgetEntity.type == null) {
            return IsRight.no("widgetEntity's type can't be null");
        }

        if (widgetEntity.version == null) {
            return IsRight.no("widgetEntity's version can't be null");
        }

        if (widgetEntity.elementId == null) {
            return IsRight.no("widgetEntity's elementId can't be null");
        }

        return IsRight.yes();
    }
}
