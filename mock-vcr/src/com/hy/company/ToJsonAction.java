package com.hy.company;

import com.hy.company.entity.ToJsonParam;
import com.hy.company.ui.SelectSavePath;
import com.hy.company.ui.ToJsonDialog;
import com.hy.company.util.ToJsonUtil;
import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;

public class ToJsonAction extends AnAction {

    public static final NotificationGroup GROUP_DISPLAY_ID_INFO;

    static {
        GROUP_DISPLAY_ID_INFO = new NotificationGroup("BMPOJOtoJSON.Group", NotificationDisplayType.STICKY_BALLOON, true);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {

        Editor editor = event.getDataContext().getData(CommonDataKeys.EDITOR);
        //获取当前操作的类文件
        PsiFile psiFile = event.getDataContext().getData(CommonDataKeys.PSI_FILE);
        //获取当前在操作的工程上下文
        Project project = editor.getProject();
        PsiElement referenceAt = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiClass hostClass = (PsiClass) PsiTreeUtil.getContextOfType(referenceAt, new Class[]{PsiClass.class});
        PsiClass selectedClass;
        if (hostClass.getName().equals(referenceAt.getText())) {
            selectedClass = hostClass;
        } else {
            selectedClass = ToJsonUtil.detectCorrectClassByName(referenceAt.getText(), hostClass, project);
        }

        if (selectedClass == null) {
            Notification notification = GROUP_DISPLAY_ID_INFO.createNotification("Selection is not a POJO.", NotificationType.ERROR);
            Notifications.Bus.notify(notification, project);
            return;
        }

        ToJsonParam toJsonParam = new ToJsonParam();
        toJsonParam.setSelectedClass(selectedClass);
        toJsonParam.setProject(project);


        ToJsonDialog toJsonDialog = new ToJsonDialog(toJsonParam);
        toJsonDialog.setResizable(true); //是否允许用户通过拖拽的方式扩大或缩小你的表单框，我这里定义为true，表示允许
        toJsonDialog.show();

//        new SelectSavePath(project).open();

    }


}