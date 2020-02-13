import java.util.*;
class Trab4{
  public static void main(String args[]){
    //Inpuf file
    //Change name to desired file
    String train_file = "iris.csv";
    //Examples file
    //Must have the same structure as the input file(same order and number of atributes)
    //The class column in not required
    //Change name to desired file
    String test_file = "example2.csv";
    //Creates database with the input file
    DataBase db = DataBase.createDataBase(train_file);
    //Creates database with the examples file
    DataBase db2 = DataBase.createDataBase(test_file);
    //Creates the tree
    DecisionTree dt = DecisionTree.ID3(db);
    //Prints the tree structure
    dt.printDecisionTree();
    //Tests the decision tree
    //If it has the class column an accuracy will be calculated
    //If the value of the class column is '?' it will be ignored
    dt.testDecisionTree(db2);
  }
}
