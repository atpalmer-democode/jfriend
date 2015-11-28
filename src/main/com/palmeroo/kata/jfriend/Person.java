package com.palmeroo.kata.jfriend;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.*;
import java.util.function.Predicate;

public class Person {
    private static final Map<Person, Person> PERSON_CACHE = new HashMap<>();
    private final Set<Person> friends = new HashSet<>();
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;

    private Person(String firstName, String lastName, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public static Person get(String firstName, String lastName, LocalDate dateOfBirth) {
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
        Objects.requireNonNull(dateOfBirth);

        Person newPerson = new Person(firstName, lastName, dateOfBirth);
        Optional<Person> existingPerson = Optional.ofNullable(PERSON_CACHE.putIfAbsent(newPerson, newPerson));
        return existingPerson.orElse(newPerson);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public MonthDay getBirthday() {
        return MonthDay.from(dateOfBirth);
    }

    public Collection<Person> getFriends() {
        return Collections.unmodifiableCollection(friends);
    }

    public boolean addFriend(Person arg) {
        Objects.requireNonNull(arg);
        if(arg == this) throw new IllegalArgumentException("Cannot add person as its own friend.");

        boolean isArgAddedToThis = this.friends.add(arg);
        boolean isThisAddedToArg = arg.friends.add(this);
        assert isArgAddedToThis == isThisAddedToArg;

        return isArgAddedToThis;
    }

    public boolean removeFriend(Person arg) {
        Objects.requireNonNull(arg);

        boolean isArgRemovedFromThis = this.friends.remove(arg);
        boolean isThisRemovedFromArg = arg.friends.remove(this);
        assert isArgRemovedFromThis == isThisRemovedFromArg;

        return isArgRemovedFromThis;
    }

    public void clearFriends() {
        friends.forEach(f -> f.friends.remove(this));
        friends.clear();
    }

    public boolean hasFriend(Person arg) {
        Objects.requireNonNull(arg);
        return friends.contains(arg);
    }

    public Person[] getMutualFriends(Person arg) {
        Objects.requireNonNull(arg);
        return filterFriends(arg.friends::contains);
    }

    public Person[] getFriendsWithBirthdayToday() {
        MonthDay today = MonthDay.now();
        return getFriendsWithThisBirthday(today);
    }

    public Person[] getFriendsWithThisBirthday(MonthDay arg) {
        Objects.requireNonNull(arg);
        return filterFriends(p -> p.getBirthday().equals(arg));
    }

    private Person[] filterFriends(Predicate<Person> filter) {
        return friends.stream().filter(filter).toArray(Person[]::new);
    }

    @Override
    public boolean equals(Object arg) {
        if (!(arg instanceof Person)) {
            return false;
        }

        Person person = (Person) arg;
        return person.firstName.equals(this.firstName)
            && person.lastName.equals(this.lastName)
            && person.dateOfBirth.equals(this.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, dateOfBirth);
    }
}
