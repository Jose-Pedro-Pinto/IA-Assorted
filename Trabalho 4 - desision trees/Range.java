import java.util.*;
//Used to set number ranges
class Range{
  double min;
  double max;
  //Creates a range betwen min and max
  Range(double min,double max){
    this.min=min;
    this.max=max;
  }
  //Prints the min and max of the range
  public void printRange(){
    System.out.println("Min: "+min);
    System.out.println("Max: "+max); 
  }
  //Checks if a number is in the range
  public Boolean inRange(double value){
    if (value >=min & value <max)
      return true;
    return false;
  }
  //Returns a list of ranges given a column of the database
  public static LinkedList<Range> getRangeList(DataColumn dc){
    if (!dc.isDouble()){
      System.out.println("Type error");
      return null;
    }
    LinkedList<Range> rangelist = new LinkedList<Range>();
    double min = dc.min();
    int nclass = ClassOps.getClassNumber(dc);
    double classamp = ClassOps.classAmp(dc.amplitude(),nclass);
    Double maxrange= Double.POSITIVE_INFINITY;
    if (classamp == 1){
      rangelist.add(new Range(-maxrange,maxrange));
      return rangelist;
    }
    rangelist.add(new Range(-maxrange,min+classamp));
    for (int i=1;i<nclass;i++){
      rangelist.add(new Range(min+(i)*classamp,min+(i+1)*classamp));
    }
    rangelist.add(new Range(min+(nclass)*classamp,maxrange));
    return rangelist;
  }
  //Prints all Ranges in the list
  public static void printRangeList(LinkedList<Range> rangelist){
    for (Range range : rangelist){
      range.printRange();
      System.out.println();
    }
  }
  //Returns the range that a given number fits in
  public static int indexOf(LinkedList<Range> rangelist,Number n){
    double value = n.doubleValue();
    for (int i=0;i<rangelist.size();i++){
      Range range = rangelist.get(i);
      if (value>=range.min && value<=range.max)
        return i;
    }
    return -1;
  }
}