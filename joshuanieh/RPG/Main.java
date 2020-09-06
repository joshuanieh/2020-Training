import java.util.Vector;
public class Main{
	public static Body player=new Body(Character.PLAYER);
	public static Vector<Body> enemylist=new Vector<>();
	public static Vector<Body> to_be_added=new Vector<>();
	public static void main(String[] args){
		boolean flag=false;
		System.out.println("First run!");
		for(int i=0;i<4;i++)
			enemylist.add(new Body(Character.SOILDER));
		enemylist.add(new Body(Character.GUARD));
		do{
			player.choose();
			if(enemylist.isEmpty()){
				flag=true;
				break;
			}
			Body.enemyattack();
		}while(player.HP>=0);
		if(flag==false)
			System.out.println("You lose, haha!");
		else{
			flag=false;
			player=new Body(Character.PLAYER);
			enemylist.removeAllElements();
			System.out.println("Second run!");
			enemylist.add(new Body(Character.SOILDER));
			enemylist.add(new Body(Character.SOILDER));
			enemylist.add(new Body(Character.WIZARD));
			enemylist.add(new Body(Character.WIZARD));
			do{
				player.choose();
				if(enemylist.isEmpty()){
					flag=true;
					break;
				}
				Body.enemyattack();
			}while(player.HP>=0);
			if(flag==false)
				System.out.println("You lose, haha!");
			else{
				flag=false;
				player=new Body(Character.PLAYER);
				enemylist.removeAllElements();
				System.out.println("Third run!");
				enemylist.add(new Body(Character.BOSS));
				do{
					player.choose();
					if(enemylist.isEmpty()){
						flag=true;
						break;
					}
					Body.enemyattack();
					enemylist.addAll(to_be_added);
					to_be_added.removeAllElements();
				}while(player.HP>=0);
				if(flag)
					System.out.println("You win!!!");
				else
					System.out.println("Too bad, you lose");
			}
		}
	}
}