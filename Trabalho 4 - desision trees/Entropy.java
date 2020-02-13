import java.util.*;
import java.math.*;
class Entropy{
  //Returns the probability of an atribute (for string atribute)
  public static double pAtrb(DataColumn atribute,String str){
    int counttotal=0,countocur=0;
    for (String str2 : atribute.datacolumn){
      if (str.equals(str2)){
        countocur++;
      }
      counttotal++;
    }
    return (double)countocur/counttotal;
  }
  //Returns the probability of an atribute (for number atribute)
  public static double pAtrb(DataColumn atribute,Range range){
    int counttotal=0,countocur=0;
    for (String str : atribute.datacolumn){
      double cur = Double.parseDouble(str);  
      if (range.inRange(cur)){
        countocur++;
      }
      counttotal++;
    }
    return (double)countocur/counttotal;
  }
  //Count the ocurence of goal on the column atribute
  public static int countGoal(DataColumn atribute,String goal){
    int count=0;
    for (String str : atribute.datacolumn){
      if (goal.equals(str)){
        count++;
      }
    }
    return count;
  }
  //Returns the class with the most ocurences
  //If it is a tie returns the first of those found
  public static String maxPPos(DataBase db){
    double p=0;
    String maxp = "";
    for (String str : ClassOps.diffStrings(db.getGoalColumn())){
      if (pAtrb(db.getGoalColumn(),str)>p){
        p=pAtrb(db.getGoalColumn(),str);
        maxp=str;
      }
    }
    return maxp;
  }
  //Returns the index of the positions where an atribute is (for string atribute)
  public static LinkedList<Integer> restrictPos(DataColumn atribute,String str){
    LinkedList<Integer> pos = new LinkedList<Integer>();
    for (int i=0;i<atribute.datacolumn.size();i++){
      if (str.equals(atribute.datacolumn.get(i))){
        pos.add(i);
      }
    }
    return pos;
  }
  //Returns the index of the positions where an atribute is (for number atribute)
  public static LinkedList<Integer> restrictPos(DataColumn atribute,Range range){
    LinkedList<Integer> pos = new LinkedList<Integer>();
    for (int i=0;i<atribute.datacolumn.size();i++){
      double cur = Double.parseDouble(atribute.datacolumn.get(i));
      if (range.inRange(cur)){
        pos.add(i);
      }
    }
    return pos;
  }
  //Returns the probability of a goal given an atribute (for string goal)
  public static double pGoal(LinkedList<Integer> pos,DataColumn goal,String str){
    int counttotal=0,countocur=0;
    for (int i=0;i<pos.size();i++){
      int posi=pos.get(i);
      if (str.equals(goal.datacolumn.get(posi))){
        countocur++;
      }
      counttotal++;
    }
    return (double)countocur/counttotal;
  }
  //Returns the entropy of a given column
  public static double entropy(DataColumn atribute,DataColumn goal){
    double entropy=0;
    //If all strings of atribute can be turned into doubles
    if (atribute.isDouble()){
      LinkedList<Range> s = Range.getRangeList(atribute);
      LinkedList<String> z = ClassOps.diffStrings(goal);
      for (Range i : s){
        for (String j : z){
          double pi = pAtrb(atribute,i);
          if (pi!=0){
            double pj = pGoal(restrictPos(atribute,i),goal,j);
            if (pj!=0)
              entropy-=pi*pj*Math.log(pj)/Math.log(2);
          }
        }
      }
    }
    //If some string of atribute can't be turned into double
    else{
      LinkedList<String> s = ClassOps.diffStrings(atribute);
      LinkedList<String> z = ClassOps.diffStrings(goal);
      for (String i : s){
        for (String j : z){
          double pi = pAtrb(atribute,i);
          if (pi!=0){
            double pj = pGoal(restrictPos(atribute,i),goal,j);
            if (pj!=0)
              entropy-=pi*pj*Math.log(pj)/Math.log(2);
          }
        }
      }
    }
    return entropy;
  }
  //Choses the column from the database with the lowest entropy
  public static int pickAtrb(DataBase db){
    double min=1;
    int max=0;
    for (int i=1;i<db.width()-1;i++){
      double cur = entropy(db.getColumn(i),db.getGoalColumn());
      if (cur<min){
        min=cur;
        max=i;
      }
    }
    return max;
  }
}