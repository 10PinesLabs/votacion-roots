package convention.transformers;

import com.google.inject.Inject;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.conversion.TypeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;
import net.sf.kfgodel.dgarcia.lang.reflection.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListToListConverter implements SpecializedTypeConverter<List, List> {

    @Inject
    private TypeConverter baseConverter;

    @Override
    public List convertTo(Type expectedType, List sourceObject, Annotation[] contextAnnotations)
        throws CannotConvertException {
        Type elementType = ReflectionUtils.getElementTypeParameterFrom(expectedType);
        ArrayList<Object> list = new ArrayList<>(sourceObject.size());
        for (Object element : sourceObject) {
            list.add(baseConverter.convertValue(element, elementType, contextAnnotations));
        }
        return list;
    }
}
