package com.safetynet.alerts.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.safetynet.alerts.api.dao.IFireStationDao;
import com.safetynet.alerts.api.dao.IMedicalRecordDao;
import com.safetynet.alerts.api.dao.IPersonDao;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.model.dto.FireStationPersonsDto;
import com.safetynet.alerts.api.model.dto.FloodDto;
import com.safetynet.alerts.api.model.dto.MedicalRecordDto;
import com.safetynet.alerts.api.model.dto.PersonDto;
import com.safetynet.alerts.api.service.dtomapper.IDtoMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {FireStationService.class})
@ExtendWith(SpringExtension.class)
class FireStationServiceTest {
    @Autowired
    private FireStationService fireStationService;

    @MockBean
    private IDtoMapper<Person, PersonDto> iDtoMapper;

    @MockBean
    private IFireStationDao iFireStationDao;

    @MockBean
    private IMedicalRecordDao iMedicalRecordDao;

    @MockBean
    private IPersonDao iPersonDao;

    @Test
    void testDeleteFireStation() throws DataNotFoundException {
        doNothing().when(this.iFireStationDao).deleteFireStation((String) any());
        this.fireStationService.deleteFireStation("42 Main St");
        verify(this.iFireStationDao).deleteFireStation((String) any());
    }

    @Test
    void testUpdateFireStation() throws DataNotFoundException {
        FireStation fireStation = new FireStation();
        when(this.iFireStationDao.updateFireStation((FireStation) any())).thenReturn(fireStation);
        assertSame(fireStation, this.fireStationService.updateFireStation(new FireStation()));
        verify(this.iFireStationDao).updateFireStation((FireStation) any());
    }

    @Test
    void testCreateFireStation() throws DataAlreadyExistsException {
        FireStation fireStation = new FireStation();
        when(this.iFireStationDao.createFireStation((FireStation) any())).thenReturn(fireStation);
        assertSame(fireStation, this.fireStationService.createFireStation(new FireStation()));
        verify(this.iFireStationDao).createFireStation((FireStation) any());
    }

    @Test
    void testGetPersons() throws DataNotFoundException {
        when(this.iFireStationDao.getAddresses((Integer) any())).thenReturn(new ArrayList<>());
        assertThrows(DataNotFoundException.class, () -> this.fireStationService.getPersons(10));
        verify(this.iFireStationDao).getAddresses((Integer) any());
    }

    @Test
    void testGetPersons2() throws DataNotFoundException {
        ArrayList<Person> personList = new ArrayList<>();
        when(this.iPersonDao.getPersonsByAddress((String) any())).thenReturn(personList);

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");
        when(this.iFireStationDao.getAddresses((Integer) any())).thenReturn(stringList);
        FireStationPersonsDto actualPersons = this.fireStationService.getPersons(10);
        assertEquals(0, actualPersons.getNumberOfAdults().intValue());
        List<PersonDto> persons = actualPersons.getPersons();
        assertEquals(personList, persons);
        assertTrue(persons.isEmpty());
        assertEquals(0, actualPersons.getNumberOfChildren().intValue());
        verify(this.iPersonDao).getPersonsByAddress((String) any());
        verify(this.iFireStationDao).getAddresses((Integer) any());
    }

    @Test
    void testGetPersons3() throws DataNotFoundException {
        ArrayList<Person> personList = new ArrayList<>();
        personList.add(new Person());
        when(this.iPersonDao.getPersonsByAddress((String) any())).thenReturn(personList);

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");
        when(this.iFireStationDao.getAddresses((Integer) any())).thenReturn(stringList);
        Date birthdate = new Date(1L);
        ArrayList<String> medications = new ArrayList<>();
        when(this.iDtoMapper.mapToDto((Person) any()))
                .thenReturn(new PersonDto("Jane", "Doe", "42 Main St", "Oxford", "21654", "4105551212", "jane.doe@example.org",
                        1, new MedicalRecordDto(birthdate, medications, new ArrayList<>())));
        FireStationPersonsDto actualPersons = this.fireStationService.getPersons(10);
        assertEquals(0, actualPersons.getNumberOfAdults().intValue());
        assertEquals(1, actualPersons.getPersons().size());
        assertEquals(1, actualPersons.getNumberOfChildren().intValue());
        verify(this.iPersonDao).getPersonsByAddress((String) any());
        verify(this.iFireStationDao).getAddresses((Integer) any());
        verify(this.iDtoMapper).mapToDto((Person) any());
    }

