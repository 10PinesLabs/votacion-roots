package ar.com.kfgodel.temas.apiRest;

import convention.persistent.Minuta;
import convention.persistent.TemaDeMinuta;
import convention.persistent.TemaParaRepasarActionItems;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TemaDeMinutaResourceTest extends ResourceTest {

    @Test
    public void putTheTemaParaRepasarActionItemsEsExitoso() throws IOException {
        TemaParaRepasarActionItems temaParaRepasarActionItems =
            persistentHelper.crearUnTemaDeReunionParaRepasarActionItems();
        Minuta minuta = minutaService.save(Minuta.create(temaParaRepasarActionItems.getReunion()));
        TemaDeMinuta temaDeMinuta = minuta.getTemas().get(0);

        String requestBody = convertirAJsonString(convertirATo(temaDeMinuta));
        HttpResponse response =
            makeJsonPutRequest("temaDeMinuta/" + temaDeMinuta.getId(), requestBody);

        String collect =
            new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines()
                .collect(Collectors.joining("\n"));
        assertThatResponseStatusCodeIs(response, HttpStatus.SC_OK);
    }
}