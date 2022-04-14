package com.eyepax.newsapp.base;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity {

    protected B dataBinding;

    @LayoutRes
    protected abstract int layoutRes();

    protected void bindView() {
        dataBinding = DataBindingUtil.setContentView(this, layoutRes());
    }
}
