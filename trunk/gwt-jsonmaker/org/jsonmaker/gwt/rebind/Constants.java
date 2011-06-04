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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.ext.typeinfo.JPrimitiveType;

/**
 * 
 * @author Gaurav Saxena<gsaxena81@gmail.com> 
 * Credited to Andrés Adolfo Testi
 *
 */
public final class Constants {
	public static final String PACKAGE = "org.jsonmaker.gwt.client";
	
	public static final String JSONIZER_SUFFIX = "Jsonizer";
	public static final String JSONIZER_INTERFACE = PACKAGE + "." + JSONIZER_SUFFIX;
	public static final String BASE_PACKAGE = PACKAGE + ".base";
	public static final String BEAN_JSONIZER_CLASS = BASE_PACKAGE + ".Bean" + JSONIZER_SUFFIX;
	
	public static final String DEFAULTS_CLASS = BASE_PACKAGE + ".Defaults";
	public static final String ARRAY_JSONIZER_CLASS = BASE_PACKAGE + ".Array" + JSONIZER_SUFFIX;
	public static final String EXCEPTION_CLASS = PACKAGE + ".JsonizerException";
	public static final String JS_OBJECT_CLASS = "com.google.gwt.core.client.JavaScriptObject";	
		
	public static final String TRANSIENT_ANNOTATION = "transient";
	public static final String REQUIRED_ANNOTATION = "required";
	public static final String NOTNULL_ANNOTATION = "notNull";
	public static final String PROPNAME_ANNOTATION = "propName";
	
	public static final String GWT_CLASS = "com.google.gwt.core.client.GWT";
	public static final String HASHMAP_JSONIZER = BASE_PACKAGE + ".HashMap" + JSONIZER_SUFFIX;
	public static final String LINKED_HASHMAP_JSONIZER = BASE_PACKAGE + ".LinkedHashMap" + JSONIZER_SUFFIX;
	public static final String TREEMAP_JSONIZER = BASE_PACKAGE + ".TreeMap" + JSONIZER_SUFFIX;
	public static final String ARRAYLIST_JSONIZER = BASE_PACKAGE + ".ArrayList" + JSONIZER_SUFFIX;
	public static final String LINKEDLIST_JSONIZER = BASE_PACKAGE + ".LinkedList" + JSONIZER_SUFFIX;
	public static final String VECTOR_JSONIZER = BASE_PACKAGE + ".Vector" + JSONIZER_SUFFIX;
	public static final String HASHSET_JSONIZER = BASE_PACKAGE + ".HashSet" + JSONIZER_SUFFIX;
	public static final String LINKED_HASHSET_JSONIZER = BASE_PACKAGE + ".LinkedHashSet" + JSONIZER_SUFFIX;
	public static final String TREESET_JSONIZER = BASE_PACKAGE + ".TreeSet" + JSONIZER_SUFFIX;
	public static final String STACK_JSONIZER = BASE_PACKAGE + ".Stack" + JSONIZER_SUFFIX;	
	
	public static final String AS_STRING_METHOD = "asString";
	public static final String AS_JAVAOBJECT_METHOD = "asJavaObject";
	public static final String SETPARAMCONVERTERS_METHOD = "setParamConverters";
	public static final String SETVALUES_METHOD = "setValues";

	public static final String JS_OBJECT_SIGNATURE = "Lcom/google/gwt/core/client/JavaScriptObject;";
	
	public static final String ASJAVAOBJECT_METHOD_SIGNATURE = "@" + JSONIZER_INTERFACE + "::" + AS_JAVAOBJECT_METHOD + "(" + JS_OBJECT_SIGNATURE + ")";
	
	public static final Map<String, String> LANG_JSONIZERS;
	public static final Map<JPrimitiveType, String> PRIMITIVE_JSONIZERS;
	public static final Map<JPrimitiveType, String> PRIMITIVE_ARRAY_JSONIZERS;
	public static final Map<String, String> PARAMETRIZED_JSONIZERS;
	
	private static String jsonizerConstantName(String name){
		return DEFAULTS_CLASS + "." + name;
	}
	
	private static void registerPrimitiveJsonizer(Map<JPrimitiveType, String> map, JPrimitiveType type, String name){
		map.put(type, "asPrimitive" + name);
	}
	
