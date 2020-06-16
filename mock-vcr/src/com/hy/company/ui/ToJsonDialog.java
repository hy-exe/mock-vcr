package com.hy.company.ui;

import com.hy.company.entity.ToJsonParam;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ToJsonDialog extends DialogWrapper {

    private ToJsonSwing toJsonSwing;

    public ToJsonDialog(@Nullable ToJsonParam toJsonParam) {
        super(toJsonParam.getProject());
        toJsonSwing = new ToJsonSwing(toJsonParam);
        setTitle("mock-vcr~~"); // 设置会话框标题
        init(); //触发一下init方法，否则swing样式将无法展示在会话框
    }

    // 重写下面的方法，返回一个自定义的swing样式，该样式会展示在会话框的最上方的位置
    @Override
    protected JComponent createNorthPanel() {
        return null;
    }

    // 重写下面的方法，返回一个自定义的swing样式，该样式会展示在会话框的最下方的位置
    @Override
    protected JComponent createSouthPanel() {
        return toJsonSwing.initSouth();
    }

    // 重写下面的方法，返回一个自定义的swing样式，该样式会展示在会话框的中央位置
    @Override
    protected JComponent createCenterPanel() {
        return toJsonSwing.initCenter();
    }

    public static void main(String[] args) {

        System.exit(0);
    }
}
