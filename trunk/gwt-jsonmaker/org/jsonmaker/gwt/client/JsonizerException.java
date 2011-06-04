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

/**
 * An exception that can be thrown when a JSON translation is
 * executed.
 * 
 * @author Andrés Testi
 *
 */
@SuppressWarnings("serial")
public class JsonizerException extends RuntimeException{
	
	/**
	 * Constructs a new JsonizerException.
	 *
	 */
	public JsonizerException(){}
	
	/**
	 * Constructs a new JsonizerException.
	 * @param msg a message
	 */
	public JsonizerException(String msg){
		super(msg);
	}

	/**
	 * Constructs a new JsonizerException.
	 * @param cause the exception cause
	 */
	public JsonizerException(Throwable cause){
		super(cause);
	}
	
	/**
	 * Constructs a new JsonizerException.
	 * @param msg a message
	 * @param cause the exception cause
	 */
	public JsonizerException(String msg, Throwable cause){
		super(msg, cause);
	}
	
	
}
