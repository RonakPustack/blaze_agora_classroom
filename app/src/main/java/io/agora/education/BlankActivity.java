package io.agora.education;


import android.util.Base64;

import com.tencent.bugly.crashreport.CrashReport;

import io.agora.base.PreferenceManager;
import io.agora.base.ToastManager;
import io.agora.base.network.RetrofitManager;
import io.agora.education.api.util.CryptoUtil;
import io.agora.education.base.BaseActivity;
import kotlin.text.Charsets;

public class BlankActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_blank;
    }

    @Override
    protected void initData() {
        EduApplication eduApplication = new EduApplication();
        eduApplication.onCreate();
    }

    @Override
    protected void initView() {

    }

}