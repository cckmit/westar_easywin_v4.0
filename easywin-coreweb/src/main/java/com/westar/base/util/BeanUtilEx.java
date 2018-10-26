package com.westar.base.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

/**
 * JAVA BEAN操作工具类
 */
public class BeanUtilEx {

	/**
	 * 复制不为Null的Bean属性
	 * @param source
	 * @param target
	 */
	public static void copyIgnoreNulls(Object source, Object target) {
		BeanUtilEx.copyIgnoreNulls(source, target, null, null);
	}

	/**
	 * 复制不为Null的Bean属性
	 * @param source
	 * @param target
	 * @param editable
	 * @param ignoreProperties
	 */
	@SuppressWarnings({"unchecked" })
	public static void copyIgnoreNulls(Object source, Object target, Class editable, String ignoreProperties[]) {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");
		Class actualEditable = target.getClass();
		if (editable != null) {
			if (!editable.isInstance(target)){
				throw new IllegalArgumentException((new StringBuilder()).append("Target class [").append(target.getClass().getName()).append("] not assignable to Editable class [").append(editable.getName()).append("]").toString());
			}
			actualEditable = editable;
		}
		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
		List ignoreList = ignoreProperties == null ? null : Arrays.asList(ignoreProperties);
		PropertyDescriptor[] arr$ = targetPds;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++) {
			PropertyDescriptor targetPd = arr$[i$];
			if (targetPd.getWriteMethod() == null || ignoreProperties != null && ignoreList.contains(targetPd.getName())){
				continue;
			}
			PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
			if (sourcePd == null || sourcePd.getReadMethod() == null){
				continue;
			}
			try {
				Method readMethod = sourcePd.getReadMethod();
				if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())){
					readMethod.setAccessible(true);
				}
				Object value = readMethod.invoke(source, new Object[0]);
				if (value != null) {
					Method writeMethod = targetPd.getWriteMethod();
					if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())){
						writeMethod.setAccessible(true);
					}
					writeMethod.invoke(target, new Object[] { value });
				}
			} catch (Throwable ex) {
				throw new FatalBeanException("Could not copy properties from source to target", ex);
			}
		}

	}
}
