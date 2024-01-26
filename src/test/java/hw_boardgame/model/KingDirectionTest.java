package hw_boardgame.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KingDirectionTest {
    @Test
    void of() {
        assertSame(KingDirection.UP_LEFT, KingDirection.of(-1, -1));
        assertSame(KingDirection.UP, KingDirection.of(-1, 0));
        assertSame(KingDirection.UP_RIGHT, KingDirection.of(-1, 1));
        assertSame(KingDirection.RIGHT, KingDirection.of(0, 1));
        assertSame(KingDirection.DOWN_RIGHT, KingDirection.of(1, 1));
        assertSame(KingDirection.DOWN, KingDirection.of(1, 0));
        assertSame(KingDirection.DOWN_LEFT, KingDirection.of(1, -1));
        assertSame(KingDirection.LEFT, KingDirection.of(0, -1));
    }
    @Test
    void of_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> KingDirection.of(0, 0));
    }
}