# Workshop Workflow

## Iteration 04: Testing

### DDD Concepts: Testability

### Overview

```text
dddhexagonalworkshop
├── conference
│   └── attendees
│       ├── domain
│       │   ├── aggregates
│       │   │   └── Attendee.java
│       │   ├── events
│       │   │   └── AttendeeRegisteredEvent.java
│       │   ├── services
│       │   │   ├── AttendeeRegistrationResult.java
│       │   │   └── AttendeeService.java
│       │   │   └── RegisterAttendeeCommand.java
│       │   └── valueobjects
│       │       └── Address.java
│       ├── infrastructure
│       │   ├── AttendeeEndpoint.java
│       │   ├── AttendeeDTO.java
│       │   └── AttendeeEventPublisher.java
│       ├── integration
│       │   └── salesteam
│       │       ├── Customer.java
│       │       ├── CustomerDetails.java
│       │       ├── DietaryRequirements.java
│       │       ├── SalesteamEndpoint.java
│       │       ├── SalesteamRegistrationRequest.java
│       │       └── SalesteamToDomainTranslator.java
│       └── persistence
│           ├── AttendeeEntity.java
│           └── AttendeeRepository.java
```

As you progress through the workshop, you will fill in the missing pieces of code in the appropriate packages. The workshop authors have stubbed out the classes so that you can focus on the Domain Driven Design concepts as much as possible and Java and framework concepts as little as possilb.
You can type in the code line by line or copy and paste the code provided into your IDE. You can also combine the approaches as you see fit. The goal is to understand the concepts and how they fit together in a DDD context.

**Quarkus**

Quarkus, https://quarkus.io, is a modern Java framework designed for building cloud-native applications. It provides a set of tools and libraries that make it easy to develop, test, and deploy applications. In this workshop, we will leverage Quarkus to implement our DDD concepts and build a RESTful API for registering attendees.
The project uses Quarkus, a Java framework that provides built-in support for REST endpoints, JSON serialization, and database access. Quarkus also features a `Dev Mode` that automatically spins up external dependencies like Kafka and PostgreSQL, allowing you to focus on writing code without worrying about the underlying infrastructure.

**Steps:**

#### 1. Create a Unit Test for the Attendee aggregate

One of the advantages of encapsulating the business logic in an aggregate is that we can test it independently of the rest of the system. Create a unit test for the `Attendee` aggregate.

```java
package dddhexagonalworkshop.conference.attendees.domain.aggregates;

import dddhexagonalworkshop.conference.attendees.domain.events.AttendeeRegisteredEvent;
import dddhexagonalworkshop.conference.attendees.domain.services.AttendeeRegistrationResult;
import dddhexagonalworkshop.conference.attendees.domain.valueobjects.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class AttendeeTest {

    @Test
    @DisplayName("Should successfully register an attendee")
    public void testRegisterAttendee() {
        // Arrange
        String email = "frodo.baggins@shire.me";
        String firstName = "Frodo";
        String lastName = "Baggins";
        Address address = new Address(
                "Bag End",
                "Bagshot Row",
                "Hobbiton",
                "The Shire",
                "SH1R3",
                "Middle Earth"
        );

        // Act
        AttendeeRegistrationResult result = Attendee.registerAttendee(email, firstName, lastName, address);
        Attendee attendee = result.attendee();
        AttendeeRegisteredEvent event = result.attendeeRegisteredEvent();

        // Assert
        assertNotNull(attendee, "Attendee should not be null");
        assertNotNull(event, "Event should not be null");
        assertEquals(email, attendee.getEmail(), "Email should match");
        assertEquals("Frodo Baggins", attendee.getFullName(), "Full name should match");
        assertEquals(address, attendee.getAddress(), "Address should match");
        assertEquals(email, event.email(), "Event email should match");
        assertEquals("Frodo Baggins", event.fullName(), "Event full name should match");
    }

    @Test
    @DisplayName("Should get correct full name")
    public void testGetFullName() {
        // Arrange
        Address address = new Address(
                "Meduseld",
                null,
                "Edoras",
                "Rohan",
                "RH4N",
                "Middle Earth"
        );
        Attendee attendee = new Attendee("eowyn@rohan.me", "Éowyn", "of Rohan", address);

        // Act
        String fullName = attendee.getFullName();

        // Assert
        assertEquals("Éowyn of Rohan", fullName, "Full name should be correctly concatenated");
    }

    @Test
    @DisplayName("Should get correct address")
    public void testGetAddress() {
        // Arrange
        Address address = new Address(
                "Citadel",
                "7th Level",
                "Minas Tirith",
                "Gondor",
                "MT777",
                "Middle Earth"
        );
        Attendee attendee = new Attendee("aragorn@gondor.me", "Aragorn", "Elessar", address);

        // Act
        Address retrievedAddress = attendee.getAddress();

        // Assert
        assertNotNull(retrievedAddress, "Address should not be null");
        assertEquals(address, retrievedAddress, "Retrieved address should match the original");
        assertEquals("Citadel", retrievedAddress.street(), "Street should match");
        assertEquals("7th Level", retrievedAddress.street2(), "Street2 should match");
        assertEquals("Minas Tirith", retrievedAddress.city(), "City should match");
        assertEquals("Gondor", retrievedAddress.stateOrProvince(), "State should match");
        assertEquals("MT777", retrievedAddress.postCode(), "Post code should match");
        assertEquals("Middle Earth", retrievedAddress.country(), "Country should match");
    }

    @Test
    @DisplayName("Should get correct email")
    public void testGetEmail() {
        // Arrange
        Address address = new Address(
                "Grey Havens",
                null,
                "Lindon",
                "Eriador",
                "GH123",
                "Middle Earth"
        );
        String email = "gandalf@istari.me";
        Attendee attendee = new Attendee(email, "Gandalf", "the Grey", address);

        // Act
        String retrievedEmail = attendee.getEmail();

        // Assert
        assertEquals(email, retrievedEmail, "Email should match");
    }

    @Test
    @DisplayName("Should verify AttendeeRegisteredEvent contains correct data")
    public void testAttendeeRegisteredEvent() {
        // Arrange
        String email = "legolas@mirkwood.me";
        String firstName = "Legolas";
        String lastName = "Greenleaf";
        Address address = new Address(
                "Royal Palace",
                "Woodland Realm",
                "Mirkwood",
                "Rhovanion",
                "MK001",
                "Middle Earth"
        );

        // Act
        AttendeeRegistrationResult result = Attendee.registerAttendee(email, firstName, lastName, address);
        AttendeeRegisteredEvent event = result.attendeeRegisteredEvent();

        // Assert
        assertNotNull(event, "Event should not be null");
        assertEquals(email, event.email(), "Event email should match");
        assertEquals("Legolas Greenleaf", event.fullName(), "Event full name should match");
    }
}
```

