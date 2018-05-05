package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author dimz
 * @since 1/5/18.
 */
@DisplayName("Session class unit testing")
class SessionTest {

    @Mock private Cinema cinema;
    @Mock private Booking booking;
    @Captor private ArgumentCaptor<Session> argCaptor;

    private Session session;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        session = new Session(0,1,2,3,4);
    }

    @Test
    @DisplayName("Confirm set cinema method also adds session to cinema")
    void setCinema_addsSessionToCinema(){
        session.setCinema(cinema);
        Mockito.verify(cinema).addSession(argCaptor.capture());
        assertEquals(session, argCaptor.getValue());
    }

    @Nested
    @DisplayName("Add booking method tests")
    class Session_addBooking {

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5})
        @DisplayName("Add booking, verify cinema get max seats was called correctly")
        void addBooking_verify_max_seats_was_called(int argument) {
           when(cinema.getMAX_SEATS()).thenReturn(argument);
           //ArgumentCaptor<Integer> capturingArgs = ArgumentCaptor.forClass(Integer.class);
           session.setCinema(cinema);
           session.addBooking(booking);
           verify(cinema, atMost(1)).getMAX_SEATS();
           assertEquals(argument, session.getCinema().getMAX_SEATS());
        }

        @Test
        @DisplayName("Add booking, throws error because too many bookings")
        void addBooking_ThrowTooManyBookings_Error(){
            when(cinema.getMAX_SEATS()).thenReturn(0);
            session.setCinema(cinema);
            assertThrows(IndexOutOfBoundsException.class, () -> session.addBooking(booking) );
        }
    }

}