import java.util.Scanner;
import java.util.Random;
import java.util.Vector;
import java.util.Iterator;
public class Body{
	public int HP;
	public int MP;
	public int AP;
	public int DF;
	public int MDF;
	public String name;
	public int freezing_times=0;
	Body(Character ch){
		switch(ch){
			case PLAYER:
				HP=500;
				MP=500;
				AP=70;
				DF=40;
				MDF=10;
				name="player";
				break;
			case SOILDER:
				HP=150;
				MP=0;
				AP=60;
				DF=10;
				MDF=0;
				name="soilder";
				break;
			case GUARD:
				HP=270;
				MP=0;
				AP=100;
				DF=20;
				MDF=40;
				name="guard";
				break;
			case WIZARD:
				HP=90;
				MP=700;
				AP=5;
				DF=20;
				MDF=60;
				name="wizard";
				break;
			case BOSS:
				HP=1000;
				MP=300;
				AP=90;
				DF=40;
				MDF=40;
				name="boss";
				break;
		}
	}
	void general_attack(){		//for enemy use
		int damage=AP-Main.player.DF;
		if(damage>0)
			Main.player.HP-=damage;
	}
	void general_attack(Body en){		//for player use
		int damage=AP-en.DF;
		if(damage>0)
			en.HP-=damage;
		if(en.HP<=0)
			Main.enemylist.remove(en);
	}
	void magical_attack(Skill sk){
		int damage;
		Vector<Body> to_be_removed=new Vector<>();
		switch(sk){
			case WATERBALL:
				if(MP-50>=0){
					MP-=50;
					Iterator it=Main.enemylist.iterator();
					while(it.hasNext()){
						Body enemy=(Body)it.next();
						damage=50-enemy.MDF;
						if(damage>0)
							enemy.HP-=damage;
						if(enemy.HP<=0)
							to_be_removed.add(enemy);
					}
					Main.enemylist.removeAll(to_be_removed);
				}
				break;
			case STRONGER:
				MP-=40;
				DF+=30;
				break;
			case ELECTRIFY:
				MP-=100;
				damage=250-Main.player.MDF;
				if(damage>0)
					Main.player.HP-=damage;
				break;
			case SUMMON:
				MP-=50;
				Character[] name={Character.SOILDER, Character.GUARD, Character.WIZARD};
				Random rand=new Random();
				Main.to_be_added.add(new Body(name[rand.nextInt(3)]));
		}
	}
	void magical_attack(Skill sk, int i){
		int damage;
		switch(sk){
			case FIREBALL:
				if(MP-50>=0){
					MP-=50;
					damage=150-Main.enemylist.get(i).MDF;
					if(damage>0)
						Main.enemylist.get(i).HP-=damage;
					if(Main.enemylist.get(i).HP<=0)
						Main.enemylist.remove(i);
				}
				break;
			case FREEZE:
				if(MP-100>=0){
					MP-=100;
					Main.enemylist.get(i).freezing_times+=3;
					if(Main.enemylist.get(i).freezing_times>3)
						Main.enemylist.get(i).freezing_times=3;
				}
				break;
		}
	}
	void choose(){
		Scanner scanner=new Scanner(System.in);
		System.out.println("Player\tHP\tMP\tAP\tDF\tMDF\t");
		System.out.println("\t"+ Main.player.HP+ "\t"+ Main.player.MP+"\t"+ Main.player.AP+ "\t"+ Main.player.DF+ "\t"+ Main.player.MDF);
		System.out.println("Enemy\tHP\tMP\tAP\tDF\tMDF\tnumber\tfrozen times");
		Iterator it=Main.enemylist.iterator();
		while(it.hasNext()){
			Body enemy=(Body)it.next();
			System.out.println(enemy.name+ "\t"+ enemy.HP+ "\t"+ enemy.MP+"\t"+ enemy.AP+ "\t"+ enemy.DF+ "\t"+ enemy.MDF+ "\t"+ Main.enemylist.indexOf(enemy)+ "\t"+ enemy.freezing_times);
		}
		if(MP>=40){
			System.out.println("general_attack:1, magical_attack:2");
			int i=scanner.nextInt();
			if(i==1){
				System.out.println("Whom to do the general attack?");
				Main.player.general_attack(Main.enemylist.get(scanner.nextInt()));
			}
			else{
				if(Main.player.MP>=100)
					System.out.println("stronger:1, fireball:2, waterball:3, freeze:4");
				else if(Main.player.MP>=50)
					System.out.println("stronger:1, fireball:2, waterball:3");
				else
					System.out.println("stronger:1");
				System.out.println("Which magical attack would you use?");
				int j=scanner.nextInt();
				switch(j){
					case 3:
						Main.player.magical_attack(Skill.WATERBALL);
						break;
					case 2:
						System.out.println("Whom to do the firball attack?");
						Main.player.magical_attack(Skill.FIREBALL, scanner.nextInt());
						break;
					case 1:
						Main.player.magical_attack(Skill.STRONGER);
						break;
					case 4:
						System.out.println("Whom to do the freeze attack?");
						Main.player.magical_attack(Skill.FREEZE, scanner.nextInt());
						break;			
				}
			}
		}
		else{
			System.out.println("Whom to do general attack?");
			Main.player.general_attack(Main.enemylist.get(scanner.nextInt()));
		}
	}
	static void enemyattack(){
		Iterator it=Main.enemylist.iterator();
		while(it.hasNext()){
			Body enemy=(Body)it.next();
			if(enemy.freezing_times>0)
				enemy.freezing_times-=1;
			else{
				switch(enemy.name){
					case "soilder":
						enemy.general_attack();
						break;
					case "guard":
						enemy.general_attack();
						break;
					case "wizard":
						if(enemy.MP>=100)
							enemy.magical_attack(Skill.ELECTRIFY);
						else
							enemy.general_attack();
						break;
					case "boss":
						if(enemy.MP>=50)
							enemy.magical_attack(Skill.SUMMON);
						else
							enemy.general_attack();
						break;
				}
			}
		}
	}
}