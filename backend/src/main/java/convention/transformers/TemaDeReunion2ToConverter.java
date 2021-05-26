package convention.transformers;

import com.google.inject.Inject;
import convention.persistent.TemaDeReunion;
import convention.persistent.TemaDeReunionConDescripcion;
import convention.persistent.TemaParaProponerPinosARoot;
import convention.persistent.TemaParaRepasarActionItems;
import convention.rest.api.tos.*;
import net.sf.kfgodel.bean2bean.conversion.GeneralTypeConverter;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.conversion.TypeConverter;
import net.sf.kfgodel.bean2bean.conversion.converters.AnnotatedClassConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TemaDeReunion2ToConverter implements SpecializedTypeConverter<TemaDeReunion, TemaDeReunionTo> {

    @Inject
    private TypeConverter baseConverter;

    @Override
    public TemaDeReunionTo convertTo(Type expectedType, TemaDeReunion sourceObject, Annotation[] contextAnnotations)
        throws CannotConvertException {
        return (TemaDeReunionTo) annotatedClassConverter()
            .convertFrom(sourceObject, toClass(sourceObject.getClass(), contextAnnotations), null);
    }

    private Type toClass(Class<? extends TemaDeReunion> expectedClass, Annotation[] contextAnnotations) {
        return esTemaParaRepasarActionItemsARepasar(expectedClass, contextAnnotations)
            ? TemaParaRepasarActionItemsSinTemasParaRepasarTo.class
            : toClass.get(expectedClass);
    }

    private GeneralTypeConverter<Object, Object> annotatedClassConverter() {
        return baseConverter.getGeneralConverterByName(AnnotatedClassConverter.class.getName());
    }

    private boolean esTemaParaRepasarActionItemsARepasar(
        Class<? extends TemaDeReunion> expectedClass, Annotation[] contextAnnotations) {
        return TemaParaRepasarActionItems.class.equals(expectedClass)
            && contextAnnotations != null
            && Arrays.stream(contextAnnotations).anyMatch(TemasParaRepasar.class::isInstance);
    }

    private final Map<Type, Type> toClass = new HashMap<Type, Type>() {{
        put(TemaDeReunionConDescripcion.class, TemaDeReunionConDescripcionTo.class);
        put(TemaParaProponerPinosARoot.class, TemaParaProponerPinosARootTo.class);
        put(TemaParaRepasarActionItems.class, TemaParaRepasarActionItemsTo.class);
    }};
}
