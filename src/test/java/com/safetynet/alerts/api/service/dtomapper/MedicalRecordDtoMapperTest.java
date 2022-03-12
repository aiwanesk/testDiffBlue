package com.safetynet.alerts.api.service.dtomapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.safetynet.alerts.api.model.MedicalRecord;
import com.safetynet.alerts.api.model.dto.MedicalRecordDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MedicalRecordDtoMapper.class})
@ExtendWith(SpringExtension.class)
class MedicalRecordDtoMapperTest {
    @Autowired
    private MedicalRecordDtoMapper medicalRecordDtoMapper;

    @Test
    void testMapToDto() {
        MedicalRecord medicalRecord = mock(MedicalRecord.class);
        when(medicalRecord.getAllergies()).thenReturn(new ArrayList<>());
        ArrayList<String> stringList = new ArrayList<>();
        when(medicalRecord.getMedications()).thenReturn(stringList);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(medicalRecord.getBirthdate()).thenReturn(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        MedicalRecordDto actualMapToDtoResult = this.medicalRecordDtoMapper.mapToDto(medicalRecord);
        assertEquals(stringList, actualMapToDtoResult.getAllergies());
        assertEquals(stringList, actualMapToDtoResult.getMedications());
        verify(medicalRecord).getAllergies();
        verify(medicalRecord).getBirthdate();
        verify(medicalRecord).getMedications();
    }
}

