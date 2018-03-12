package cn.f_ms.formguidelib.widget;

import android.app.Activity;
import android.text.TextUtils;
import android.view.ViewGroup;

import cn.f_ms.formguidelib.Context;
import cn.f_ms.formguidelib.FormHandler;
import cn.f_ms.formguidelib.widget.write.DateSelect0WriteHandler;
import cn.f_ms.formguidelib.widget.write.InputText0WriteHandler;
import cn.f_ms.formguidelib.widget.write.List0WriteHandler;
import cn.f_ms.formguidelib.widget.write.MultiChoice0WriteHandler;
import cn.f_ms.formguidelib.widget.write.SingleChoice0WriteHandler;

public class CommonWriteFormMatcher implements FormHandler.FormMatcher {

    @Override
    public FormHandler getConvertHolder(String type, int version, Activity activity, ViewGroup parent, Context context) {

        for (FormGuideTable formGuideTable : FormGuideTable.values()) {
            if (formGuideTable.isAccept(type, version)) {
                return formGuideTable.getConvertHolder(activity, parent, context);
            }
        }

        return null;
    }

    private enum FormGuideTable {

        DATE_SELECT0("date_select", 0) {
            @Override
            public FormHandler getConvertHolder(Activity activity, ViewGroup parent, Context context) {
                return new DateSelect0WriteHandler(activity, parent, context);
            }
        },
        INPUT_TEXT0("input_text", 0) {
            @Override
            public FormHandler getConvertHolder(Activity activity, ViewGroup parent, Context context) {
                return new InputText0WriteHandler(activity, parent, context);
            }
        },
        MULTI_CHOICE0("multi_choice", 0) {
            @Override
            public FormHandler getConvertHolder(Activity activity, ViewGroup parent, Context context) {
                return new MultiChoice0WriteHandler(activity, parent, context);
            }
        },
        SINGLE_CHOICE0("single_choice", 0) {
            @Override
            public FormHandler getConvertHolder(Activity activity, ViewGroup parent, Context context) {
                return new SingleChoice0WriteHandler(activity, parent, context);
            }
        },
        LIST0("list", 0) {
            @Override
            public FormHandler getConvertHolder(Activity activity, ViewGroup parent, Context context) {
                return new List0WriteHandler(activity, parent, context);
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

        public abstract FormHandler getConvertHolder(Activity activity, ViewGroup parent, Context context);
    }
}