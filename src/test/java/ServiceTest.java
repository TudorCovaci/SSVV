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
        int result = service.saveStudent(id, "tester", 933);
        assertEquals(1,result);
    }

    @Test
    public void test_addStudent_groupBelowRange() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 100);
        assertEquals(0,result);
    }

    @Test
    public void test_addStudent_groupAboveRange() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 999);
        assertEquals(0,result);
    }

    @Test
    public void test_addStudent_idEmptyString() {
        String id = "";
        int result = service.saveStudent(id, "tester", 933);
        assertEquals(0,result);
    }

    @Test
    public void test_addStudent_nameEmptyString() {
        String id = "1";
        int result = service.saveStudent(id, "", 933);
        assertEquals(0,result);
    }

    @Test
    public void test_addStudent_belowLowerBoundary() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 109);
        assertEquals(0,result);
    }

    @Test
    public void test_addStudent_onLowerBoundary() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 110);
        assertEquals(0,result);
    }

    @Test
    public void test_addStudent_lowerBoundaryLimit() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 111);
        assertEquals(1,result);
    }

    @Test
    public void test_addStudent_upperBoundaryLimit() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 937);
        assertEquals(1,result);
    }

    @Test
    public void test_addStudent_onUpperBoundary() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 938);
        assertEquals(0,result);
    }

    @Test
    public void test_addStudent_aboveUpperBoundary() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 939);
        assertEquals(0,result);
    }

}
