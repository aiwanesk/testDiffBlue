package com.safetynet.alerts.api.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.FireStation;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {FireStationDao.class})
@ExtendWith(SpringExtension.class)
class FireStationDaoTest {
    @Autowired
    private FireStationDao fireStationDao;

    @MockBean
    private IAlertsDataSource iAlertsDataSource;

    @Test
    void testGetFireStation() {
        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(new ArrayList<>());
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        assertFalse(this.fireStationDao.getFireStation("42 Main St").isPresent());
        verify(this.iAlertsDataSource).getData();
    }

    @Test
    void testGetFireStation2() {
        FireStation fireStation = mock(FireStation.class);
        when(fireStation.getAddress()).thenReturn("42 Main St");

        ArrayList<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);

        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(fireStationList);
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        assertTrue(this.fireStationDao.getFireStation("42 Main St").isPresent());
        verify(this.iAlertsDataSource).getData();
        verify(fireStation).getAddress();
    }

    @Test
    void testGetAddresses() {
        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(new ArrayList<>());
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        assertTrue(this.fireStationDao.getAddresses(10).isEmpty());
        verify(this.iAlertsDataSource).getData();
    }

    @Test
    void testGetAddresses2() {
        ArrayList<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(new FireStation());

        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(fireStationList);
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        assertTrue(this.fireStationDao.getAddresses(10).isEmpty());
        verify(this.iAlertsDataSource).getData();
    }

    @Test
    void testGetAddresses3() {
        ArrayList<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(new FireStation());
        fireStationList.add(new FireStation());

        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(fireStationList);
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        assertTrue(this.fireStationDao.getAddresses(10).isEmpty());
        verify(this.iAlertsDataSource).getData();
    }

    @Test
    void testGetAddresses4() {
        FireStation fireStation = mock(FireStation.class);
        when(fireStation.getStation()).thenReturn(1);

        ArrayList<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);

        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(fireStationList);
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        assertTrue(this.fireStationDao.getAddresses(10).isEmpty());
        verify(this.iAlertsDataSource).getData();
        verify(fireStation).getStation();
    }

    @Test
    void testGetAddresses5() {
        FireStation fireStation = mock(FireStation.class);
        when(fireStation.getAddress()).thenReturn("42 Main St");
        when(fireStation.getStation()).thenReturn(1);

        ArrayList<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);

        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(fireStationList);
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        List<String> actualAddresses = this.fireStationDao.getAddresses(1);
        assertEquals(1, actualAddresses.size());
        assertEquals("42 Main St", actualAddresses.get(0));
        verify(this.iAlertsDataSource).getData();
        verify(fireStation).getAddress();
        verify(fireStation).getStation();
    }

    @Test
    void testGetAddresses6() {
        FireStation fireStation = mock(FireStation.class);
        when(fireStation.getAddress()).thenReturn("42 Main St");
        when(fireStation.getStation()).thenReturn(1);

        ArrayList<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(new FireStation());
        fireStationList.add(fireStation);

        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(fireStationList);
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        List<String> actualAddresses = this.fireStationDao.getAddresses(0);
        assertEquals(1, actualAddresses.size());
        assertNull(actualAddresses.get(0));
        verify(this.iAlertsDataSource).getData();
        verify(fireStation).getStation();
    }

    @Test
    void testDeleteFireStation() throws DataNotFoundException {
        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(new ArrayList<>());
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        assertThrows(DataNotFoundException.class, () -> this.fireStationDao.deleteFireStation("42 Main St"));
        verify(this.iAlertsDataSource).getData();
    }

    @Test
    void testDeleteFireStation2() throws DataNotFoundException {
        FireStation fireStation = mock(FireStation.class);
        when(fireStation.getAddress()).thenReturn("42 Main St");

        ArrayList<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);

        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(fireStationList);
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        this.fireStationDao.deleteFireStation("42 Main St");
        verify(this.iAlertsDataSource, atLeast(1)).getData();
        verify(fireStation).getAddress();
    }

    @Test
    void testUpdateFireStation() throws DataNotFoundException {
        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(new ArrayList<>());
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        assertThrows(DataNotFoundException.class, () -> this.fireStationDao.updateFireStation(new FireStation()));
        verify(this.iAlertsDataSource).getData();
    }

    @Test
    void testUpdateFireStation2() throws DataNotFoundException {
        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(new ArrayList<>());
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        FireStation fireStation = mock(FireStation.class);
        when(fireStation.getAddress()).thenReturn("42 Main St");
        assertThrows(DataNotFoundException.class, () -> this.fireStationDao.updateFireStation(fireStation));
        verify(this.iAlertsDataSource).getData();
        verify(fireStation, atLeast(1)).getAddress();
    }

    @Test
    void testUpdateFireStation3() throws DataNotFoundException {
        FireStation fireStation = new FireStation();
        fireStation.setAddress("42 Main St");

        ArrayList<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);

        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(fireStationList);
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        FireStation fireStation1 = mock(FireStation.class);
        when(fireStation1.getStation()).thenReturn(1);
        when(fireStation1.getAddress()).thenReturn("42 Main St");
        FireStation actualUpdateFireStationResult = this.fireStationDao.updateFireStation(fireStation1);
        assertSame(fireStation, actualUpdateFireStationResult);
        assertEquals(1, actualUpdateFireStationResult.getStation());
        verify(this.iAlertsDataSource).getData();
        verify(fireStation1).getAddress();
        verify(fireStation1).getStation();
    }

    @Test
    void testCreateFireStation() throws DataAlreadyExistsException {
        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(new ArrayList<>());
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        FireStation actualCreateFireStationResult = this.fireStationDao.createFireStation(new FireStation());
        assertNull(actualCreateFireStationResult.getAddress());
        assertEquals(0, actualCreateFireStationResult.getStation());
        verify(this.iAlertsDataSource, atLeast(1)).getData();
    }

    @Test
    void testCreateFireStation2() throws DataAlreadyExistsException {
        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(new ArrayList<>());
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        FireStation fireStation = mock(FireStation.class);
        when(fireStation.getAddress()).thenReturn("42 Main St");
        FireStation actualCreateFireStationResult = this.fireStationDao.createFireStation(fireStation);
        assertNull(actualCreateFireStationResult.getAddress());
        assertEquals(0, actualCreateFireStationResult.getStation());
        verify(this.iAlertsDataSource, atLeast(1)).getData();
        verify(fireStation).getAddress();
    }

    @Test
    void testCreateFireStation3() throws DataAlreadyExistsException {
        FireStation fireStation = new FireStation();
        fireStation.setAddress("42 Main St");

        ArrayList<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);

        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(fireStationList);
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        FireStation fireStation1 = mock(FireStation.class);
        when(fireStation1.getAddress()).thenReturn("42 Main St");
        assertThrows(DataAlreadyExistsException.class, () -> this.fireStationDao.createFireStation(fireStation1));
        verify(this.iAlertsDataSource).getData();
        verify(fireStation1, atLeast(1)).getAddress();
    }

    @Test
    void testGetFireStationNumber() throws DataNotFoundException {
        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(new ArrayList<>());
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        assertThrows(DataNotFoundException.class, () -> this.fireStationDao.getFireStationNumber("42 Main St"));
        verify(this.iAlertsDataSource).getData();
    }

    @Test
    void testGetFireStationNumber2() throws DataNotFoundException {
        FireStation fireStation = mock(FireStation.class);
        when(fireStation.getStation()).thenReturn(1);
        when(fireStation.getAddress()).thenReturn("42 Main St");

        ArrayList<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);

        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(new ArrayList<>());
        data.setFirestations(fireStationList);
        data.setMedicalrecords(new ArrayList<>());
        when(this.iAlertsDataSource.getData()).thenReturn(data);
        assertEquals(1, this.fireStationDao.getFireStationNumber("42 Main St"));
        verify(this.iAlertsDataSource).getData();
        verify(fireStation).getAddress();
        verify(fireStation).getStation();
    }
}

