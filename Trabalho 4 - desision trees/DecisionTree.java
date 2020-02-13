import java.util.*;
import java.lang.*;
class DecisionTree{
  LinkedList<String> atributes;
  DecisionTreeNode root;
  //Initializes a decision tree
  public DecisionTree(DataBase db){
    atributes = db.atrbs;
  }
  //Initializes a decision tree
  public static DecisionTree ID3(DataBase db){
    DecisionTree dt = new DecisionTree(db);
    dt.root = DecisionTreeNode.decisionTreeLearning(db,null);
    return dt;
  }
  //Tests the decision tree with the test database and prints the results
  public void testDecisionTree(DataBase db){
    int right=0,total=0;
    System.out.println("testing:");
    for (DataLine dl : db.datatable){
      LinkedList<String> strlist = new LinkedList<String>();
      for (String str : dl.dataline){
        strlist.add(str);
      }
      String testresult = searchDecisionTree(strlist,null);
      System.out.println(strlist.getFirst()+":"+testresult);
      //If classes have a value(instead of '?') the accuracy of the test is calculated
      //If only some have the value the accuracy is calculated for those
      //If any class has the name '?' the value might be wrong
      String realresult = dl.getLastValue();
      if (!realresult.equals("?") && db.atrbs.getLast().equals(atributes.getLast())){
        if (realresult.equals(testresult))  
          right++;
        total++;
        }
    }
    double accuracy = (double)right/total;
    if (total!=0)
      System.out.println("accuracy = "+accuracy);
  }
  //Prints the decision tree
  public void printDecisionTree(){
    root.printDecisionTree(0);
  }
  //Searchs on the decision tree and returns the class
  public String searchDecisionTree(LinkedList<String> strlist,DecisionTreeNode dtn){
    if (dtn == null)
      dtn = root;
    if (dtn.atribute == null)
      return dtn.goal;
    String str = strlist.get(atributes.indexOf(dtn.atribute));
    //If the atribute is a number
    if (AuxFunc.isDouble(str)){
      if (dtn.rangelist==null)
        return dtn.goal;
      else if (dtn.rangelist.size()>0){
        double value = Double.parseDouble(str);
        for (int i=0;i<dtn.rangelist.size();i++){
          Range range = dtn.rangelist.get(i);
          if (range.inRange(value))
            return searchDecisionTree(strlist,dtn.getChildren(i));
        }
        return dtn.goal;
      }
    }
    //If the atribute is a string
    else {
      if (dtn.strlist==null)
        return dtn.goal;
      else if (dtn.strlist.size()>0){
        for (int i=0;i<dtn.strlist.size();i++){
          String str2 = dtn.strlist.get(i);
          if (str2.equals(str)){
            return searchDecisionTree(strlist,dtn.getChildren(i));}
        }
        return dtn.goal;
      }
    }
    return str;
  }
}
//A node of the decision tree
class DecisionTreeNode{
  String atribute;
  String goal;
  int count;
  LinkedList<DecisionTreeNode> children;
  LinkedList<String> strlist;
  LinkedList<Range> rangelist;
  DecisionTreeNode(){}
  DecisionTreeNode(String goal){
    this.goal = goal;
    strlist = new LinkedList<String>();
    children = new LinkedList<DecisionTreeNode>();
  }
  //Creates the decision tree
  public static DecisionTreeNode decisionTreeLearning(DataBase db,String parent_goal){
    //If no examples returns tree with parent goal
    if (!db.hasExamples()){
      return new DecisionTreeNode(parent_goal);
    }
    //If all same class returns tree with the class
    if (db.allSameClass()){
      DecisionTreeNode dt = new DecisionTreeNode(db.getGoalColumn().getFirstValue());
      dt.count=Entropy.countGoal(db.getGoalColumn(),dt.goal);
      return dt;
    }
    String maxp = Entropy.maxPPos(db);
    DecisionTreeNode dt = new DecisionTreeNode(maxp);
    dt.count=Entropy.countGoal(db.getGoalColumn(),dt.goal);
    //If there are no atributes returns tree with the most likely class
    if (!db.hasAtributes()){
      return dt;
    }
    int bestpos = Entropy.pickAtrb(db);
    dt.atribute=db.atrbs.get(bestpos);
    DataColumn dc = db.getColumn(bestpos);
    //If the atribute is a number
    if (dc.isDouble()){
      dt.rangelist = new LinkedList<Range>();
      for (Range range : Range.getRangeList(dc)){
        DataBase db2 = db.cloneDataBase();
        db2.removeColumn(bestpos);
        db2.keepLines(Entropy.restrictPos(dc,range));
        dt.rangelist.add(range);
        dt.children.add(decisionTreeLearning(db2,dt.goal));
      }
    }
    //If the atribute is not a number
    else{
      for (String str : ClassOps.diffStrings(dc)){
        DataBase db2 = db.cloneDataBase();
        db2.removeColumn(bestpos);
        db2.keepLines(Entropy.restrictPos(dc,str));
        dt.strlist.add(str);
        dt.children.add(decisionTreeLearning(db2,dt.goal));
      }
    }
    return dt;
  }
  //Prints the decision tree
  public void printDecisionTree(int indent){
    if (atribute!=null){
      System.out.println();
      for (int i=0;i<indent;i++)
        System.out.print("   ");
      System.out.println("<"+atribute+">");
      for (int i=0 ; i<children.size();i++){
        for (int j=0;j<indent;j++)
          System.out.print("   ");
        if (rangelist!=null){
          System.out.print(" range: "+rangelist.get(i).min+" "+rangelist.get(i).max);
        }
        else{
          System.out.print("  "+strlist.get(i)+":");
        }
        children.get(i).printDecisionTree(indent+1);
      }
      for (int j=0;j<indent;j++)
          System.out.print("   ");
      System.out.println(" default: "+goal+" ("+count+")");
    }
    else{
      System.out.println(" "+goal+" ("+count+")");
    }
  }
  //Returns the children on a given position
  public DecisionTreeNode getChildren(int i){
    return children.get(i);
  }
}