	static{
		
		Map<String, String> _LANG_JSONIZERS  = new HashMap<String, String>();
		_LANG_JSONIZERS.put("java.lang.String", jsonizerConstantName("STRING_JSONIZER"));
		_LANG_JSONIZERS.put("java.lang.Number", jsonizerConstantName("DOUBLE_JSONIZER"));
		_LANG_JSONIZERS.put("java.lang.Double", jsonizerConstantName("DOUBLE_JSONIZER"));
		_LANG_JSONIZERS.put("java.lang.Float", jsonizerConstantName("FLOAT_JSONIZER"));
		_LANG_JSONIZERS.put("java.lang.Integer", jsonizerConstantName("INTEGER_JSONIZER"));
		_LANG_JSONIZERS.put("java.lang.Long", jsonizerConstantName("LONG_JSONIZER"));
		_LANG_JSONIZERS.put("java.lang.Byte", jsonizerConstantName("BYTE_JSONIZER"));
		_LANG_JSONIZERS.put("java.lang.Boolean", jsonizerConstantName("BOOLEAN_JSONIZER"));
		_LANG_JSONIZERS.put("java.lang.Character", jsonizerConstantName("CHARACTER_JSONIZER"));
		_LANG_JSONIZERS.put("java.util.Date", jsonizerConstantName("DATE_JSONIZER"));
		
		LANG_JSONIZERS = Collections.unmodifiableMap(_LANG_JSONIZERS);
				
		Map<JPrimitiveType, String> _PRIMITIVE_ARRAY_JSONIZERS  = new HashMap<JPrimitiveType, String>();
		_PRIMITIVE_ARRAY_JSONIZERS.put(JPrimitiveType.BOOLEAN, jsonizerConstantName("A_BOOLEAN_JSONIZER"));
		_PRIMITIVE_ARRAY_JSONIZERS.put(JPrimitiveType.BYTE, jsonizerConstantName("A_BYTE_JSONIZER"));
		_PRIMITIVE_ARRAY_JSONIZERS.put(JPrimitiveType.CHAR, jsonizerConstantName("A_CHAR_JSONIZER"));
		_PRIMITIVE_ARRAY_JSONIZERS.put(JPrimitiveType.DOUBLE, jsonizerConstantName("A_DOUBLE_JSONIZER"));
		_PRIMITIVE_ARRAY_JSONIZERS.put(JPrimitiveType.FLOAT, jsonizerConstantName("A_FLOAT_JSONIZER"));
		_PRIMITIVE_ARRAY_JSONIZERS.put(JPrimitiveType.INT, jsonizerConstantName("A_INT_JSONIZER"));
		_PRIMITIVE_ARRAY_JSONIZERS.put(JPrimitiveType.LONG, jsonizerConstantName("A_LONG_JSONIZER"));
		_PRIMITIVE_ARRAY_JSONIZERS.put(JPrimitiveType.SHORT, jsonizerConstantName("A_SHORT_JSONIZER"));
		
		PRIMITIVE_ARRAY_JSONIZERS = Collections.unmodifiableMap(_PRIMITIVE_ARRAY_JSONIZERS);
		
		Map<JPrimitiveType, String> _PRIMITIVE_JSONIZERS = new HashMap<JPrimitiveType, String>();
		registerPrimitiveJsonizer(_PRIMITIVE_JSONIZERS, JPrimitiveType.INT, "Int");
		registerPrimitiveJsonizer(_PRIMITIVE_JSONIZERS, JPrimitiveType.LONG, "Long");
		registerPrimitiveJsonizer(_PRIMITIVE_JSONIZERS, JPrimitiveType.DOUBLE, "Double");
		registerPrimitiveJsonizer(_PRIMITIVE_JSONIZERS, JPrimitiveType.FLOAT, "Float");
		registerPrimitiveJsonizer(_PRIMITIVE_JSONIZERS, JPrimitiveType.BYTE, "Byte");
		registerPrimitiveJsonizer(_PRIMITIVE_JSONIZERS, JPrimitiveType.BOOLEAN, "Boolean");
		registerPrimitiveJsonizer(_PRIMITIVE_JSONIZERS, JPrimitiveType.SHORT, "Short");
		registerPrimitiveJsonizer(_PRIMITIVE_JSONIZERS, JPrimitiveType.CHAR, "Char");
		
		PRIMITIVE_JSONIZERS = Collections.unmodifiableMap(_PRIMITIVE_JSONIZERS);		
		
		Map<String, String> _PARAMETRIZED_JSONIZERS = new HashMap<String, String>();
		_PARAMETRIZED_JSONIZERS.put("java.util.Map", HASHMAP_JSONIZER);
		_PARAMETRIZED_JSONIZERS.put("java.util.HashMap", HASHMAP_JSONIZER);
		_PARAMETRIZED_JSONIZERS.put("java.util.LinkedHashMap", LINKED_HASHMAP_JSONIZER);
		_PARAMETRIZED_JSONIZERS.put("java.util.TreeMap", TREEMAP_JSONIZER);
		_PARAMETRIZED_JSONIZERS.put("java.util.AbstractMap", HASHMAP_JSONIZER);
		
		_PARAMETRIZED_JSONIZERS.put("java.util.Collection", ARRAYLIST_JSONIZER);
		_PARAMETRIZED_JSONIZERS.put("java.util.List", ARRAYLIST_JSONIZER);
		_PARAMETRIZED_JSONIZERS.put("java.util.ArrayList", ARRAYLIST_JSONIZER);
		_PARAMETRIZED_JSONIZERS.put("java.util.LinkedList", LINKEDLIST_JSONIZER);
		
		_PARAMETRIZED_JSONIZERS.put("java.util.Vector", VECTOR_JSONIZER);
		
		_PARAMETRIZED_JSONIZERS.put("java.util.Set", HASHSET_JSONIZER);
		_PARAMETRIZED_JSONIZERS.put("java.util.HashSet", HASHSET_JSONIZER);
		_PARAMETRIZED_JSONIZERS.put("java.util.LinkedHashSet", LINKED_HASHSET_JSONIZER);
		_PARAMETRIZED_JSONIZERS.put("java.util.TreeSet", TREESET_JSONIZER);
		_PARAMETRIZED_JSONIZERS.put("java.util.AbstractSet", HASHSET_JSONIZER);
		
		_PARAMETRIZED_JSONIZERS.put("java.util.Stack", STACK_JSONIZER);
		
		PARAMETRIZED_JSONIZERS = Collections.unmodifiableMap(_PARAMETRIZED_JSONIZERS);
	
	}
	
}
