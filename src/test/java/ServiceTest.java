import domain.Student;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    private final Service service = new Service(
            new StudentXMLRepository(new StudentValidator(), "studenti.xml"),
            new TemaXMLRepository(new TemaValidator(), "teme.xml"),
            new NotaXMLRepository(new NotaValidator(), "note.xml"));

    @Test
    public void test_addStudent_success() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 1);
        assertEquals(result,1);
    }

    @Test
    public void test_addStudent_negativeGroup() {
        String id = "1";
        int result = service.saveStudent(id, "tester", -1);
        assertEquals(result,0);
    }

    @Test
    public void test_addStudent_emptyId() {
        String id = "";
        int result = service.saveStudent(id, "tester", 1);
        assertEquals(result,0);
    }

    @Test
    public void test_addStudent_emptyName() {
        String id = "1";
        int result = service.saveStudent(id, "", 1);
        assertEquals(result,0);
    }

    @Test
    public void test_addStudent_zeroGroup() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 0);
        assertEquals(result,0);
    }
}
