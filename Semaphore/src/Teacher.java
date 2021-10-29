
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 *@author Praloy Saha
 *Project2
 *@Fall2020 
 */
public class Teacher extends Thread {

//    static ArrayList<Student> arrivalList;
    static Semaphore arrival;
    static ArrayList<Student> inClass;
    int arrived;
    int left;
    String attendanceReport;
    School school;

    public Teacher(School school) {
        left = 0;
        arrival = new Semaphore(1);
        this.school = school;
        inClass = new ArrayList();
        arrived = 0;
        attendanceReport = "";
    }

    @Override
    public void run() {
        //Call students
        msg("Waiting for students to arrive");

        msg("Start class");

        try {

            school.classInSession.acquire();

            while (school.classInSession.getQueueLength() < school.numStudents) {
                sleep(100);
            }
            school.classInSession.release();
            System.out.println("Out here");
            //for 2 periods
            for (int i = 0; i < 2; i++) {
                msg("Teaching period " + (i + 1));
                //Teach
                sleep(500);
                //Interrupt
                msg("Tell students class is over. Period " + (i + 1));
                while (inClass.size() > 0) {
                    Student s = inClass.remove(0);
                    s.attendedClass("Class " + Math.round(Math.random() * 1000), (i + 1));
                    s.interrupt();
                }
                sleep(200);
                while (inClass.size() < school.numStudents) {
                    sleep(100);
                }
            }

            //lunch break
            sleep(1000);
            //for 2 periods
            while (inClass.size() < school.numStudents) {
                sleep(100);
            }
            for (int i = 0; i < 2; i++) {
                msg("Teaching period " + (i + 1 + 3));
                //Teach
                sleep(500);
                //Interrupt
                msg("Tell students class is over. Period " + +(i + 4));
                for (int j = 0; j < inClass.size(); j++) {
                    Student s = inClass.get(j);
                    s.attendedClass("Class " + Math.round(Math.random() * 1000), (i + 4));
                    s.interrupt();
                }
                sleep(200);
            }

            //SOrt the students by Id
            for (int i = 0; i < inClass.size(); i++) {
                for(int j = 0; j < inClass.size() - 1; j++){
                    Student s1 = inClass.get(j);
                    Student s2 = inClass.get(j+1);
                    if(s2.getStudentId() < s1.getStudentId()){
                        inClass.remove(j+1);
                        inClass.add(j, s2);
                    }
                }
            }

            for (int i = 0; i < inClass.size(); i++) {
                Student s = inClass.remove(i);
                attendanceReport += s.report() + "\n";
                s.leave();
            }

            this.interrupt();
        } catch (InterruptedException e) {

        }
        System.out.println("Attendance Report");
        System.out.println(attendanceReport);
        msg("Left school");
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - school.time) + "] Teacher: " + m);
    }
}
