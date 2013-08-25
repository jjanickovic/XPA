package org.xpa.example.data.people;

import java.util.ArrayList;
import java.util.List;

import org.xpa.example.data.people.Person.Gender;
import org.xpaframework.xml.annotation.XmlElement;
import org.xpaframework.xml.annotation.XmlRootElement;


@XmlRootElement
public class People {

	@XmlElement(name = "person")
	private List<Person> people = new ArrayList<Person>();

	public List<Person> getPeople() {
		return people;
	}

	public void setPeople(List<Person> people) {
		this.people = people;
	}
	
	public int getCount() {
		int count = 0;
		
		for(Person person : this.people) {
			count += person.getCount();
		}
		
		return count;
	}

	public static People createPeople(int size) {
		People people = new People();
		List<Person> persons = new ArrayList<Person>();
		
		for(int i = 0; i < size; i++) {
			Person p = new Person();
			p.setGender(Gender.values()[i % 2]);
			p.setName("Name");
			p.setSurname("Surname");
			persons.add(p);
		}
		
		people.setPeople(persons);
		return people;
	}
}
