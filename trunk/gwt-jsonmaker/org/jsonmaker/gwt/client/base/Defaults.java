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

import java.util.Date;

import org.jsonmaker.gwt.client.Jsonizer;
import org.jsonmaker.gwt.client.JsonizerException;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Contains:
 * 	Static instances of Jsonizers for basic types contained in java.lang package.
 *	Static instances of Jsonizers for arrays of primitive types.
 *	Methods to translate JavaScript values to Java primitive types.
 * 
 * @author Gaurav Saxena<gsaxena81@gmail.com> 
 * Credited to Andrés Adolfo Testi
 *
 */
public class Defaults {
	
	private static void throwJsonizerException() throws JsonizerException{
		throw new JsonizerException();
	}	
	
	/**
	 * Jsonizer for java.util.Date
	 */
	public static final Jsonizer DATE_JSONIZER = new Jsonizer(){
		
		public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException {
			return new Date((long)asPrimitiveLong(jsValue));
		}	
		
		public String asString(Object javaValue) throws JsonizerException {
			return Long.toString(((Date)javaValue).getTime());			
		}
		
	};	
	
	/**
	 * Translates JavaScript values to java primitive chars.
	 * @param jsValue a JavaScript value to be translated.
	 * @return translated char.
	 * @throws JsonizerException if jsValue doesn't match a java char.
	 */
	public static char asPrimitiveChar(JavaScriptObject jsValue)throws JsonizerException{
		return (char)asPrimitiveLong(jsValue);		
	}

	/**
	 * Translates JavaScript values to java primitive ints.
	 * @param jsValue a JavaScript value to be translated.
	 * @return translated int.
	 * @throws JsonizerException if jsValue doesn't match a java int.
	 */	
	public static int asPrimitiveInt(JavaScriptObject jsValue)throws JsonizerException{
		return (int)asPrimitiveLong(jsValue);
	}
	public static String asPrimitiveString(String jsValue)throws JsonizerException{
		return jsValue;
	}

	/**
	 * Translates JavaScript values to java primitive boolean.
	 * @param jsValue a JavaScript value to be translated.
	 * @return translated boolean.
	 * @throws JsonizerException if jsValue doesn't match a java boolean.
	 */	
	public static native boolean asPrimitiveBoolean(JavaScriptObject jsValue) throws JsonizerException /*-{
		if (jsValue==true) 
		{
			return true;
		}
		return false;

		@org.jsonmaker.gwt.client.base.Defaults::throwJsonizerException()();
	}-*/;
	
	/**
	 * Translates JavaScript values to java primitive double.
	 * @param jsValue a JavaScript value to be translated.
	 * @return translated double.
	 * @throws JsonizerException if jsValue doesn't match a java double.
	 */
	public static native double asPrimitiveDouble(JavaScriptObject jsValue) throws JsonizerException /*-{
		if(jsValue instanceof Number || ((typeof jsValue)=='number'))
			return parseFloat(jsValue);
		@org.jsonmaker.gwt.client.base.Defaults::throwJsonizerException()();
	}-*/;
	
	/**
	 * Translates JavaScript values to java primitive long.
	 * @param jsValue a JavaScript value to be translated.
	 * @return translated long.
	 * @throws JsonizerException if jsValue doesn't match a java long.
	 */
	public static native double asPrimitiveLong(JavaScriptObject jsValue) throws JsonizerException /*-{
		if((jsValue instanceof Number || ((typeof jsValue)=='number'))&& Math.floor(jsValue)==jsValue)
			return parseInt(jsValue);
		@org.jsonmaker.gwt.client.base.Defaults::throwJsonizerException()();
	}-*/;
	
	/**
	 * Jsonizer for java.lang.Character.
	 */
	public static final Jsonizer CHARACTER_JSONIZER = new Jsonizer(){
		public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException {
			return new Character(asPrimitiveChar(jsValue));			
		}
		public String asString(Object javaValue) throws JsonizerException {
			if(javaValue != null)
				return Long.toString((long)((Character)javaValue).charValue());
			else return null;
		}
	};
	
	private static final native String translateString(JavaScriptObject jsValue)/*-{
		if(jsValue instanceof String || ((typeof jsValue)=='String'))
			return jsValue;
		@org.jsonmaker.gwt.client.base.Defaults::throwJsonizerException()();
	}-*/;
	
