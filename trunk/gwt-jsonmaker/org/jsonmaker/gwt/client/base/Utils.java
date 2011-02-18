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

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Utility class with static methods.
 * @author Gaurav Saxena 
 * Credited to Andrés Adolfo Testi
 *
 */
public class Utils {
	
	/**
	 * Determines if a JavaScript value is null.
	 * 
	 * @param jsValue a JavaScript value to be tested.
	 * @return true if jsValue is null, false in other case.
	 */
	public static native boolean isNull(JavaScriptObject jsValue) /*-{
		return jsValue == null;
	}-*/;

	/**
	 * Determines if a JavaScript value is a JavaScript Array.
	 * 
	 * @param jsValue a JavaScript value to be tested.
	 * @return true if jsValue is a JavaScript Array, false in other case.
	 */
	public native static boolean isArray(JavaScriptObject jsValue) /*-{
		return (jsValue instanceof Array) || ((typeof jsValue)=="object");
					
	}-*/;

	/**
	 * Determines if a JavaScript value is a JavaScript Object.
	 * 
	 * @param jsValue a JavaScript value to be tested.
	 * @return true if jsValue is a JavaScript Object, false in other case.
	 */
	public native static boolean isObject(JavaScriptObject jsValue) /*-{
		return (jsValue instanceof Object) || ((typeof jsValue)=="object");	
	}-*/;
	
	private static JavaScriptObject escapeTable = initEscapeTable();

	private static native String escapeChar(String c) /*-{
		var escapeTable = @org.jsonmaker.gwt.client.base.Utils::escapeTable;
		var lookedUp = escapeTable[c.charCodeAt(0)];
	    return (lookedUp == null) ? c : lookedUp;
	}-*/;

	private static native JavaScriptObject initEscapeTable() /*-{
		var out = [
	      "\\u0000", "\\u0001", "\\u0002", "\\u0003", "\\u0004", "\\u0005",
	      "\\u0006", "\\u0007", "\\b", "\\t", "\\n", "\\u000B",
	      "\\f", "\\r", "\\u000E", "\\u000F", "\\u0010", "\\u0011",
	      "\\u0012", "\\u0013", "\\u0014", "\\u0015", "\\u0016", "\\u0017",
	      "\\u0018", "\\u0019", "\\u001A", "\\u001B", "\\u001C", "\\u001D",
	      "\\u001E", "\\u001F"];
	    out[34] = '\\"';
	    out[92] = '\\\\';
	    return out;
	}-*/;
	  
	/**
	 * Escapes strings to JSON format.
	 * @param toEscape a string to be escaped.
	 * @return the string escaped.
	 */
	public static native String escapeValue(String toEscape) /*-{
    	var s = null;
    	if(toEscape != null) 
    		s = toEscape.replace(/[\x00-\x1F"\\]/g, function(x) {
	    		return @org.jsonmaker.gwt.client.base.Utils::escapeChar(Ljava/lang/String;)(x);
	    	});
	    return "\"" + s + "\"";
	}-*/;
		
}
