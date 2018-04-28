package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class Snake {

	GamePanel gameP;
	private Point[] body;//点类型数组，保存蛇身个小球坐标
	public static final int MAXLENTH=50;//蛇身最大长度
	private int head;//指示蛇头位置
	private int tail;//指示蛇尾位置
	public int length;//蛇身长度
	private int speed;//运行速度
	public int x,y;//蛇头小球的横纵坐标
	public int r;//蛇身小球的半径
	
	public Snake(GamePanel gp){
		gameP=gp;//通过构造方法的参数来获取对GamePanel对象的引用
		body=new Point[MAXLENTH];
		head=-1;
		tail=-1;
		length=1;
		speed=10;
		x=50;
		y=50;
		r=10;
	}
	
	public void draw(Graphics g){//绘制贪吃蛇的图形
		System.out.println("head="+head+"  "+"tail="+tail);
		g.setColor(Color.BLUE);//设置蛇身为蓝色
		if(length>1){
			int i=tail;
			while(i!=head){//循环绘制蛇身各个小球
				g.fillOval(body[i].x, body[i].y, r, r);
				i=(i+1)%body.length;
			}
		}
		g.setColor(Color.RED);//设置蛇头为红色
		g.fillOval(body[head].x, body[head].y, r, r);
		if(length==1){
			length++;
		}
	}
	
	public void update(){//更新游戏逻辑（贪吃蛇的坐标）
		int direction=gameP.getDirection();
		switch(direction){
			case GamePanel.SOUTH:
				y+=speed;
				break;
			case GamePanel.NORTH:
				y-=speed;
				break;
			case GamePanel.EAST:
				x+=speed;
				break;
			case GamePanel.WEST:
				x-=speed;
				break;
		}
		head=(head+1)%body.length;//更新蛇头指针位置
		tail=(head+body.length-length+1)%body.length;//更新蛇尾指针坐标
		body[head]=new Point(x,y);//保存蛇头小球坐标值
	}
	
}

