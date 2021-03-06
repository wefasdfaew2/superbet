package fr.ele.core.search.ui;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.BeanUtils;

import fr.ele.core.search.Search;
import fr.ele.core.search.SearchOperator;
import fr.ele.core.search.criteria.date.DateOperator;
import fr.ele.core.search.criteria.date.DateValueCriteria;
import fr.ele.core.search.criteria.enums.EnumOperator;
import fr.ele.core.search.criteria.enums.EnumValueCriteria;
import fr.ele.core.search.criteria.number.NumberOperator;
import fr.ele.core.search.criteria.number.NumberValueCriteria;
import fr.ele.core.search.criteria.string.StringOperator;
import fr.ele.core.search.criteria.string.StringValueCriteria;

public class SearchToUi {
    public static <T extends Search> UiForm transform(Class<T> clazz) {
        return transform(null, null, clazz);
    }

    public static <T extends Search> UiForm transform(String pathPrefix,
            String formTitle, Class<T> clazz) {
        List<UiCriteria> criterias = new LinkedList<UiCriteria>();
        List<UiForm> subForms = new LinkedList<UiForm>();
        PropertyDescriptor[] properties = BeanUtils
                .getPropertyDescriptors(clazz);
        for (PropertyDescriptor property : properties) {
            String name = property.getName();
            if (!name.equals("class")) {
                String path = createPath(pathPrefix, name);
                String title = WordUtils.capitalize(name);
                if (StringValueCriteria.class.isAssignableFrom(property
                        .getPropertyType())) {
                    UiCriteria criteria = createCriteria(path, title,
                            ValueType.STRING, StringOperator.values());
                    criterias.add(criteria);
                } else if (NumberValueCriteria.class.isAssignableFrom(property
                        .getPropertyType())) {
                    UiCriteria criteria = createCriteria(path, title,
                            ValueType.NUMBER, NumberOperator.values());
                    criterias.add(criteria);
                } else if (DateValueCriteria.class.isAssignableFrom(property
                        .getPropertyType())) {
                    UiCriteria criteria = createCriteria(path, title,
                            ValueType.DATE, DateOperator.values());
                    criteria.setHtmlClass("datepicker");
                    criterias.add(criteria);
                } else if (EnumValueCriteria.class.isAssignableFrom(property
                        .getPropertyType())) {
                    UiCriteria criteria = createCriteria(path, title,
                            ValueType.ENUM, EnumOperator.values());
                    try {
                        Field field = clazz.getDeclaredField(name);
                        ParameterizedType type = (ParameterizedType) field
                                .getGenericType();
                        Class<?> genericClass = (Class<?>) type
                                .getActualTypeArguments()[0];
                        if (genericClass.isEnum()) {
                            Class<Enum<?>> enumClass = (Class<Enum<?>>) genericClass;
                            Enum<?>[] constants = enumClass.getEnumConstants();
                            criteria.setValues(Arrays.asList(constants));
                        }
                    } catch (Throwable e) {

                    }
                    criterias.add(criteria);
                } else if (Search.class.isAssignableFrom(property
                        .getPropertyType())) {
                    Class<? extends Search> sub = (Class<? extends Search>) property
                            .getPropertyType();
                    subForms.add(transform(path, WordUtils.capitalize(name),
                            sub));
                }
            }
        }
        UiForm form = new UiForm();
        form.setFields(criterias);
        form.setTitle(formTitle);
        form.setSubForms(subForms);
        return form;
    }

    private static String createPath(String pathPrefix, String name) {
        if (StringUtils.isNotEmpty(pathPrefix)) {
            StringBuilder sb = new StringBuilder(pathPrefix.length()
                    + name.length() + 1);
            return sb.append(pathPrefix).append('.').append(name).toString();
        }
        return name;
    }

    private static UiCriteria createCriteria(String path, String title,
            ValueType type, SearchOperator... operators) {
        UiCriteria criteria = new UiCriteria();
        criteria.setName(path);
        criteria.setTitle(title);
        criteria.setType(type);
        criteria.addOperators(operators);
        return criteria;
    }
}
