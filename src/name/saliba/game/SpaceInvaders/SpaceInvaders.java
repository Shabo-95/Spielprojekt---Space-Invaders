package name.saliba.game.SpaceInvaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import name.panitz.game.framework.AbstractGame;
import name.panitz.game.framework.Button;
import name.panitz.game.framework.GameObject;
import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.KeyCode;
import name.panitz.game.framework.SoundObject;
import name.panitz.game.framework.TextObject;
import name.panitz.game.framework.Vertex;
import name.saliba.game.play.fx.PlayFX;

public class SpaceInvaders<I,S> extends AbstractGame<I,S>{
  //Here Are The Lists Of Objects
  List<GameObject<I>> hintergrund = new ArrayList<>();
  List<GameObject<I>> gegner = new ArrayList<>();
  List<GameObject<I>> superGegner = new ArrayList<>();
  List<GameObject<I>> bullet = new ArrayList<>();
  List<GameObject<I>> gifts = new ArrayList<>();
  List<GameObject<I>> boss = new ArrayList<>();
  List<GameObject<I>> bossBullet = new ArrayList<>();
  //To Avoid The Concurrent Modification Exception Error
  List<GameObject<I>> toRemove = new ArrayList<>();
  //
  //Here Are The Sound Objects
  SoundObject<S> playerIsKilled = new SoundObject<>("PlayerIsKilled.wav");
  SoundObject<S> enemyIsKilled = new SoundObject<>("EnemyIsKilled.wav");
  SoundObject<S> shootingBullets = new SoundObject<>("ShootingBullets.wav");
  SoundObject<S> giftSound = new SoundObject<>("GiftSound.wav");
  //
  // New
  private boolean left = false;
  private boolean right = false;
  private boolean up = false;
  private boolean down = false;
  private boolean isShooting = false;
  private boolean bossArrived = false;
  private boolean restarting = false;
  //New
  private int lives = 3;
  private int score = 0;
  private int bossEnergy = 30;
  //Create A Timer
  private double bulletTimer = 0;
  private double bossBulletTimer = 2000;
  private double enemyTimer = 0;
  private double giftsTimer = 10000;
  //
  //Random Number Generator
  Random random = new Random();
  //
  //Lives Text
  TextObject<I> livesInfo
    = new TextObject<>(new Vertex(30,35)
       ,"Lives: "+lives,"Arial",28);
  //Score Text
  TextObject<I> scoreInfo
  = new TextObject<>(new Vertex(30,65)
     ,"Score: "+score,"Arial",28);
  //BossEnergy Text
  TextObject<I> bossEnergyInfo
  = new TextObject<>(new Vertex(30,95)
     ,"","Arial",28);
  //Constructor
  public SpaceInvaders() {
    super(new ImageObject<>("Player.png"
        ,new Vertex(200,200),new Vertex(1,0)),800,600);

    hintergrund.add(new ImageObject<>("Background.jpg"));
    hintergrund.add(livesInfo);
    hintergrund.add(scoreInfo);
    hintergrund.add(bossEnergyInfo);

    gegner.add(new ImageObject<>("Enemy.png"
        ,new Vertex(800,random.nextInt((500 - 420) + 1) + 420),new Vertex(-1.5,0)));
    gegner.add(new ImageObject<>("Enemy.png"
        ,new Vertex(800,random.nextInt((350 - 230) + 1) + 230),new Vertex(-1.5,0)));
    gegner.add(new ImageObject<>("Enemy.png"
        ,new Vertex(800,random.nextInt((190 - 50) + 1) + 50),new Vertex(-1.5,0)));
       
    getGOss().add(hintergrund);
    goss.add(gegner);
    goss.add(bullet);
    goss.add(gifts);
    goss.add(superGegner);
    goss.add(boss);
    goss.add(bossBullet);
    
    getButtons().add(new Button("Pause", ()-> pause()));
    getButtons().add(new Button("Start", ()-> start()));
    getButtons().add(new Button("Exit", ()-> System.exit(0)));
  }
  //End Of Constructor
  //Updating
  @Override
  public void doChecks() {
	  //Updating The Lives and Score InfoText
      livesInfo.text = "Lives: "+lives;
      scoreInfo.text = "Score: "+score;
	  //
	  giftsTimer -= 10;
	  bossBulletTimer -= 10;
	  
	  for(GameObject<I> b:boss) {
	  if (bossBulletTimer <= 0) {
		    bossBullet.add(new ImageObject<>("BossBullet.png"
		            ,new Vertex(b.getPos().x,b.getPos().y+90),new Vertex(-3.5,0)));
		    bossBulletTimer = 1000;
	  }
	  }
	  if (giftsTimer <= 0) {
		    gifts.add(new ImageObject<>("Gift.png"
		            ,new Vertex(random.nextInt((600 - 100) + 1) + 100,0),new Vertex(0,1.5)));
		    giftsTimer = 10000;
	  }
	  //Testing the Enemy & Bullets Numbers
	  System.out.println("bullet = "+bullet.size());
	  System.out.println("Gegner = "+gegner.size());
	  System.out.println("Score = "+score);
	  System.out.println("Bullet Timer = "+bulletTimer);
	  System.out.println("Enemy Timer = "+enemyTimer);
	  System.out.println("Gifts Timer = "+giftsTimer);
	  System.out.println(getPlayer().getPos().y);
	  //Timer Delay
	  if(bulletTimer > 0) {
		  bulletTimer-=100;
	  }
	  if(enemyTimer > 0) {
		  enemyTimer-=100;
	  }
	  //
	 //Adding Enemy if Enemy <= 2; 
	 if(gegner.size() <= 2 && !bossArrived) {
		//Test superGegner
		superGegner.add(new ImageObject<>("Enemy.png"
			      ,new Vertex(800,player.getPos().y),new Vertex(-5,0)));
		//
		gegner.add(new ImageObject<>("Enemy.png"
			      ,new Vertex(800,random.nextInt((500 - 420) + 1) + 420),new Vertex(-1.5,0)));
		
		gegner.add(new ImageObject<>("Enemy.png"
		      ,new Vertex(800,random.nextInt((350 - 230) + 1) + 230),new Vertex(-1.5,0)));
		if(enemyTimer <= 0) {
		gegner.add(new ImageObject<>("Enemy.png"
			  ,new Vertex(800,random.nextInt((190 - 50) + 1) + 50),new Vertex(-1.5,0)));
		enemyTimer = 6000;
		}
	 }
	 //Check if The Bullet Touches The Enemy	 
	  for (GameObject<I> bu:bullet) {
	    for (GameObject<I> ge:gegner) {
		  if(bu.touches(ge)) {
			playSound(enemyIsKilled);
			score++;
			toRemove.add(ge);
			toRemove.add(bu);
		  }
		}
	  }
	  //
	  //Check if The Bullet Touches The SuperGegner
	  for (GameObject<I> bu:bullet) {
		    for (GameObject<I> sg:superGegner) {
			  if(bu.touches(sg)) {
				playSound(enemyIsKilled);
				score++;
				toRemove.add(sg);
				toRemove.add(bu);
			  }
			}
		  }
	  //
	  //Check that the Enemy don't Touch Another Enemy
	  ////////////////////////////////////////////////
	  //

    for (GameObject<I> g:gegner){
    //Check if The Enemy Goes Outside The Screen
      if (g.getPos().x+g.getWidth()<0) {
        g.getPos().x = getWidth();
        g.getPos().y = random.nextInt((500 - 50) + 1) + 50;
      }
    //
    //Check if The Enemy Touches The Player
      if (player.touches(g)){ 
    	g.getPos().moveTo(new Vertex(getWidth()+10,random.nextInt((500 - 50) + 1) + 50));
        lives--;
        player.getPos().moveTo(new Vertex(100,300));
        playSound(playerIsKilled);
      }
    }
    //
	//Check if SuperGegner Goes Outside The Screen
	 for(GameObject<I> sg:superGegner) {
		 if(sg.getPos().x<=0) {
			 sg.getPos().x = 800;
			 sg.getPos().y = getPlayer().getPos().y;
		 }
		//Check if The SuperGegner Touches The Player
	      if (player.touches(sg)){ 
	        lives--;
	        toRemove.add(sg);
	        sg.getPos().moveTo(new Vertex(getWidth()+10,800));
	        player.getPos().moveTo(new Vertex(100,300));
	        playSound(playerIsKilled);
	      }
	    }
    //
	//Check if The Player Touches The Gift
	 for(GameObject<I> gi:gifts) {
		 if(player.touches(gi)) {
			 toRemove.add(gi);
			 playSound(giftSound);
			 lives++;
		 }
		 if(gi.getPos().y >= 650) toRemove.add(gi);
	 }
	//
	 
    //Check if The User W, S, A, D Buttons Pressed
    movePlayer();
    //
    
	//Check if The Player Goes Outside The Screen
    if(player.getPos().x <= 0) {
    	player.getPos().x = 0;
    	//Smoother Movment
    	player.getVelocity().x*= -0.010;
    }
    if(player.getPos().x+player.getWidth()+5>=800) {
    	player.getPos().x = 800-player.getWidth()-5;
    	//Smoother Movment
    	player.getVelocity().x*= -0.010;
    }
    if(player.getPos().y<=0) {
    	player.getPos().y = 0;
    	//Smoother Movment
    	player.getVelocity().y*= -0.010;
    }
    if(player.getPos().y+player.getHeight()+60>=650) {
    	player.getPos().y = 630-player.getHeight()-40;
    	//Smoother Movment
    	player.getVelocity().y *= -0.010;
    }
  //
    if (lives <= 0 && !restarting){
      gegner.add(new TextObject<>(new Vertex(270,200)
          ,"Game Over","Arial",56));
      gegner.add(new TextObject<>(new Vertex(270,300)
              ,"Your Score: "+score,"Arial",40));
      gegner.add(new TextObject<>(new Vertex(270,400)
              ,"You are Dead! You Lose","Arial",40));
      livesInfo.text = "Lives: "+0;
      gegner.add(new TextObject<>(new Vertex(270,500)
              ,"Press R To Restart","Arial",30));
      gegner.add(new TextObject<>(new Vertex(270,550)
              ,"Programming By: Shabo Saliba","Arial",20));
      pause();   
    }
    //The Boss//
    if(score >= 30 && !bossArrived) {
    	bossArrived = true;
    	for(GameObject<I> g:gegner)
    		toRemove.add(g);
    	for(GameObject<I> sg:superGegner)
    		toRemove.add(sg);
		    boss.add(new ImageObject<>("Boss.png"
		            ,new Vertex(900,300),new Vertex(-2,0)));
		    bossEnergyInfo.text = "Boss Energy: "+bossEnergy;
    	}
    	for(GameObject<I> b:boss) {
    		boolean boo = true;
    		if(b.getPos().x <= 550 && boo) {
    			boo = false;
    			b.getVelocity().x = 0;
    			b.getVelocity().y = getPlayer().getVelocity().y;
    		}
    		if(boo) {
    			b.getPos().y = getPlayer().getPos().y - 50;
    		}
    	    if(player.touches(b)) {
    	        lives--;
    	        player.getPos().moveTo(new Vertex(100,300));
    	        playSound(playerIsKilled);
    	    }
    	    for(GameObject<I> bu: bullet) {
    	    	if(bu.touches(b)) {
    	    		toRemove.add(bu);
    	    		bossEnergy--;
    	    	    bossEnergyInfo.text = "Boss Energy: "+bossEnergy;
    	    		b.getVelocity().y *= -1;
    	    	}
    	    }
    	    for(GameObject<I> bb:bossBullet) {
    	    	if(player.touches(bb)) {
        	        lives--;
        	        player.getPos().moveTo(new Vertex(100,300));
        	        b.getPos().y = getPlayer().getPos().y - 50;
        	        toRemove.add(bb);
        	        playSound(playerIsKilled);
    	    	}
    	    }
    	    if(bossEnergy <= 0 && lives>=1) {
    	    	toRemove.add(b);
	            score = 100;
    	        gegner.add(new TextObject<>(new Vertex(270,200)
    	                ,"Game Over","Arial",56));
    	            gegner.add(new TextObject<>(new Vertex(270,300)
    	                    ,"Your Score: "+100,"Arial",40));
    	            gegner.add(new TextObject<>(new Vertex(270,400)
    	                    ,"Boss is Dead! You Win","Arial",40));
    	            gegner.add(new TextObject<>(new Vertex(270,500)
    	                    ,"Press R To Restart","Arial",30));
    	            gegner.add(new TextObject<>(new Vertex(270,550)
    	                    ,"Programming By: Shabo Saliba","Arial",20));
    	            pause();
    	    }
    	}
    //
    //RemoveAll
    gegner.removeAll(toRemove);
    superGegner.removeAll(toRemove);
    bullet.removeAll(toRemove);
    gifts.removeAll(toRemove);
    boss.removeAll(toRemove);
    bossBullet.removeAll(toRemove);
  }
  // End Of Update //

