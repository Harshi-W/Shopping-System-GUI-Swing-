import org.junit.jupiter.api.BeforeEach;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class WestminsterShoppingManagerTest {
    private WestminsterShoppingManager shoppingManager;

    @BeforeEach
    void setUp() {
        ArrayList<Product> products = new ArrayList<>();
        shoppingManager = new WestminsterShoppingManager(products);
    }

    @org.junit.jupiter.api.Test
    void userDetails() {
        String testInput = "input\n";
        System.setIn(new ByteArrayInputStream(testInput.getBytes()));
        String testResult = shoppingManager.userDetails("Enter details: ");
        assertEquals("input", testResult);
    }

    @org.junit.jupiter.api.Test
    void inputValidatorPrice() {
        String testInput = "500\n";
        System.setIn(new ByteArrayInputStream(testInput.getBytes()));
        int testResult = shoppingManager.inputValidatorItems("Enter price: ");
        assertEquals(500.0, testResult);
    }

    @org.junit.jupiter.api.Test
    void inputValidatorItems() {
        String testInput = "5\n";
        System.setIn(new ByteArrayInputStream(testInput.getBytes()));
        int testResult = shoppingManager.inputValidatorItems("Enter no of items: ");
        assertEquals(5, testResult);
    }


    @org.junit.jupiter.api.Test
    void saveFile() {
        File testFile = new File("File.txt");
        assertTrue(testFile.exists());
    }

}