#### 2. Create an Integration Test for the AttendeeService

```java
package test.dddhexagonalworkshop.conference.attendees.domain.services;

import dddhexagonalworkshop.conference.attendees.domain.valueobjects.Address;
import dddhexagonalworkshop.conference.attendees.domain.valueobjects.MealPreference;
import dddhexagonalworkshop.conference.attendees.domain.valueobjects.TShirtSize;
import dddhexagonalworkshop.conference.attendees.domain.commands.RegisterAttendeeCommand;
import dddhexagonalworkshop.conference.attendees.domain.events.AttendeeRegisteredEvent;
import dddhexagonalworkshop.conference.attendees.domain.repositories.AttendeeRepository;
import dddhexagonalworkshop.conference.attendees.domain.services.AttendeeService;
import dddhexagonalworkshop.conference.attendees.domain.publishers.AttendeeEventPublisher;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class AttendeeServiceTest {

    @Inject
    AttendeeService attendeeService;

    @InjectMock
    AttendeeRepository attendeeRepository;

    @InjectMock
    AttendeeEventPublisher attendeeEventPublisher;

    @BeforeEach
    public void setUp() {
        // Setup code if needed, e.g., mocking behavior of injected components
        Mockito.doNothing().when(attendeeRepository).persist(any(AttendeeEntity.class));
        Mockito.doNothing().when(attendeeEventPublisher).publish(any(AttendeeRegisteredEvent.class));
    }

    @Test
    public void testRegisterAttendee() {
        // Given: Prepare the command to register an attendee with Lord of the Rings data
        RegisterAttendeeCommand command = new RegisterAttendeeCommand(
                "frodo.baggins@shire.me",
                "Frodo",
                "Baggins",
                new dddhexagonalworkshop.conference.attendees.domain.valueobjects.Address(
                        "Bag End",
                        "Bagshot Row",
                        "Hobbiton",
                        "The Shire",
                        "SH1 1RE",
                        "Middle Earth"
                ),
                dddhexagonalworkshop.conference.attendees.domain.valueobjects.MealPreference.VEGETARIAN,
                dddhexagonalworkshop.conference.attendees.domain.valueobjects.TShirtSize.S
        );

        // When: Call the service method
        attendeeService.registerAttendee(command);

        // Then: Verify the repository and event publisher were called
        Mockito.verify(attendeeRepository).persist(any(
                dddhexagonalworkshop.conference.attendees.domain.aggregates.Attendee.class));
        Mockito.verify(attendeeEventPublisher).publish(any(
                dddhexagonalworkshop.conference.attendees.domain.events.AttendeeRegisteredEvent.class));
    }
}
```

## Summary

### Key points
