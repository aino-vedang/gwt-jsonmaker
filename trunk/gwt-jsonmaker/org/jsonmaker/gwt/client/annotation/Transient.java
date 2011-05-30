package org.jsonmaker.gwt.client.annotation;

/**
 * @author Stefan Asseg <stefan.asseg@gmail.com>
 * Marker interface which is used to notify that the annotated field need not be serialized. This needs to be
 * used with the getter
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Transient {
}