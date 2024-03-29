package it.interno.normalizzatore.service;

import it.interno.normalizzatore.dto.ResponseDto;
import it.interno.normalizzatore.dto.divitech.LuogoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@FeignClient(name = "luoghi", path = "/luoghi", url = "http://localhost:8091")
public interface LuoghiClient {

    @GetMapping(path = "/alternativi")
    ResponseEntity<ResponseDto<LuogoDto>> getLuogoAlternativoSDI(@RequestParam @NotBlank(message = "Il campo 'Descrizione Luogo' {errore.campo.obbligatorio}") String desLuo,
                                                              @RequestParam @NotBlank(message = "Il campo 'Sigla Provincia' {errore.campo.obbligatorio}") String sglPrv,
                                                              @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") @NotNull(message = "Il campo 'Data Riferimento' {errore.campo.obbligatorio}") LocalDate dataRif) ;
    @GetMapping(path = "/originali")
    ResponseEntity<ResponseDto<LuogoDto>> getLuoghiOriginaliSDI(
            @RequestParam @NotBlank(message = "Il campo 'Descrizione Luogo' {errore.campo.obbligatorio}") String desLuo,
            @RequestParam @NotBlank(message = "Il campo 'Sigla Provincia' {errore.campo.obbligatorio}") String sglPrv,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") @NotNull(message = "Il campo 'Data Riferimento' {errore.campo.obbligatorio}") LocalDate dataRif) ;

}
