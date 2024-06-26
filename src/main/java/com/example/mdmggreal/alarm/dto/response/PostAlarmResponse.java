package com.example.mdmggreal.alarm.dto.response;

import com.example.mdmggreal.alarm.dto.AlarmDTO;
import com.example.mdmggreal.global.response.BaseResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.List;


@Getter
@SuperBuilder
public class PostAlarmResponse extends BaseResponse {
    private List<AlarmDTO> alarmDTOList;

    public static PostAlarmResponse from(List<AlarmDTO> alarmDTOList, HttpStatus status) {
        return PostAlarmResponse.builder()
                .resultCode(status.value())
                .resultMsg(status.name())
                .alarmDTOList(alarmDTOList)
                .build();
    }
}
