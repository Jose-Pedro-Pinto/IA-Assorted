import java.util.*;
import java.lang.*;
//Handles a single column of the database
//Has all the funcions used on a single column
class DataColumn{
  LinkedList<String> datacolumn;
  //Creates an empty column
  DataColumn(){
    datacolumn = new LinkedList<String>();
  }
  //Print funcions
  //Write information to the screen
  //-----------------------------------------------------------------------
  //Prints the values in the column
  public void print(){
    for (String str : datacolumn){
      System.out.print(str+" ");
    }
    System.out.println();
  }
  //Retrieval funcions
  //Returns structures and values from the database
  //-----------------------------------------------------------------------
  //Returns the values in the column
  public LinkedList<String> getValues(){
    return datacolumn;
  }
  //Returns a given value
  public String getValue(int i){
    return datacolumn.get(i);
  }
  //Returns the first value
  public String getFirstValue(){
    return datacolumn.getFirst();
  }
  //-----------------------------------------------------------------------
  //Returns the minimum of a column
  public double min(){
    double min=Double.parseDouble(getFirstValue());
    //Change the object into a double
    double cur;
    for (String str : datacolumn){
      cur=Double.parseDouble(str);
      if (cur<min)
        min=cur;
    }
    return min;
  }
  //Returns the maximum of a column
  public double max(){
    double max=Double.parseDouble(getFirstValue());
    //Change the object into a double
    double cur;
    for (String str : datacolumn){
      cur=Double.parseDouble(str);
      if (cur>max)
        max=cur;
    }
    return max;
  }
  //Returns the amplitude of the column
  //0 for non-numbers
  public double amplitude(){
    return max()-min();
    }
  //Returns a list with the diferent ranges for numbers
  public LinkedList<Range> getRangeList(){
    return Range.getRangeList(this);
  }
  //Prints a list with the diferent ranges for numbers
  public void printRangeList(){
    Range.printRangeList(Range.getRangeList(this));
  }
  //True if all strings can be turned into doubles
  public Boolean isDouble(){
    for (String str : datacolumn){
      if (!AuxFunc.isDouble(str))
        return false;
    }
    return true;
  }
}