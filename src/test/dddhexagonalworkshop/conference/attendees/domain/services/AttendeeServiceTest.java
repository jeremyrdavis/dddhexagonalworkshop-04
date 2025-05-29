package  dddhexagonalworkshop.conference.attendees.domain.services;

import dddhexagonalworkshop.conference.attendees.domain.events.AttendeeRegisteredEvent;
import dddhexagonalworkshop.conference.attendees.domain.services.AttendeeService;
import dddhexagonalworkshop.conference.attendees.domain.valueobjects.MealPreference;
import dddhexagonalworkshop.conference.attendees.domain.valueobjects.TShirtSize;
import dddhexagonalworkshop.conference.attendees.infrastructure.AttendeeEventPublisher;
import dddhexagonalworkshop.conference.attendees.persistence.AttendeeEntity;
import dddhexagonalworkshop.conference.attendees.persistence.AttendeeRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

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
    }
}
