package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {// 继承键盘事件监听接口
	private int x, y;
	private int dx, dy;
	private int direction;// 保存方向值
	public static final int SOUTH = 0, NORTH = 1, EAST = 2, WEST = 3;// 向南、北、东、西运动
	private Snake sk;// 建立贪吃蛇对象
	private Food bk;// 建立食物对象
	Image im;
	Graphics g;
	ImageIcon ima=new ImageIcon("C:\\Users\\Alienware\\Desktop\\123.jpg");

	public GamePanel() {
		JFrame f = new JFrame("贪吃蛇");
		f.setLocation(600, 100);
		f.setSize(1000, 800);
		f.add(this);
		f.setDefaultCloseOperation(3);

		x = 50;
		y = 50;
		dx = 10;
		dy = 10;
		addKeyListener(this);// 注册键盘事件监听器
		// this.setVisible(true);
		// System.out.println(this.getGraphics());
		f.setVisible(true);
		
		// 实例化贪吃蛇的对象，并传递一个GamePanel对象的引用
		sk = new Snake(this);
		// 实例化食物对象并传递一个GamePanel对象和Snake对象的引用
		bk = new Food(this, sk);
		this.requestFocus();
	}

	public void gameUpdate() {
		sk.update();// 更新贪吃蛇坐标位置
		bk.update(this);// 更新食物坐标位置

		switch (direction) {
		case SOUTH:
			y = y + dy;
			break;
		case NORTH:
			y = y - dy;
			break;
		case EAST:
			x = x + dx;
			break;
		case WEST:
			x = x - dx;
			break;
		}
	}

	// Image im=new BufferedImage(100,100,BufferedImage.TYPE_4BYTE_ABGR);
	public void gameRender(Image im) {
		// System.out.println(im);
		Graphics dbg = im.getGraphics();
		dbg.drawImage(ima.getImage(),0,0,this.getWidth(),this.getHeight(),null);
		sk.draw(dbg);// 在后备缓冲区绘制贪吃蛇图形
		bk.draw(dbg);// 在后备缓冲区绘制食物图形
	}

	public void gamePaint(Image im) {
		g = this.getGraphics();
		// System.out.println(im);
		// System.out.println(g);
		g.drawImage(im, 0, 0, null);// 将后备缓冲区的内容在屏幕上显示出来
		//g.dispose();
	}

	boolean isPaused = false;
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			im = new BufferedImage(this.getWidth(), this.getHeight(),
					BufferedImage.TYPE_4BYTE_ABGR);
			
			if (isPaused == false) {
				gameUpdate();
			}
			gameRender(im);
			gamePaint(im);
			//g.drawImage(im, 0, 0, null);
			// System.out.println("x="+x+"   y="+y);
		}
	}

	public void keyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();// 获取按键信息
		System.out.println("keycode=" + keycode);
		if (keycode == KeyEvent.VK_SPACE)// 若按下的是“空格”键。则切换
			isPaused = !isPaused;

		switch (keycode) {// 根据不同的按键为direction赋值
		case KeyEvent.VK_DOWN:// 如果按键盘“下”方向键
			direction = SOUTH;
			System.out.println(direction);
			break;
		case KeyEvent.VK_UP:// 如果按键盘“上”方向键
			direction = NORTH;
			System.out.println(direction);
			break;
		case KeyEvent.VK_RIGHT:// 如果按键盘“右”方向键
			direction = EAST;
			System.out.println(direction);
			break;
		case KeyEvent.VK_LEFT:// 如果按键盘“左”方向键
			direction = WEST;
			System.out.println(direction);
			break;

		}

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public int getDirection() {
		return direction;
	}

	public static void main(String[] args) {
		GamePanel g = new GamePanel();
		Thread thread1 = new Thread(g);
		thread1.start();

	}

}
