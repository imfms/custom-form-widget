package cn.f_ms.android.form.custom.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.f_ms.formguidelib.CustomFormWidgetContainerHolder;
import cn.f_ms.formguidelib.entity.ResultEntity;
import cn.f_ms.formguidelib.entity.WidgetEntity;
import cn.f_ms.formguidelib.widget.CommonShowFormMatcher;
import cn.f_ms.formguidelib.widget.CommonWriteFormMatcher;
import cn.f_ms.library.logic.IsRight;

public class MainActivity extends AppCompatActivity {

    private EditText etFormElement;
    private LinearLayout llFormContainer;
    private TextView tvResultShow;
    private Button btnSubmitEdit;
    private Button btnSubmitShow;
    private Button btnSubmitForm;
    private EditText etFormResult;

    private CustomFormWidgetContainerHolder mFormGuideWriteHolder;

    private Gson mGson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFormElement = findViewById(R.id.et_form_element);
        llFormContainer = findViewById(R.id.ll_form_container);
        tvResultShow = findViewById(R.id.tv_result_show);
        btnSubmitEdit = findViewById(R.id.btn_submit_edit);
        btnSubmitShow = findViewById(R.id.btn_submit_show);
        btnSubmitForm = findViewById(R.id.btn_submit_form);
        etFormResult = findViewById(R.id.et_form_result);

        btnSubmitEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit(true);
            }
        });
        btnSubmitShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit(false);
            }
        });
        btnSubmitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitForm();
            }
        });
    }


    private void onSubmitForm() {
        if (mFormGuideWriteHolder == null) {
            showToast("please generate form first");
            return;
        }

        CustomFormWidgetContainerHolder.Result result = mFormGuideWriteHolder.getResult();
        if (!result.isSuccess) {
            showToast("form verify error:" + result.errorMessage);
            return;
        }

        tvResultShow.setText(
                mGson.toJson(result.result)
        );
    }

    private void onSubmit(boolean isEdit) {

        String formJson = etFormElement.getText().toString();

        if (TextUtils.isEmpty(formJson)) {
            showToast("input form json can't be null");
            return;
        }

        List<WidgetEntity> formEntitys;
        try {
            formEntitys = mGson.fromJson(formJson, new TypeToken<List<WidgetEntity>>() {
            }.getType());
        } catch (Exception e) {
            showToast("json parse error:" + e.getMessage());
            return;
        }

        llFormContainer.removeAllViews();
        tvResultShow.setText(null);

        try {
            mFormGuideWriteHolder = new CustomFormWidgetContainerHolder(this, llFormContainer,
                    isEdit
                            ? new CommonWriteFormMatcher()
                            : new CommonShowFormMatcher()
            );
        } catch (Exception e) {
            showToast("json guide error:" + e.getMessage());
            return;
        }

        llFormContainer.addView(mFormGuideWriteHolder.getContentView());

        IsRight isRight = mFormGuideWriteHolder.checkWidgetDesc(formEntitys);
        if (!isRight.isRight()) {
            showToast("guide check error: " + isRight.errorMessage());
            return;
        } else {
            mFormGuideWriteHolder.fillWidgetDesc(formEntitys);
        }

        String resultJson = etFormResult.getText().toString().trim();
        if (TextUtils.isEmpty(resultJson)) {
            return;
        }

        try {
            List<ResultEntity> resultEntities = new Gson().fromJson(resultJson, new TypeToken<List<ResultEntity>>() {
            }.getType());

            IsRight isResultRight = mFormGuideWriteHolder.checkResultData(resultEntities);
            if (!isResultRight.isRight()) {
                showToast("guide check error: " + isResultRight.errorMessage());
            } else {
                mFormGuideWriteHolder.fillResultData(resultEntities);
            }

        } catch (Exception e) {
            showToast("json form result error: " + e.getMessage());
        }
    }

    private void showToast(String tips) {
        Toast.makeText(this, tips, Toast.LENGTH_SHORT).show();
    }

}
