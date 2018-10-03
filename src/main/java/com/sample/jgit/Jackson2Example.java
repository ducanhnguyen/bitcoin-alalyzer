package com.sample.jgit;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * https://www.mkyong.com/java/jackson-2-convert-java-object-to-from-json/
 * 
 * @author adn0019
 *
 */
public class Jackson2Example {
	public static void main(String[] args) {
		Jackson2Example obj = new Jackson2Example();
		obj.run();
	}

	private void run() {
		ObjectMapper mapper = new ObjectMapper();

		Staff staff = createDummyObject();

		try {
			// Convert object to JSON string and save into a file directly
			mapper.writeValue(new File("./staff.json"), staff);

			// Convert object to JSON string
			String jsonInString = mapper.writeValueAsString(staff);
			System.out.println(jsonInString);

			// Convert object to JSON string and pretty print
			jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(staff);
			System.out.println(jsonInString);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Staff createDummyObject() {

		Staff staff = new Staff();

		staff.setName("mkyong");
		staff.setAge(33);
		staff.setPosition("Developer");
		staff.setSalary(new BigDecimal("7500"));

		List<String> skills = new ArrayList<String>();
		skills.add("java");
		skills.add("python");

		staff.setSkills(skills);

		return staff;

	}

	class Staff {

		private String name;
		private int age;
		private String position;
		private BigDecimal salary;
		private List<String> skills;

		public void setName(String name) {
			this.name = name;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public void setSkills(List<String> skills) {
			this.skills = skills;
		}

		public void setSalary(BigDecimal salary) {
			this.salary = salary;
		}
	}
}
