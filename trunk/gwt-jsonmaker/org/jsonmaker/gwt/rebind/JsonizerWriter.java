/*
 * Copyright 2007 Andr�s Adolfo Testi < andres.a.testi AT gmail.com >
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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * 
 * @author Gaurav Saxena 
 * Credited to Andr�s Adolfo Testi
 *
 */
public class JsonizerWriter {
	
	private SourceWriter sw;
	private GeneratorContext ctx;
	private TreeLogger logger;
	private JClassType beanClass;
	private String implClassName;
	
	public JsonizerWriter(TreeLogger logger, GeneratorContext ctx, 
			SourceWriter sw, JClassType beanClass) 
	{
		this.sw = sw;
		this.ctx = ctx;
		this.logger = logger;
		this.beanClass = beanClass;
		this.implClassName = RebindUtils.jsonizerImplQualifiedName(beanClass);
	}
	
	private String jsonPropName(BeanProperty prop){
		String[][] metaData = prop.getGetter().getMetaData(Constants.PROPNAME_ANNOTATION); 
		if(metaData.length > 0){
			if(metaData[0].length > 0){ 
				return metaData[0][0];
			}else{
				logger.log(TreeLogger.WARN, "@" + Constants.PROPNAME_ANNOTATION + 
						" annotation is not setted", null);
			}
		}
		return prop.getName();		
	}
	
				
	private void writeJSNISetter(String self, BeanProperty prop, Map jsonizers) 
		throws UnableToCompleteException
	{

		final String bean = "__bean__";
		final String jsValue = "__jsValue__";
		
		sw.println("\"" + jsonPropName(prop) + "\": function(" + bean + ", " + jsValue + "){");
		sw.indent();
		
		if(prop.getType().isPrimitive() == null && !isNotNull(prop) && !prop.getType().toString().equals("class java.lang.String")){
			sw.println("if(" + jsValue + " == null){");
			sw.indent();
			sw.println(prop.getJSNISetterInvocation(bean, "null") + "; ");
			sw.println("return;");
			sw.outdent();
			sw.println("}");
		}
		
		String toJavaExp;
		
		if(prop.getType().isPrimitive()!=null){
			toJavaExp = 
				"@" + Constants.DEFAULTS_CLASS + "::" + 
				(String)Constants.PRIMITIVE_JSONIZERS.get(prop.getType()) + 
				"(" + Constants.JS_OBJECT_SIGNATURE +")(Object(" + jsValue + "))";
		} else if(prop.getType().toString().equals("class java.lang.String"))
		{
			toJavaExp = 
				"@" + Constants.DEFAULTS_CLASS + "::asPrimitiveString" + 
				//(String)Constants.PRIMITIVE_JSONIZERS.get(prop.getType()) + 
				"(" + "Ljava/lang/String;" +")(Object(" + jsValue + "))";
		}
		else{
			String jsonizerExp;
			if(prop.getType().equals(beanClass)){
				jsonizerExp = self;
			}else{
				final String jsonizerVar = "__jsonizer__";
				String jsonizerDeclaration = "var " + jsonizerVar + " = " + 
					self + ".@" + implClassName + "::" + jsonizers.get(prop.getType())
					+ "()();";
				sw.println(jsonizerDeclaration);
				jsonizerExp = jsonizerVar;
			}		
			toJavaExp = jsonizerExp + "." + Constants.ASJAVAOBJECT_METHOD_SIGNATURE + 
				"(Object(" + jsValue + "))";
		}
		String setterInvocation = prop.getJSNISetterInvocation(bean, toJavaExp) + ";";
		sw.println(setterInvocation);
		
		sw.outdent();		
		sw.print("}");

	}
	

