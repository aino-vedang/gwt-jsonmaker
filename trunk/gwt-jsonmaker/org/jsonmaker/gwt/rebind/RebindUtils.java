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

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;

/**
 * 
 * @author Gaurav Saxena<gsaxena81@gmail.com> 
 * Credited to Andrés Adolfo Testi
 *
 */
public final class RebindUtils {
	

	public static String simpleStubClassName(String className){
		return "__" + className + "_impl__";
	}
	
	public static String simpleStubClassName(JClassType baseClass){
	    return simpleStubClassName(baseClass.getSimpleSourceName());
	}
	
	public static String qualifiedStubClassName(JClassType baseClass){
		return baseClass.getPackage().toString() + simpleStubClassName(baseClass);
	}

	public static String jsonizerImplSimpleName(JClassType beanClass){
		return beanClass.getName() + Constants.JSONIZER_SUFFIX;
	}
	
	public static String jsonizerImplQualifiedName(JClassType beanClass){
		return beanClass.getPackage().getName() + "." + simpleStubClassName(jsonizerImplSimpleName(beanClass));
		
	}
	
	public static String gwtCreateExp(String cls) {		
		return "((" + cls + ")" + Constants.GWT_CLASS + ".create(" + cls + ".class))";
	}
	
	public static String jsonizerSimpleName(JClassType beanClass){
		return beanClass.getSimpleSourceName() + Constants.JSONIZER_SUFFIX;
	}

	public static String jsonizerQualifiedName(JClassType beanClass){
		return beanClass.getQualifiedSourceName() + Constants.JSONIZER_SUFFIX;
	}
	
	public static String getJSNIInvocationExp(JMethod method, String paramExp){
		String argSignature = "";
		JParameter[] params = method.getParameters();
		for(int i = 0; i < params.length; i++){
			if(params[i].getType().equals(JPrimitiveType.LONG))
				argSignature += JPrimitiveType.DOUBLE.getJNISignature();
			else
				argSignature += params[i].getType().getJNISignature();
		}		
		return "@" + method.getEnclosingType().getQualifiedSourceName() + "::" + method.getName() + "("	+ argSignature + ")(" + paramExp + ")";
	}
	
	public static String getJSNIInvocationExp(String obj, JMethod method, String paramExp){
		return obj + "." + getJSNIInvocationExp(method, paramExp); 
	}
}
