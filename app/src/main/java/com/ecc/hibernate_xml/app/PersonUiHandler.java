package com.ecc.hibernate_xml.app;

import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.math.BigDecimal;

import com.ecc.hibernate_xml.dto.PersonDTO;
import com.ecc.hibernate_xml.dto.NameDTO;
import com.ecc.hibernate_xml.dto.AddressDTO;
import com.ecc.hibernate_xml.service.PersonService;
import com.ecc.hibernate_xml.util.app.InputHandler;

public class PersonUiHandler {
	private static final String UPDATE_PROMPT = "Please enter the person ID you wish to update: ";
	private static final String DELETE_PROMPT = "Please enter the person ID you wish to delete: ";

	private static final String TITLE_PROMPT = "Please enter the title: ";
	private static final String LAST_NAME_PROMPT = "Please enter the last name: ";
	private static final String FIRST_NAME_PROMPT = "Please enter the first name: ";
	private static final String MIDDLE_NAME_PROMPT = "Please enter the middle name: ";
	private static final String SUFFIX_PROMPT = "Please enter the suffix: ";

	private static final String STREET_NUMBER_PROMPT = "Please enter the street number: ";
	private static final String BARANGAY_PROMPT = "Please enter the barangay: ";
	private static final String MUNICIPALITY_PROMPT = "Please enter the municipality: ";
	private static final String ZIP_CODE_PROMPT = "Please enter the zip code: ";

	private static final String BIRTHDAY_PROMPT = "Please enter the birthday (yyyy-MM-dd): ";
	private static final String GWA_PROMPT = "Please enter the GWA: ";

	private static final String EMPLOYMENT_PROMPT = "Please enter the employment status (y/n): ";
	private static final String DATE_HIRED_PROMPT = "Please enter date hired (yyyy-MM-dd): ";

	private static final String NO_PERSONS_MESSAGE = "There are no persons.";
	private static final String NO_PERSONS_TO_UPDATE = "There are no persons to update.";
	private static final String NO_PERSONS_TO_DELETE = "There are no persons to delete.";
	private static final String CREATE_SUCCESS_MESSAGE = "Successfully created the person ID [%d] \"%s\"!";
	private static final String UPDATE_SUCCESS_MESSAGE = "Successfully updated person's %s to \"%s\"!";
	private static final String DELETE_SUCCESS_MESSAGE = "Successfully deleted the person ID \"%d\"!";
	private static final String EMPLOYMENT_SUCCESS_MESSAGE = "Successfully updated person's employment status!";
	
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final PersonService personService = new PersonService();

	public void listByDateHired() {		
		list(personService.listPersonsByDateHired(), "currentlyEmployed");
	}

	public void listByLastName() {		
		list(personService.listPersonsByLastName(), "name");
	}

	public void listByGWA() {		
		list(personService.listPersonsByGwa(), "GWA");
	}

	private void list(List<PersonDTO> persons, String propertyToEmphasize) {		
		if (persons.isEmpty()) {
			System.out.println(NO_PERSONS_MESSAGE);
		}
		else {	
			persons.stream().map(t -> t.toString(propertyToEmphasize))
				.forEach(System.out::println);
		}

		System.out.println("-------------------");
	}

	public Object create() throws Exception {	
		NameDTO name = new NameDTO();
		name.setLastName(InputHandler.getNextLineREPL(LAST_NAME_PROMPT, t -> personService.validateName(t, "Last name")));
		name.setFirstName(InputHandler.getNextLineREPL(FIRST_NAME_PROMPT, t -> personService.validateName(t, "First name")));
		name.setMiddleName(InputHandler.getNextLineREPL(MIDDLE_NAME_PROMPT, t -> personService.validateName(t, "Middle name")));

		PersonDTO person = new PersonDTO();
		person.setName(name);
		person.setId((Integer) personService.create(person));

		String successMessage = String.format(CREATE_SUCCESS_MESSAGE, person.getId(), person.getName());
		System.out.println(successMessage);	

		return person;
	}

	public Object update() throws Exception {
		List<PersonDTO> persons = personService.list();

		if (persons.isEmpty()) {
			throw new Exception(NO_PERSONS_TO_UPDATE);
		}
		else {		
			persons.stream().map(person -> person.toSimplifiedForm())
				.forEach(System.out::println);
			System.out.println("-------------------");		
			Integer personId = InputHandler.getNextLine(UPDATE_PROMPT, Integer::valueOf);
			return personService.get(personId);
		}
	}

	public void displayPerson(Object parameter) {
		PersonDTO person = (PersonDTO) parameter;
		System.out.println(person);
		System.out.println("-------------------");
	}

