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

import java.io.PrintWriter;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * 
 * @author Gaurav Saxena 
 * Credited to Andrés Adolfo Testi
 *
 */
public class JsonizerGenerator extends Generator {
	
	private String simpleBeanClassName(JClassType converterClass){
		String converterName = converterClass.getSimpleSourceName();
		int splitPos = converterName.lastIndexOf(Constants.JSONIZER_SUFFIX);
		return converterName.substring(0, splitPos);
	}
	
	public String generate(TreeLogger logger, GeneratorContext context,	String typeName) throws UnableToCompleteException {
	    TypeOracle typeOracle = context.getTypeOracle();
	    
	    if(!typeName.endsWith(Constants.JSONIZER_SUFFIX)){
	    	logger.log(TreeLogger.ERROR, "Jsonizer named must be suffixed with '" + Constants.JSONIZER_SUFFIX + "'", null);
	    	throw new UnableToCompleteException();
	    }

	    
	    JClassType converterClass;
	    
	    try {
			converterClass = typeOracle.getType(typeName);
		} catch (NotFoundException e) {
	    	logger.log(TreeLogger.ERROR, "Doesn't exists a Jsonizer for '" + typeName + "'", e);
	    	throw new UnableToCompleteException();	    
		}    
	    
	    if(converterClass.isClass()!=null){
	    	if(converterClass.isAbstract()){
		    	logger.log(TreeLogger.ERROR, "Jsonizer class '" + typeName + "' can´t be abstract", null);
		    	throw new UnableToCompleteException();	    		
	    	}
	    	return null;
	    }
	    
	    String simpleBeanClassName = simpleBeanClassName(converterClass);
	    
	    String qualifiedBeanClassName = converterClass.getPackage().getName() + "." + simpleBeanClassName;	    
	  //  logger.log(TreeLogger.INFO, "buscando el bean '" + qualifiedBeanClassName + "'", null);
	    
	    JClassType beanClass = context.getTypeOracle().findType(qualifiedBeanClassName);

	    if(beanClass==null){
	    	logger.log(TreeLogger.ERROR, "Class '" + qualifiedBeanClassName + "' not found but Jsonizer found", null);
	    	throw new UnableToCompleteException();
	    }
	    
	    String packageName = converterClass.getPackage().getName();
	    JClassType superBeanClass = beanClass.getSuperclass();
	    
	    // Verificacion de que los conversores de superclase implementen BeanJ2BConverter
	    // si el bean no es subclase de object
	    if(!superBeanClass.equals(typeOracle.getJavaLangObject())){ 
	    	String superConverterName = packageName + "." + RebindUtils.jsonizerSimpleName(superBeanClass);
	    	JClassType superConverter = typeOracle.findType(superConverterName);
	    	//si el superconverter esta definido
	    	if(superConverter!=null){

	    		
	    		// si el super converter es clase
	    		if(superConverter.isClass()!=null){
	    			if(superConverter.isAbstract()){
	    				logger.log(TreeLogger.ERROR, "An user defined Jsonizer is abstract: '" + superConverter.getQualifiedSourceName() + "'", null);
	    				throw new UnableToCompleteException();
	    			}
	    			
		    		JClassType beanConverterClass = typeOracle.findType(Constants.BEAN_JSONIZER_CLASS);
		    		if(beanConverterClass==null){
		    			logger.log(TreeLogger.ERROR, "'" + Constants.BEAN_JSONIZER_CLASS +"' class not found", null);
		    			throw new UnableToCompleteException();
		    		}
	    			
	    			if(!superConverter.isAssignableTo(beanConverterClass)){
		    			logger.log(TreeLogger.ERROR, "Super Jsonizer for '" + beanClass.getQualifiedSourceName() + "' class doesn't extends '" + Constants.BEAN_JSONIZER_CLASS + "' class", null);
		    			throw new UnableToCompleteException();	    				
	    			}
	    				    				    			
	    		}
	    		
	    	}
	    }
	    
	    String simpleStubClassName = RebindUtils.simpleStubClassName(converterClass);

	    String qualifiedStubClassName = packageName + "." + simpleStubClassName;
	    SourceWriter swBean = getSourceWriter(logger, context, packageName, simpleBeanClassName);
	    SourceWriter sw = getSourceWriter(logger, context, packageName, simpleStubClassName, converterClass.getQualifiedSourceName());
	    if (sw == null) {
	      return qualifiedStubClassName;
	    }
	    
	    JsonizerWriter converterWriter = new JsonizerWriter(logger, context, sw, beanClass);
	    
	    converterWriter.writeMethods();
	    
	    sw.commit(logger);

	    return qualifiedStubClassName;
	}
	
	private SourceWriter getSourceWriter(TreeLogger logger,
			GeneratorContext ctx, String packageName, String className,
			String interfaceName) {
		PrintWriter printWriter = ctx.tryCreate(logger, packageName, className);
		if (printWriter == null) {
			return null;
		}
		ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
			packageName, className);
		
		composerFactory.setSuperclass(Constants.BEAN_JSONIZER_CLASS);
		composerFactory.addImplementedInterface(interfaceName);

		return composerFactory.createSourceWriter(ctx, printWriter);
	}
	
	private SourceWriter getSourceWriter(TreeLogger logger,	GeneratorContext ctx, String packageName
			, String className) {
		PrintWriter printWriter = ctx.tryCreate(logger, packageName, className);
		if (printWriter == null) {
			return null;
		}
		ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
			packageName, className);

		return composerFactory.createSourceWriter(ctx, printWriter);
	}
}
