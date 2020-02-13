import java.util.*;
class ClassOps{
  //Returns a list with the diferent strings
  public static LinkedList<String> diffStrings(DataColumn dc){
    LinkedList<String> strlist = new LinkedList<String>();
    for (String str : dc.datacolumn){
      if (!strlist.contains(str))
        strlist.add(str);
    }
    return strlist;
  }
  //Returns the number of classes acording to sturges
  public static int sturges(int n){
    double math = 1+(3.322*Math.log10(n));
    return ((int)Math.ceil(math));
   }
  //Returns the amplitude of classes acording to the amplitude and number of classes
  public static double classAmp(double amp,int nclass){
    double classamp;
    classamp=(double)amp/nclass;
    return classamp;
  }
  //Returns the amplitude of classes acording to sturges
  public static double classAmp(DataBase db,int column){
    return classAmp(db.columnAmplitude(column),sturges(db.depth()));
  }
  //Returns the number of classes
  public static int getClassNumber(DataColumn dc){
    if (AuxFunc.isDouble(dc.getFirstValue())){
      return sturges(dc.datacolumn.size());
    }
    else{
      return diffStrings(dc).size();
    }
  }
}