package com.revature.jack.ObjectMapper;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectGetterAndSetter {
	public void invokeSetter(Object obj, String propertyName, Object variableValue) {
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(propertyName, obj.getClass());
			Method setter = pd.getWriteMethod();
			try {
				setter.invoke(obj, variableValue);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}

	}

	public void invokeGetter(Object obj, String variableName) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(variableName, obj.getClass());
			Method getter = pd.getReadMethod();
			Object f = getter.invoke(obj);
			System.out.println(f);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			e.printStackTrace();
		}
	}
}