	/**
	 * Jsonizer for java.lang.String.
	 */
	public static final Jsonizer STRING_JSONIZER = new Jsonizer(){
		
		
		public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException{
			return translateString(jsValue);
		}
		
		public Object asJavaObject(String jsValue) throws JsonizerException{
			return (jsValue);
		}
		
		public String asString(Object javaValue) throws JsonizerException {
			return Utils.escapeValue(javaValue.toString());
		}
		
	};
	
	/**
	 * Jsonizer for java.lang.Double.
	 */
	public static final Jsonizer DOUBLE_JSONIZER = new Jsonizer(){
		public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException {
			return new Double(asPrimitiveDouble(jsValue));
		}
		public String asString(Object javaValue) throws JsonizerException {
			if(javaValue != null)
				return javaValue.toString();
			else return null;
		}		
	};
	
	/**
	 * Jsonizer for java.lang.Float.
	 */
	public static final Jsonizer FLOAT_JSONIZER = new Jsonizer(){
		public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException {
			return new Float(asPrimitiveFloat(jsValue));
		}
		public String asString(Object javaValue) throws JsonizerException {
			if(javaValue != null)
				return javaValue.toString();
			else return null;	
		}
	};

	/**
	 * Jsonizer for java.lang.Long.
	 */
	public static final Jsonizer LONG_JSONIZER = new Jsonizer(){
		public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException {
			return new Long((long)asPrimitiveLong(jsValue));
		}
		public String asString(Object javaValue) throws JsonizerException {
			if(javaValue != null)
				return javaValue.toString();
			else return null;		
		}
		
	};
	
	/**
	 * Jsonizer for java.lang.Short.
	 */
	public static final Jsonizer SHORT_JSONIZER = new Jsonizer(){
		public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException {
			return new Short((short)asPrimitiveLong(jsValue));
		}
		public String asString(Object javaValue) throws JsonizerException {
			if(javaValue != null)
				return javaValue.toString();
			else return null;		
		}		
	};

	/**
	 * Jsonizer for java.lang.Integer.
	 */
	public static final Jsonizer INTEGER_JSONIZER = new Jsonizer(){
		public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException {
			return new Integer((int)asPrimitiveLong(jsValue));
		}
		public String asString(Object javaValue) throws JsonizerException {
			if(javaValue != null)
				return javaValue.toString();
			else return null;
		}
	};

	/**
	 * Jsonizer for java.lang.Byte.
	 */
	public static final Jsonizer BYTE_JSONIZER = new Jsonizer(){
		public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException {
			return new Byte((byte)asPrimitiveLong(jsValue));
		}
		public String asString(Object javaValue) throws JsonizerException {
			return javaValue.toString();			
		}
	};
	
	/**
	 * Translates JavaScript values to java primitive byte.
	 * @param jsValue a JavaScript value to be translated.
	 * @return translated byte.
	 * @throws JsonizerException if jsValue doesn't match a java byte.
	 */
	public static byte asPrimitiveByte(JavaScriptObject jsValue) throws JsonizerException{
		return (byte)asPrimitiveLong(jsValue);		
	}
	
	/**
	 * Translates JavaScript values to java primitive byte.
	 * @param jsValue a JavaScript value to be translated.
	 * @return translated byte.
	 * @throws JsonizerException if jsValue doesn't match a java byte.
	 */
	public static float asPrimitiveFloat(JavaScriptObject jsValue) throws JsonizerException{
		return (float)asPrimitiveDouble(jsValue);		
	}

	/**
	 * Jsonizer for java.lang.Boolean.
	 */
	public static final Jsonizer BOOLEAN_JSONIZER = new Jsonizer(){
		public Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException {
			return asPrimitiveBoolean(jsValue) ? Boolean.TRUE: Boolean.FALSE;
		}
		public String asString(Object javaValue) throws JsonizerException {
			if(javaValue != null)
				return javaValue.toString();
			else return null;		
		}
	};

	/**
	 * Translates JavaScript values to java primitive short.
	 * @param jsValue a JavaScript value to be translated.
	 * @return translated short.
	 * @throws JsonizerException if jsValue doesn't match a java short.
	 */
	public static final short asPrimitiveShort(JavaScriptObject jsValue) throws JsonizerException{
		return (short)asPrimitiveLong(jsValue);		
	};
	
