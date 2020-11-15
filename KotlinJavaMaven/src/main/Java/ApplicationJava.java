import paqJava.JavaService;
import paqKotlin.KotlinService;

public class ApplicationJava {
    final static String JAVA="java";
    final static String KOTLIN="kotlin";

    public static void main(String[] args){
        //comentario vic 1 java
        //String language = args[0];
        String language = "java";
        JavaService JA=new JavaService();
        KotlinService KO=new KotlinService();
        switch (language){
            case JAVA:
                //new JavaService().sayHello();
                JA.sayHello();
                break;
            case KOTLIN:
                //new KotlinService().sayHello();
                KO.sayHello();
                break;
            default:
                //Do nothing
                break;
        }
    }
}
