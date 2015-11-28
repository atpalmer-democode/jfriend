#JFriend

This project is a short code sample I prepared from an interview question in response to the following prompt:

> Create a person object with the following properties and methods:  
> 
> **Properties**  
> 
> * First Name
> * Last Name
> * Date of Birth
> * List of Friends (Person Objects)
> * Override System.Object’s Equals method
> 
> **Methods**  
> 
> * Add a new friend to the friends’ list. The method should accept a Person object as a parameter and add that person to the list of friends. The method should return false if the friend is already contained in the friend’s list and true if the user was added without issue.
> * Remove a friend from the friends’ list. The method should accept a Person object as a parameter and remove that person from the list of friends. The method should return false if the friend is not on the friend’s list and true if the user was removed without issue.
> * Get a list of all friends whose birthday is today. The method should not have any parameters and return a list of People.
> * Get a list of mutual friends. The method should accept a Person object as a parameter and return a list of People.

## Discussion

Although this is actually a C#-specific prompt, I have coded an implementation in Java. I also did not necessarily hold myself to following the prompt literally in other respects. If a better design choice conflicted with a requirement, I chose the better design.

### Instance Control

Each person is an individual. Likewise, each `Person` object should be unique in the system. Rather than exposing a constructor, a static factory method checks whether the requested `Person` object has already been created and, if so, returns that object. If not, a new object is returned.

Reinforcing this concept, the "List of Friends" is actually implemented as a `HashSet<Person>`, since no `Person` should exist in the collection more than once (and since that type is optimal for add, remove, and search operations).

### API

The public accessor `getFriends` returns an unmodifiable collection, encapsulating the underlying collection.

The `addFriend` and `removeFriend` methods take the important step of ensuring that friendships are mutual. These methods modify both the friend collections of the current `Person` object and the `Person` provided as an argument, such that when Person A is a friend of Person B, Person B is also a friend of Person A.

A `hasFriend` method is not included in the specification, but is included as an additional API feature. The method simply delegates to the friend collection's `contains` method.

The `getFriendsWithBirthdayToday` is implemented on top of the method `getFriendsWithThisBirthday`, the latter of which is not required by the prompt, but exposed as part of the class's API to increase the usefulness and flexibility of the class.

### Java 8 Techniques

Java 8 date classes `LocalDate` and `MonthDay` are used to represent dates. With these immutable date classes, there is no need to make defensive copies of the object to provide proper encapsulation, and they can be used to generate hash codes which will reliably return the same value.

The `getMutualFriends` and `getFriendsWithThisBirthday` methods are built on top of the private `filterFriends` method which, if determined to be useful as part of the class's API, can easily be exposed. The method is implemented with Java 8 streams which provide a fluent interface for filtering according to a given condition provided by the Java 8 functional interface `Predicate<Person>`. This predicate may be specified using Java 8 method references or lambda expressions.

### Object Overrides

The design of the `Person` class is that each instance is fundamentally identified by three immutable fields, `firstName`, `lastName`, and `dateOfBirth`, each of which is assigned in the class's constructor. These properties serve as the basis for the implementation of the `equals` and `hashCode` methods. A `hashCode` method, though not specified as a requirement in the prompt, is required for the correct behavior of the object when stored in a `HashSet` (such as the friends collection) or `HashMap` (such as the `Person` cache). The `hashCode` method is used in conjunction with the `equals` method for hash lookups and must be overridden when `equals` is overridden.
