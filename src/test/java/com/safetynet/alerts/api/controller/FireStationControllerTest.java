package com.safetynet.alerts.api.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.api.controller.uitls.IRequestLogger;
import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.model.dto.FireStationPersonsDto;
import com.safetynet.alerts.api.model.dto.MedicalRecordDto;
import com.safetynet.alerts.api.model.dto.PersonDto;
import com.safetynet.alerts.api.service.IFireStationService;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {FireStationController.class})
@ExtendWith(SpringExtension.class)
class FireStationControllerTest {
    @Autowired
    private FireStationController fireStationController;

    @MockBean
    private IFireStationService iFireStationService;

    @MockBean
    private IRequestLogger iRequestLogger;

    @Test
    void testDeleteFireStation() throws Exception {
        doNothing().when(this.iRequestLogger)
                .logResponseSuccess((org.springframework.http.HttpStatus) any(), (String) any());
        doNothing().when(this.iRequestLogger).logRequest((String) any());
        doNothing().when(this.iFireStationService).deleteFireStation((String) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/firestation/{address}",
                "42 Main St");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.fireStationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testDeleteFireStation2() throws Exception {
        doNothing().when(this.iRequestLogger)
                .logResponseSuccess((org.springframework.http.HttpStatus) any(), (String) any());
        doNothing().when(this.iRequestLogger).logRequest((String) any());
        doNothing().when(this.iFireStationService).deleteFireStation((String) any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/firestation/{address}", "42 Main St");
        deleteResult.contentType("Not all who wander are lost");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.fireStationController)
                .build()
                .perform(deleteResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testCreateFireStation() throws Exception {
        doNothing().when(this.iRequestLogger)
                .logResponseSuccess((org.springframework.http.HttpStatus) any(), (String) any());
        doNothing().when(this.iRequestLogger).logRequest((String) any());
        when(this.iFireStationService.createFireStation((FireStation) any())).thenReturn(new FireStation());

        FireStation fireStation = new FireStation();
        fireStation.setStation(1);
        fireStation.setAddress("42 Main St");
        String content = (new ObjectMapper()).writeValueAsString(fireStation);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.fireStationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/firestation/"));
    }

    @Test
    void testUpdateFireStation() throws Exception {
        doNothing().when(this.iRequestLogger)
                .logResponseSuccess((org.springframework.http.HttpStatus) any(), (String) any());
        doNothing().when(this.iRequestLogger).logRequest((String) any());
        when(this.iFireStationService.updateFireStation((FireStation) any())).thenReturn(new FireStation());

        FireStation fireStation = new FireStation();
        fireStation.setStation(1);
        fireStation.setAddress("42 Main St");
        String content = (new ObjectMapper()).writeValueAsString(fireStation);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.fireStationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetFireStationPersons() throws Exception {
        doNothing().when(this.iRequestLogger)
                .logResponseSuccess((org.springframework.http.HttpStatus) any(), (String) any());
        doNothing().when(this.iRequestLogger).logRequest((String) any());
        when(this.iFireStationService.getPersons((Integer) any()))
                .thenReturn(new FireStationPersonsDto(new ArrayList<>(), 10, 10));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/firestation");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("stationNumber", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(this.fireStationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"persons\":[],\"numberOfAdults\":10,\"numberOfChildren\":10}"));
    }

    @Test
    void testGetFireStationPersons2() throws Exception {
        doNothing().when(this.iRequestLogger)
                .logResponseSuccess((org.springframework.http.HttpStatus) any(), (String) any());
        doNothing().when(this.iRequestLogger).logRequest((String) any());

        ArrayList<PersonDto> personDtoList = new ArrayList<>();
        Date birthdate = new Date(1L);
        ArrayList<String> medications = new ArrayList<>();
        personDtoList.add(new PersonDto("Jane", "Doe", "42 Main St", "Oxford", "21654", "4105551212",
                "jane.doe@example.org", 1, new MedicalRecordDto(birthdate, medications, new ArrayList<>())));
        FireStationPersonsDto fireStationPersonsDto = new FireStationPersonsDto(personDtoList, 10, 10);

        when(this.iFireStationService.getPersons((Integer) any())).thenReturn(fireStationPersonsDto);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/firestation");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("stationNumber", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(this.fireStationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"persons\":[{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"address\":\"42 Main St\",\"city\":\"Oxford\",\"zip\":\"21654"
                                        + "\",\"phone\":\"4105551212\"}],\"numberOfAdults\":10,\"numberOfChildren\":10}"));
    }

    @Test
    void testGetFiredPersons() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/flood/stations");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.fireStationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testGetPhoneAlert() throws Exception {
        doNothing().when(this.iRequestLogger)
                .logResponseSuccess((org.springframework.http.HttpStatus) any(), (String) any());
        doNothing().when(this.iRequestLogger).logRequest((String) any());
        when(this.iFireStationService.getPhones((Integer) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/phoneAlert");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("firestation", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(this.fireStationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}

