package com.palmeroo.kata.jfriend;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Collection;
import java.util.Collections;

import static java.time.Month.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class PersonTests {
    private static final Person SOME_PERSON = Person.get("a", "b", LocalDate.of(2000, JANUARY, 1));
    private static final Person ANOTHER_PERSON = Person.get("c", "d", LocalDate.of(2001, FEBRUARY, 2));

    @After
    public void after() {
        SOME_PERSON.clearFriends();
    }

    @Test
    public void factory_method_returns_same_instance() {
        Person samePerson = Person.get("a", "b", LocalDate.of(2000, JANUARY, 1));
        assertThat(SOME_PERSON, is(sameInstance(samePerson)));
    }

    @Test
    public void getBirthday_returns_equal_DayMonth() {
        assertThat(SOME_PERSON.getBirthday(), is(equalTo(MonthDay.of(JANUARY, 1))));
    }

    @Test
    public void getFriends_returns_collection_of_added_friends() {
        SOME_PERSON.addFriend(ANOTHER_PERSON);
        Collection<Person> expected = Collections.singleton(ANOTHER_PERSON);
        assertThat(SOME_PERSON.getFriends().toArray(), is(equalTo(expected.toArray())));
    }

    @Test
    public void getFriends_returns_empty_collection_after_removing_added_friend() {
        SOME_PERSON.addFriend(ANOTHER_PERSON);
        SOME_PERSON.removeFriend(ANOTHER_PERSON);
        assertThat(SOME_PERSON.getFriends().size(), is(0));
    }

    @Test
    public void addFriend_returns_true_when_friend_was_added() {
        assertThat(SOME_PERSON.addFriend(ANOTHER_PERSON), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFriend_throws_exception_when_adding_self() {
        SOME_PERSON.addFriend(SOME_PERSON);
        Assert.fail();
    }

    @Test
    public void addFriend_returns_false_when_friend_already_exists() {
        SOME_PERSON.addFriend(ANOTHER_PERSON);
        assertThat(SOME_PERSON.addFriend(ANOTHER_PERSON), is(false));
    }

    @Test
    public void hasFriend_returns_true_when_friend_added() {
        SOME_PERSON.addFriend(ANOTHER_PERSON);
        assertThat(SOME_PERSON.hasFriend(ANOTHER_PERSON), is(true));
    }

    @Test
    public void hasFriend_returns_false_when_friend_not_added() {
        assertThat(SOME_PERSON.hasFriend(ANOTHER_PERSON), is(false));
    }

    @Test
    public void hasFriend_returns_true_when_added_as_friend() {
        SOME_PERSON.addFriend(ANOTHER_PERSON);
        assertThat(ANOTHER_PERSON.hasFriend(SOME_PERSON), is(true));
    }

    @Test
    public void hasFriend_returns_false_when_removed_as_friend() {
        SOME_PERSON.addFriend(ANOTHER_PERSON);
        SOME_PERSON.removeFriend(ANOTHER_PERSON);
        assertThat(ANOTHER_PERSON.hasFriend(SOME_PERSON), is(false));
    }

    @Test
    public void getFriends_returns_empty_collection_after_clearFriends() {
        SOME_PERSON.addFriend(ANOTHER_PERSON);
        SOME_PERSON.clearFriends();
        assertThat(SOME_PERSON.getFriends().size(), is(0));
    }

    @Test
    public void hasFriend_returns_false_after_clearFriends_on_friend() {
        SOME_PERSON.addFriend(ANOTHER_PERSON);
        SOME_PERSON.clearFriends();
        assertThat(ANOTHER_PERSON.hasFriend(SOME_PERSON), is(false));
    }

    @Test
    public void getMutualFriends_returns_mutual_friend() {
        Person mutualFriend = Person.get("e", "f", LocalDate.of(2002, MARCH, 31));
        SOME_PERSON.addFriend(mutualFriend);
        ANOTHER_PERSON.addFriend(mutualFriend);
        assertThat(SOME_PERSON.getMutualFriends(ANOTHER_PERSON), is(new Person[]{mutualFriend}));
    }

    @Test
    public void getFriendsWithThisBirthday_returns_matching_friend() {
        SOME_PERSON.addFriend(ANOTHER_PERSON);
        assertThat(SOME_PERSON.getFriendsWithThisBirthday(MonthDay.of(FEBRUARY, 2)), is(new Person[]{ANOTHER_PERSON}));
    }
}
