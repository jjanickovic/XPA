package org.xpa.example.data.people;

import org.xpaframework.ValueAdapter;
import org.xpaframework.ValueConversionException;
import org.xpaframework.xml.annotation.XmlAttribute;
import org.xpaframework.xml.annotation.XmlTransient;
import org.xpaframework.xml.annotation.XmlValueAdapter;



public class Person implements ValueAdapter<Person.Gender> {

	private String name;
	
	private String surname;
	
	@XmlAttribute
	@XmlValueAdapter(adapter = Person.class)
	private Gender gender;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.count++;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
		this.count++;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
		this.count++;
	}

	public enum Gender {
		MALE("Mr.", "M"),
		FEMALE("Mrs.", "F");
		
		private String label;
		
		public String getLabel() {
			return label;
		}

		private String value;
		
		public String getValue() {
			return value;
		}
		
		public static Gender fromValue(String value) {
			if(value == null) {
				throw new NullPointerException();
			}
			
			for(Gender gender : Gender.values()) {
				if(gender.getValue().equalsIgnoreCase((value))) {
					return gender;
				}
			}
			
			throw new IllegalArgumentException("No gender for value: " + value);
		}

		private Gender(String label, String value) {
			this.label = label;
			this.value = value;
		}
		
	}

	public String getFullName() {
		return gender.getLabel() + " " + name + " " + surname;
	}
	
	@Override
	public String toString() {
		return "Person [" + gender + "]: " + name + " " + surname;
	}
	
	@XmlTransient
	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int elementCount) {
		this.count = elementCount;
	}

	@Override
	public Class<?> getType() {
		return Gender.class;
	}

	@Override
	public Gender convertValue(String value) throws ValueConversionException {
		if(value == null) {
			return null;
		}
		
		return Gender.fromValue(value);
	}

	@Override
	public String toString(Gender target) {
		return target == null ? null : target.getValue();
	}
}