	/**
	 * Jsonizer for int[].
	 */
	public static final Jsonizer A_INT_JSONIZER = new A_IntJsonizer();
	
	private static class A_IntJsonizer implements Jsonizer{			
			
		public native Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException /*-{
			if(!(jsValue instanceof Array))
				@org.jsonmaker.gwt.client.base.Defaults::throwJsonizerException()();
					
			var javaArray = @org.jsonmaker.gwt.client.base.Defaults.A_IntJsonizer::createArray(I)(jsValue.length);
				
			var i;
			for( var i=0, len=jsValue.length; i<len; i++ ){		
				@org.jsonmaker.gwt.client.base.Defaults.A_IntJsonizer::storeValue([IILcom/google/gwt/core/client/JavaScriptObject;)(javaArray, parseInt(i), Object(jsValue[i]));
			}			
				
			return javaArray;
		}-*/;
		
		public String asString(Object javaValue) throws JsonizerException {
			if(javaValue != null)
			{
				int[] array = (int[])javaValue;
				StringBuffer buffer = new StringBuffer();
				buffer.append('[');
				int top = array.length - 1;
				for(int i = 0; i < array.length; i++){
					buffer.append(array[i]);
					if(i < top)
						buffer.append(',');
				}
				buffer.append(']');
				return buffer.toString();
			}
			else return "null";
		}
		
		private static void storeValue(int[] array, int index, JavaScriptObject jsValue) throws JsonizerException{
			array[index] = asPrimitiveInt(jsValue);
		}
		
		private static int[] createArray(int size){
			return new int[size];
		}
		
	}
	
	/**
	 * Jsonizer for long[].
	 */
	public static final Jsonizer A_LONG_JSONIZER = new A_LongJsonizer();
	
	private static class A_LongJsonizer implements Jsonizer{			
		
		public native Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException /*-{
			if(!(jsValue instanceof Array))
				@org.jsonmaker.gwt.client.base.Defaults::throwJsonizerException()();
					
			var javaArray = @org.jsonmaker.gwt.client.base.Defaults.A_LongJsonizer::createArray(I)(jsValue.length);
				
			for( var i=0, len=jsValue.length; i<len; i++ ){
				@org.jsonmaker.gwt.client.base.Defaults.A_LongJsonizer::storeValue([JILcom/google/gwt/core/client/JavaScriptObject;)(javaArray, parseInt(i), Object(jsValue[i]));
			}			
				
			return javaArray;
		}-*/;
		
		public String asString(Object javaValue) throws JsonizerException {
			long[] array = (long[])javaValue;
			StringBuffer buffer = new StringBuffer();
			buffer.append('[');
			int top = array.length - 1;
			for(int i = 0; i < array.length; i++){
				buffer.append(array[i]);
				if(i < top)
					buffer.append(',');
			}
			buffer.append(']');
			return buffer.toString();
		}
		
		private static void storeValue(long[] array, int index, JavaScriptObject jsValue) throws JsonizerException{
			array[index] = (long) asPrimitiveLong(jsValue);
		}
		
		private static long[] createArray(int size){
			return new long[size];
		}
		
	}

	/**
	 * Jsonizer for float[].
	 */
	public static final Jsonizer A_FLOAT_JSONIZER = new A_FloatJsonizer();
	
	private static class A_FloatJsonizer implements Jsonizer{			
		
		public native Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException /*-{
			if(!(jsValue instanceof Array))
				@org.jsonmaker.gwt.client.base.Defaults::throwJsonizerException()();
					
			var javaArray = @org.jsonmaker.gwt.client.base.Defaults.A_FloatJsonizer::createArray(I)(jsValue.length);
				
			for( var i=0, len=jsValue.length; i<len; i++ ){
				@org.jsonmaker.gwt.client.base.Defaults.A_FloatJsonizer::storeValue([FILcom/google/gwt/core/client/JavaScriptObject;)(javaArray, parseInt(i), Object(jsValue[i]));
			}			
				
			return javaArray;
		}-*/;
		
