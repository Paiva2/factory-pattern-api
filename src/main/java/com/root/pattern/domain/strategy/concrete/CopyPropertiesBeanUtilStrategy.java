package com.root.pattern.domain.strategy.concrete;

import com.root.pattern.domain.strategy.CopyPropertiesStrategy;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class CopyPropertiesBeanUtilStrategy implements CopyPropertiesStrategy {
    @Override
    public void copyNonNullProps(Object source, Object target) {
        BeanWrapper beanSource = new BeanWrapperImpl(source);
        BeanWrapper beanTarget = new BeanWrapperImpl(target);

        List<String> nonCopyableProps = Arrays.asList("class", "id", "createdAt", "role");

        List<PropertyDescriptor> fieldsToUpdate = Arrays.asList(beanSource.getPropertyDescriptors());

        fieldsToUpdate.forEach(field -> {
            String fieldName = field.getName();
            Object fieldValue = beanSource.getPropertyValue(fieldName);

            if (nonCopyableProps.contains(fieldName) || Objects.isNull(fieldValue)) return;

            beanTarget.setPropertyValue(fieldName, fieldValue);
        });
    }
}
