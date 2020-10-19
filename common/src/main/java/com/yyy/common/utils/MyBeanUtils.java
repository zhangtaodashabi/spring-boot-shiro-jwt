package com.yyy.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Author Bazinga
 * @Description
 * @Date Create in 2019-03-06
 * @Modified By:
 */
@Slf4j
public class MyBeanUtils<E> extends BeanUtils {

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (ToolUtil.isEmpty(srcValue)) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 解决org.springframework.beans.BeanUtils的copyProperties方法
     * 复制时候,弊端就是会将null也复制过来
     * 当想要 复制后的类某些值不会被src 中的null给覆盖时候, 考虑此方法
     *
     * @param src
     * @param src
     */
    public static void copyPropertiesNotNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    /**
     * 将集合中的元素类型转成 自定义类型
     *
     * @param sourceList
     * @param eClass
     * @param <E>
     * @return
     */
    public static <E> List<E>
    copyListProperties(List sourceList, Class<E> eClass) {
        List<E> targetList = new ArrayList<>();
        sourceList.forEach(source ->
                targetList.add(copyProperties(source, eClass))
        );
        return targetList;
    }

    /**
     * 将数组中的元素类型转成 自定义类型
     *
     * @param sources
     * @param eClass
     * @param <E>
     * @return
     */
    public static <E> List<E> copyListProperties(Object[] sources, Class<E> eClass) {
        return copyListProperties(Arrays.asList(sources), eClass);
    }

    /**
     * 将数组中的元素类型转成 自定义类型
     *
     * @param sources
     * @param eClass
     * @param <E>
     * @return
     */
    public static <E> E copyProperties(Object sources, Class<E> eClass) {
        if (sources == null) {
            return null;
        }
        E target = getE(eClass);
        copyProperties(sources, target);
        return target;
    }

    /**
     * 将数组中的元素类型转成 自定义类型
     *
     * @param sources
     * @param eClass
     * @param ignoreProperties 忽略属性
     * @param <E>
     * @return
     */
    public static <E> E copyProperties(Object sources, Class<E> eClass, String... ignoreProperties) {
        if (sources == null) {
            return null;
        }
        E target = getE(eClass);
        copyProperties(sources, target, ignoreProperties);
        return target;
    }

    /**
     * 对比数据，以第一个为标准，有值对比没有跳过
     *
     * @param o1
     * @param o2
     * @return
     */
    public static <E> boolean equalsWithoutNull(E o1, E o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null) {
            return false;
        }
        Field[] fields = o1.getClass().getDeclaredFields();
        // 检查是否
        return Arrays.stream(fields).filter(e -> !ToolUtil.equals(e.getName(), "serialVersionUID")).allMatch(e -> {
            try {
                ReflectionUtils.makeAccessible(e);
                if (ToolUtil.isEmpty(e.get(o1)) || Objects.equals(e.get(o1), e.get(o2))) {
                    return Boolean.TRUE;
                }
            } catch (IllegalAccessException e1) {
                log.info("context", e);
            }
            return Boolean.FALSE;
        });
    }


    /**
     * 初始化一个指定类型的对象
     *
     * @param eClass
     * @param <E>
     * @return
     */
    private static <E> E getE(Class<E> eClass) {
        E e = null;
        try {
            e = eClass.newInstance();
        } catch (InstantiationException e1) {
            log.error("始化指定类型的对象，原因为{}", e1.getMessage());
        } catch (IllegalAccessException e2) {
            log.error("始化指定类型的对象，原因为{}", e2.getMessage());
        }
        return e;
    }

    /**
     * 获取某个对象里 满足特定条件的属性名
     *
     * @param obj
     * @param predicate
     * @return
     */
    public static List<String> getFilterFieldNames(Object obj, Predicate<Object> predicate) {
        Field[] fields = obj.getClass().getDeclaredFields();
        return Arrays.stream(fields).filter(e -> {
            try {
                ReflectionUtils.makeAccessible(e);
                if (predicate.test(e.get(obj))) {
                    return Boolean.TRUE;
                }
            } catch (IllegalAccessException e1) {
	            log.error("获取某个对象里 满足特定条件的属性名失败，原因为{}",e1.getMessage());
            }
            return Boolean.FALSE;
        }).map(Field::getName).collect(Collectors.toList());
    }

    /**
     * @author: liulinchuan
     * @description: 拷贝集合
     * @date: 2020/5/28 14:23
     * @param source: 源数据集合
     * @param target: 目标数据集合
     * @return: java.util.List<T>
     */
    public static <S, T> List<T> copyListProperties(List<S> source, Supplier<T> target) {
        List<T> targetList = null;
        if (source != null && !source.isEmpty() && target != null) {
            targetList = new ArrayList<>(source.size());
            for (S s : source) {
                T t = target.get();
                BeanUtils.copyProperties(s, t);
                targetList.add(t);
            }
        }
        return targetList;
    }

}
