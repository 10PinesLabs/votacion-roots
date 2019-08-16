package convention.transformers;

import com.google.inject.Inject;
import convention.persistent.TemaDeReunion;
import convention.persistent.TemaDeReunionConDescripcion;
import convention.persistent.TemaParaProponerPinosARoot;
import convention.persistent.TemaParaRepasarActionItems;
import convention.rest.api.tos.TemaDeReunionConDescripcionTo;
import convention.rest.api.tos.TemaDeReunionTo;
import convention.rest.api.tos.TemaParaProponerPinosARootTo;
import convention.rest.api.tos.TemaParaRepasarActionItemsTo;
import convention.transformers.persistibles.PersistableTo2PersistableConverter;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.conversion.TypeConverter;
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
        return (TemaDeReunion) persistableTo2PersistableConverter()
                .convertTo(domainClass(sourceObject.getClass()), sourceObject, null);
    }

    private SpecializedTypeConverter<Object, Object> persistableTo2PersistableConverter() {
        return baseConverter.getSpecializedConverterByName(PersistableTo2PersistableConverter.class.getName());
    }

    private Type domainClass(Class<? extends TemaDeReunionTo> toType) {
        return domainClass.get(toType);
    }

    private Map<Type, Type> domainClass = new HashMap<Type, Type>() {{
        put(TemaDeReunionConDescripcionTo.class, TemaDeReunionConDescripcion.class);
        put(TemaParaProponerPinosARootTo.class, TemaParaProponerPinosARoot.class);
        put(TemaParaRepasarActionItemsTo.class, TemaParaRepasarActionItems.class);
    }};
}
