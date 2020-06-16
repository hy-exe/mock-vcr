package com.hy.company.ui;

import com.hy.company.entity.ToJsonParam;
import com.hy.company.util.ToJsonUtil;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.util.Map;

/**
 * @author 003664
 */
public class ToJsonSwing {

    private ToJsonParam toJsonParam;

    public ToJsonSwing(ToJsonParam toJsonParam) {
        this.toJsonParam = toJsonParam;
    }

    /**
     * 文本框
     */
    private JTextArea textArea = new JTextArea(20, 60);
    /**
     * 路径字段
     */
    private JTextField pathField = new JTextField(40);

    public JPanel initCenter() {
        JPanel center = new JPanel();
        // 设置自动换行
        textArea.setLineWrap(true);

        // 添加到内容面板
        center.add(textArea);

        Map<String, Object> outputMap = ToJsonUtil.generateMap(toJsonParam.getSelectedClass(), toJsonParam.getProject());
        String jsonStr = ToJsonUtil.toFormatJsonStr(outputMap);
        textArea.setText(jsonStr);

        refreshPath();

        center.add(pathField);

        return center;
    }

    public JPanel initSouth() {

        JPanel south = new JPanel();
        //定义表单的提交按钮，放置到IDEA会话框的底部位置

        JButton submit = new JButton("生成文件");
        submit.setHorizontalAlignment(SwingConstants.CENTER); //水平居中
        submit.setVerticalAlignment(SwingConstants.CENTER); //垂直居中
        south.add(submit);
        //按钮事件绑定
        submit.addActionListener(e -> {

        });

        /**
         * 路径选择按钮
         */
        JButton pathChooseButton = new JButton("选择路径");
        pathChooseButton.setHorizontalAlignment(SwingConstants.CENTER); //水平居中
        pathChooseButton.setVerticalAlignment(SwingConstants.CENTER); //垂直居中
        south.add(pathChooseButton);


        //选择路径
        pathChooseButton.addActionListener(e -> {
            //将当前选中的model设置为基础路径
            VirtualFile path = LocalFileSystem.getInstance().findFileByPath(toJsonParam.getProject().getBasePath());
            VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), toJsonParam.getProject(), path);
            if (virtualFile != null) {
                pathField.setText(virtualFile.getPath());
            }
        });

        return south;
    }

    /**
     * 获取基本路径
     *
     * @return 基本路径
     */
    private String getBasePath() {
        String baseDir = toJsonParam.getProject().getBasePath();
        return baseDir;
    }

    /**
     * 刷新目录
     */
    private void refreshPath() {

        // 获取基本路径
        String path = getBasePath();
        // 兼容Linux路径
        path = path.replace("\\", "/");
        pathField.setText(path);
    }
}