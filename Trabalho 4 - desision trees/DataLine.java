import java.util.*;
//Handles a single line of the database
//Has all the funcions used on a single line
class DataLine{
  LinkedList<String> dataline;
  //Creates an empty line
  DataLine(){
    dataline = new LinkedList<String>();
  }
  //Creates a line with some data
  DataLine(String data){
    String [] line = data.split(",");
    dataline = new LinkedList<String>();
    for (String str : line){
      dataline.add(str);
    }
  }
  //Print funcions
  //Write information to the screen
  //-----------------------------------------------------------------------
  //Prints the values in the line
  public void print(){
    for (String str : dataline){
      System.out.print(str+" ");
    }
    System.out.println();
  }
  //Retrieval funcions
  //-----------------------------------------------------------------------
  //Returns the values in the line
  public LinkedList<String> getValue(){
    return dataline;
  }
  //Returns the last value
  public String getLastValue(){
    return dataline.getLast();
  }
  //-----------------------------------------------------------------------
}