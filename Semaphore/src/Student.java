
import static java.lang.Thread.sleep;
import java.util.ArrayList;

/**
 *@author Praloy Saha
 *Project2
 *@Fall2020 
 */
public class Student extends Thread{
    private School school;
    private boolean wait;
    private int id;
    private boolean inClass;
    private boolean called;
    private boolean goneToBathroom;
    private boolean canLeave;
    private int classesAttended;
    private String classes;
    private ArrayList<Integer> periodsAttended;
    
    public Student(School school, int id){
        this.school = school;
        classes = "";
        wait = false;
        this.id = id;
        inClass = false;
        called = false;
        goneToBathroom = false;
        canLeave = false;
        classesAttended = 0;
        periodsAttended = new ArrayList();
    }
    
    public void call(){
        called = true;
    }
    
    public void outOfBathroom(){
        goneToBathroom = true;
    }
    
    public void leave(){
        canLeave = true;
    }
    
    public int getStudentId(){
        return id;
    }
    
    public void attendedClass(String className, int period){
        if(periodsAttended.contains(period)) return;
        periodsAttended.add(period);
        classesAttended++;
        if(classes.length() > 0) classes+=",";
        classes+=" Class: "+className+" Period: "+period;
    }
    
    @Override
    public void run() {
        try {
            //Take health questionaire
            msg("Taking questionaire");
            sleep((long) (Math.random()*1000));
            //Commute to school
            msg("Commuting to school");
            sleep((long) (Math.random()*1000));
            //wait to be called by Teacher
            msg("Arrived at School. Waiting to be called by teacher");
            school.teacher.arrival.acquire();
            sleep(50);
            call();
            school.teacher.arrival.release();
            msg("Called by teacher");
            //wait to go to bathroom
            if(id%2 == 0){
                msg("Going to boys bathroom");
                
                school.boysBathRoom.acquire();
                sleep(100);
                school.boysBathRoom.release();
                msg("Used the boys bathroom");
            }else{
                msg("Going to girls bathroom");
                
                school.girlsBathRoom.acquire();
                sleep(150);
                school.girlsBathRoom.release();
                msg("Used the girls bathroom");
            }
            
            if(((int)(Math.random()*100)%2) == 0){
                this.setPriority(MIN_PRIORITY);
                msg("In a hurry to get to class");
            }
            else{
                this.setPriority(MAX_PRIORITY);
                msg("Not in a hurry to get to class");
            }
            
            this.setPriority(5);
            school.classInSession.acquire();
            //Get into class
            school.classInSession.release();
//            while(!school.classInSession){sleep(100);}
            msg("In class");
            school.teacher.inClass.add(this);
            //for 2 periods
            for(int i = 0; i < 2; i++){
                
                //Sleep period
                try{
                    msg("Sleeping in class in period "+(i+1));
                    sleep(Integer.MAX_VALUE);
                }catch(InterruptedException e){
                    //Student is woken up
                    school.classInSession.release();
                    msg("Woken up by teacher from period "+(i+1));
                }
                //Go for break
                msg("Gone for the break");
                sleep((long) (Math.random()*500));
                msg("Back in class");
                school.teacher.inClass.add(this);
            }
            
            System.out.println("Office break");
            //office break
            sleep(1000);
            
            msg("Back in class");
            school.teacher.inClass.add(this);
            for(int i = 0; i < 2; i++){
                
                //Sleep period
                try{
                    msg("Sleeping in class in period "+(i+4));
                    sleep(Integer.MAX_VALUE);
                }catch(InterruptedException e){
                    //Student is woken up
                    msg("Woken up by teacher from period "+(i+4));
                }
                //Go for break
                msg("Gone for the break");
                sleep((long) (Math.random()*500));
                msg("Back in class");
                //school.teacher.inClass.add(this);
            }
            
            msg("Waiting to leave school");
            //leave class when given permission by teacher
            while(!canLeave){
                sleep(100);
            }
            school.canLeave.acquire();
            sleep(500);
            school.canLeave.release();
        } catch (InterruptedException ex) {
            //Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
        msg("Left School");
    }
    
    public void msg(String m){
        System.out.println("["+(System.currentTimeMillis()-school.time)+"] Student "+id+": "+m);
    }
    
    public String report(){
        return "Student "+id+" attended "+classesAttended+": "+classes;
    }
}
