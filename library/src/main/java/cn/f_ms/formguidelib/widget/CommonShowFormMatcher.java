package cn.f_ms.formguidelib.widget;

import android.app.Activity;
import android.text.TextUtils;
import android.view.ViewGroup;

import cn.f_ms.formguidelib.FormContext;
import cn.f_ms.formguidelib.FormHandler;
import cn.f_ms.formguidelib.widget.show.DateSelect0ShowHandler;
import cn.f_ms.formguidelib.widget.show.InputText0ShowHandler;
import cn.f_ms.formguidelib.widget.show.List0ShowHandler;
import cn.f_ms.formguidelib.widget.show.MultiChoice0ShowHandler;
import cn.f_ms.formguidelib.widget.show.SingleChoice0Handler;

public class CommonShowFormMatcher implements FormHandler.FormMatcher {

    @Override
    public FormHandler getConvertHolder(String type, int version, Activity activity, ViewGroup parent, FormContext formContext) {

        for (FormGuideTable formGuideTable : FormGuideTable.values()) {
            if (formGuideTable.isAccept(type, version)) {
                return formGuideTable.getConvertHolder(activity, parent, formContext);
            }
        }

        return null;
    }

    private enum FormGuideTable {

        DATE_SELECT0("date_select", 0) {
            @Override
            public FormHandler getConvertHolder(Activity activity, ViewGroup parent, FormContext formContext) {
                return new DateSelect0ShowHandler(activity, parent, formContext);
            }
        },
        INPUT_TEXT0("input_text", 0) {
            @Override
            public FormHandler getConvertHolder(Activity activity, ViewGroup parent, FormContext formContext) {
                return new InputText0ShowHandler(activity, parent, formContext);
            }
        },
        MULTI_CHOICE0("multi_choice", 0) {
            @Override
            public FormHandler getConvertHolder(Activity activity, ViewGroup parent, FormContext formContext) {
                return new MultiChoice0ShowHandler(activity, parent, formContext);
            }
        },
        SINGLE_CHOICE0("single_choice", 0) {
            @Override
            public FormHandler getConvertHolder(Activity activity, ViewGroup parent, FormContext formContext) {
                return new SingleChoice0Handler(activity, parent, formContext);
            }
        },
        CONTAINER0("container", 0) {
            @Override
            public FormHandler getConvertHolder(Activity activity, ViewGroup parent, FormContext formContext) {
                return new Container0Holder(activity, parent, formContext);
            }
        },
        COMBINE_NAME_WITH_WIDGET("combine_name_with_widget", 0) {
            @Override
            public FormHandler getConvertHolder(Activity activity, ViewGroup parent, FormContext formContext) {
                return new CombineNameWithWidget0Holder(activity, parent, formContext);
            }
        },
        LIST0("list", 0) {
            @Override
            public FormHandler getConvertHolder(Activity activity, ViewGroup parent, FormContext formContext) {
                return new List0ShowHandler(activity, parent, formContext);
            }
        };

        private final String type;
        private final int version;

        FormGuideTable(String type, int version) {
            this.type = type;
            this.version = version;
        }


        public boolean isAccept(String type, int version) {
            return TextUtils.equals(this.type, type)
                    && this.version == version;
        }

        public abstract FormHandler getConvertHolder(Activity activity, ViewGroup parent, FormContext formContext);
    }
}