	public void changeName(Object parameter) throws Exception {
		PersonDTO person = (PersonDTO) parameter;
		NameDTO name = person.getName();

		System.out.println("Current value: " + name);
		name.setTitle(InputHandler.getNextLineREPL(TITLE_PROMPT, t -> personService.validateName(t, "Title")));
		name.setLastName(InputHandler.getNextLineREPL(LAST_NAME_PROMPT, t -> personService.validateName(t, "Last name")));
		name.setFirstName(InputHandler.getNextLineREPL(FIRST_NAME_PROMPT, t -> personService.validateName(t, "First name")));
		name.setMiddleName(InputHandler.getNextLineREPL(MIDDLE_NAME_PROMPT, t -> personService.validateName(t, "Middle name")));
		name.setSuffix(InputHandler.getNextLineREPL(SUFFIX_PROMPT, t -> personService.validateName(t, "Suffix")));

		person.setName(name);
		personService.update(person);

		String successMessage = String.format(UPDATE_SUCCESS_MESSAGE, "name", name);
		System.out.println(successMessage);
	}

	public void changeAddress(Object parameter) throws Exception {
		PersonDTO person = (PersonDTO) parameter;		
		AddressDTO address = person.getAddress();

		System.out.println("Current value: " + (address == null? "(uninitialized)": address));
		address = new AddressDTO();
		address.setStreetNumber(InputHandler.getNextLineREPL(STREET_NUMBER_PROMPT, t -> personService.validateAddress(t, "Street number")));
		address.setBarangay(InputHandler.getNextLineREPL(BARANGAY_PROMPT, t -> personService.validateAddress(Integer.valueOf(t), "Barangay")));
		address.setMunicipality(InputHandler.getNextLineREPL(MUNICIPALITY_PROMPT, t -> personService.validateAddress(t, "Municipality")));
		address.setZipCode(InputHandler.getNextLineREPL(ZIP_CODE_PROMPT, t -> personService.validateAddress(Integer.valueOf(t), "Zip code")));

		person.setAddress(address);
		personService.update(person);

		String successMessage = String.format(UPDATE_SUCCESS_MESSAGE, "address", address);
		System.out.println(successMessage);
	}

	public void changeBirthday(Object parameter) throws Exception {
		PersonDTO person = (PersonDTO) parameter;
		Date birthday = person.getBirthday();

		System.out.println("Current value: " + (birthday == null? "(uninitialized)": birthday));
		birthday = InputHandler.getNextLineREPL(BIRTHDAY_PROMPT, dateFormat::parse);
		person.setBirthday(birthday);
		personService.update(person);

		String successMessage = String.format(UPDATE_SUCCESS_MESSAGE, "birthday", dateFormat.format(birthday));
		System.out.println(successMessage);
	}

	public void changeGWA(Object parameter) throws Exception {
		PersonDTO person = (PersonDTO) parameter;
		BigDecimal GWA = person.getGWA();

		System.out.println("Current value: " + (GWA == null? "(uninitialized)": GWA));
		GWA = InputHandler.getNextLineREPL(GWA_PROMPT, input -> 
			personService.validateGWA(new BigDecimal(input)));

		person.setGWA(GWA);
		personService.update(person);

		String successMessage = String.format(UPDATE_SUCCESS_MESSAGE, "GWA", GWA);
		System.out.println(successMessage);
	}

	public void changeEmploymentStatus(Object parameter) throws Exception {
		PersonDTO person = (PersonDTO) parameter;
		System.out.println("Current value: " + person.getEmploymentStatus());

		Boolean currentlyEmployed = InputHandler.getNextLineREPL(EMPLOYMENT_PROMPT, input -> {
			if (input.equals("y")) {
				return true;
			}
			else if (input.equals("n")) {
				return false;
			}
			throw new ParseException("Invalid input!", 0);
		});

		person.setCurrentlyEmployed(currentlyEmployed);

		if (currentlyEmployed) {
			Date dateHired = InputHandler.getNextLineREPL(DATE_HIRED_PROMPT, input -> 
				personService.validateDateHired(currentlyEmployed, dateFormat.parse(input)));
			person.setDateHired(dateHired);
		}

		personService.update(person);
		System.out.println(EMPLOYMENT_SUCCESS_MESSAGE);
	}

	public void delete() throws Exception {			
		List<PersonDTO> persons = personService.list();

		if (persons.isEmpty()) {
			throw new Exception(NO_PERSONS_TO_DELETE);
		}
		else {		
			persons.stream().map(person -> person.toSimplifiedForm())
				.forEach(System.out::println);
			System.out.println("-------------------");		
		
			Integer personId = InputHandler.getNextLine(DELETE_PROMPT, Integer::valueOf);

			personService.delete(personId);

			String successMessage = String.format(DELETE_SUCCESS_MESSAGE, personId);
			System.out.println(successMessage);
		}
	}
}