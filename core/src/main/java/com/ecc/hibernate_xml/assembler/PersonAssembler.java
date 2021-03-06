package com.ecc.hibernate_xml.assembler;

import java.util.stream.Collectors;

import com.ecc.hibernate_xml.model.Person;
import com.ecc.hibernate_xml.model.Name;
import com.ecc.hibernate_xml.model.Address;
import com.ecc.hibernate_xml.model.Contact;
import com.ecc.hibernate_xml.dto.AddressDTO;
import com.ecc.hibernate_xml.dto.NameDTO;
import com.ecc.hibernate_xml.dto.PersonDTO;

public class PersonAssembler extends AbstractAssembler<Person, PersonDTO> {
	private RoleAssembler roleAssembler = new RoleAssembler();
	private ContactAssembler contactAssembler = new ContactAssembler();
	private NameAssembler nameAssembler = new NameAssembler();

	@Override
	public PersonDTO createDTO(Person model) {
		if (model == null) {
			return null;
		}
		PersonDTO dto = new PersonDTO();
		dto.setId(model.getId());
		dto.setName(nameAssembler.createDTO(model.getName()));
		dto.setAddress(createAddressDTO(model.getAddress()));
		dto.setBirthday(model.getBirthday());
		dto.setGWA(model.getGWA());
		dto.setCurrentlyEmployed(model.getCurrentlyEmployed());
		dto.setDateHired(model.getDateHired());
		dto.setGWA(model.getGWA());
		dto.setContacts(contactAssembler.createDTO(model.getContacts().stream().collect(Collectors.toList())));
		dto.setRoles(roleAssembler.createDTO(model.getRoles().stream().collect(Collectors.toList())));
		return dto;
	}

	private AddressDTO createAddressDTO(Address model) {
		if (model == null) {
			return null;
		}
		AddressDTO dto = new AddressDTO();
		dto.setStreetNumber(model.getStreetNumber());
		dto.setBarangay(model.getBarangay());
		dto.setMunicipality(model.getMunicipality());
		dto.setZipCode(model.getZipCode());
		return dto;	
	}

	@Override 
	public Person createModel(PersonDTO dto) {
		if (dto == null) {
			return null;
		}
		Person model = new Person();
		model.setId(dto.getId());
		model.setName(nameAssembler.createModel(dto.getName()));
		model.setAddress(createAddressModel(dto.getAddress()));
		model.setBirthday(dto.getBirthday());
		model.setGWA(dto.getGWA());
		model.setCurrentlyEmployed(dto.getCurrentlyEmployed());
		model.setDateHired(dto.getDateHired());
		model.setGWA(dto.getGWA());
		model.setContacts(contactAssembler.createModel(dto.getContacts()).stream().collect(Collectors.toSet()));
		model.setRoles(roleAssembler.createModel(dto.getRoles()).stream().collect(Collectors.toSet()));
		return model;
	}

	private Address createAddressModel(AddressDTO dto) {
		if (dto == null) {
			return null;
		}
		Address model = new Address();
		model.setStreetNumber(dto.getStreetNumber());
		model.setBarangay(dto.getBarangay());
		model.setMunicipality(dto.getMunicipality());
		model.setZipCode(dto.getZipCode());
		return model;	
	}
}