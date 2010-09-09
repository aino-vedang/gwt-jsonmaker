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

import org.jsonmaker.gwt.client.Jsonizer;
import org.jsonmaker.gwt.client.JsonizerException;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Translates arrays of objects. Must be subclassed to 
 * implement the createArray method, to avoid ClassCastExceptions
 * in hosted mode. 
 * 
 * @author Gaurav Saxena 
 * Credited to Andrés Adolfo Testi
 *
 */
public abstract class ArrayJsonizer implements Jsonizer{
	
	private Jsonizer elemJsonizer;
	
	/**
	 * Constructs with a particular  
	 * 
	 * @param elemJsonizer Jsonizer for the inner element type.
	 */
	public ArrayJsonizer(Jsonizer elemJsonizer){
		this.elemJsonizer = elemJsonizer;		
	}
	
	/**
	 * Subclasses must implement this method to construct arrays of particular types to
	 * avoid ClassCastExceptions in hosted mode.
	 * 
	 * @param size size of the array.
	 * @return a new array of type associated with the element Jsonizer.
	 */
	protected abstract Object[] createArray(int size);
	
	private native Object[] storeArray(JavaScriptObject jsArray) throws JsonizerException/*-{
		var javaArray = this.@org.jsonmaker.gwt.client.base.ArrayJsonizer::createArray(I)(jsArray.length);
		var elemJsonizer = this.@org.jsonmaker.gwt.client.base.ArrayJsonizer::elemJsonizer;//gaurav
		for(var i = 0; i < jsArray.length; i++){//gaurav
			var value = jsArray[i];
			var finalValue;//gaurav
			if(value == null)
				finalValue = null;
			else
			{
				if(typeof value != 'string')
					finalValue = elemJsonizer.@org.jsonmaker.gwt.client.Jsonizer::asJavaObject(Lcom/google/gwt/core/client/JavaScriptObject;)(Object(value));
				else
					finalValue = @org.jsonmaker.gwt.client.base.Defaults::asPrimitiveString(Ljava/lang/String;)(value);
			}
			this.@org.jsonmaker.gwt.client.base.ArrayJsonizer::storeValue([Ljava/lang/Object;ILjava/lang/Object;)(javaArray, i, finalValue);
		}
		return javaArray;
	}-*/;
	
	private void storeValue(Object[] array, int index, Object jsValue) throws JsonizerException{
		array[index] = jsValue; 
			//elemJsonizer.asJavaObject(jsValue);
	}
	
	public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException{
		if(!Utils.isArray(jsValue)) 
			throw new JsonizerException();
		return storeArray(jsValue);
	}
	
	public String asString(Object javaValue) throws JsonizerException{
		Object[] array = (Object[])javaValue;
		StringBuffer buffer = new StringBuffer();
		buffer.append('[');
		int top = array.length - 1;
		for(int i = 0; i <array.length; i++){
			buffer.append(elemJsonizer.asString(array[i]));
			if(i<top)
				buffer.append(',');
		}
		buffer.append(']');
		return buffer.toString();
	}	
	
}
