package hw_boardgame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KingPositionTest {
    KingPosition position;

    void assertPosition(int expectedRow, int expectedCol, KingPosition position) {
        assertAll("position",
                () -> assertEquals(expectedRow, position.row()),
                () -> assertEquals(expectedCol, position.col())
        );
    }

    @BeforeEach
    void init() {
        position = new KingPosition(0, 0);
    }

    @Test
    void getPosition() {
        assertPosition(-1, -1, position.moveTo(KingDirection.UP_LEFT));
        assertPosition(-1, 0, position.moveTo(KingDirection.UP));
        assertPosition(-1, 1, position.moveTo(KingDirection.UP_RIGHT));
        assertPosition(0, 1, position.moveTo(KingDirection.RIGHT));
        assertPosition(1, 1, position.moveTo(KingDirection.DOWN_RIGHT));
        assertPosition(1, 0, position.moveTo(KingDirection.DOWN));
        assertPosition(1, -1, position.moveTo(KingDirection.DOWN_LEFT));
        assertPosition(0, -1, position.moveTo(KingDirection.LEFT));
    }

    @Test
    void testToString() {
        assertEquals(new KingPosition(0, 0), position);
    }
}