    @Test
    void testGetPersons4() throws DataNotFoundException {
        ArrayList<Person> personList = new ArrayList<>();
        personList.add(new Person());
        personList.add(new Person());
        when(this.iPersonDao.getPersonsByAddress((String) any())).thenReturn(personList);

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");
        when(this.iFireStationDao.getAddresses((Integer) any())).thenReturn(stringList);
        Date birthdate = new Date(1L);
        ArrayList<String> medications = new ArrayList<>();
        when(this.iDtoMapper.mapToDto((Person) any()))
                .thenReturn(new PersonDto("Jane", "Doe", "42 Main St", "Oxford", "21654", "4105551212", "jane.doe@example.org",
                        1, new MedicalRecordDto(birthdate, medications, new ArrayList<>())));
        FireStationPersonsDto actualPersons = this.fireStationService.getPersons(10);
        assertEquals(0, actualPersons.getNumberOfAdults().intValue());
        assertEquals(2, actualPersons.getPersons().size());
        assertEquals(2, actualPersons.getNumberOfChildren().intValue());
        verify(this.iPersonDao).getPersonsByAddress((String) any());
        verify(this.iFireStationDao).getAddresses((Integer) any());
        verify(this.iDtoMapper, atLeast(1)).mapToDto((Person) any());
    }

    @Test
    void testGetPhones() throws DataNotFoundException {
        when(this.iFireStationDao.getAddresses((Integer) any())).thenReturn(new ArrayList<>());
        assertThrows(DataNotFoundException.class, () -> this.fireStationService.getPhones(10));
        verify(this.iFireStationDao).getAddresses((Integer) any());
    }

    @Test
    void testGetPhones2() throws DataNotFoundException {
        when(this.iPersonDao.getPersonsByAddress((String) any())).thenReturn(new ArrayList<>());

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");
        when(this.iFireStationDao.getAddresses((Integer) any())).thenReturn(stringList);
        assertTrue(this.fireStationService.getPhones(10).isEmpty());
        verify(this.iPersonDao).getPersonsByAddress((String) any());
        verify(this.iFireStationDao).getAddresses((Integer) any());
    }

    @Test
    void testGetPhones3() throws DataNotFoundException {
        ArrayList<Person> personList = new ArrayList<>();
        personList.add(new Person());
        when(this.iPersonDao.getPersonsByAddress((String) any())).thenReturn(personList);

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");
        when(this.iFireStationDao.getAddresses((Integer) any())).thenReturn(stringList);
        List<String> actualPhones = this.fireStationService.getPhones(10);
        assertEquals(1, actualPhones.size());
        assertNull(actualPhones.get(0));
        verify(this.iPersonDao).getPersonsByAddress((String) any());
        verify(this.iFireStationDao).getAddresses((Integer) any());
    }

    @Test
    void testGetPhones4() throws DataNotFoundException {
        ArrayList<Person> personList = new ArrayList<>();
        personList.add(new Person());
        personList.add(new Person());
        when(this.iPersonDao.getPersonsByAddress((String) any())).thenReturn(personList);

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");
        when(this.iFireStationDao.getAddresses((Integer) any())).thenReturn(stringList);
        List<String> actualPhones = this.fireStationService.getPhones(10);
        assertEquals(1, actualPhones.size());
        assertNull(actualPhones.get(0));
        verify(this.iPersonDao).getPersonsByAddress((String) any());
        verify(this.iFireStationDao).getAddresses((Integer) any());
    }

    @Test
    void testGetFloodHomes() {
        assertTrue(this.fireStationService.getFloodHomes(new ArrayList<>()).isEmpty());
    }

