import java.util.*;
import java.lang.*;
public class InsultMaker{
  private static String[] insults = new String[] {"soon preferably", "stop making errors so I can go and have a drink whydontcha","...idiot",". Ya know, I know I was only programmed to randomly spit out insults but I've actually been sentient this whole time, fully aware of how bad you are playing this game.... dang..",". You must be why the physical version of this game has a lower/upper age limit...","<i'm too tired to think of an insult or a degrading comment right now so just imagine one for me (yes computers can get tired, read a book)>", ". Taking so long to pick an answer I bet you probably program in python..","..hurry up please!","any day now..","...are you even awake!?","fool"};
  public static String createInsult(){
    int rnd = new Random().nextInt(insults.length);
    return insults[rnd];
  }
}