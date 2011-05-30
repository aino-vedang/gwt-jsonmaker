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

import java.util.LinkedHashMap;
import java.util.Map;

import org.jsonmaker.gwt.client.Jsonizer;

/**
 * Translates objects of class java.util.HashMap.
 * 
 * @author Gaurav Saxena<gsaxena81@gmail.com> 
 * Credited to Andrés Adolfo Testi
 *
 */
public class LinkedHashMapJsonizer extends HashMapJsonizer{
	
	/**
	 * Constructs a new HashMapJsonizer.
	 * @param elemJsonizer Jsonizer for inner type.
	 */
	public LinkedHashMapJsonizer(Jsonizer elemJsonizer){
		super(elemJsonizer);
	}
		
	protected Map createMap(){
		return new LinkedHashMap();
	}
}
