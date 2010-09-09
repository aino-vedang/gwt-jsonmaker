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

import java.util.Collection;
import java.util.Iterator;

import org.jsonmaker.gwt.client.Jsonizer;
import org.jsonmaker.gwt.client.JsonizerException;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Root class of Jsonizers for collections.
 * 
 * @author Gaurav Saxena 
 * Credited to Andrés Adolfo Testi
 *
 */
public abstract class CollectionJsonizer implements Jsonizer {
	
	private Jsonizer elemJsonizer;
	
	
	protected CollectionJsonizer(Jsonizer elemJsonizer){
		this.elemJsonizer = elemJsonizer;
	}
	
	protected abstract Collection createCollection(); 
	
	private native Collection storeCollection(JavaScriptObject jsArray) throws JsonizerException/*-{
		var javaArray = this.@org.jsonmaker.gwt.client.base.CollectionJsonizer::createCollection()();
		var elemJsonizer = this.@org.jsonmaker.gwt.client.base.CollectionJsonizer::elemJsonizer;
		//GS - jsArray is an array and here it was accessed as an object. IE and FF adjusted to this conversion 
		//but the correct way to access jsArray is to access it as an array. Chrome could not digest this.
		//for(var i in jsArray){
		for(var i = 0; i < jsArray.length; i++){
			var rawValue = jsArray[i];
			var finalValue;
			if(rawValue == null){
				finalValue = null;
			}else{
				if(typeof rawValue != 'string')
					finalValue = elemJsonizer.@org.jsonmaker.gwt.client.Jsonizer::asJavaObject(Lcom/google/gwt/core/client/JavaScriptObject;)(Object(rawValue));
				else
					finalValue = @org.jsonmaker.gwt.client.base.Defaults::asPrimitiveString(Ljava/lang/String;)(rawValue);//gaurav
			}
			javaArray.@java.util.Collection::add(Ljava/lang/Object;)(finalValue);
		}
		return javaArray;
	}-*/;

	public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException {
		if(!Utils.isArray(jsValue))
			throw new JsonizerException();
		return storeCollection(jsValue);
	}
	
	public String asString(Object javaValue) throws JsonizerException {
		StringBuffer buffer = new StringBuffer();
		Iterator it = ((Collection)javaValue).iterator();
		buffer.append('[');
		while(it.hasNext()){
			buffer.append(elemJsonizer.asString(it.next()));
			if(it.hasNext())
				buffer.append(',');
		}
		buffer.append(']');			
		return buffer.toString();
	}
	
}
