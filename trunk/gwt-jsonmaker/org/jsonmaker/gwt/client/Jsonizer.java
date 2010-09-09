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
package org.jsonmaker.gwt.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * An interface to be implemented automatically by the Jsonizer API,
 * to encode and decode Java Objects to JSON representations. It's 
 * possible too, to implement user defined Jsonizers. 
 * 
 * @author Gaurav Saxena 
 * Credited to Andrés Adolfo Testi
 *
 */
public interface Jsonizer {

	/**
	 * Translates a JavaScript value in JSON format to a
	 * Java Object.
	 * 
	 * @param jsValue a raw JSON value to be translated.
	 * @return a JavaObject translated from jsValue.
	 * @throws JsonizerException when occurs a convertion constraint.
	 */
	public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException;
	
	/**
	 * Translates a Java Object to an String representing a 
	 * JavaScript value in JSON format.
	 * 
	 * @param javaValue a Java Object to be translated.
	 * @return a String representing the JSON translation from javaValue.
	 * @throws JsonizerException if occurs a convertion constraint.
	 */
	public String asString(Object javaValue) throws JsonizerException;

}
