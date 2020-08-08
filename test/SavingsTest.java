import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SavingsTest {

    @Test
    void getCustNum() {
        Savings savings = new Savings("123", "aksd","Deluxe",123.12, 8);

        assertNotNull(savings.getCustNum());
    }

    @Test
    void getCustName() {
        Savings savings = new Savings("123", "aksd","Deluxe",123.12, 8);

        assertNotNull(savings.getCustName());
    }
}