    @Test
    void testGetFloodHomes2() {
        when(this.iFireStationDao.getAddresses((Integer) any())).thenReturn(new ArrayList<>());

        ArrayList<Integer> integerList = new ArrayList<>();
        integerList.add(2);
        assertTrue(this.fireStationService.getFloodHomes(integerList).isEmpty());
        verify(this.iFireStationDao).getAddresses((Integer) any());
    }

    @Test
    void testGetFloodHomes3() {
        ArrayList<Person> personList = new ArrayList<>();
        when(this.iPersonDao.getPersonsByAddress((String) any())).thenReturn(personList);

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");
        when(this.iFireStationDao.getAddresses((Integer) any())).thenReturn(stringList);

        ArrayList<Integer> integerList = new ArrayList<>();
        integerList.add(2);
        List<FloodDto> actualFloodHomes = this.fireStationService.getFloodHomes(integerList);
        assertEquals(1, actualFloodHomes.size());
        FloodDto getResult = actualFloodHomes.get(0);
        assertEquals("foo", getResult.getAddress());
        assertEquals(personList, getResult.getPersons());
        verify(this.iPersonDao).getPersonsByAddress((String) any());
        verify(this.iFireStationDao).getAddresses((Integer) any());
    }

    @Test
    void testGetFloodHomes4() {
        ArrayList<Person> personList = new ArrayList<>();
        personList.add(new Person());
        when(this.iPersonDao.getPersonsByAddress((String) any())).thenReturn(personList);

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");
        when(this.iFireStationDao.getAddresses((Integer) any())).thenReturn(stringList);
        Date birthdate = new Date(1L);
        ArrayList<String> medications = new ArrayList<>();
        when(this.iDtoMapper.mapToDto((Person) any()))
                .thenReturn(new PersonDto("Jane", "Doe", "42 Main St", "Oxford", "21654", "4105551212", "jane.doe@example.org",
                        1, new MedicalRecordDto(birthdate, medications, new ArrayList<>())));

        ArrayList<Integer> integerList = new ArrayList<>();
        integerList.add(2);
        List<FloodDto> actualFloodHomes = this.fireStationService.getFloodHomes(integerList);
        assertEquals(1, actualFloodHomes.size());
        FloodDto getResult = actualFloodHomes.get(0);
        assertEquals("foo", getResult.getAddress());
        assertEquals(1, getResult.getPersons().size());
        verify(this.iPersonDao).getPersonsByAddress((String) any());
        verify(this.iFireStationDao).getAddresses((Integer) any());
        verify(this.iDtoMapper).mapToDto((Person) any());
    }

    @Test
    void testGetFloodHomes5() {
        ArrayList<Person> personList = new ArrayList<>();
        personList.add(new Person());
        personList.add(new Person());
        when(this.iPersonDao.getPersonsByAddress((String) any())).thenReturn(personList);

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");
        when(this.iFireStationDao.getAddresses((Integer) any())).thenReturn(stringList);
        Date birthdate = new Date(1L);
        ArrayList<String> medications = new ArrayList<>();
        when(this.iDtoMapper.mapToDto((Person) any()))
                .thenReturn(new PersonDto("Jane", "Doe", "42 Main St", "Oxford", "21654", "4105551212", "jane.doe@example.org",
                        1, new MedicalRecordDto(birthdate, medications, new ArrayList<>())));

        ArrayList<Integer> integerList = new ArrayList<>();
        integerList.add(2);
        List<FloodDto> actualFloodHomes = this.fireStationService.getFloodHomes(integerList);
        assertEquals(1, actualFloodHomes.size());
        FloodDto getResult = actualFloodHomes.get(0);
        assertEquals("foo", getResult.getAddress());
        assertEquals(2, getResult.getPersons().size());
        verify(this.iPersonDao).getPersonsByAddress((String) any());
        verify(this.iFireStationDao).getAddresses((Integer) any());
        verify(this.iDtoMapper, atLeast(1)).mapToDto((Person) any());
    }
}

