package com.shopiroller.util.validation;

import android.content.Context;

import com.shopiroller.util.validation.annotations.LocalizedName;
import com.shopiroller.util.validation.annotations.Required;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class Validator {


    public static boolean validateForNulls(Object objectToValidate, Context context)
            throws RequiredFieldException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {

        /**
         *  Get all the fields of the class
         */
        Field[] declaredFields = objectToValidate.getClass().getDeclaredFields();

        /**
         *  Iterate over each field to check if that field
         *  has the "Required" annotation declared for it or not
         */
        for (Field field : declaredFields) {

            Annotation annotation = field.getAnnotation(Required.class);

            /**
             *  Check if the annotation is present on that field
             */
            if (annotation != null) {

                Required required = (Required) annotation;

                /**
                 *  Check if it says this field is required
                 */
                if (required.value()) {
                    /**
                     *  Now we make sure we can access the private
                     *  fields also, so we need to call this method also
                     *  other wise we would get a {@link java.lang.IllegalAccessException}
                     */
                    field.setAccessible(true);
                    /**
                     *  If this field is required, then it should be present
                     *  in the declared fields array, if it is throw the
                     *  {@link RequiredFieldException}
                     */
                    if (field.get(objectToValidate) == null) {
                        Annotation localizedNameAnnotation = field.getAnnotation(LocalizedName.class);
                        if (localizedNameAnnotation != null) {
                            LocalizedName customAnnotation = (LocalizedName) localizedNameAnnotation;
                            throw new RequiredFieldException(customAnnotation.value(), context);
                        } else
                            throw new RequiredFieldException(field.getName());
                    }
                }
            }
        }
        return true;
    }

}
