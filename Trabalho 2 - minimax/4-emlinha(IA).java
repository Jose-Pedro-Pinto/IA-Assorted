import java.util.*;
import java.time.*;
import java.math.*;

class Line4game{
  public static void main(String[] args){
    Scanner scan=new Scanner(System.in);
    System.out.println("Chose dificulty:\n1:Easy\n2:Normal\n3:Hard\n4:Slow");
    int diff=-1;
    //Choses difficulty(changes the depth of search)
    while(diff==-1){
      int x=scan.nextInt();
      switch(x){
      case 1:diff=0;break;
      case 2:diff=2;break;
      case 3:diff=4;break;
      case 4:diff=6;break;
      default:System.out.println("No such difficulty");
      }
    }
    //Select player that goes first
    System.out.println("Do you go first or second?\n1:First\n2:Second");
    while(global.playorder==0){
      int x=scan.nextInt();
      switch(x){
      case 1:global.playorder=1;break;
      case 2:global.playorder=-1;break;
      default:System.out.println("Not possible");
      }
    }
    //Creates board
    Line4 n=new Line4(7,6);
    if (global.playorder==-1){
      //int temp=AImove.Minimax(n,diff);
      int temp=AImove.Alphabeta(n,diff);
      n=n.Addincolumn(temp);
      System.out.println("COMPUTER played on column "+(temp+1));
    }
    n.Printboard();
    //Main game loop
    while (true){
      //Player move
      System.out.println("Chose the column to play(1-"+n.width+")");
      Line4 play=n.Addincolumn(scan.nextInt()-1);
      if (play!=null){
        n=play;
        if (Condition.Endgame(n.Calcvalue(),n.depth,n.width*n.height)){
          n.Printboard();
          return;
        }
        //Computer move,change max search depth to a custom one by switching "diff" with the pretended value 
        //Chose betwen Minimax and Alphabeta(default Alpha-beta because its faster)
        //int temp=AImove.Minimax(n,diff);
        int temp=AImove.Alphabeta(n,diff);
        n=n.Addincolumn(temp);
        if (Condition.Endgame(n.Calcvalue(),n.depth,n.width*n.height)){
          System.out.println("COMPUTER played on column "+(temp+1));
          n.Printboard();
          return;
        }
        System.out.println("COMPUTER played on column "+(temp+1));
        n.Printboard();
      }
      else{ 
        System.out.println("invalid move");
      }
    }
  }
}
class Condition{
  //checks if someone has won or if the game is a draw
  public static Boolean Endgame(int value,int plays,int maxplays){
    if (value==512){
      System.out.println("COMPUTER WON");
      return true;
    }
    else if(value==-512){
      System.out.println("YOU WON");
      return true;
    }
    else if(plays==maxplays){
      System.out.println("Draw");
      return true;
    }
    return false;
  }
}
//Search funcions
class AImove{
  //Minimax algoritm
  public static int Minimax(Line4 n,int diff){
    Instant start=Instant.now();
    int temp=n.Calcvalue(); //Returns the value if any of these conditions is met:Someone wins,game length reached
    if (temp == 512 || temp == -512 || n.depth==n.width*n.height)
      return temp;
    int v =-513;
    int posv=-1;
    Line4[] n1=n.Expand();
    //In order to return the move(and not the value of the best move)the funcion works as a Maxvalue for the first expantion
    for (int i=0;i<n1.length;i++){
      if (n1[i]!=null){
        temp=MinValue(n1[i],diff);
        if (v>=512 && temp>=512 && temp<v){
          v=temp;
          posv=i;
        }
        if (temp>v && v<512){
          v=temp;
          posv=i;
        }
      }
    }
    //Prints the time the algoritm took and the number of nodes expanded
    //System.out.println(n.root.size+" "+Duration.between(start,Instant.now()).toMillis());
    //Resets the size because in the next moves it will use the same(n.root.size),so that it shows nodes expanded per move and not in total
    n.root.size=0;
    return posv;
  }
  //Aux funcion for Minimax
  public static int MaxValue(Line4 n,int diff){
    int temp=n.Calcvalue();
     //Returns the value if any of these conditions is met:Someone wins,game length reached,depth limit reached(to save time and space)
    if (temp == 512 || temp == -512 || n.depth==n.width*n.height || n.depth>=diff)
      return temp;
    int v =-513;
    for (Line4 n1:n.Expand()){
      if (n1!=null)
        temp=MinValue(n1,diff);
      if (v>=512 && temp>=512 && temp<v)
        v=temp;
      if (temp>v && v<512){
        v=temp;
      }
    }
    if (v>=512)
      return v+1;
    return v;
  }
  //Aux funcion for Minimax
  public static int MinValue(Line4 n,int diff){
    int temp=n.Calcvalue();
    //Returns the value if any of these conditions is met:Someone wins,game length reached,depth limit reached(to save time and space)
    if (temp == 512 || temp == -512 || n.depth==n.width*n.height || n.depth>=diff)
      return temp;
    int v =999;
    for (Line4 n1:n.Expand()){
      if (n1!=null)
        temp=MaxValue(n1,diff);
      if (temp<v){
        v=temp;
      }
    }
    if (v>=512)
      return v+1;
    return v;
  }
  //Alpha-Beta algoritm
  public static int Alphabeta(Line4 n,int diff){
    System.out.println(n.depth+" "+diff);
    Instant start=Instant.now();
    int temp=n.Calcvalue();
    int alpha=-513;
    int beta=999;
    //Returns the value if any of these conditions is met:Someone wins,game length reached
    if (temp == 512 || temp == -512 || n.depth==n.width*n.height)
      return temp;
    int v =-513;
    int posv=-1;
    Line4[] n1=n.Expand();
    //In order to return the move(and not the value of the best move)the funcion works as a Maxvalue for the first expantion
    for (int i=0;i<n1.length;i++){
      if (n1[i]!=null){
        temp=MinValue(n1[i],alpha,beta,diff);
        if (v>=512 && temp>=512 && temp<v){
          v=temp;
          posv=i;
        }
        if (temp>v && v<512){
          v=temp;
          posv=i;
        }
        if (v>=beta){
          return v;
        }
        if (v>alpha)
          alpha=v;
      }
    }
    //Prints the time the algoritm took and the number of nodes expanded
    //System.out.println(n.root.size+" "+Duration.between(start,Instant.now()).toMillis());
    //Resets the size because in the next moves it will use the same(n.root.size),so that it shows nodes expanded per move and not in total
    n.root.size=0;
    return posv;
  }
  //Aux funcion for Alphabeta
  public static int MaxValue(Line4 n,int alpha,int beta,int diff){
    int temp=n.Calcvalue();
    //Returns the value if any of these conditions is met:Someone wins,game length reached,depth limit reached(to save time and space)
    if (temp == 512 || temp == -512 || n.depth==n.width*n.height || n.depth>=diff)
      return temp;
    int v =-513;
    for (Line4 n1:n.Expand()){
      if (n1!=null)
        temp=MinValue(n1,alpha,beta,diff);
      if (v>=512 && temp>=512 && temp<v)
        v=temp;
      if (temp>v && v<512){
        v=temp;
      }
      if (v>=beta){
        return v;
      }
      if (v>alpha)
        alpha=v;
    }
    if (v>=512)
      return v+1;
    return v;
  }
  //Aux funcion for Alphabeta
  public static int MinValue(Line4 n,int alpha,int beta,int diff){
    int temp=n.Calcvalue();
    //Returns the value if any of these conditions is met:Someone wins,game length reached,depth limit reached(to save time and space)
    if (temp == 512 || temp == -512 || n.depth==n.width*n.height || n.depth>=diff)
      return temp;
    int v =513;
    for (Line4 n1:n.Expand()){
      if (n1!=null)
        temp=MaxValue(n1,alpha,beta,diff);
      if (temp<v){
        v=temp;
        if (v<=alpha){
          return v;
        }
        if (v<beta)
          beta=v;
      }
    }
    if (v>=512)
      return v+1;
    return v;
  }
}
//Defines node atributes and has auxiliary funcions to work with it
class Line4{
  //Size of the board
  int width;
  int height;
  //Createsa matrix with the values
  int values[][];
  //Saves the size of the tree
  int size;
  //Depth of the current node
  int depth;
  //Used to switch betwen diferent players instead of a game loop variable;
  int lastplayer;
  //Direct acces to the root node
  Line4 root;
  //The parent of the current node
  Line4 parent;
  //Used to create the root node
  Line4(int width,int height){
    root=this;
    size=1;
    depth=0;
    this.width=width;
    this.height=height;
    values=new int[height][width];
  }
  //Used to create all child nodes(other than the root)
  Line4(Line4 parent,int column,int line){
    this.parent=parent;
    root=parent.root;
    root.size+=1;
    depth=parent.depth+1;
    width=parent.width;
    height=parent.height;
    values=new int[height][width];
    for (int i=0;i<height;i++)
      for (int j=0;j<width;j++)
      values[i][j]=parent.values[i][j];
    if (parent.lastplayer==-1){
      values[line][column]=global.playorder*1;
      lastplayer=1;
    }
    else{
      values[line][column]=global.playorder*-1;
      lastplayer=-1;
    }
  }
  //Prints the game board(most of the code is just to make it look better)
  public void Printboard(){
    System.out.print("  ");
    for (int i=0;i<width;i++)
      System.out.print((i+1)+" ");
    System.out.println();
    for (int i=0;i<height;i++){
      System.out.print((i+1)+"|");
      for (int j=0;j<width;j++){
        if (values[i][j]==0)
        System.out.print("-|");
        if (values[i][j]==-1)
        System.out.print("X|");
        if (values[i][j]==1)
        System.out.print("O|");
      if (j==width-1)
        System.out.println();
      }
    }
  }
  //Creates and returns the node that results from addint a piece in a row
  public Line4 Addincolumn(int column){
    if(column>=0 && column<width && values[0][column]==0){
      for (int i=height-1;i>=0;i--)
        if (values[i][column]==0){
        Line4 n=new Line4(this,column,i);
        return n;
      }
    }
    return null;
  }
  //Creates all child nodes and returns them in an array
  public Line4[] Expand(){
    Line4[] k1 = new Line4[width];
    for (int i=0;i<width;i++){
      k1[i]=Addincolumn(i);
    }
    return k1;
  }
  //Calculates the value based or the heuristic;selects every position and calls the aux funcion
  public int Calcvalue(){
    int value=0;
    for (int i=0;i<height;i++)
      for (int j=0;j<width;j++){
      int temp=Calcvalueaux(i,j);
      if (temp==512 || temp==-512)
        return temp;
      value+=temp;
    }
  return value-lastplayer*16;
  }
  //Calculates the value of a position;all 4 long lines(diagonal ascending,descending,horisontal and vertical)
  public int Calcvalueaux(int row,int col){
    int value=0;
    int aux=0;
    int keep=0;
    int temp=0;
    //Calculates the vertical line
    for (int i=row;i<row+4 && row+4<=height;i++){
      if (values[i][col]!=0 && (keep==0 || keep==values[i][col])){
        keep=values[i][col];
        aux++;
      }
      else if(values[i][col]!=0){
        aux=0;
        break;
      }
    }
    temp=keep*Calcvaluetable(aux);
    if (temp==512 || temp==-512)
      return temp;
    value+=temp;
    aux=0;keep=0;
    //Calculates the horizontal line
    for (int j=col;j<col+4 && col+4<=width;j++){
      if (values[row][j]!=0 && (keep==0 || keep==values[row][j])){
        keep=values[row][j];
        aux++;
      }
      else if(values[row][j]!=0){
        aux=0;
        break;
      }
    }
    temp=keep*Calcvaluetable(aux);
    if (temp==512 || temp==-512)
      return temp;
    value+=temp;
    aux=0;keep=0;
    //Calculates the diagonal descending line
    for (int i=row,j=col;i<row+4 && j<col+4 && row+4<=height && col+4<=width;j++,i++){
      if (values[i][j]!=0 && (keep==0 || keep==values[i][j])){
        keep=values[i][j];
        aux++;
      }
      else if(values[i][j]!=0){
        aux=0;
        break;
      }
    }
    temp=keep*Calcvaluetable(aux);
    if (temp==512 || temp==-512)
      return temp;
    value+=temp;
    aux=0;keep=0;
    //Calculates the diagonal ascending line
    for (int i=row,j=col;i>row-4 && j<col+4 && row-4>=0 && col+4<=width;j++,i--){
      if (values[i][j]!=0 && (keep==0 || keep==values[i][j])){
        keep=values[i][j];
        aux++;
      }
      else if(values[i][j]!=0){
        aux=0;
        break;
      }
    }
    temp=keep*Calcvaluetable(aux);
    if (temp==512 || temp==-512)
      return temp;
    value+=temp;
    return value;
  }
  //Recieves the number of nodes in a row and returns the associated values
  public int Calcvaluetable(int nodesinrow){
    switch(nodesinrow){
      case 0:
        return 0;
      case 1:
        return 1;
      case 2:
        return 10;
      case 3:
        return 50;
      default:
        return 512;
    }
  }
}
//has global variables
class global{
  //stores who goes first
  public static int playorder=0;
}