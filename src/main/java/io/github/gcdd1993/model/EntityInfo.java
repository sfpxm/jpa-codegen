package io.github.gcdd1993.model;

import io.github.gcdd1993.util.ReflectUtils;
import lombok.Getter;

import javax.persistence.Id;
import javax.persistence.IdClass;
import java.lang.reflect.Field;

/**
 * 实体信息
 *
 * @author gaochen
 * Created on 2019/6/18.
 */
@Getter
public class EntityInfo {

    private EntityInfo() {
    }

    /**
     * 实体全类名
     */
    private String fullName;

    /**
     * 实体类名
     */
    private String simpleName;

    /**
     * 主键全类名
     */
    private Class<?> idClass;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 通过反射获取实体类属性
     *
     * @param clazz 实体类
     * @return 实体信息
     */
    public static EntityInfo buildFromEntity(Class<?> clazz) {
        EntityInfo entityInfo = new EntityInfo();
        // idClass
        // 尝试从类上获取复合主键
        IdClass[] annotations = clazz.getAnnotationsByType(IdClass.class);
        if (annotations != null && annotations.length > 0) {
            entityInfo.idClass = annotations[0].value();
        } else {
            // 从字段获取
            Field field = ReflectUtils.getFieldByAnnotation(clazz, Id.class)
                    .orElse(ReflectUtils.getFieldByAnnotation(clazz, org.springframework.data.annotation.Id.class).orElse(null));
            if (field == null) {
                return null;
            }
            entityInfo.idClass = field.getType();
        }

        entityInfo.fullName = clazz.getName();
        entityInfo.simpleName = clazz.getSimpleName();
        entityInfo.packageName = clazz.getPackage().getName();

        return entityInfo;

    }


}