		public String asString(Object javaValue) throws JsonizerException {
			float[] array = (float[])javaValue;
			StringBuffer buffer = new StringBuffer();
			buffer.append('[');
			int top = array.length - 1;
			for(int i = 0; i < array.length; i++){
				buffer.append(array[i]);
				if(i < top)
					buffer.append(',');
			}
			buffer.append(']');
			return buffer.toString();
		}
		
		private static void storeValue(float[] array, int index, JavaScriptObject jsValue) throws JsonizerException{
			array[index] = asPrimitiveFloat(jsValue);
		}
		
		private static float[] createArray(int size){
			return new float[size];
		}
		
	}

	/**
	 * Jsonizer for double[].
	 */
	public static final Jsonizer A_DOUBLE_JSONIZER = new A_DoubleJsonizer();
	
	private static class A_DoubleJsonizer implements Jsonizer{			
		
		public native Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException /*-{
			if(!(jsValue instanceof Array))
				@org.jsonmaker.gwt.client.base.Defaults::throwJsonizerException()();
					
			var javaArray = @org.jsonmaker.gwt.client.base.Defaults.A_DoubleJsonizer::createArray(I)(jsValue.length);
				
			for( var i=0, len=jsValue.length; i<len; i++ ){
				@org.jsonmaker.gwt.client.base.Defaults.A_DoubleJsonizer::storeValue([DILcom/google/gwt/core/client/JavaScriptObject;)(javaArray, parseInt(i), Object(jsValue[i]));
			}			
				
			return javaArray;
		}-*/;
		
		public String asString(Object javaValue) throws JsonizerException {
			double[] array = (double[])javaValue;
			StringBuffer buffer = new StringBuffer();
			buffer.append('[');
			int top = array.length - 1;
			for(int i = 0; i < array.length; i++){
				buffer.append(array[i]);
				if(i < top)
					buffer.append(',');
			}
			buffer.append(']');
			return buffer.toString();
		}
		
		private static void storeValue(double[] array, int index, JavaScriptObject jsValue) throws JsonizerException{
			array[index] = asPrimitiveDouble(jsValue);
		}
		
		private static double[] createArray(int size){
			return new double[size];
		}
		
	}

	/**
	 * Jsonizer for byte[].
	 */
	public static final Jsonizer A_BYTE_JSONIZER = new A_ByteJsonizer();
	
	private static class A_ByteJsonizer implements Jsonizer{			

		public String asString(Object javaValue) throws JsonizerException {
			byte[] array = (byte[])javaValue;
			StringBuffer buffer = new StringBuffer();
			buffer.append('[');
			int top = array.length - 1;
			for(int i = 0; i < array.length; i++){
				buffer.append(array[i]);
				if(i < top)
					buffer.append(',');
			}
			buffer.append(']');
			return buffer.toString();
		}
		
		public native Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException /*-{
			if(!(jsValue instanceof Array))
				@org.jsonmaker.gwt.client.base.Defaults::throwJsonizerException()();
					
			var javaArray = @org.jsonmaker.gwt.client.base.Defaults.A_ByteJsonizer::createArray(I)(jsValue.length);
				
			for( var i=0, len=jsValue.length; i<len; i++ ){
				@org.jsonmaker.gwt.client.base.Defaults.A_ByteJsonizer::storeValue([BILcom/google/gwt/core/client/JavaScriptObject;)(javaArray, parseInt(i), Object(jsValue[i]));
			}			
				
			return javaArray;
		}-*/;
		
		private static void storeValue(byte[] array, int index, JavaScriptObject jsValue) throws JsonizerException{
			array[index] = asPrimitiveByte(jsValue);
		}
		
		private static byte[] createArray(int size){
			return new byte[size];
		}
		
	}
	
	/**
	 * Jsonizer for char[].
	 */
	public static final Jsonizer A_CHAR_JSONIZER = new A_CharJsonizer();
	
	private static class A_CharJsonizer implements Jsonizer{			

		public String asString(Object javaValue) throws JsonizerException {
			char[] array = (char[])javaValue;
			StringBuffer buffer = new StringBuffer();
			buffer.append('[');
			int top = array.length - 1;
			for(int i = 0; i < array.length; i++){
				buffer.append((int)array[i]);
				if(i < top)
					buffer.append(',');
			}
			buffer.append(']');
			return buffer.toString();
		}
		
