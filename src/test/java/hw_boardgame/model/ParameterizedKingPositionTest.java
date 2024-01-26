package hw_boardgame.model;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

public class ParameterizedKingPositionTest {
    void assertPosition(int expectedRow, int expectedCol, KingPosition position) {
        assertAll("position",
                () -> assertEquals(expectedRow, position.row()),
                () -> assertEquals(expectedCol, position.col())
        );
    }

    static Stream<KingPosition> positionProvider() {
        return Stream.of(new KingPosition(2, 0),
                new KingPosition(3, 7));
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void getPosition(KingPosition position) {
        assertPosition(position.row() - 1, position.col() -1, position.moveTo(KingDirection.UP_LEFT));
        assertPosition(position.row() - 1, position.col(), position.moveTo(KingDirection.UP));
        assertPosition(position.row() - 1, position.col() + 1, position.moveTo(KingDirection.UP_RIGHT));
        assertPosition(position.row(), position.col() + 1, position.moveTo(KingDirection.RIGHT));
        assertPosition(position.row() + 1, position.col() + 1, position.moveTo(KingDirection.DOWN_RIGHT));
        assertPosition(position.row() + 1, position.col(), position.moveTo(KingDirection.DOWN));
        assertPosition(position.row() + 1, position.col() - 1, position.moveTo(KingDirection.DOWN_LEFT));
        assertPosition(position.row(), position.col() - 1, position.moveTo(KingDirection.LEFT));
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void testToString(KingPosition position) {
        assertEquals(new KingPosition(position.row(), position.col()), position);
    }
}
