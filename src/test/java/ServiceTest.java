import domain.Nota;
import domain.Pair;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ServiceTest {


    private StudentXMLRepository studentXmlRepo;
    private TemaXMLRepository temaXmlRepo;
    private NotaXMLRepository notaXMLRepository;
    private Service service;


    @BeforeEach
    public void setup() {
        String rootXmlDoc = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<Entitati/>";
        File file = new File("testStudenti.xml");
        try {
            file.createNewFile();
            BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
            fos.write(rootXmlDoc.getBytes(StandardCharsets.UTF_8));
            fos.flush();
            file = new File("testTeme.xml");
            fos = new BufferedOutputStream(new FileOutputStream(file));
            fos.write(rootXmlDoc.getBytes(StandardCharsets.UTF_8));
            fos.flush();
            file.createNewFile();
            file = new File("testNote.xml");
            file.createNewFile();
            fos = new BufferedOutputStream(new FileOutputStream(file));
            fos.write(rootXmlDoc.getBytes(StandardCharsets.UTF_8));
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        studentXmlRepo = new StudentXMLRepository(new StudentValidator(), "testStudenti.xml");
        temaXmlRepo = new TemaXMLRepository(new TemaValidator(), "testTeme.xml");
        notaXMLRepository = new NotaXMLRepository(new NotaValidator(), "testNote.xml");
        service = new Service(studentXmlRepo, temaXmlRepo, notaXMLRepository);
    }

    @AfterEach
    public void teardown() {
        File file = new File("testStudenti.xml");
            file.delete();
            file = new File("testTeme.xml");
            file.delete();
            file = new File("testNote.xml");
            file.delete();

//        studentXmlRepo.findAll().forEach(s -> studentXmlRepo.delete(s.getID()));
//        temaXmlRepo.findAll().forEach(t -> temaXmlRepo.delete(t.getID()));
//        notaXMLRepository.findAll().forEach(n -> notaXMLRepository.delete(n.getID()));
    }

    @Test
    public void test_addStudent_success() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 933);
        assertEquals(1, result);
    }

    @Test
    public void test_addStudent_groupBelowRange() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 100);
        assertEquals(0, result);
    }

    @Test
    public void test_addStudent_groupAboveRange() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 999);
        assertEquals(0, result);
    }

    @Test
    public void test_addStudent_idEmptyString() {
        String id = "";
        int result = service.saveStudent(id, "tester", 933);
        assertEquals(0, result);
    }

    @Test
    public void test_addStudent_nameEmptyString() {
        String id = "1";
        int result = service.saveStudent(id, "", 933);
        assertEquals(0, result);
    }

    @Test
    public void test_addStudent_belowLowerBoundary() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 109);
        assertEquals(0, result);
    }

    @Test
    public void test_addStudent_onLowerBoundary() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 110);
        assertEquals(0, result);
    }

    @Test
    public void test_addStudent_lowerBoundaryLimit() {
        String id = "111";
        int result = service.saveStudent(id, "tester", 111);
        assertEquals(1, result);
    }

    @Test
    public void test_addStudent_upperBoundaryLimit() {
        String id = "937";
        int result = service.saveStudent(id, "tester", 937);
        assertEquals(1, result);
    }

    @Test
    public void test_addStudent_onUpperBoundary() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 938);
        assertEquals(0, result);
    }

    @Test
    public void test_addStudent_aboveUpperBoundary() {
        String id = "1";
        int result = service.saveStudent(id, "tester", 939);
        assertEquals(0, result);
    }

    @Test
    public void test_WBT_addAssignment_success() {
        int result = service.saveTema("1", "desc", 10, 9);
        assertEquals(1, result);
    }

    @Test
    public void test_WBT_addAssignment_descriptionEmptyString() {
        int result = service.saveTema("1", "", 10, 9);
        assertEquals(0, result);
    }

    @Test
    public void test_WBT_addGrade_success() {
        Student mockStudent = mock(Student.class);
        Tema mockTema = mock(Tema.class);
        StudentXMLRepository mockStudentXmlRepo = mock(StudentXMLRepository.class);
        TemaXMLRepository mockTemaXmlRepo = mock(TemaXMLRepository.class);
        NotaXMLRepository mockNotaXMLRepository = mock(NotaXMLRepository.class);
        Service testService = new Service(mockStudentXmlRepo, mockTemaXmlRepo, mockNotaXMLRepository);
        Nota mockNota = mock(Nota.class);
        when(mockStudent.getID()).thenReturn("12");
        when(mockTema.getID()).thenReturn("333");
        when(mockStudentXmlRepo.findOne("12")).thenReturn(mockStudent);
        when(mockTemaXmlRepo.findOne(anyString())).thenReturn(mockTema);
        when(mockNotaXMLRepository.save(any(Nota.class))).thenReturn(mockNota);
        int result = testService.saveNota("12", "333", 10, 12, "feedback");
        assertEquals(result, 0);
    }

    @Test
    public void test_BBI_addStudentAddAssignmentAddGrade() {
        test_addStudent_aboveUpperBoundary();
        test_WBT_addAssignment_descriptionEmptyString();
        test_WBT_addGrade_success();
    }

    @Test
    public void test_integration() {

        int result = service.saveStudent("1", "name", 933);
        assertEquals(1, result);
        Iterable<Student> allStudents = service.findAllStudents();
        assertNotNull(allStudents);
        assertTrue(allStudents.iterator().hasNext());
        result = service.saveTema("22", "descriere", 12, 8);
        assertEquals(1, result);
        Iterable<Tema> allAssignments = service.findAllTeme();
        assertNotNull(allAssignments);
        assertTrue(allStudents.iterator().hasNext());
        result = service.saveNota("1", "22", 8, 9, "ok");
        assertEquals(1, result);
        Iterable<Nota> allGrades = service.findAllNote();
        assertNotNull(allGrades);
        assertTrue(allGrades.iterator().hasNext());
        assertEquals(allStudents.iterator().next().getID(), "1");
        assertEquals(allAssignments.iterator().next().getID(), "22");
        assertEquals(allGrades.iterator().next().getID(), new Pair<>("1", "22"));

    }

}