		public native Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException /*-{
			if(!(jsValue instanceof Array))
				@org.jsonmaker.gwt.client.base.Defaults::throwJsonizerException()();
					
			var javaArray = @org.jsonmaker.gwt.client.base.Defaults.A_CharJsonizer::createArray(I)(jsValue.length);
				
			for( var i=0, len=jsValue.length; i<len; i++ ){
				@org.jsonmaker.gwt.client.base.Defaults.A_CharJsonizer::storeValue([CILcom/google/gwt/core/client/JavaScriptObject;)(javaArray, parseInt(i), Object(jsValue[i]));
			}			
				
			return javaArray;
		}-*/;
		
		@SuppressWarnings("unused")//used in native function
		private static void storeValue(char[] array, int index, JavaScriptObject jsValue) throws JsonizerException{
			array[index] = asPrimitiveChar(jsValue);
		}
		@SuppressWarnings("unused")//used in native function
		private static char[] createArray(int size){
			return new char[size];
		}
		
	}
	
	/**
	 * Jsonizer for short[].
	 */
	public static final Jsonizer A_SHORT_JSONIZER = new A_ShortJsonizer();
	
	private static class A_ShortJsonizer implements Jsonizer{			

		public String asString(Object javaValue) throws JsonizerException {
			short[] array = (short[])javaValue;
			StringBuffer buffer = new StringBuffer();
			buffer.append('[');
			int top = array.length - 1;
			for(int i = 0; i < array.length; i++){
				buffer.append(array[i]);
				if(i < top)
					buffer.append(',');
			}
			buffer.append(']');
			return buffer.toString();
		}
		
		public native Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException /*-{
			if(!(jsValue instanceof Array))
				@org.jsonmaker.gwt.client.base.Defaults::throwJsonizerException()();
					
			var javaArray = @org.jsonmaker.gwt.client.base.Defaults.A_ShortJsonizer::createArray(I)(jsValue.length);
				
			for( var i=0, len=jsValue.length; i<len; i++ ){
				@org.jsonmaker.gwt.client.base.Defaults.A_ShortJsonizer::storeValue([SILcom/google/gwt/core/client/JavaScriptObject;)(javaArray, parseInt(i), Object(jsValue[i]));
			}			
				
			return javaArray;
		}-*/;
		
		private static void storeValue(short[] array, int index, JavaScriptObject jsValue) throws JsonizerException{
			array[index] = asPrimitiveShort(jsValue);
		}
		
		private static short[] createArray(int size){
			return new short[size];
		}
		
	}	

	/**
	 * Jsonizer for boolean[].
	 */
	public static final Jsonizer A_BOOLEAN_JSONIZER = new A_BooleanJsonizer();
	
	private static class A_BooleanJsonizer implements Jsonizer{			
		
		public native Object asJavaObject(JavaScriptObject jsValue) throws JsonizerException /*-{
			if(!(jsValue instanceof Array))
				@org.jsonmaker.gwt.client.base.Defaults::throwJsonizerException()();
					
			var javaArray = @org.jsonmaker.gwt.client.base.Defaults.A_BooleanJsonizer::createArray(I)(jsValue.length);	
			for( var i=0, len=jsValue.length; i<len; i++ ){
				@org.jsonmaker.gwt.client.base.Defaults.A_BooleanJsonizer::storeValue([ZILcom/google/gwt/core/client/JavaScriptObject;)(javaArray, parseInt(i), Object(jsValue[i]));
			}			
				
			return javaArray;
		}-*/;
		
		public String asString(Object javaValue) throws JsonizerException {
			boolean[] array = (boolean[])javaValue;
			StringBuffer buffer = new StringBuffer();
			buffer.append('[');
			int top = array.length - 1;
			for(int i = 0; i < array.length; i++){
				buffer.append(array[i]);
				if(i < top)
					buffer.append(',');
			}
			buffer.append(']');
			return buffer.toString();
		}
				
		private static void storeValue(boolean[] array, int index, JavaScriptObject jsValue) throws JsonizerException{
			array[index] = asPrimitiveBoolean(jsValue);
		}
		
		private static boolean[] createArray(int size){
			return new boolean[size];
		}
		
	}
		
}
