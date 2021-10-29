
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *@author Praloy Saha
 *Project1
 *@Fall2020 
 */
public class School {
    static Teacher teacher;
    static ArrayList<Student> girlsWaitingList;
    static ArrayList<Student> boysWaitingList;
    static Thread[] students;
    static int girlsBathRoom;
    static int boysBathRoom;
    static boolean classInSession;
    static int numStudents;
    static int usedBathroom;
    static long time;
    
    public School(int numStudents){
        time = System.currentTimeMillis();
        this.numStudents = numStudents;
        students = new Thread[numStudents];
        girlsBathRoom = 0;
        boysBathRoom = 0;
        girlsWaitingList = new ArrayList();
        boysWaitingList = new ArrayList();
        classInSession = false;
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
        
        while(usedBathroom < numStudents){
            if(girlsWaitingList.size() > 0){
                girlsWaitingList.remove(0).outOfBathroom();
                usedBathroom++;
                girlsBathRoom++;
            }
            if(boysWaitingList.size() > 0){
                boysWaitingList.remove(0).outOfBathroom();
                usedBathroom++;
                boysBathRoom++;
            }
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(School.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
