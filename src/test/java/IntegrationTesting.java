import domain.Nota;
import domain.Pair;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.AfterEach;
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
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTesting {

    private StudentXMLRepository studentXMLRepository;
    private TemaXMLRepository temaXMLRepository;
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

        studentXMLRepository = new StudentXMLRepository(new StudentValidator(), "testStudenti.xml");
        temaXMLRepository = new TemaXMLRepository(new TemaValidator(), "testTeme.xml");
        notaXMLRepository = new NotaXMLRepository(new NotaValidator(), "testNote.xml");
        service = new Service(studentXMLRepository, temaXMLRepository, notaXMLRepository);
    }

    @AfterEach
    public void teardown() {
        File file = new File("testStudenti.xml");
        file.delete();
        file = new File("testTeme.xml");
        file.delete();
        file = new File("testNote.xml");
        file.delete();
    }

    @Test
    public void test_studentService_addStudent_success() {
        String testId = "testId";
        String testName = "testName";
        int testGroup = 933;
        assertFalse(service.findAllStudents().iterator().hasNext());
        service.saveStudent(testId, testName, testGroup);
        Iterator<Student> studentIterator = service.findAllStudents().iterator();
        assertTrue(studentIterator.hasNext());
        Student addedStudent = studentIterator.next();
        assertFalse(studentIterator.hasNext());
        assertNotNull(addedStudent);
        assertEquals(testId, addedStudent.getID());
        assertEquals(testName, addedStudent.getNume());
        assertEquals(testGroup, addedStudent.getGrupa());
    }

    @Test
    public void test_temaService_addAssignment_success() {
        // why do we need addStudent here? (check req)
        String testId = "testId";
        String testDescription = "testDescription";
        int testDeadline = 14;
        int testStartline = 12;
        assertFalse(service.findAllTeme().iterator().hasNext());
        service.saveTema(testId, testDescription, testDeadline, testStartline);
        Iterator<Tema> temaIterator = service.findAllTeme().iterator();
        assertTrue(temaIterator.hasNext());
        Tema addedTema = temaIterator.next();
        assertFalse(temaIterator.hasNext());
        assertNotNull(addedTema);
        assertEquals(testId, addedTema.getID());
        assertEquals(testDescription, addedTema.getDescriere());
        assertEquals(testDeadline, addedTema.getDeadline());
        assertEquals(testStartline, addedTema.getStartline());
    }

    @Test
    public void test_gradeService_addGrade_success() {
        Student testStudent = new Student("testStudentId", "testName", 933);
        Tema testTema = new Tema("testTemaId", "testDescription", 14, 12);
        assertFalse(studentXMLRepository.findAll().iterator().hasNext());
        assertFalse(temaXMLRepository.findAll().iterator().hasNext());
        assertFalse(notaXMLRepository.findAll().iterator().hasNext());
        service.saveStudent(testStudent.getID(), testStudent.getNume(), testStudent.getGrupa());
        assertTrue(studentXMLRepository.findAll().iterator().hasNext());
        service.saveTema(testTema.getID(), testTema.getDescriere(), testTema.getDeadline(), testTema.getStartline());
        assertTrue(temaXMLRepository.findAll().iterator().hasNext());
        assertEquals(testStudent, studentXMLRepository.findOne(testStudent.getID()));
        assertEquals(testTema, temaXMLRepository.findOne(testTema.getID()));
        Nota testNota = new Nota(new Pair<>(testStudent.getID(), testTema.getID()), 10, 14, "ok");
        service.saveNota(testStudent.getID(), testTema.getID(), 10, 14, "ok");
        assertTrue(notaXMLRepository.findAll().iterator().hasNext());
        Nota result = notaXMLRepository.findOne(new Pair<>(testStudent.getID(), testTema.getID()));
        assertNotNull(result);
        assertEquals(testStudent.getID(), result.getID().getObject1());
        assertEquals(testTema.getID(), result.getID().getObject2());
        assertEquals(10, result.getNota());
        assertEquals(14, result.getSaptamanaPredare());
        assertEquals("ok", result.getFeedback());


    }
}