package com.hy.company.entity;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.eclipse.xtend.lib.annotations.Data;

@Data
public class ToJsonParam {

    private PsiClass selectedClass;

    private Project project;

    public PsiClass getSelectedClass() {
        return this.selectedClass;
    }

    public void setSelectedClass(PsiClass selectedClass) {
        this.selectedClass = selectedClass;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
