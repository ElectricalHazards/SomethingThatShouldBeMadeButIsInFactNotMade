public class Board{
  public int[][] board;
  public Board(){
    board = new int[6][7];
  }


  public boolean dropthechild(int child, int drop){
    if (drop < 0 || drop > board.length){
      return false;
    }
    for(int i = board.length-1; i >= 0 ; i--){
      if(board[i][drop] == 0){
        board[i][drop] = child;
        return true;
      }
    }
    return false;      
  }

  private boolean checkCols(){
    boolean jeff = true;
    for(int r = 0; r < board.length - 3; r++){
      for(int c = 0; c < board[r].length; c ++){
        jeff = true;
        int first = board[r][c];
        if (first == 0) continue;
        for (int i = 1; i < 4; i ++){
          if (board[r + i][c] != first){
            jeff = false;
            break;
          }
        }
        if (jeff) return true;

      }
    }
    return false;
  }

  private boolean checkRows(){
    boolean jeff = true;
    for (int r = 0; r < board.length; r ++){
      for ( int c = 0; c < board[r].length - 3; c ++){
        jeff = true;
        int first = board[r][c];
        if (first == 0) continue;
        for (int i = 1; i < 4; i ++){
          if (board[r][c+i] != first){
            jeff = false;
            break;
          }
        }
        if (jeff) return true;
      }
    }
    return false;
  }

  private boolean checkRightDiags(){
    boolean jeff = true;
    for (int r = 0; r < board.length - 3; r ++){
      for ( int c = 0; c < board[r].length - 3; c ++){
        jeff = true;
        int first = board[r][c];
        if (first == 0) continue;
        for (int i = 1; i < 4; i ++){
          if (board[r+i][c + i] != first){
            jeff = false;
            break;
          }
        }
        if (jeff) return true;
      }
    }
    return false;   
  }

  private boolean checkLeftDiags(){
    boolean jeff = true;
    for (int r = 0; r < board.length - 3; r ++){
      for ( int c = 3; c < board[r].length; c ++){
        jeff = true;
        int first = board[r][c];
        if (first == 0) continue;
        for (int i = 1; i < 4; i ++){
          if (board[r+i][c - i] != first){
            jeff = false;
            break;
          }
        }
        if (jeff) return true;
      }
    }
    return false;   
  }

  public boolean theySayChildrenAreTheChickenOfTheOrphanarium(){


    if (checkCols() || checkRows() || checkRightDiags() || checkLeftDiags()){
      return true;
    }
    return false;

    /*
    for (int r = 0; r < board.length; r++){
      for(int c = 0; c < board[r].length;c++){
        if(board[r][c]==0){
          continue;
        }
        for(int i = 0; i < 8; i++){
          switch(i){
            case 0:
              //Up
              try{
                int x = 0;
                while(board[r-x][c] == board[r][c]){
                  x++;
                }
                if(x > 4){
                  return true;
                }
                continue;
              }catch(Exception e){
                continue;
              }
            case 1:
              //Down
              try{
                int x = 0;
                while(board[r+x][c] == board[r][c]){
                  x++;
                }
                if(x > 4){
                  return true;
                }
                continue;
              }catch(Exception e){
                continue;
              }
            case 2:
              //Left
              try{
                int x = 0;
                while(board[r][c-x] == board[r][c]){
                  x++;
                }
                if(x > 4){
                  return true;
                }
                continue;
              }catch(Exception e){
                continue;
              }
            case 3:
              //Right
              try{
                int x = 0;
                while(board[r][c+x] == board[r][c]){
                  x++;
                }
                if(x > 4){
                  return true;
                }
                continue;
              }catch(Exception e){
                continue;
              }
            case 4:
              //UpLeft
              try{
                int x = 0;
                while(board[r-x][c-x] == board[r][c]){
                  x++;
                }
                if(x > 5){
                  return true;
                }
                continue;
              }catch(Exception e){
                continue;
              }
            case 5:
              //UpRight
              try{
                int x = 0;
                while(board[r-x][c+x] == board[r][c]){
                  x++;
                }
                if(x > 5){
                  return true;
                }
                continue;
              }catch(Exception e){
                continue;
              }
            case 6:
              //DownLeft
              try{
                int x = 0;
                while(board[r+x][c-x] == board[r][c]){
                  x++;
                }
                if(x > 5){
                  return true;
                }
                continue;
              }catch(Exception e){
                continue;
              }
            case 7:
              //DownRight
              try{
                int x = 0;
                while(board[r+x][c+x] == board[r][c]){
                  x++;
                }
                if(x > 5){
                  return true;
                }
                continue;
              }catch(Exception e){
                continue;
              }
          }
        }
      }
    }
    return false;
    */
  }

  public int[][] getBoard(){
    return board;
  }

  public String toString(){
    String[] conversions = {"⏺️","🔴","🟡"};
    String output = " ";
    for(int c = 0; c < board[0].length;c++){
        output += ""+(c+1);
      if (c != board[0].length - 1){
        output += " ";
    }
    }
    output += "\n";
    for (int r = 0; r < board.length; r++){
      for(int c = 0; c < board[r].length;c++){
        output += conversions[board[r][c]];
      if (c != board[r].length - 1){
        output += " ";
    }  
      }
      if (r != board.length - 1){
        output += "\n";
    }
  }
  return output;
  }
}
