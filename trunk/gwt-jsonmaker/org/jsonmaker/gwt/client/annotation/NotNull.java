/* Licensed under the Apache License, Version 2.0 (the "License"); you may not
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