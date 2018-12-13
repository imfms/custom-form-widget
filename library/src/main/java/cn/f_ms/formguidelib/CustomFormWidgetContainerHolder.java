package cn.f_ms.formguidelib;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.f_ms.formguidelib.widget.Container0Holder;
import cn.f_ms.formguidelib.widget.NoConvertHolder;
import cn.f_ms.library.check.CheckNull;

/**
 * 自定义表单 - 入口holder
 * @author f_ms
 */
public class CustomFormWidgetContainerHolder extends Container0Holder {

    public CustomFormWidgetContainerHolder(Activity activity, ViewGroup parent, FormMatcher formMatcher) {
        this(
                activity,
                parent,
                new ViewStructureBuilder() {
                    @Override
                    public ViewStructureWrapper create(final android.content.Context context, final ViewGroup parent) {
                        return new ViewStructureWrapper() {

                            private ViewGroup rootView;
                            private TextView tvTitle;
                            private ViewGroup contentContainer;

                            {
                                rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.holder_form_single_widget_container, parent, false);

                                tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
                                contentContainer = (FrameLayout) rootView.findViewById(R.id.fl_item_container);
                            }

                            @Override
                            public View rootView() {
                                return rootView;
                            }

                            @Override
                            public TextView titleView() {
                                return tvTitle;
                            }

                            @Override
                            public ViewGroup contentContainer() {
                                return contentContainer;
                            }
                        };
                    }
                },
                formMatcher
        );
    }

    public CustomFormWidgetContainerHolder(Activity activity, ViewGroup parent, ViewStructureBuilder viewStructureBuilder, FormMatcher formMatcher) {
        super(activity, parent, makeContext(formMatcher, viewStructureBuilder));
    }

    private static FormContext makeContext(final FormMatcher formMatcher, ViewStructureBuilder viewStructureBuilder) {
        CheckNull.ifNullThrowArgException(formMatcher, "formMatcher can't be null");
        CheckNull.ifNullThrowArgException(viewStructureBuilder, "viewStructureBuilder can't be null");

        return new FormContext(
                viewStructureBuilder,
                new FormMatcher() {

                    @Override
                    public FormHandler getConvertHolder(String type, int version, Activity activity, ViewGroup parent, FormContext formContext) {
                        FormHandler convertHolder = formMatcher.getConvertHolder(type, version, activity, parent, formContext);
                        if (convertHolder == null) {
                            convertHolder = new NoConvertHolder(activity, parent, formContext);
                        }
                        return convertHolder;
                    }
                }
        );
    }
}
