package cn.f_ms.formguidelib;

import android.app.Activity;
import android.view.ViewGroup;

import cn.f_ms.library.check.CheckNull;

/**
 * Desc: 自定义表单控件上下文环境
 *
 * @author f_ms
 * @date 18-3-10
 */

public final class Context {

    /**
     * 自定义表单描述处理器
     */
    private final FormHandler.FormMatcher formMatcher;
    /**
     * 视图结构构建者
     */
    private final ViewStructureBuilder viewStructureBuilder;

    public Context(ViewStructureBuilder viewStructureBuilder, FormHandler.FormMatcher formMatcher) {
        this.viewStructureBuilder = CheckNull.ifNullThrowArgException(viewStructureBuilder, "viewStructureBuilder can't be null");
        this.formMatcher = CheckNull.ifNullThrowArgException(formMatcher, "formMatcher can't be null");
    }

    public ViewStructureBuilder getViewStructureBuilder() {
        return viewStructureBuilder;
    }

    public FormHandler getConvertHolder(String type, int version, Activity activity, ViewGroup parent) {
        return formMatcher.getConvertHolder(type, version, activity, parent, this);
    }

}
