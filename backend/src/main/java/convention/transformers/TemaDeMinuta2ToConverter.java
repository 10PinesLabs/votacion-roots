package convention.transformers;

import com.google.inject.Inject;
import convention.persistent.ActionItem;
import convention.persistent.TemaDeMinuta;
import convention.persistent.TemaDeReunion;
import convention.rest.api.tos.ActionItemTo;
import convention.rest.api.tos.TemaDeMinutaTo;
import convention.rest.api.tos.TemaDeReunionTo;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.conversion.TypeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TemaDeMinuta2ToConverter implements SpecializedTypeConverter<TemaDeMinuta, TemaDeMinutaTo> {

    @Inject
    private TypeConverter baseConverter;

    @Override
    public TemaDeMinutaTo convertTo(Type expectedType, TemaDeMinuta temaDeMinuta, Annotation[] contextAnnotations)
        throws CannotConvertException {
        return new TemaDeMinutaTo(
            temaDeMinuta.getId(),
            temaDeMinuta.getMinuta().getId(),
            convert(contextAnnotations, temaDeMinuta.getTema()),
            convert(contextAnnotations, temaDeMinuta.getActionItems()),
            temaDeMinuta.getConclusion(),
            temaDeMinuta.getFueTratado()
        );
    }

    private List<ActionItemTo> convert(Annotation[] contextAnnotations, List<ActionItem> actionItems) {
        return actionItems == null
            ? new ArrayList<>()
            : actionItems.stream()
            .map(actionItem -> (ActionItemTo)
                baseConverter.convertValue(actionItem, ActionItemTo.class, contextAnnotations))
            .collect(Collectors.toList());
    }

    private TemaDeReunionTo convert(Annotation[] contextAnnotations, TemaDeReunion tema) {
        return baseConverter.convertValue(tema, TemaDeReunionTo.class, contextAnnotations);
    }
}
