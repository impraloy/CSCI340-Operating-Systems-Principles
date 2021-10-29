
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *@author Praloy Saha
 *Project1
 *@Fall2020 
 */
public class Teacher extends Thread {

    static ArrayList<Student> arrivalList;
    static ArrayList<Student> inClass;
    int arrived;
    int left;
    String attendanceReport;
    School school;

    public Teacher(School school) {
        left = 0;
        arrivalList = new ArrayList();
        this.school = school;
        inClass = new ArrayList();
        arrived = 0;
        attendanceReport = "";
    }

    @Override
    public void run() {
        //Call students
        msg("Waiting for students to arrive");
        while (arrived < school.numStudents) {
            try {
                sleep(100);
                if (arrivalList.size() > 0) {

                    arrivalList.remove(0).call();
                    arrived++;

                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        msg("Start class");
        school.classInSession = true;

        try {
            while (inClass.size() < school.numStudents) {sleep(100);
            }
            //for 2 periods
            for (int i = 0; i < 2; i++) {
                msg("Teaching period " + (i + 1));
                //Teach
                sleep(500);
                //Interrupt
                msg("Wake up students from period " + (i + 1));
                while (inClass.size() > 0) {
                    Student s = inClass.remove(0);
                    s.attendedClass("Class "+Math.round(Math.random()*1000), (i+1));
                    s.interrupt();
                }
                sleep(200);
                while (inClass.size() < school.numStudents) {sleep(100);
                }
            }

            //lunch break
            sleep(1000);
            //for 2 periods
            while (inClass.size() < school.numStudents) { sleep(100);
            }
            for (int i = 0; i < 2; i++) {
                msg("Teaching period " + (i + 1 + 3));
                //Teach
                sleep(500);
                //Interrupt
                msg("Wake up students from period "+ + (i + 4));
                for (int j = 0; j < inClass.size(); j++) {
                    Student s = inClass.get(j);
                    s.attendedClass("Class "+Math.round(Math.random()*1000), (i+4));
                    s.interrupt();
                }
                sleep(200);
                //while (inClass.size() < school.numStudents) {sleep(100);
                //}
            }

            int lowestId = 0;
            int index = 0;
            boolean skip = false;
            while (inClass.size() > 0) {

                lowestId = Integer.MAX_VALUE;
                index = 0;

                for (int i = 0; i < inClass.size(); i++) {
                    Student s = inClass.get(i);
                    if (s.getStudentId() < lowestId) {
                        lowestId = s.getStudentId();
                        index = i;
                    }
                }
                
                Student s = inClass.remove(index);
                if(!skip){
                    attendanceReport+=s.report()+"\n";
                    skip = true;
                }else{
                    skip = false;
                }
                s.leave();
                if(s.isAlive())
                    s.join();
            }
            this.interrupt();
        } catch (InterruptedException e) {

        }
        System.out.println("Attendance Report");
        System.out.println(attendanceReport);
        msg("Left school");
    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-school.time)+"] Teacher: " + m);
    }
}
