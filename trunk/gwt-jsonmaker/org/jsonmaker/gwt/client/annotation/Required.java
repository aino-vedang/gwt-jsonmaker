package org.jsonmaker.gwt.client.annotation;

import org.jsonmaker.gwt.client.JsonizerException;

/**
 * @author Stefan Asseg <stefan.asseg@gmail.com>
 * Marker interface which is used to notify that a property is required in json while desrialization.
 * If the annotated property is not found, gwt-jsonmaker throws {@link JsonizerException}  
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Required {
}