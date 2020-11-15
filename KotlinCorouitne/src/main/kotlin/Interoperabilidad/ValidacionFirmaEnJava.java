package Interoperabilidad;

public class ValidacionFirmaEnJava {

    public int validarAutenticidad() {
        //System.out.println("Inicio la validacion de la autenticidad");
        try{
            Thread.sleep(1000);
        }
        catch (Exception e)
        {

        }
        //System.out.println("Finalizo la validacion de la autenticidad");
        return 50;
    }

    public int validarRevocator(){
        //System.out.println("Inicio la validacion de la revocacion");
        try{
            Thread.sleep(1000);
        }
        catch (Exception e)
        {

        }
        //System.out.println("Finalizo la validacion de la revocacion");
        return 30;
    }
}
