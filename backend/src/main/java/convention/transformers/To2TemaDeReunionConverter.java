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

public class To2TemaDeReunionConverter implements SpecializedTypeConverter<TemaDeReunionTo, TemaDeReunion> {
    @Inject
    private TypeConverter baseConverter;

    @Override
    public TemaDeReunion convertTo(Type expectedType, TemaDeReunionTo sourceObject, Annotation[] contextAnnotations)
            throws CannotConvertException {
        return (TemaDeReunion) annotatedClassConverter()
                .convertTo(domainClass(sourceObject.getClass()), sourceObject, null);
    }

    private GeneralTypeConverter<Object, Object> annotatedClassConverter() {
        return baseConverter.getGeneralConverterByName(AnnotatedClassConverter.class.getName());
    }

    private Type domainClass(Class<? extends TemaDeReunionTo> toType) {
        return domainClass.get(toType);
    }

    private Map<Type, Type> domainClass = new HashMap<Type, Type>() {{
        put(TemaDeReunionConDescripcionTo.class, TemaDeReunionConDescripcion.class);
        put(TemaParaProponerPinosARootTo.class, TemaParaProponerPinosARoot.class);
    }};
}
