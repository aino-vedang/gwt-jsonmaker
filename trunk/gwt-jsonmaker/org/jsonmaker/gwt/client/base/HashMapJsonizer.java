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
package org.jsonmaker.gwt.client.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jsonmaker.gwt.client.Jsonizer;
import org.jsonmaker.gwt.client.JsonizerException;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Translates objects of class java.util.HashMap.
 * 
 * @author Gaurav Saxena<gsaxena81@gmail.com> 
 * Credited to Andrés Adolfo Testi
 *
 */
public class HashMapJsonizer implements Jsonizer{
	
	private Jsonizer keyJsonizer;
	private Jsonizer valueJsonizer;
	
	/**
	 * Constructs a new HashMapJsonizer.
	 * @param elemJsonizer Jsonizer for inner type.
	 */
	public HashMapJsonizer(Jsonizer keyJsonizer, Jsonizer valueJsonizer){
		this.keyJsonizer = keyJsonizer;
		this.valueJsonizer = valueJsonizer;
	}
		
	protected Map createMap(){
		return new HashMap();
	}
	
	private native Map storeMap(JavaScriptObject jsObject)/*-{
		var map = this.@org.jsonmaker.gwt.client.base.HashMapJsonizer::createMap()();
		for(var i = 0; i < jsObject.length; i++){
			if(jsObject[i]['key'].toString().indexOf('__gwt_ObjectId') != 0)//hack for chrome because gwt introduces another attribute __gwt_ObjectId for chrome!
			{
				var rawValue = jsObject[i]['value'];
				var finalValue;
				if(rawValue == null)
					finalValue = null;
				else {
					if(typeof rawValue != 'string') {
						var jsonizer = this.@org.jsonmaker.gwt.client.base.HashMapJsonizer::valueJsonizer;
						finalValue = jsonizer.@org.jsonmaker.gwt.client.Jsonizer::asJavaObject(Lcom/google/gwt/core/client/JavaScriptObject;)(Object(rawValue));
					} else
						finalValue = @org.jsonmaker.gwt.client.base.Defaults::asPrimitiveString(Ljava/lang/String;)(rawValue);
				}
				var rawKey = jsObject[i]['key'];
				var finalKey;
				if(rawKey == null)
					finalKey = null;
				else {
					if(typeof rawKey != 'string') {
						var jsonizer = this.@org.jsonmaker.gwt.client.base.HashMapJsonizer::keyJsonizer;
						finalKey = jsonizer.@org.jsonmaker.gwt.client.Jsonizer::asJavaObject(Lcom/google/gwt/core/client/JavaScriptObject;)(Object(rawKey));
					} else
						finalKey = @org.jsonmaker.gwt.client.base.Defaults::asPrimitiveString(Ljava/lang/String;)(rawKey);
				}
				map.@java.util.Map::put(Ljava/lang/Object;Ljava/lang/Object;)(finalKey, finalValue);
			}
		}
		return map;
	}-*/;
	
	public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException {
		if(!Utils.isObject(jsValue))
			throw new JsonizerException();
		return storeMap(jsValue);
	}
	
	public String asString(Object javaValue) throws JsonizerException {
		Map map = (Map)javaValue;
		Iterator it = map.entrySet().iterator();
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		while(it.hasNext()){
			Map.Entry entry = (Map.Entry)it.next();
			buffer.append("{key:");
			buffer.append(keyJsonizer.asString(entry.getKey()));
			buffer.append(",value:");
			buffer.append(valueJsonizer.asString(entry.getValue()));
			buffer.append("}");
			if(it.hasNext()){
				buffer.append(',');
			}
		}
		buffer.append("]");
		return buffer.toString();
	}

}
