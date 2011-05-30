package org.jsonmaker.gwt.client.annotation;

/**
 * @author Stefan Asseg <stefan.asseg@gmail.com>
 * Used to provide alias for default property name during serialization. This annotation is used on getter
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface PropName {
  java.lang.String value();
}