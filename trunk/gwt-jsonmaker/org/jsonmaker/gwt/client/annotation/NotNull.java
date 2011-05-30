package org.jsonmaker.gwt.client.annotation;

import org.jsonmaker.gwt.client.JsonizerException;

/**
 * @author Stefan Asseg <stefan.asseg@gmail.com>
 * Marker interface which is used to notify that the field cannot be null during serialization. gwt-jsonmaker
 * throws {@link JsonizerException} if this field is found null. This annotation is used on the getter.
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface NotNull {
}