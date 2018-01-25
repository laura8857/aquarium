//資管二B 101403032 胡瑋庭 HW4
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class HW4_101403032 extends JFrame {
	private JButton fish = new JButton("增加魚");
	private JButton turtle = new JButton("增加烏龜");
	private JButton clear = new JButton("移除選單");
	private JButton clearall = new JButton("移除全部");
	private JLabel funtion = new JLabel("目前功能:");
	private JLabel total = new JLabel("魚數量:0");
	private JPanel aPanel;
	private CGPanel bPanel;
	private Color myblue = new Color(173, 216, 230);// 水的顏色
	private String[] fIcon = { "1.png", "2.png", "3.png", "4.png", "5.png",
			"6.png" };
	private String[] tIcon = { "w.png", "w2.png" };
	private int m = 0; // fish數量
	private int l;// choose
	private final static Random ran = new Random();
	public Point p1;
	// fish and turtle
	private int fpx[] = new int[10000];
	private int fpy[] = new int[10000];
	private int tpx[] = new int[10000];
	private int tpy[] = new int[10000];
	private int flgth[] = new int[10000];
	private int tlgth[] = new int[10000];
	private int fcount = -1;
	private int tcount = -1;
	private Image fisharray[] = new Image[10000];
	private Image turtlearray[] = new Image[10000];
	private int fishdirection[] = new int[10000];
	private int turtledirection[] = new int[10000];
	private Thread fishThread[] = new Thread[10000];
	private Thread turtleThread[] = new Thread[10000];
	public boolean run = false;
	private boolean frunning[] = new boolean[10000];
	private boolean trunning[] = new boolean[10000];

	private Image srcImage = null;

	public HW4_101403032() {

		// 上層button的panel
		aPanel = new JPanel();
		aPanel.setLayout(new GridLayout(3, 2));
		aPanel.setBackground(Color.BLACK);
		add(aPanel, BorderLayout.NORTH);

		// button
		aPanel.add(fish);
		aPanel.add(clear);
		aPanel.add(turtle);
		aPanel.add(clearall);

		// button相關處置
		ButtonHandler handler = new ButtonHandler();
		fish.addActionListener(handler);
		turtle.addActionListener(handler);
		clear.addActionListener(handler);
		clearall.addActionListener(handler);

		// other funtion
		funtion.setForeground(Color.cyan);
		total.setForeground(Color.cyan);
		aPanel.add(funtion);
		aPanel.add(total);

		// 底層畫面panel
		bPanel = new CGPanel();
		bPanel.setBackground(myblue);
		add(bPanel, BorderLayout.CENTER);

		// 滑鼠
		MouseHandler handler1 = new MouseHandler();
		bPanel.addMouseListener(handler1);

	}

	private class MouseHandler extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			p1 = e.getPoint();

			if (l == 1) { // get fish
				srcImage = getFish();
				fcount++;
				m++;
				total.setText("魚數量:" + m);

				fpx[fcount] = p1.x;
				fpy[fcount] = p1.y;
				int i = getsize();
				flgth[fcount] = i;
				fisharray[fcount] = srcImage;
				fishThread[fcount] = new Thread(new fishmove(fcount));
				fishThread[fcount].start();
				frunning[fcount] = true;
				repaint();
			} else if (l == 2) { // get turtle
				srcImage = getTurtle();
				tcount++;
				tpx[tcount] = p1.x;
				tpy[tcount] = p1.y;
				int i = getsize();
				tlgth[tcount] = i;
				turtlearray[tcount] = srcImage;

				turtleThread[tcount] = new Thread(new turtlemove(tcount));
				turtleThread[tcount].start();
				trunning[tcount] = true;
				repaint();

			} else if (l == 3) {
				for (int i = 0; i < fcount + 1; i++) {
					if (p1.x >= fpx[i] && p1.x <= fpx[i] + flgth[i]
							&& p1.y >= fpy[i] && p1.y <= fpy[i] + flgth[i]) {

						frunning[i] = run;
						flgth[i] = 0; // 邊長變成0
						m--;
						total.setText("魚數量:" + m);
						repaint();

						break;
					}
				}

				for (int i = 0; i < tcount + 1; i++) {
					if (p1.x >= tpx[i] && p1.x <= tpx[i] + tlgth[i]
							&& p1.y >= tpy[i] && p1.y <= tpy[i] + tlgth[i]) {

						trunning[i] = run;
						tlgth[i] = 0; // 邊長變成0
						repaint();

						break;
					}
				}
			}

		}
	}

	public Image getFish() {
		Image fish = null;
		int i = ran.nextInt(6);
		try {
			fish = ImageIO.read(getClass().getResource(fIcon[i]));
			fishdirection[fcount + 1] = i;
		} catch (IOException io) {
			JOptionPane.showMessageDialog(null, "Image Not Found !");
		}
		return fish;
	}

	public Image getTurtle() {
		Image turtle = null;
		int i = ran.nextInt(2);
		try {
			turtle = ImageIO.read(getClass().getResource(tIcon[i]));
			turtledirection[tcount + 1] = i;

		} catch (IOException io) {
			JOptionPane.showMessageDialog(null, "Image Not Found !");
		}
		return turtle;
	}

	public int getsize() {
		int i = ran.nextInt(50) + 50;
		return i;
	}

	class CGPanel extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			for (int i = 0; i < fcount + 1; i++) {
				g.drawImage(fisharray[i], fpx[i], fpy[i], flgth[i], flgth[i],
						null);

			}

			for (int i = 0; i < tcount + 1; i++) {
				g.drawImage(turtlearray[i], tpx[i], tpy[i], tlgth[i], tlgth[i],
						null);

			}

		}
	}

	// button

	private class ButtonHandler implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == fish) {
				funtion.setText("目前功能:增加魚");
				l = 1;

			} else if (event.getSource() == turtle) {

				funtion.setText("目前功能:增加烏龜");
				l = 2;

			} else if (event.getSource() == clear) {
				funtion.setText("目前功能:移除選單");
				l = 3;

			} else if (event.getSource() == clearall) {
				funtion.setText("目前功能:移除全部");
				l = 4;
				fpx = new int[10000];
				fpy = new int[10000];
				tpx = new int[10000];
				tpy = new int[10000];
				tlgth = new int[10000];
				flgth = new int[10000];
				fisharray = new Image[10000];
				turtlearray = new Image[10000];
				total.setText("魚數量:0");
				m = 0;// 魚數量歸0
				fishdirection = new int[10000];
				turtledirection = new int[10000];
				fishThread = new Thread[10000];
				turtleThread = new Thread[10000];
				fcount = -1;
				tcount = -1;
				frunning = new boolean[10000];
				trunning = new boolean[10000];

			}

		}
	}

	public class fishmove implements Runnable {
		int speedx, speedy;
		int countfish;
		private Timer timer = new Timer();

		public fishmove(int c) {
			timer.schedule(new ranSpeed(), 5000, 3000);
			countfish = c;
			if (fishdirection[countfish] == 0 || fishdirection[countfish] == 2
					|| fishdirection[countfish] == 4)
				switch (ran.nextInt(2)) {
				case 0:
					speedx = ran.nextInt(5) + 1;
					speedy = ran.nextInt(5) + 1;
					break;
				case 1:
					speedx = ran.nextInt(5) + 1;
					speedy = -(ran.nextInt(5) + 1);
					break;
				default:
					break;
				}
			else if (fishdirection[countfish] == 1
					|| fishdirection[countfish] == 3
					|| fishdirection[countfish] == 5) {
				switch (ran.nextInt(2)) {
				case 0:
					speedx = -(ran.nextInt(5) + 1);
					speedy = ran.nextInt(5) + 1;
					break;
				case 1:
					speedx = -(ran.nextInt(5) + 1);
					speedy = -(ran.nextInt(5) + 1);
					break;
				default:
					break;
				}
			}
		}

		public class ranSpeed extends TimerTask {

			public void run() {
				int speedx0 = speedx;
				switch (ran.nextInt(4)) {
				case 0:
					speedx = ran.nextInt(5) + 1;
					speedy = ran.nextInt(5) + 1;
					break;
				case 1:
					speedx = -(ran.nextInt(5) + 1);
					speedy = ran.nextInt(5) + 1;
					break;
				case 2:
					speedx = ran.nextInt(5) + 1;
					speedy = -(ran.nextInt(5) + 1);
					break;
				case 3:
					speedx = -(ran.nextInt(5) + 1);
					speedy = -(ran.nextInt(5) + 1);
					break;
				default:
					break;
				}
				if (speedx0 * speedx < 0) {
					if (speedx0 > 0) {
						try {
							switch (fishdirection[countfish]) {
							case 0:
								fisharray[countfish] = ImageIO.read(getClass()
										.getResource(fIcon[1]));
								fishdirection[countfish] = 1;
								break;
							case 2:
								fisharray[countfish] = ImageIO.read(getClass()
										.getResource(fIcon[3]));
								fishdirection[countfish] = 3;
								break;
							case 4:
								fisharray[countfish] = ImageIO.read(getClass()
										.getResource(fIcon[5]));
								fishdirection[countfish] = 5;
								break;
							default:
								break;
							}
						

						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							switch (fishdirection[countfish]) {
							case 1:
								fisharray[countfish] = ImageIO.read(getClass()
										.getResource(fIcon[0]));
								fishdirection[countfish] = 0;
								break;
							case 3:
								fisharray[countfish] = ImageIO.read(getClass()
										.getResource(fIcon[2]));
								fishdirection[countfish] = 2;
								break;
							case 5:
								fisharray[countfish] = ImageIO.read(getClass()
										.getResource(fIcon[4]));
								fishdirection[countfish] = 4;
								break;
							default:
								break;
							}
				

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}

		}

		@Override
		public void run() {
			while (frunning[countfish]) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}

				fpx[countfish] = fpx[countfish] + speedx;
				fpy[countfish] = fpy[countfish] + speedy;

				// While Fish at the Panel's border
				if (fpx[countfish] + flgth[countfish] >= 793) {
					speedx = -speedx;
					try {
						switch (fishdirection[countfish]) {
						case 0:
							fisharray[countfish] = ImageIO.read(getClass()
									.getResource(fIcon[1]));
							fishdirection[countfish] = 1;
							break;
						case 2:
							fisharray[countfish] = ImageIO.read(getClass()
									.getResource(fIcon[3]));
							fishdirection[countfish] = 3;
							break;
						case 4:
							fisharray[countfish] = ImageIO.read(getClass()
									.getResource(fIcon[5]));
							fishdirection[countfish] = 5;
							break;
						default:
							break;
						}
					

					} catch (IOException io) {
					}
				}

				if (fpx[countfish] <= 0) {
					speedx = -speedx;
					try {
						switch (fishdirection[countfish]) {
						case 1:
							fisharray[countfish] = ImageIO.read(getClass()
									.getResource(fIcon[0]));
							fishdirection[countfish] = 0;
							break;
						case 3:
							fisharray[countfish] = ImageIO.read(getClass()
									.getResource(fIcon[2]));
							fishdirection[countfish] = 2;
							break;
						case 5:
							fisharray[countfish] = ImageIO.read(getClass()
									.getResource(fIcon[4]));
							fishdirection[countfish] = 4;
							break;
						default:
							break;
						}
					
					} catch (IOException io) {
					}
				}

				if (fpy[countfish] <= 0
						|| fpy[countfish] + flgth[countfish] >= 488) {
					speedy = -speedy;
				}
				repaint();
			}
		}
	}

	public class turtlemove implements Runnable {
		private int speedx;
		int countturtle;

		public turtlemove(int c) {
			countturtle = c;
			if (turtledirection[countturtle] == 0)
				speedx = ran.nextInt(5) + 1;
			else
				speedx = -(ran.nextInt(5) + 1);
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			while (tpy[countturtle] + tlgth[countturtle] <= 450) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
				tpy[countturtle] = tpy[countturtle] + 5;
				repaint();
			}

			while (trunning[countturtle]) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}

				tpx[countturtle] = tpx[countturtle] + speedx;

				// While Turtle at the Panel's Border
				if (tpx[countturtle] + tlgth[countturtle] >= 745) {
					speedx = -speedx;
					try {
						turtlearray[countturtle] = ImageIO.read(getClass()
								.getResource(tIcon[1]));
						turtledirection[countturtle] = 1;

					} catch (IOException io) {
					}
				}

				if (tpx[countturtle] <= 0) {
					speedx = -speedx;
					try {
						turtlearray[countturtle] = ImageIO.read(getClass()
								.getResource(tIcon[0]));
						turtledirection[countturtle] = 0;

					} catch (IOException io) {
					}
				}
				repaint();
			}
		}
	}
}
