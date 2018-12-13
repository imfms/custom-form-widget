package cn.f_ms.formguidelib;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 视图建造者
 * @author f_ms
 */
public interface ViewStructureBuilder {

    /**
     * 建造视图包装器
     */
    ViewStructureWrapper create(Context context, ViewGroup parent);

    /**
     * 视图结构包装器
     */
    interface ViewStructureWrapper {
        /**
         * 根视图
         */
        View rootView();

        /**
         * 标题视图
         */
        TextView titleView();

        /**
         * 内容容器视图
         *
         * @return
         */
        ViewGroup contentContainer();
    }
}