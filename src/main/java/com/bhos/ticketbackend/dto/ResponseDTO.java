package com.bhos.ticketbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO {
    private int errCode;
    private String errMessage;
    private String succMessage;
    private Object responseData;

    private ResponseDTO(){

    }

    public static ResponseDTO of(Object data){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResponseData(data);
        return responseDTO;
    }

    public static ResponseDTO of(Object data, String succMessage){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResponseData(data);
        responseDTO.setSuccMessage(succMessage);
        return responseDTO;
    }

    public static ResponseDTO of(Object data, Integer errCode, String errMessage){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResponseData(data);
        responseDTO.setErrCode(errCode);
        responseDTO.setErrMessage(errMessage);
        return responseDTO;
    }

}
