
import java.util.concurrent.Semaphore;

/**
 *@author Praloy Saha
 *Project2
 *@Fall2020 
 */
public class School {
    static Teacher teacher;
    static Thread[] students;
    static Semaphore girlsBathRoom;
    static Semaphore boysBathRoom;
    static Semaphore classInSession;
    static Semaphore canLeave;
    static int numStudents;
    static int usedBathroom;
    static long time;
    
    public School(int numStudents){
        time = System.currentTimeMillis();
        this.numStudents = numStudents;
        students = new Thread[numStudents];
        //Semaphore to manage contention when students want to go to the bathroom
        girlsBathRoom = new Semaphore(3, true);
        boysBathRoom = new Semaphore(3, true);
        //Manage contention before students are all called in order to enter class
        //Initially held by teacher until all students are called
        classInSession = new Semaphore(1, false);
        //Allow students to leave one by one
        canLeave = new Semaphore(1, true);
        usedBathroom = 0;
    }
    
    public static void main(String[] args) {
        int numStudents = 13;
        if(args.length > 0){
            try{numStudents = Integer.parseInt(args[0]);}catch(Exception e){}
        }
        School school = new School(numStudents);
        school.teacher = new Teacher(school);
        school.teacher.start();
        
        for(int i = 0; i < school.numStudents; i++){
            students[i] = new Student(school, i);
        }
        
        for(Thread th: students)
            th.start();

    }
}
