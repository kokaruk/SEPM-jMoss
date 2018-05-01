package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author dimz
 * @since 1/5/18.
 */
@DisplayName("Session class unit testing")
class SessionTest {



    private Session session;

    @Nested
    @DisplayName("Add booking")
    class Session_addBooking {
        @Mock private Cinema cinema;
        @Mock private Booking booking;

        @BeforeEach
        void setUp(){
            MockitoAnnotations.initMocks(this);
            session = new Session(1,2,3,4);
            doNothing().when(cinema).addSession(session);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5})
        @DisplayName("Add booking, verify cinema get max seats was called")
        void addBooking_verify_max_seats_was_called(int argument) {
           when(cinema.getMAX_SEATS()).thenReturn(argument);
           //ArgumentCaptor<Integer> capturingArgs = ArgumentCaptor.forClass(Integer.class);
           session.setCinema(cinema);
           session.addBooking(booking);
           verify(cinema, times(1)).getMAX_SEATS();
           assertEquals(argument, cinema.getMAX_SEATS());
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