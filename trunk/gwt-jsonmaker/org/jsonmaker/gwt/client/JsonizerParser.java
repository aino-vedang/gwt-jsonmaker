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

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * A singleton class, that parses a string with JSON format and evaluates it. 
 * 
 * @author Gaurav Saxena<gsaxena81@gmail.com>
 * @author Stefan Asseg <stefan.asseg@gmail.com>
 * Credited to Andrés Adolfo Testi
 *
 */
public final class JsonizerParser {

	private JsonizerParser(){}
	
	/**
	 * Parses a string with JSON format and evaluates it. 
	 * 
	 * @param jsonizer a jsonizer used to translate the evaluated JavaScript value
	 * @param jsonString a string with data in JSON format
	 * @return translated Java Object
	 * @throws JsonizerException throwed if occurs an error in translation
	 */
	public static Object parse(Jsonizer jsonizer, String jsonString) throws JsonizerException {

		if (jsonString == null) {
			throw new NullPointerException();
		}
		else if (jsonString.isEmpty()) {
			throw new IllegalArgumentException("empty argument");
		}
		try {
			return jsonizer.asJavaObject(evaluate(jsonString));
		} catch (JavaScriptException ex) {
			throw new JsonizerException(ex);
		}
		
	}
	
	public static native JavaScriptObject evaluate(String jsonString) /*-{
		var x = eval('(' + jsonString + ')');
	 	if (typeof x == 'number' || typeof x == 'string' || typeof x == 'array' || typeof x == 'boolean') {
	 		x = (Object(x));
	 	}
	 	return x;
	}-*/;
	
}