	private String jsonizerExp(JType type) throws UnableToCompleteException{
				
		JParameterizedType paramType = type.isParameterized();
		if(paramType!=null){
			return parametrizedJsonizerExp(paramType);
		}
				
		if(Constants.PARAMETRIZED_JSONIZERS.containsKey(type.getQualifiedSourceName())){
			logger.log(TreeLogger.ERROR, "Property of class '" + type.getQualifiedSourceName() + 
					"' has not associated parameters", null);
			throw new UnableToCompleteException();
		}
		
		JClassType classType = type.isClass();
		if(classType != null){		
			return classJsonizerExp(classType);
		}
		
		JArrayType arrayType = type.isArray();
		if(arrayType != null){
			return arrayJsonizerExp(arrayType);				
		}
		
		logger.log(TreeLogger.ERROR, 
				"Unable to jsonize an object of type '" + 
				type.getQualifiedSourceName() + "'", 
				null);
		throw new UnableToCompleteException();
	}
	

	
	private String ensureJsonizer(JClassType beanClass){
		String simpleName = RebindUtils.jsonizerSimpleName(beanClass);
				
		PrintWriter writer = ctx.tryCreate(logger, beanClass.getPackage().getName(), simpleName);
		if(writer!=null){
			ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
				beanClass.getPackage().getName(), simpleName);
			
			composerFactory.makeInterface();

			composerFactory.addImplementedInterface(Constants.JSONIZER_INTERFACE);

			composerFactory.createSourceWriter(ctx, writer).commit(logger);
		}
		
