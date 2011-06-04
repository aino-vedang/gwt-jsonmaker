/*
 * Copyright 2007 Andrés Adolfo Testi < andres.a.testi AT gmail.com >
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jsonmaker.gwt.rebind;

import java.util.ArrayList;
import java.util.List;

import org.jsonmaker.gwt.client.annotation.Transient;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * 
 * @author Gaurav Saxena<gsaxena81@gmail.com> 
 * Credited to Andrés Adolfo Testi
 *
 */
public class BeanProperty {
	
	private JMethod setter;
	private JMethod getter;
	private String name;
	
	public BeanProperty(String capitalizedName, JMethod getter, JMethod setter){
		this.name = recapitalize(capitalizedName);
		this.getter = getter;
		this.setter = setter;
	}
	
	public String getName(){
		return name;
	}
	
	public JMethod getGetter(){
		return getter;
	}
	
	public JMethod getSetter(){
		return setter;
	}
		
	public JType getValueType(){
		return getter.getReturnType();
	}
	
	public JClassType getEnclosingType(){
		return getter.getEnclosingType();
	}
	private static String recapitalize(String capitalized){
		StringBuffer buffer = new StringBuffer(capitalized);
		char first = buffer.charAt(0);
		char recap = Character.isUpperCase(first)? Character.toLowerCase(first): Character.toUpperCase(first);
		buffer.setCharAt(0, Character.toLowerCase(recap));
		return buffer.toString();
	}
		
	private static boolean isCandidateAccessor(JMethod method){
		return 
			!method.isAnnotationPresent(Transient.class) &&  
			method.isPublic() && 
			!method.isAbstract() && 
			!method.isStatic();
	}
	
	private static boolean hasAccessors(JClassType cls, JMethod getter, JMethod setter){
		final JType[] params = {};
		JMethod superGetter = cls.findMethod(getter.getName(), params);
		if(superGetter==null)
			return false;
		if(!superGetter.getReturnType().equals(getter.getReturnType()))
			return false;
		if(!isCandidateAccessor(superGetter))
			return false;
		
		JMethod superSetter = cls.findMethod(setter.getName(), new JType[]{getter.getReturnType()});
		if(superSetter==null)
			return false;
		if(!isCandidateAccessor(superSetter))
			return false;
		if(!setter.getReturnType().equals(JPrimitiveType.VOID))
			return false;
		
		return true;			
	}
	
	private static boolean isOverride(JClassType cls, JMethod getter, JMethod setter){
		JClassType langObject = cls.getOracle().getJavaLangObject();
		while(true){
			cls = cls.getSuperclass();
			if(cls.equals(langObject))
				return false;
			if(hasAccessors(cls, getter, setter))
				return true;
		}
	}
		
	public JType getType(){
		return getter.getReturnType();
	}
	
	public static List<BeanProperty> getFullProperties(JClassType cls) {

		ArrayList<BeanProperty> properties = new ArrayList<BeanProperty>();
		JMethod[] methods = cls.getMethods();

		for (int i = 0; i < methods.length; i++) {
			JMethod getter = methods[i];
			String getterName = getter.getName();
			int propertyNameStartIndex = getterName.startsWith("get") ? 3 : 2;
			String capitalizedName = getterName.substring(propertyNameStartIndex);
			//JField field = cls.findField((char)(capitalizedName.charAt(0) + 32) + capitalizedName.substring(1));
			if ((getterName.startsWith("get") || getterName.startsWith("is"))
					&& getter.getParameters().length == 0 && isCandidateAccessor(getter))
			{
				JType type = getter.getReturnType();
				String setterName = "set" + capitalizedName;
				JMethod setter = cls.findMethod(setterName,	new JType[] { type });
				/*if(type.equals(JPrimitiveType.LONG))
				{
					setter = cls.findMethod(setterName,	new JType[] { JPrimitiveType.DOUBLE });
					if(setter == null)
						throw new IllegalArgumentException(
								"variable named '" + capitalizedName.substring(0,1).toLowerCase() 
								+ capitalizedName.substring(1) + "' in class named '" + cls.getName()
								+ "' should declare a setter with argument of type double; This argument may be cast to long in the method");
				}*/
				if (setter != null && //field needs to be null checked. If field is not found, don't add
					setter.getReturnType().equals(JPrimitiveType.VOID) && 
					isCandidateAccessor(setter) &&
					!isOverride(cls, getter, setter))
				{
					properties.add(new BeanProperty(capitalizedName, getter, setter));
				}
			}
		}
		return properties;
	}
	
	public String getJSNISetterInvocation(String obj, String paramExp){
		return RebindUtils.getJSNIInvocationExp(obj, setter, paramExp);
	}
}
