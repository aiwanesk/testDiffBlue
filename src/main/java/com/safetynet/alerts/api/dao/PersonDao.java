package com.safetynet.alerts.api.dao;

import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersonDao implements IPersonDao{

    private final IAlertsDataSource dataSource;

    /**
     * Get a person from a datasource.
     *
     * @param firstName first name of the person to get.
     * @param lastName last name of the person to get.
     * @return the person if found.
     */
    @Override
    public Optional<Person> getPerson(final String firstName, final String lastName){
        List<Person> persons = dataSource.getData().getPersons();
        Optional<Person> personResult = persons.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) &&
                        p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        return personResult;
    }
    /**
     * Delete a person from a datasource.
     *
     * @param firstName first name of the person to delete.
     * @param lastName last name of the person to delete.
     * @throws DataNotFoundException if the person does not exist in the datasource. (No person with
     * given firstName and lastName has been found).
     *
     */
    @Override
    public void deletePerson(final String firstName, final String lastName) throws DataNotFoundException {
        Person person;
        Optional<Person> personResult = getPerson(firstName, lastName);
        if(personResult.isPresent()){
            person = personResult.get();
            dataSource.getData().getPersons().remove(person);
        } else {
            throw new DataNotFoundException("Person " + firstName + " " + lastName);
        }
    }
    /**
     * Update an existing person into a datasource.
     *
     * @param personToUpdate person to update.
     * @return updated person.
     * @throws DataNotFoundException if the person does not exist in the datasource. (No person with
     * same firstName and lastName has been found).
     *
     */
    @Override
    public Person updatePerson(Person personToUpdate) throws DataNotFoundException {
        Person person;
        Optional<Person> personResult = getPerson(personToUpdate.getFirstName(), personToUpdate.getLastName());
        if(personResult.isPresent()){
            person = personResult.get();
            person.setAddress(personToUpdate.getAddress());
            person.setCity(personToUpdate.getCity());
            person.setZip(personToUpdate.getZip());
            person.setEmail(personToUpdate.getEmail());
            person.setPhone(personToUpdate.getPhone());
        } else {
            throw new DataNotFoundException("Person " + personToUpdate.getFirstName() + " " + personToUpdate.getLastName());
        }
        return person;
    }

    /**
     * Add a new a person into a datasource.
     *
     * @param personToCreate person to add.
     * @return added person.
     * @throws DataAlreadyExistsException if the person already exist in the datasource. (person with
     * same firstName and lastName has been found).
     *
     */
    @Override
    public Person createPerson(Person personToCreate) throws DataAlreadyExistsException {
        Person person;
        Optional<Person> personResult = getPerson(personToCreate.getFirstName(), personToCreate.getLastName());
        if(personResult.isEmpty()){
            person = new Person(personToCreate);
            dataSource.getData().getPersons().add(person);
        } else {
            throw new DataAlreadyExistsException("Person " + personToCreate.getFirstName() + " " + personToCreate.getLastName());
        }
        return person;
    }

    /**
     * Get a list of persons that live to a given address.
     *
     * @param address the address.
     * @return list of Person object.
     */
    @Override
    public List<Person> getPersonsByAddress(String address){
        List<Person> persons = dataSource.getData().getPersons();
        return persons.stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }
    /**
     * Get a list of persons that live in a given city.
     *
     * @param city the city name.
     * @return list of Person object.
     */
    @Override
    public List<Person> getPersonsByCity(String city){
        List<Person> persons = dataSource.getData().getPersons();
        return persons.stream()
                .filter(p -> p.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    /**
     * Get all persons named 'firstName' 'lastName' from a datasource.
     *
     * @param firstName first name of the person to get.
     * @param lastName  last name of the person to get.
     * @return a list of person
     */
    @Override
    public List<Person> getPersons(String firstName, String lastName) {
        List<Person> persons = dataSource.getData().getPersons();
        return  persons
                .stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }
}
