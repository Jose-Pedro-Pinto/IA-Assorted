import java.util.*;
import java.time.*;
class Jogodos15{
  public static void main(String[] args){
    int configi[]=new int[16];
    int configf[]=new int[16];
    Scanner scan=new Scanner(System.in);
    System.out.println("Write 2 sets of 16 non-repeating numbers(0-15)");
    for (int i=0;i<16;i++)
      configi[i]=scan.nextInt();
    Node15 x=new Node15(configi);
    for (int i=0;i<16;i++)
      configf[i]=scan.nextInt();
    Node15 y=new Node15(configf);
    if(!x.Solvable(y))
      System.out.println("Not possible to reach the final configuration from the inicial one.");
    else{
      //Because of the way that the nodes are implemented it is necessary to reset them in order to get the correct value of size
      System.out.println("**************BFS**************");
      Node15search.BFS(x,y);
      x=new Node15(configi);
      y=new Node15(configf);
      System.out.println("**************DFS**************");
      Node15search.DFS(x,y);
      x=new Node15(configi);
      y=new Node15(configf);
      System.out.println("**************IDFS**************");
      Node15search.IDFS(x,y);
      x=new Node15(configi);
      y=new Node15(configf);
      System.out.println("**************Greedy - Manhattan distance**************");
      Node15search.Greedy(x,y,1);
      x=new Node15(configi);
      y=new Node15(configf);
      System.out.println("**************Greedy - out of position**************");
      Node15search.Greedy(x,y,0);
      x=new Node15(configi);
      y=new Node15(configf);
      System.out.println("**************Astar - Manhattan distance**************");
      Node15search.Astar(x,y,1);
      x=new Node15(configi);
      y=new Node15(configf);
      System.out.println("**************Astar - out of position**************");
      Node15search.Astar(x,y,0);
    }
  }
}
//Funcions that calculate the heuristics of the nodes
class Node15heuristic{
  //Returns the number of nodes out of position(when comparing n1 to n2)
  public static int Outofpos(Node15 n1,Node15 n2){
    int outofpos=0;
    for (int i=0;i<4;i++)
      for (int j=0;j<4;j++)
      if (n1.values[i][j]!=n2.values[i][j] && n1.values[i][j]!=0)
        outofpos++;
    return outofpos;
  }
  //Returns the manhattan distance of n1 relative to n2
  public static int Manhattan(Node15 n1,Node15 n2){
    int manhattan=0;
    for (int i=0;i<4;i++)
      for (int j=0;j<4;j++)
      if (n1.values[i][j]!=n2.values[i][j] && n1.values[i][j]!=0){
      for (int m=0;m<4;m++)
        for (int n=0;n<4;n++)
        if (n1.values[i][j]==n2.values[m][n])
        manhattan+=Math.abs(m-i)+Math.abs(n-j);
    }
    return manhattan;
  }
}
//Search funcions
class Node15search{
  //Breadth First Search
  public static void BFS(Node15 n1,Node15 n2){
    Instant start = Instant.now();
    int maxsize=0;
    Queue<Node15> q=new LinkedList<Node15>();
    while (true){
      if (n1.Equal(n2)){    
        Endsearch(start,n1,maxsize);
        return;
      }
      for (Node15 n:n1.Expand()){
        if (maxsize<n1.root.size)
          maxsize=n1.root.size;
        if (!n.Equalinbranch())
          q.add(n);
      }
      n1=q.remove();
      if (Timelimitexceded(start)){
        return;
      }
    }
  }
  //Depth First Search with cycle detection
  public static void DFS(Node15 n1,Node15 n2){
    Instant start = Instant.now();
    int maxsize=0;
    Stack<Node15> q=new Stack<Node15>();
    while (true){
      if (n1.Equal(n2)){    
        Endsearch(start,n1,maxsize);
        return;
      }
      for (Node15 n:n1.Expand()){
        if (maxsize<n1.root.size)
          maxsize=n1.root.size;
        if (!n.Equalinbranch())
          q.push(n);
      }
      n1=q.pop();
      if (Timelimitexceded(start)){
        return;
      }
    }
  }
  //Iterative Depth Firt Search
  public static void IDFS(Node15 n3,Node15 n2){
    Instant start = Instant.now();
    int maxsize=0;
    Stack<Node15> q=new Stack<Node15>();
    for (int depth=0;;depth++){
      q.push(n3);
      while (!q.empty()){
        Node15 n1=q.pop();
        if (n1.Equal(n2)){    
          Endsearch(start,n1,maxsize);
          return;
        }
        for (Node15 n:n1.Expand()){
          if (maxsize<n1.root.size)
            maxsize=n1.root.size;
          if (!n.Equalinbranch() && n.depth<=depth)
            q.push(n);
        }
        if (Timelimitexceded(start)){
          return;
        }
      }
      n3.size=0;
    }
  }
  //Gredy search algoritm(with cycle detection), allows to change heuristic with the "int heuristic",1->manhattan distance , 2->Out of position
  public static void Greedy(Node15 n1,Node15 n2,int heuristic){
    Instant start = Instant.now();
    int maxsize=0;
    LinkedList<Node15> l=new LinkedList<Node15>();
    while (true){
      if (n1.Equal(n2)){
        Endsearch(start,n1,maxsize);
        return;
      }
      for (Node15 n:n1.Expand()){
        if (maxsize<n.root.size)
          maxsize=n.root.size;
        if(!n.Equalinbranch())
          Node15search.SortedHeuristicInsert(l,n,n2,heuristic,0);
      }
      n1=l.pop();
      if (Timelimitexceded(start)){
        return;
      }
    }
  }
  //A* search algoritm, allows to change heuristic with the "int heuristic",1->manhattan distance , 2->Out of position
  public static void Astar(Node15 n1,Node15 n2,int heuristic){
    Instant start = Instant.now();
    int maxsize=0;
    LinkedList<Node15> l=new LinkedList<Node15>();
    while (true){
      if (n1.Equal(n2)){    
        Endsearch(start,n1,maxsize);
        return;
      }
      for (Node15 n:n1.Expand()){
        if (maxsize<n1.root.size)
          maxsize=n1.root.size;
        if (!n.Equalinbranch())
          Node15search.SortedHeuristicInsert(l,n,n2,heuristic,1);
      }
      n1=l.pop();
      if (Timelimitexceded(start)){
        return;
      }
    }
  }
  //Prints the output of the search funcion
  public static void Endsearch(Instant start,Node15 n,int maxsize){
    Instant end = Instant.now();
    Duration timeElapsed = Duration.between(start, end);
    n.Printsteps();
    System.out.println(timeElapsed.toMillis()/1000+" seconds taken");
    System.out.println("Amount of memory used: "+maxsize);
  }
  //Checks if the limit of time has been exceded(change 'timelimit' to the number of seconds pretended)
  public static Boolean Timelimitexceded(Instant start){
    int timelimit = 5;
    Instant end = Instant.now();
    if (Duration.between(start, end).toMillis()/1000>timelimit){
      System.out.println("Time limit of "+timelimit+"s exceded");
      return true;
    }
    return false;
  }
 //Inserts a Node in a LinkedList so that it is sorted based on the heuristic provided,and the type of search(wont sort unsorted lists and may crash)
  public static void SortedHeuristicInsert(LinkedList<Node15> l,Node15 n1,Node15 n2,int heuristic,int modifier){
    int min=0,max=l.size(),mid=(max+min)/2;
    while (min<max){
      if (max>l.size()){
        l.add(max,n1);
        return;
      }
      if (modifier == 0){
        if(Node15heuristic.Manhattan(n1,n2)>Node15heuristic.Manhattan(l.get(mid),n2) && heuristic == 1 || Node15heuristic.Outofpos(n1,n2)>Node15heuristic.Outofpos(l.get(mid),n2) && heuristic == 0){
          min=mid+1;
          mid=(max+min)/2;
        }
        else if(Node15heuristic.Manhattan(n1,n2)<Node15heuristic.Manhattan(l.get(mid),n2) && heuristic == 1 || Node15heuristic.Outofpos(n1,n2)<Node15heuristic.Outofpos(l.get(mid),n2) && heuristic == 0){
          max=mid;
          mid=(max+min)/2;
        }
        else{
          min=mid+1;
          mid=(max+min)/2;
        }
      }
      else if (modifier == 1){
        if(Node15heuristic.Manhattan(n1,n2)+n1.depth>Node15heuristic.Manhattan(l.get(mid),n2)+l.get(mid).depth && heuristic == 1 || Node15heuristic.Outofpos(n1,n2)+n1.depth>Node15heuristic.Outofpos(l.get(mid),n2)+l.get(mid).depth && heuristic == 0){
          min=mid+1;
          mid=(max+min)/2;
        }
        else if(Node15heuristic.Manhattan(n1,n2)+n1.depth<Node15heuristic.Manhattan(l.get(mid),n2)+l.get(mid).depth && heuristic == 1 || Node15heuristic.Outofpos(n1,n2)+n1.depth<Node15heuristic.Outofpos(l.get(mid),n2)+l.get(mid).depth && heuristic == 0){
          max=mid;
          mid=(max+min)/2;
        }
        else{
          min=mid+1;
          mid=(max+min)/2;
        }
      }
      else
        System.out.println("no modifier with number:"+modifier);
    }
    l.add(mid,n1);
    return;
  }
}
//Defines node atributes and has auxiliary funcions to work with the nodes
class Node15{
  //Createsa matrix with the values
  int values[][]=new int[4][4];
  //Saves the position of the empty(0) position
  int empty;
  //Saves the size of the tree
  int size;
  //Depth of the current node
  int depth;
  //Saves what the direction of the node is(left,right,up,down)
  String direction;
  //Direct acces to the root node
  Node15 root;
  //The parent of the current node
  Node15 parent;
  //Used to create the root node
  Node15(int vals[]){
      root=this;
      size=1;
      depth=0;
    for (int i=0;i<4;i++)
      for (int j=0;j<4;j++){
      values[i][j]=vals[4*i+j];
      if (values[i][j]==0)
        empty=4*i+j;
    }
  }
  //Used to create all child nodes(other than the root)
  Node15(int vals[],Node15 parent,String direction){
    this.parent=parent;
    root=parent.root;
    root.size+=1;
    depth=parent.depth+1;
    this.direction=direction;
    for (int i=0;i<4;i++)
      for (int j=0;j<4;j++){
      values[i][j]=vals[4*i+j];
      if (values[i][j]==0)
        empty=4*i+j;
    }
  }
  //Creates and returns the node that results from moving left
  public Node15 Addleft(){
    if(empty%4>0){
      int vals[]=new int[16];
      for (int i=0;i<4;i++)
        for (int j=0;j<4;j++)
        vals[4*i+j]=values[i][j];
      vals[empty]=vals[empty-1];
      vals[empty-1]=0;
      return new Node15(vals,this,"Left");
    }
    return null;
  }
  //Creates and returns the node that results from moving right
  public Node15 Addright(){
    if(empty%4<3){
      int vals[]=new int[16];
      for (int i=0;i<4;i++)
        for (int j=0;j<4;j++)
        vals[4*i+j]=values[i][j];
      vals[empty]=vals[empty+1];
      vals[empty+1]=0;
      return new Node15(vals,this,"Right");
    }
    return null;
  }
  //Creates and returns the node that results from moving up
  public Node15 Addup(){
    if(empty/4>0){
      int vals[]=new int[16];
      for (int i=0;i<4;i++)
        for (int j=0;j<4;j++)
        vals[4*i+j]=values[i][j];
      vals[empty]=vals[empty-4];
      vals[empty-4]=0;
      return new Node15(vals,this,"Up");
    } 
    return null;
  }
  //Creates and returns the node that results from moving down
  public Node15 Adddown(){
    if(empty/4<3){
      int vals[]=new int[16];
      for (int i=0;i<4;i++)
        for (int j=0;j<4;j++)
        vals[4*i+j]=values[i][j];
      vals[empty]=vals[empty+4];
      vals[empty+4]=0;
      return new Node15(vals,this,"Down");
    }
    else 
      return null;
  }
  //Prints the values of the node
  public void Printnode(){
    for (int i=0;i<4;i++)
      for (int j=0;j<4;j++){
      System.out.print(values[i][j]+" ");
      if (j==3)
        System.out.println();
    }
  }
  //Prints the steps(left right,up,down) and the number of steps
  public void Printsteps(){
    System.out.println(Printstepsaux()+" steps.");
  }
  public int Printstepsaux(){
    int steps=0;
    if (parent!=null){
      steps++;
      System.out.println(direction);
      steps+=parent.Printstepsaux();
    }
    return steps;
  }
  //verifies if 2 nodes are equal
  public Boolean Equal(Node15 n2){
    if (n2==null)
      return false;
    for (int i=0;i<4;i++)
      for (int j=0;j<4;j++)
      if (values[i][j]!=n2.values[i][j])
        return false;
    return true;
  }
  //Verifies if there is a node equal to the input in the branch(direct path from node to root)
  public Boolean Equalinbranch(){
    Node15 node=parent;
    while(node!=null){
      if (Equal(node))
        return true;
      else
        node=node.parent;
    }
    return false;
  }
  //Creates all child nodes and returns them in an array
  public Node15[] Expand(){
    int k=0;
    Node15[] k1 = new Node15[4];
    k1[k]=Addleft();
    if (k1[k]!=null){
      k+=1;
    }
    k1[k]=Addright();
    if (k1[k]!=null){
      k+=1;
    }
    k1[k]=Addup();
    if (k1[k]!=null){
      k+=1;
    }
    k1[k]=Adddown();
    if (k1[k]!=null){
      k+=1;
    }
    Node15[] k2 = new Node15[k];
    for (int i=0;i<k;i++)
      k2[i]=k1[i];
    return k2;
  }
  //Checks if you can reach from a configuration to the standard
  public Boolean SolvableSTD(){
    int x[]=new int[16];
    for(int i=0;i<4;i++)
      for(int j=0;j<4;j++){
      x[4*i+j]=values[i][j];
    }
    int inv=0;
    for (int i=0;i<16;i++)
      for (int j=i+1;j<16;j++){
      if(x[i]>x[j] && x[j]!=0)
        inv++;
    }
    if ((inv%2==0)==((4-empty/4)%2==1))
      return true;
    return false;
  }
  //Checks if you can reach from a configuration to another
  public Boolean Solvable(Node15 n2){
    if (SolvableSTD()==n2.SolvableSTD())
      return true;
    return false;
  }
}