  @Override
  public boolean isStopped() {
    return super.isStopped() /*|| lives<=0*/;
  }

  public void movePlayer() {  
  	  if(left) {
      	moveLeft();
      }
      if(right) {
      	moveRight();
      }
      if(up) {
      	moveUp();
      }
      if(down) {
      	moveDown();
      }
  }
      
  // New **
  void moveLeft(){
	  getPlayer().getVelocity().move(new Vertex(-0.040,0));
  }
  void moveRight(){
	  getPlayer().getVelocity().move(new Vertex(0.040,0));
  }
  void moveUp(){
	  getPlayer().getVelocity().move(new Vertex(0,-0.040));
  }
  void moveDown(){
	  getPlayer().getVelocity().move(new Vertex(0,0.040));
  }
  void shoot() {
	  playSound(shootingBullets);
	  bullet.add(new ImageObject<>("PlayerBullet.png"
	 		  ,new Vertex(getPlayer().getPos().x+70,getPlayer().getPos().y+30),new Vertex(2,0)));
  }
  void restartGame() {
	  restarting = true;
	  for(GameObject<I> bb:bossBullet) {
		  toRemove.add(bb);
	  }
	  for(GameObject<I> b:bullet) {
		  toRemove.add(b);
	  }
	  for(GameObject<I> g:gegner) {
		  toRemove.add(g);
	  }
	  for(GameObject<I> sg:superGegner) {
		  toRemove.add(sg);
	  }
	  for(GameObject<I> gi:gifts) {
		  toRemove.add(gi);
	  }
	  for(GameObject<I> b:boss) {
		  toRemove.add(b);
	  }
      player.getPos().moveTo(new Vertex(100,300));
      lives = 3;
      score = 0;
      bossEnergyInfo.text = "";
      left = false;
      right = false;
      up = false;
      down = false;
      isShooting = false;
      bulletTimer = 0;
      bossBulletTimer = 2000;
      enemyTimer = 0;
      giftsTimer = 10000;
      bossEnergy = 30;
      bossArrived = false;
      gegner.add(new ImageObject<>("Enemy.png"
    	        ,new Vertex(800,random.nextInt((500 - 420) + 1) + 420),new Vertex(-1.5,0)));
      gegner.add(new ImageObject<>("Enemy.png"
    	        ,new Vertex(800,random.nextInt((350 - 230) + 1) + 230),new Vertex(-1.5,0)));
      gegner.add(new ImageObject<>("Enemy.png"
    	        ,new Vertex(800,random.nextInt((190 - 50) + 1) + 50),new Vertex(-1.5,0)));
      start();
      restarting = false;
  }

