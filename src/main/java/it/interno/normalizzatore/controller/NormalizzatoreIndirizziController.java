package it.interno.normalizzatore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.interno.normalizzatore.dto.ResponseDto;
import it.interno.normalizzatore.dto.divitech.LuogoDto;
import it.interno.normalizzatore.service.NormalizzatoreIndirizziService;
import it.interno.normalizzatore.util.StringUpperCaseEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class NormalizzatoreIndirizziController {

    @Autowired
    private NormalizzatoreIndirizziService normalizzatoreIndirizziService;


    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {

        StringUpperCaseEditor stringUpperCaseEditor = new StringUpperCaseEditor();
        webDataBinder.registerCustomEditor(String.class, stringUpperCaseEditor);

    }

    @Operation(description = "API per recuperare luoghi filtrando per descrizione luogo, tipo luogo e data ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dati recuperati correttamente"),
            @ApiResponse(responseCode = "404", description = "Url non definita"),
            @ApiResponse(responseCode = "500", description = "Errore del sistema")
    })
    @GetMapping
    public ResponseEntity<ResponseDto> normalizzazioneIndirizzo(@RequestParam String comune, @RequestParam String siglaProvincia,
                                                                @RequestParam String indirizzo,
                                                                @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataRif) throws IOException {

        List<LuogoDto> response = normalizzatoreIndirizziService.callDivitechService(comune, indirizzo, siglaProvincia, dataRif);

        return ResponseEntity.ok().body(ResponseDto.<List<LuogoDto>>builder().code(HttpStatus.OK.value()).body(response).build()) ;
    }

}
