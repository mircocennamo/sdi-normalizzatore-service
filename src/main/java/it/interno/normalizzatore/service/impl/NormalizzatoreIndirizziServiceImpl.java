package it.interno.normalizzatore.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.interno.normalizzatore.config.ApplicationProperties;
import it.interno.normalizzatore.dto.ResponseDto;
import it.interno.normalizzatore.dto.divitech.*;
import it.interno.normalizzatore.service.LuoghiClient;
import it.interno.normalizzatore.service.NormalizzatoreIndirizziService;
import it.interno.normalizzatore.util.Util;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NormalizzatoreIndirizziServiceImpl implements NormalizzatoreIndirizziService {

    public static final String UTF_8 = "UTF-8";

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    LuoghiClient luoghiClient ;

    @Override
    public List<LuogoDto> callDivitechService(String comune, String indirizzo, String sglPrv, LocalDate dataRif) throws IOException {

        ResponseEntity<ResponseDto<LuogoDto>> responseEntity = luoghiClient.getLuogoAlternativoSDI(comune, sglPrv, dataRif);
        LuogoDto luogoDto = responseEntity.getBody().getBody();

        OkHttpClient client = new OkHttpClient();
        String encodedLocation = URLEncoder.encode(luogoDto.getDescrizioneLuogo(), UTF_8);
        String encodedAddress = URLEncoder.encode(indirizzo, UTF_8);
        String encodedCivicNumber = URLEncoder.encode(Util.extractCivicNumber(indirizzo), UTF_8);
        String encodedMaxRoadNumber = URLEncoder.encode("4", UTF_8);
        Request request = new Request.Builder()
                .url(applicationProperties.getDivitechUrl() + "admiName=" + encodedLocation + "&roadName=" + encodedAddress + "&civNumber=" + encodedCivicNumber + "&roadsMaxNum=" + encodedMaxRoadNumber)
                .get()
                .build();
        ResponseBody responseBody = client.newCall(request).execute().body();

        JSONObject jsonObject = XML.toJSONObject(responseBody.string());
        ObjectMapper objectMapper = new ObjectMapper() ;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        Response resDivitech = objectMapper.readValue(jsonObject.toString(), Response.class);

        return this.setResponseNormalizzatore(indirizzo, dataRif, resDivitech);
    }

    private List<LuogoDto> setResponseNormalizzatore(String indirizzo, LocalDate dataRif, Response resDivitech) {

        List<LuogoDto> listaLuogoDto = new ArrayList<>();
        if("NO ROAD FOUND".equalsIgnoreCase(resDivitech.getMessage()) || resDivitech.getListRoad() == null)
            return listaLuogoDto;

        if(resDivitech.getListRoad() != null || !"false".equalsIgnoreCase(resDivitech.getResult())) {
            List<RoadDetails> roadDetailsList = resDivitech.getListRoad().getRoadDetails().stream().filter(roadDetails ->
                    roadDetails.getAdminLevel().equals("4") && roadDetails.getLinkList().getLinkDetails().stream().findFirst().get().getMiddlePoint() != null).collect(Collectors.toList());

            roadDetailsList.forEach(rd -> {
                LuogoDto dto = null;
                ResponseEntity<ResponseDto<LuogoDto>> responseEntity = luoghiClient.getLuoghiOriginaliSDI(rd.getAdminName(), rd.getProvince_Abbreviation(), dataRif);
                dto = responseEntity.getBody().getBody();
                if (dto == null) {
                    LuogoDto body = LuogoDto.builder().descrizioneLuogo(rd.getAdminName()).siglaProvincia(rd.getProvince_Abbreviation()).flagIndirizzoValidoSdi(false).build();
                } else {
                    dto.setFlagIndirizzoValidoSdi(true);
                }
                LuogoDto body = dto;
                body.setIndirizzo(StringUtils.isEmpty(rd.getAddress()) ? indirizzo : rd.getAddress());
                body.setNumeroCivico(rd.getCivicNumber());
                LinkDetails linkDetails = rd.getLinkList().getLinkDetails().stream().findFirst().get();
                DecimalFormat df = new DecimalFormat("#0.0000000") ;
                MiddlePoint mp = linkDetails.getMiddlePoint() ;
                body.setCoordinataX(df.format(Double.valueOf(mp.getX())));
                body.setCoordinataY(df.format(Double.valueOf(mp.getY())));

                listaLuogoDto.add(body);
            });

        }

        return listaLuogoDto;
    }

}
