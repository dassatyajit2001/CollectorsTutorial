package com.java.demo.collectors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import static java.util.stream.Collectors.*;

//Exploring Collectors by Venkat Subramaniam
//https://www.youtube.com/watch?v=pGroX3gmeP8
//Streams are lazy evaluator

//Java has 2 reduce function: reduce and collect
//Lazy evaluation requires purity of functions
//Pure functions returns the same result any number of time given the same input: called idempotency
//Pure function dont change anything
//Pure function doesnt depend on anything that may possibily change
public class Demo {

	public static void main(String[] args) {
		List<Person> listPerson = List.of(new Person(20, "Amir"), new Person(22, "Bob"), new Person(32, "Gorge"),
				new Person(55, "Hermine"), new Person(40, "India"), new Person(55, "Jack"), new Person(57, "India"),
				new Person(59, "Lui"), new Person(25, "Catherine"), new Person(27, "Donald"), new Person(30, "Egor"),
				new Person(32, "Fany"), new Person(25, "Giros"), new Person(27, "Hermine"), new Person(37, "Iliena"),
				new Person(57, "Fany"));

		List<Person> listPersonNonDuplicate = List.of(new Person(20, "Amir"), new Person(22, "Bob"),
				new Person(32, "Gorge"));
		// list of all persons
		listPerson.stream().filter(p -> p.getAge() > 40).forEach(p -> System.out.println(p));
		System.out.println();
		// only take 2 values after the condition is met
		listPerson.stream().filter(p -> p.getAge() > 40).limit(2).forEach(p -> System.out.println(p));

		System.out.println();
		// if all the condition is matched then true else false. like And
		System.out.println(listPerson.stream().allMatch(p -> p.getAge() > 40));
		System.out.println(listPerson.stream().allMatch(p -> p.getAge() > 4));

		// if any 1 condition is matched like Or
		System.out.println();
		System.out.println(listPerson.stream().anyMatch(p -> p.getAge() > 400));

		System.out.println(listPerson.stream().anyMatch(p -> p.getAge() > 50));

		System.out.println();
		// sorting
		listPerson.stream().sorted(Comparator.comparingInt(p -> p.getAge())).forEach(p -> System.out.println(p));
		System.out.println();
		// sorting
		listPerson.stream().sorted((p1, p2) -> p1.getAge() - p2.getAge()).forEach(p -> System.out.println(p));
		// read about take while and drop while its not working
		System.out.println();
		listPerson.stream().sorted((p1, p2) -> p1.getAge() - p2.getAge()).dropWhile(p -> p.getAge() > 35)
				.forEach(p -> System.out.println(p));

		System.out.println();
		listPerson.stream().sorted((p1, p2) -> p1.getAge() - p2.getAge()).takeWhile(p -> p.getAge() > 35)
				.forEach(p -> System.out.println(p));

		System.out.println();
		// mapping on an attribute
		listPerson.stream().filter(p -> p.getAge() > 35).map(p -> p.getAge() * 2).forEach(p -> System.out.println(p));

		// mapping on whole object
		System.out.println();
		listPerson.stream().filter(p -> p.getAge() > 35).map(p -> map(p)).forEach(p -> System.out.println(p));

		// Reducer example
		System.out.println();
		System.out.println(
				listPerson.stream().map(p -> p.getAge() * 2).reduce(0, (total, age) -> total + age).intValue());
		// another way
		System.out.println(listPerson.stream().map(p -> p.getAge() * 2)
				.reduce(0, (total, age) -> Integer.sum(total, age)).intValue());
		// another way
		System.out.println(listPerson.stream().map(p -> p.getAge() * 2).reduce(0, Integer::sum));

		// another way
		System.out.println("listPerson.stream().mapToInt(p -> p.getAge() * 2).sum())--->"
				+ listPerson.stream().mapToInt(p -> p.getAge() * 2).sum());

		System.out.println("listPerson.stream().mapToInt(p -> p.getAge() * 2).max())--->"
				+ listPerson.stream().mapToInt(p -> p.getAge() * 2).max());

		System.out.println("listPerson.stream().mapToInt(p -> p.getAge() * 2).min())--->"
				+ listPerson.stream().mapToInt(p -> p.getAge() * 2).min());

		// parallel stream
		System.out.println(listPerson.parallelStream().map(p -> p.getAge() * 2).reduce(0, Integer::sum));

		// create a list of names in lower case who are more than 30
		// this is worst as it is shared mutability
		List<String> names = new ArrayList<>();
		System.out.println();
		listPerson.stream().filter(p -> p.getAge() > 30).map(p -> p.getName()).map(name -> name.toLowerCase())
				.forEach(p -> names.add(p));

		System.out.println();
		// here mutability is within the scope. as arraylist is created within the pure
		// function
		// Here we can use parallel stream as no global data structure is modified
		// but this is very error prone and ugly, so we use Collectors
		System.out.println(
				listPerson.stream().filter(p -> p.getAge() > 30).map(p -> p.getName()).map(name -> name.toLowerCase())
						// 1st param is the starting value,2nd paramis the operation and 3rd is the
						// combiner
						.reduce(new ArrayList<String>(), (names1, names2) -> {
							names1.add(names2);
							return names1;
						}, (names1, names2) -> {
							names1.addAll(names2);
							return names1;
						}));

		// using Collectors:
		System.out.println(listPerson.stream().filter(p -> p.getAge() > 30).map(p -> p.getName())
				.map(name -> name.toLowerCase()).collect(toList()));

		System.out.println(listPerson.stream().filter(p -> p.getAge() > 30).map(p -> p.getName())
				.map(name -> name.toLowerCase()).collect(toSet()));

		// create a map of perons
		System.out.println(listPersonNonDuplicate.stream().collect(toMap(Function.identity(), person -> person)));
		// this will work for non duplicate keys
		System.out.println(listPersonNonDuplicate.stream().collect(toMap(person -> person.getAge(), person -> person)));

		System.out.println(listPersonNonDuplicate.stream().collect(toMap(Person::getAge, person -> person)));
		// this will work for non duplicate keys
		System.out
				.println(listPerson.stream().collect(toMap(person -> person.getAge(), person -> person, (s, a) -> s)));

		// create a list of names in comma separated format
		System.out.println(listPerson.stream().map(person -> person.getName()).collect(joining(",")));

		// how to create a list of persons of even aged and odd aged
		// normally we can use filter twice with age%2==0 and age%2!=0
		// smart of doing it is by partitioningby
		System.out.println(listPerson.stream().collect(partitioningBy(p -> p.getAge() % 2 == 0)));

		// how to use count along with partition
		System.out.println(listPerson.stream().collect(partitioningBy(p -> p.getAge() % 2 == 0, counting())));

		// Grouping done in several list example Map if string,List
		System.out.println(listPerson.stream().collect(groupingBy(p -> p.getName())));

		// Grouping person's name and collect the lsit of ages for the common names
		// Map<String,List<integer>>
		// Grouping(Function<T,R>) T param, R is a collector, Another overloaded method
		// Grouping<Function<T,R>, Collector>
		// Lets exploit this
		System.out
				.println(listPerson.stream().collect(groupingBy(p -> p.getName(), mapping(p -> p.getAge(), toList()))));

		// Example for a set
		System.out
				.println(listPerson.stream().collect(groupingBy(p -> p.getName(), mapping(p -> p.getAge(), toSet()))));

		// Count the number of elements present in the list of a key
		System.out.println(listPerson.stream().collect(groupingBy(p -> p.getName(), counting())));
		// counting() returns a Long but what if we want Integer instead of
		// Long
		// That means groupingBy (Function,Collector)
		// But we want is to convert a collector and map it to a Int
		// so collectingBy(Collector,Function)
		System.out.println(listPerson.stream()
				.collect(groupingBy(p -> p.getName(), collectingAndThen(counting(), i -> i.intValue()))));

		// MaxBy,MinBy Find the person with maximum age,minimum age
		System.out.println(listPerson.stream().collect(maxBy(Comparator.comparing(Person::getAge))));
		System.out.println(listPerson.stream().collect(minBy(Comparator.comparing(Person::getAge))));

		// What if we want the person name andnot the whole person object
		// That means we have to map the person to the name sp use collectingAndThen`
		System.out.println(listPerson.stream().collect(collectingAndThen(minBy(Comparator.comparing(Person::getAge)),
				p -> p.map(Person::getName).orElse(""))));
	}

	// another form

	public static Person map(Person p) {
		p.setAge(p.getAge() * 10);
		return p;
	}
}

class Person {
	int age;

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	String name;

	public Person(int age, String name) {
		super();
		this.age = age;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Person [age=" + age + ", name=" + name + "]";
	}

}
