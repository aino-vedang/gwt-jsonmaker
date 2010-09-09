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
package org.juglar.gwt.jsonizer.client.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.juglar.gwt.jsonizer.client.Jsonizer;
import org.juglar.gwt.jsonizer.client.JsonizerException;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Used internally by the Jsonizer API.
 * 
 * @author Andr�s Adolfo Testi
 *
 */
public abstract class BeanJsonizer implements Jsonizer {
	
	public BeanJsonizer(){
		setterPool = createSetterPool();
		requiredProperties = createRequiredProperties();
	}
	
	protected abstract Object createBean();
	
	private JavaScriptObject setterPool;
	private JavaScriptObject requiredProperties;
	
	protected abstract JavaScriptObject createSetterPool();
	
	protected abstract void storeStringList(Object javaValue, List bufferList) throws JsonizerException;
	
	public String asString(Object javaValue) throws JsonizerException{
		BeanJsonizer jsonizer = this;
		ArrayList list = new ArrayList();
		StringBuffer buffer = new StringBuffer();
		buffer.append('{');
		do{
			jsonizer.storeStringList(javaValue, list);				
			jsonizer = jsonizer.getSuperJsonizer();			
		}while(jsonizer!=null);
		
		Iterator it = list.iterator();
		while(it.hasNext()){
			buffer.append((String)it.next());
			if(it.hasNext())
				buffer.append(',');
		}		
		buffer.append('}');
		return buffer.toString();
	}
	
	private native JavaScriptObject setProperties(Object bean, JavaScriptObject jsValue) throws JsonizerException /*-{
		var prop;
		var setterPool = this.@org.juglar.gwt.jsonizer.client.base.BeanJsonizer::setterPool;
		var remain = {};
		for(prop in jsValue){
			var setProp = setterPool[prop];
			if(setProp == undefined){
				remain[prop] = jsValue[prop];
			}else if(typeof setProp == 'function'){
				setProp(bean, jsValue[prop]);
			}
		}
		return remain;
	}-*/;
	
	private native boolean containsRequiredProperties(JavaScriptObject jsValue)/*-{
		var requiredProperties = this.@org.juglar.gwt.jsonizer.client.base.BeanJsonizer::requiredProperties;
		//var prop;
		//GS - requriedProperties is an array and here it was accessed as an object. IE and FF did not find any properties
		//assigned to an array but chrome did find some properties. The correct way to access requiredProperties is to access
		//it as an array.
			//for(prop in requiredProperties){
		for(var i = 0; i < requiredProperties.length; i++){
			if(jsValue[requiredProperties[i]] == undefined){
				return false;
			}
		}
		return true;
	}-*/;
	
	public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException {
		if(Utils.isNull(jsValue) || !Utils.isObject(jsValue))
			throw new JsonizerException();
		Object bean = createBean();
		BeanJsonizer jsonizer = this;
		JavaScriptObject values = jsValue;
		do{
			if(!jsonizer.containsRequiredProperties(values))
				throw new JsonizerException();
			values = jsonizer.setProperties(bean, values);
			jsonizer = jsonizer.getSuperJsonizer();
		}while(jsonizer!=null);
		return bean;
	}	

	protected abstract BeanJsonizer getSuperJsonizer();
	protected abstract JavaScriptObject createRequiredProperties();	
	
}