  @Override
  public void keyPressedReaction(KeyCode keycode) {
    if (keycode!=null)
     switch (keycode){
      case VK_D:
    	  //Smoother Movement
    	  right = true;
        break;
      case VK_A:
    	//Smoother Movement
    	  left = true;
        break;
      case VK_S:
    	//Smoother Movement
    	  down = true;
        break;
      case VK_W:
    	//Smoother Movement
    	  up = true;
        break;
      case VK_R:
    	  restartGame();
    	  break;
      case VK_SPACE:
    	  if(isShooting == false) {
    		  if(bulletTimer == 0) {
              shoot();
              bulletTimer = 2500;
    		  }
              isShooting = true;
    	  }
    	  break;
      default: ;
     }
  }
  
 @Override
 public void keyReleasedReaction(KeyCode keycode) {
   if (keycode!=null)
	 switch (keycode){
       case VK_D:
    	   right = false;
         getPlayer().getVelocity().move(new Vertex(-0.040,0));
         break;
       case VK_A:
    	   left = false;
         getPlayer().getVelocity().move(new Vertex(+0.040,0));
         break;
       case VK_S:
    	   down = false;
         getPlayer().getVelocity().move(new Vertex(0,-0.040));
         break;
       case VK_W:
    	   up = false;
         getPlayer().getVelocity().move(new Vertex(0,+0.040));
         break;
       case VK_SPACE:
     	  isShooting = false;
     	  break;
       default: ;
      }
   }
	   
}