		return beanClass.getPackage().getName() + "." + simpleName;
		
	}
	
	private String parametrizedJsonizerExp(JParameterizedType paramType) 
		throws UnableToCompleteException
	{
		
		Object jsonizerClass = Constants.PARAMETRIZED_JSONIZERS.get(
				paramType.getNonParameterizedQualifiedSourceName());
		if(jsonizerClass == null){
			logger.log(TreeLogger.ERROR, "Parametrized type '" + 
					paramType.getQualifiedSourceName() + "' is not supported", null);
			throw new UnableToCompleteException();
		}
		
		String elemJsonizer;
		
		JType[] args = paramType.getTypeArgs();
		if(args.length > 1){
			if(!args[0].getQualifiedSourceName().equals("java.lang.String")){
				logger.log(TreeLogger.ERROR, "First parameter in parametrized class '" + 
						paramType.getQualifiedSourceName() + 
						"' must be of type 'java.lang.String'", null);
				throw new UnableToCompleteException();
			}
			elemJsonizer = jsonizerExp(args[1]);
		}else{
			elemJsonizer = jsonizerExp(args[0]);
		}
		
		return "(new " + jsonizerClass + "(" + elemJsonizer + "))";
	}
	
	private String classJsonizerExp(JClassType classType) throws UnableToCompleteException{

		Object exp = Constants.LANG_JSONIZERS.get(classType.getQualifiedSourceName());

		if(exp!=null){
			return exp.toString();
		}

		return RebindUtils.gwtCreateExp(ensureJsonizer(classType));
		
	}
	
	private String createArrayExp(JArrayType arrayType, String sizeExp){
		StringBuffer brackets = new StringBuffer();
		JType compType = arrayType.getComponentType();
		JArrayType compArrayType;
		while((compArrayType = compType.isArray())!=null){
			brackets.append("[]");
			compType = compArrayType.getComponentType();
		}
		return "new " + compType.getQualifiedSourceName() + "[" + sizeExp + "]" + brackets.toString();
	}
	
	private String arrayJsonizerExp(JArrayType arrayType) throws UnableToCompleteException{
		
		JType compType = arrayType.getComponentType();
		
		JPrimitiveType compPrimType = compType.isPrimitive();
		if(compPrimType!=null){
			return Constants.PRIMITIVE_ARRAY_JSONIZERS.get(compPrimType).toString();
		}

		return "(new " + Constants.ARRAY_JSONIZER_CLASS + 
			"("+ jsonizerExp(compType) + "){protected java.lang.Object[] createArray(int size){return " + 
				createArrayExp(arrayType, "size") + ";}})";

	}
	
	private void writeCreateBeanMethod() throws UnableToCompleteException{
		sw.println("protected java.lang.Object createBean(){");
		sw.indent();
		if(!beanClass.isDefaultInstantiable()){
			logger.log(TreeLogger.WARN, "Jsonizable class '" + beanClass.getQualifiedSourceName() + 
					"' is not default instantiable", null);
			sw.println("throw new java.lang.IllegalArgumentException();");
		}else{
			sw.println("return new " + beanClass.getQualifiedSourceName() + "();");
		}
		sw.outdent();
		sw.println("}");
	}
	
	public void writeGetSuperJsonizerMethod() throws UnableToCompleteException{
		final String superJsonizer = "__superJsonizer__";
		sw.println("protected " + Constants.BEAN_JSONIZER_CLASS + " getSuperJsonizer(){");
		sw.indent();
		
		JClassType langObject = beanClass.getOracle().getJavaLangObject();
		JClassType superClass = beanClass.getSuperclass();
		
		if(superClass.equals(langObject)){
			sw.println("return null;");			
		}else{		
			sw.println("final " + Constants.BEAN_JSONIZER_CLASS + " " + superJsonizer + " = (" + 
					Constants.BEAN_JSONIZER_CLASS + ")" + Constants.GWT_CLASS + ".create(" + 
					ensureJsonizer(superClass) + ".class);");		
			sw.println("return " + superJsonizer +";");
		}
		sw.outdent();
		sw.println("}");
	}
	
	private String getGetJsonizerMethodName(int number){
		return "__jsonizer" + number + "__";
	}
	
	public void writeJsonizerInstance(JType type, String jsonizerName) throws UnableToCompleteException{
		
		sw.println("private " + Constants.JSONIZER_INTERFACE + " " + jsonizerName + ";");

		sw.println("private " + Constants.JSONIZER_INTERFACE + " " + jsonizerName + "(){");
		sw.indent();
		sw.println("if(" + jsonizerName + " == null)");
		sw.indent();
		sw.println(jsonizerName + " = " + jsonizerExp(type) + ";");
		sw.outdent();
		sw.println("return " + jsonizerName + ";");		
		sw.outdent();
		sw.println("}");
	}
	
	private void writeStoreStringListMethod(List props, Map jsonizers) 
		throws UnableToCompleteException
	{
		final String javaValue = "__javaValue__";
		final String bean = "__bean__";
		final String list = "__list__";
		sw.println("protected void storeStringList(java.lang.Object " + javaValue + 
			", java.util.List " + list +") throws " + Constants.EXCEPTION_CLASS +"{");
		sw.indent();
				
		sw.println(beanClass.getQualifiedSourceName() + " " + bean + " = (" + 
			beanClass.getQualifiedSourceName() + ")" + javaValue + ";");
		
		Iterator it = props.iterator();
		
		while(it.hasNext()){
			
			BeanProperty bp = (BeanProperty)it.next();
			
			sw.println();
			
			String propLabel = "\\\"" + jsonPropName(bp) + "\\\":"; 
			
			
			String getterExp = bean + "." + bp.getGetter().getName() + "()";

			if(bp.getType().isPrimitive()!=null){
				sw.println(list + ".add(\"" + propLabel + "\" + " + getterExp + ");");
				
			}else{ 	
				
				sw.println("{");
				sw.indent();
				
				final String propValue = "__propValue__";
				
				sw.println(bp.getType().getQualifiedSourceName() + " " + propValue + " = " + getterExp + ";");
				
				sw.println("if(" + propValue + " != null){");
				sw.indent();			
				
				String asStringExp;
				if(bp.getType().equals(beanClass)){
					asStringExp = Constants.AS_STRING_METHOD;
				}else{
					asStringExp = (String)jsonizers.get(bp.getType()) + "()." + Constants.AS_STRING_METHOD;
				}
				sw.println();
				sw.println(list + ".add(\"" + propLabel + "\" + " + asStringExp + "(" + getterExp + "));");
				sw.outdent();
				sw.print("}");
				if(isNotNull(bp)){
					sw.println("else{");
					sw.indent();
					sw.println("throw new " + Constants.EXCEPTION_CLASS + "();");
					sw.outdent();
					sw.println("}");							
				}else if(isRequired(bp)){					
					sw.println("else{");
					sw.indent();
					sw.println(list + ".add(\"" + propLabel + "null\");");
					sw.outdent();
					sw.println("}");
				}else{
					sw.println();
				}
				
				sw.outdent();
				sw.println("}");								
			}			
			
	
		}

		sw.outdent();		
		sw.println("}");
				
	}
	
	private Map createJsonizerGetterNames(List props){
		Map jsonizers = new HashMap();
		Iterator it = props.iterator();
		int i = 0;
		while(it.hasNext()){
			BeanProperty prop = (BeanProperty)it.next();
			if(!prop.getType().equals(beanClass)){
				if(!jsonizers.containsKey(prop.getType())){
					jsonizers.put(prop.getType(), getGetJsonizerMethodName(i));
					i++;
				}
			}
		}
		return jsonizers;
	}
	
	private void writeCreateSetterPoolMethod(List properties, Map jsonizers) throws UnableToCompleteException{
		
		final String self = "__self__";
		sw.println("protected native " + Constants.JS_OBJECT_CLASS + " createSetterPool()/*-{");

		sw.indent();
		sw.println("var " + self + " = this;");
		
		sw.println("return {");
		
		sw.indent();		
		
		Iterator it = properties.iterator();

		while(it.hasNext()){
			
			BeanProperty prop = (BeanProperty)it.next();
			writeJSNISetter(self, prop, jsonizers);
			if(it.hasNext()){
				sw.println(",");
			}else{
				sw.println();
				break;
			}

		}
		sw.outdent();
		sw.println("};");
		sw.outdent();
		sw.println("}-*/;");

		
	}
		
	public void writeMethods() throws UnableToCompleteException{

		List properties = BeanProperty.getFullProperties(beanClass);
		
		Map jsonizerGetters = createJsonizerGetterNames(properties);
		
		writeJsonizerGetters(jsonizerGetters);
		
		writeCreateBeanMethod();
		
		writeGetSuperJsonizerMethod();
		
		writeCreatePropertiesMethod(properties);
		
		writeCreateSetterPoolMethod(properties, jsonizerGetters);
		
		writeStoreStringListMethod(properties, jsonizerGetters);

	}

	private void writeJsonizerGetters(Map jsonizerGetters) throws UnableToCompleteException{
		Iterator it = jsonizerGetters.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry entry = (Map.Entry)it.next();			
			JType type = (JType)entry.getKey();
			if(type.isPrimitive() == null && !type.equals(beanClass))
				writeJsonizerInstance(type, (String)entry.getValue());
		}
	}
	
	private List getRequireds(List props) {
		ArrayList requireds = new ArrayList();
		Iterator it = props.iterator();
		while (it.hasNext()) {
			BeanProperty bp = (BeanProperty) it.next();
			if (isRequired(bp)) {
				requireds.add(bp);
			}
		}
		return requireds;
	}
	
	private void writeCreatePropertiesMethod(List props) throws UnableToCompleteException{
		sw.println("protected native " + Constants.JS_OBJECT_CLASS + " createRequiredProperties()/*-{");
		sw.indent();
		sw.println("return [");
		sw.indent();
		
		Iterator it = getRequireds(props).iterator();
		while(it.hasNext()){
			BeanProperty bp = (BeanProperty)it.next();
			sw.println("\"" + bp.getName() + "\"");
			if(it.hasNext()){
				sw.println(",");
			}
		}		
		sw.outdent();
		sw.println("];");		
		sw.outdent();
		sw.println("}-*/;");
		
	}
	
	private boolean isNotNull(BeanProperty prop){
		return prop.getGetter().getMetaData(Constants.NOTNULL_ANNOTATION).length > 0;
	}
	
	private boolean isRequired(BeanProperty prop){
		return prop.getGetter().getMetaData(Constants.REQUIRED_ANNOTATION).length > 0;		
	}
	
}
