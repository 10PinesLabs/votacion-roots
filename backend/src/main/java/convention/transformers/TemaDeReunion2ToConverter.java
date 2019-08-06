package convention.transformers;

import com.google.inject.Inject;
import convention.persistent.TemaDeReunion;
import convention.persistent.TemaDeReunionConDescripcion;
import convention.persistent.TemaParaProponerPinosARoot;
import convention.rest.api.tos.TemaDeReunionConDescripcionTo;
import convention.rest.api.tos.TemaDeReunionTo;
import convention.rest.api.tos.TemaParaProponerPinosARootTo;
import net.sf.kfgodel.bean2bean.conversion.GeneralTypeConverter;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.conversion.TypeConverter;
import net.sf.kfgodel.bean2bean.conversion.converters.AnnotatedClassConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TemaDeReunion2ToConverter implements SpecializedTypeConverter<TemaDeReunion, TemaDeReunionTo> {
    @Inject
    private TypeConverter baseConverter;

    @Override
    public TemaDeReunionTo convertTo(Type expectedType, TemaDeReunion sourceObject, Annotation[] contextAnnotations) throws CannotConvertException {
        GeneralTypeConverter<Object, Object> annotatedClassConverter = annotatedClassConverter();
        return (TemaDeReunionTo) annotatedClassConverter.convertFrom(sourceObject, toClass(sourceObject.getClass()), null);
    }

    private Type toClass(Class<? extends TemaDeReunion> aClass) {
        return toClass.get(aClass);
    }

    private GeneralTypeConverter<Object, Object> annotatedClassConverter() {
        return baseConverter.getGeneralConverterByName(AnnotatedClassConverter.class.getName());
    }

    private Map<Type, Type> toClass = new HashMap<Type, Type>() {{
        put(TemaDeReunionConDescripcion.class, TemaDeReunionConDescripcionTo.class);
        put(TemaParaProponerPinosARoot.class, TemaParaProponerPinosARootTo.class);
    }};
}
