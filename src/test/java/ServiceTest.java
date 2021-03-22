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
        String id = String.valueOf(new Random().longs(100000,300000).findFirst().orElse(100000));
        int result = service.saveStudent(id, "Test User" + id, 933);
        assertEquals(result,1);
    }

    @Test
    public void test_addStudent_nullName() {
        int result = service.saveStudent(String.valueOf(new Random().longs(100000,300000).findFirst().orElse(100000)), null, 933);
        assertEquals(result,1);
    }
}
