package convention.rest.api.tos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TemaParaProponerPinosARootTo.class, name = "proponerPinos"),
        @JsonSubTypes.Type(value = TemaDeReunionConDescripcionTo.class, name = "conDescripcion")
})
public class TemaDeReunionTo extends TemaTo {
}
