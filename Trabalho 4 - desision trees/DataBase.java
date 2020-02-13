import java.util.*;
import java.io.*;
import java.lang.*;
//Where all the data is stored
//Has operations to work with it
class DataBase{
  int atrbn;
  LinkedList<String> atrbs;
  LinkedList<DataLine> datatable;
  //Creates an empty database
  DataBase(){
    atrbs = new LinkedList<String>();
    datatable = new LinkedList<DataLine>();
  }
  //Creates a database with the types
  DataBase(String types){
    atrbs = new LinkedList<String>();
    String [] line = types.split(",");
    for (String atrb : line){
      atrbs.add(atrb);
    }
    atrbn = atrbs.size();
    datatable = new LinkedList<DataLine>();
  }
  //Add new lines to the database
  public void adddata(String data){
    datatable.add(new DataLine(data));
  }
  //Creates the database
  //Recieves the data from 'input_file'
  //Uses the first line as a header
  public static DataBase createDataBase(String input_file){
    Scanner scan = new Scanner(System.in);
    File file = new File(input_file);
    //Error in case file does not exist
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      line = br.readLine();
      DataBase db=new DataBase(line);
      while ((line = br.readLine()) != null) {
        db.adddata(line);
      }
      return db;
    }
    catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }
  //Clones an existing database
  //Used when it is necessary to alter a database
  public DataBase cloneDataBase(){
    DataBase db = new DataBase();
    for (String str : atrbs)
      db.atrbs.add(str);
    int i=0;
    for (DataLine dl : datatable){
      db.datatable.add(new DataLine());
      for (String str : dl.dataline){
        db.getLine(i).dataline.add(str);
      }
      i++;
    }
    return db;
  }
  //Print funcions
  //Write information to the screen
  //-----------------------------------------------------------------------
  //Prints the Database values
  public void printDB(){
    for (String str : atrbs){
      System.out.print(str+" ");
    }
    System.out.println();
    for (DataLine dl : datatable){
      dl.print();
      }
    }
  //Prints the values of a given line
  public void printLine (int line){
    DataLine dl = getLine(line);
    dl.print();
  }
  //Prints the values of a given column
  public void printColumn (int column){
    DataColumn dc = getColumn(column);
    dc.print();
  }
  //Prints the dimensions of the database
  //Error for depth of 0
  public void printSize(){
    System.out.println("Depth: "+datatable.size());
    System.out.println("Width: "+getLineVal(0).size());
  }
  //Retrieval funcions
  //Returns structures and values from the database
  //-----------------------------------------------------------------------
  //Returns a line of the database
  public DataLine getLine(int line){return datatable.get(line);}
  //Returns a column of the database
  public DataColumn getColumn(int column){
    DataColumn dc = new DataColumn();
    for (DataLine dl : datatable){
      dc.datacolumn.add(dl.dataline.get(column));
    }
    return dc;
  }
  //Returns the last column(class/goal column)
  public DataColumn getGoalColumn(){
    return getColumn(width()-1);
  }
  //Returns the last line
  public DataLine getFirstLine(){
    return getLine(0);
  }
  //Returns the values in a given line
  public LinkedList<String> getLineVal(int line){return getLine(line).getValue();}
  //Returns the values in a given column
  public LinkedList<String> getColumnVal(int column){return getColumn(column).getValues();}
  //Removal funcions
  //Removes lines and columns from the database
  //-----------------------------------------------------------------------
  //Removes a line from the database
  public void removeLine(int line){
    datatable.remove(line);
  }
  //Removes a column from the database
  public void removeColumn(int column){
    for (DataLine dl : datatable){
      dl.dataline.remove(column);
    }
    atrbs.remove(column);
  }
  //Removes a set of lines (on positions in the linkedlist llist)
  public void removeLines(LinkedList<Integer> llist){
    Collections.sort(llist);
    for (int i=0;i<llist.size();i++)
    {
      removeLine(llist.get(i)-i);
    }
  }
  //Removes a set of column (on positions in the linkedlist clist)
  public void removeColumns(LinkedList<Integer> clist){
    Collections.sort(clist);
    for (int i=0;i<clist.size();i++)
    {
      removeColumn(clist.get(i)-i);
    }
  }
  //Keeps a set of lines removing the rest (on positions in the linkedlist llist)
  public void keepLines(LinkedList<Integer> llist){
    Collections.sort(llist);
    LinkedList<Integer> llist2 = new LinkedList<Integer>();
    for (int i=0;i<depth();i++){
      if (!llist.contains(i))
        llist2.add(i);
    }
    removeLines(llist2);
  }
  //Keeps a set of columns removing the rest (on positions in the linkedlist clist)
  public void keepColumns(LinkedList<Integer> clist){
    Collections.sort(clist);
    LinkedList<Integer> clist2 = new LinkedList<Integer>();
    for (int i=0;i<width();i++){
      if (!clist.contains(i))
        clist2.add(i);
    }
    removeColumns(clist2);
  }
  //-----------------------------------------------------------------------
  //Returns the depth of the database
  public int depth(){
    return datatable.size();
  }
  //Returns the width of the database
  public int width(){
    return getLineVal(0).size();
  }
  //The amplitude of values of a given column
  public Double columnAmplitude(int column){
    return getColumn(column).amplitude();
  }
  //Checks if the database has examples
  public Boolean hasExamples(){
    if (depth()>0)
      return true;
    return false;
  }
  //Checks if the database has atributes
  public Boolean hasAtributes(){
    if (width()>2)
      return true;
    return false;
  }
  //Checks if all the goals are of the same class
  public Boolean allSameClass(){
    DataLine fline = getFirstLine();
    for (DataLine dl : datatable){
      if (!dl.getLastValue().equals(fline.getLastValue()))
        return false;
    }
    return true;
  }
}