package it.interno.normalizzatore.service;

import it.interno.normalizzatore.dto.divitech.LuogoDto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface NormalizzatoreIndirizziService {


    List<LuogoDto> callDivitechService(String comune, String indirizzo, String sglPrv, LocalDate dataRif) throws IOException;
}
