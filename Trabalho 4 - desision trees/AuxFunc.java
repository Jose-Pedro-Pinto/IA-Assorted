import java.math.*;
//Contains auxiliary funcions not directly linked to the work
class AuxFunc{
  //Checks wether or not a sttring can be turned into a double
  public static boolean isDouble(String str){  
    try  {  
      Double value = Double.parseDouble(str);  
    }  
    catch(NumberFormatException nfe)  {  
      return false;  
    }  
    return true;  
  }
}