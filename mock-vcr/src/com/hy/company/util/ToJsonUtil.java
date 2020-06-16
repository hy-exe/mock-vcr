package com.hy.company.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

import java.util.*;

public class ToJsonUtil {

    public static Map<String, Object> generateMap(PsiClass psiClass, Project project) {
        Map<String, Object> outputMap = new LinkedHashMap();
        PsiField[] psiFields = psiClass.getFields();

        for (int idx = 0; idx < psiFields.length; ++idx) {
            PsiField psiField = psiFields[idx];
            outputMap.put(psiField.getName(), getObjectForField(psiField, project));
        }

        return outputMap;
    }

    private static Object getObjectForField(PsiField psiField, Project project) {
        PsiType type = psiField.getType();
        if (type instanceof PsiPrimitiveType) {
            if (type.equals(PsiType.INT)) {
                return 0;
            } else if (type.equals(PsiType.BOOLEAN)) {
                return Boolean.TRUE;
            } else if (type.equals(PsiType.BYTE)) {
                return Byte.valueOf("1");
            } else if (type.equals(PsiType.CHAR)) {
                return '-';
            } else if (type.equals(PsiType.DOUBLE)) {
                return 0.0D;
            } else if (type.equals(PsiType.FLOAT)) {
                return 0.0F;
            } else if (type.equals(PsiType.LONG)) {
                return 0L;
            } else {
                return type.equals(PsiType.SHORT) ? Short.valueOf("0") : type.getPresentableText();
            }
        } else {
            String typeName = type.getPresentableText();
            if (!typeName.equals("Integer") && !typeName.equals("Long")) {
                if (!typeName.equals("Double") && !typeName.equals("Float")) {
                    if (typeName.equals("Boolean")) {
                        return Boolean.TRUE;
                    } else if (typeName.equals("Byte")) {
                        return Byte.valueOf("1");
                    } else if (typeName.equals("String")) {
                        return "";
                    } else if (typeName.equals("Date")) {
                        return null;
                    } else if (typeName.startsWith("List")) {
                        return handleList(type, project, psiField.getContainingClass());
                    } else {
                        PsiClass fieldClass = detectCorrectClassByName(typeName, psiField.getContainingClass(), project);
                        return fieldClass != null ? generateMap(fieldClass, project) : typeName;
                    }
                } else {
                    return 0.0F;
                }
            } else {
                return 0;
            }
        }
    }

    private static Object handleList(PsiType psiType, Project project, PsiClass containingClass) {
        List<Object> list = new ArrayList();
        PsiClassType classType = (PsiClassType) psiType;
        PsiType[] subTypes = classType.getParameters();
        if (subTypes.length > 0) {
            PsiType subType = subTypes[0];
            String subTypeName = subType.getPresentableText();
            if (subTypeName.startsWith("List")) {
                list.add(handleList(subType, project, containingClass));
            } else {
                PsiClass targetClass = detectCorrectClassByName(subTypeName, containingClass, project);
                if (targetClass != null) {
                    list.add(generateMap(targetClass, project));
                } else if (subTypeName.equals("String")) {
                    list.add("");
                } else if (subTypeName.equals("Date")) {
                    list.add(null);
                } else {
                    list.add(subTypeName);
                }
            }
        }

        return list;
    }

    public static PsiClass detectCorrectClassByName(String className, PsiClass containingClass, Project project) {
        PsiClass[] classes = PsiShortNamesCache.getInstance(project).getClassesByName(className, GlobalSearchScope.projectScope(project));
        if (classes.length == 0) {
            return null;
        } else if (classes.length == 1) {
            return classes[0];
        } else {
            PsiJavaFile javaFile = (PsiJavaFile) containingClass.getContainingFile();
            PsiImportList importList = javaFile.getImportList();
            PsiImportStatement[] statements = importList.getImportStatements();
            Set<String> importedPackageSet = new HashSet();

            int idx;
            for (idx = 0; idx < statements.length; ++idx) {
                importedPackageSet.add(statements[idx].getQualifiedName());
            }

            for (idx = 0; idx < classes.length; ++idx) {
                PsiClass targetClass = classes[idx];
                PsiJavaFile targetClassContainingFile = (PsiJavaFile) targetClass.getContainingFile();
                String packageName = targetClassContainingFile.getPackageName();
                if (importedPackageSet.contains(packageName + "." + targetClass.getName())) {
                    return targetClass;
                }
            }

            return null;
        }
    }

    public static String toFormatJsonStr(Object object) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return gson.toJson(object);
    }

}