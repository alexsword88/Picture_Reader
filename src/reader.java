import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class reader implements ActionListener
{
	//========build UI=========
	Frame main=new Frame("ImageReader");
	TextField tf=new TextField();
	Button brobt=new Button("Browse");
	Button show=new Button("Show");
	Button cancel=new Button("Cancel");
	FileDialog brodg=new FileDialog(main,"Pick a Picture",FileDialog.LOAD);
	PopupMenu rightmenu=new PopupMenu();
	MenuItem menuitem=new MenuItem("TESTING");
	String targetfile="",targetfilename="";
	myfilter filefilter=new myfilter();
	BufferedImage img,temp,temp2;
	Showfr ab;
	RGBarray array;
	int mywidth=0,myheight=0,userwidth=0,userheight=0,Rmax=0,Gmax=0,Bmax=0;
	//========cancel button setting=========
	void cancel()
	{
		cancel.setBounds(150, 100, 100, 30);
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);
	}
	//========"show" button setting=========
	void showpic()
	{
		show.setBounds(25, 100, 100, 30);
		show.setActionCommand("show");
		show.addActionListener(this);
	}
	//========textfield setting=========
	void textfield()
	{
		tf.setEditable(false);
		tf.setFont(new Font(Font.DIALOG,Font.PLAIN,18));
		tf.setBounds(25, 50, 390, 30);
	}
	//========browse button setting=========
	void browsebutton()
	{
		brobt.addActionListener(this);
		brobt.setActionCommand("browsedialog");
		brobt.setBounds(420,50,70,30);
	}
	//========browse dialog setting=========
	void browsedialog()
	{
		filefilter.cre8filter(".jpg",".png",".bmp",".jpeg");
		brodg.setDirectory("C:\\");
		brodg.setFile("*.jpg;*.png;*.bmp;*.jpeg");
		brodg.setFilenameFilter(filefilter);
		brodg.setVisible(true);
		targetfile=brodg.getDirectory();
		targetfilename=brodg.getFile();
		if(targetfilename==null)
		{
			tf.setText("NO Select File");
		}
		else
		{
			tf.setText(targetfile+targetfilename);
			try
			{
				//========image reading=========
				img =ImageIO.read(new File(targetfile+targetfilename));
				array=new RGBarray(img);
				ab=new Showfr();
			}
			catch(IOException e)
			{
				tf.setText("Image Loading Fail");
			}
		}
	}
	//=====constructor======
	reader()
	{
		textfield();
		browsebutton();
		showpic();
		cancel();

		main.setBounds(400,200,500,150);
		main.setTitle("ImageReader");
		main.setVisible(true);
		main.addWindowListener(new closeX());
		main.setLayout(null);
		main.setResizable(false);
		main.add(tf);
		main.add(brobt);
		main.add(show);
		main.add(cancel);
	}
	//========button action=================
	public void actionPerformed(ActionEvent e)
	{
		String temp=e.getActionCommand();
		if(temp=="show")
		{
			ab.showfr(img);
		}
		else if(temp=="cancel")
		{
			main.dispose();
		}
		else if(temp=="browsedialog")
		{
			browsedialog();
		}
	}
	//==========main=========
	public static void main(String[] arg)
	{
		reader ab=new reader();
	}
	class RGBAnalysis extends Panel
	{
		private static final long serialVersionUID = 11234L;
		String xmax;
		char mycolor;
		int[] toptrix={91,111,101};
		int[] toptriy={45,45,35};
		int[] bottrix={880,880,890};
		int[] bottriy={391,411,401};
		RGBAnalysis(int maxnumber,char whatcolor)
		{
			xmax=String.valueOf(maxnumber);
			mycolor=whatcolor;
			repaint();
		}
		public void paint(Graphics g)
		{
			g.setColor(new Color(255,255,255));
			g.fillRect(0,0,900,500);
			g.setColor(new Color(0,0,0));
			g.drawString(xmax, 40, 55);
			g.drawString(String.valueOf(Integer.parseInt(xmax)/2),40,178 );
			g.drawString("0", 60, 400);
			g.drawString("128", 384, 420);
			g.drawString("255", 850, 420);
			g.fillPolygon(toptrix,toptriy,3);
			g.fillPolygon(bottrix,bottriy,3);
			g.drawLine(100,400,100,45);
			g.drawLine(100,400,880,400);
			rectpaint(g);
		}
		public void rectpaint(Graphics g)
		{
			int xposition=100;
			int yposition=0;
			float parameter=(float)350/(Integer.parseInt(xmax));
			switch(mycolor)
			{
				case 'R':
					for(int i=0;i<256;i++)
					{
						yposition=(int)(Math.ceil((((float)(array.Rarray[i]))*parameter)));
						g.setColor(new Color(255,0,0));
						g.fillRect(xposition, 400-yposition, 3, yposition);
						g.setColor(new Color(0,0,0));
						g.drawRect(xposition, 400-yposition, 3, yposition);
						xposition+=3;;
					}
					break;
				case 'G':
					for(int i=0;i<256;i++)
					{
						yposition=(int)(Math.ceil((((float)(array.Garray[i]))*parameter)));
						g.setColor(new Color(0,255,0));
						g.fillRect(xposition, 400-yposition, 3, yposition);
						g.setColor(new Color(0,0,0));
						g.drawRect(xposition, 400-yposition, 3, yposition);
						xposition+=3;;
					}
					break;
				case 'B':
					for(int i=0;i<256;i++)
					{
						yposition=(int)(Math.ceil((((float)(array.Barray[i]))*parameter)));
						g.setColor(new Color(0,0,255));
						g.fillRect(xposition, 400-yposition, 3, yposition);
						g.setColor(new Color(0,0,0));
						g.drawRect(xposition, 400-yposition, 3, yposition);
						xposition+=3;;
					}
					break;
			}
			
		}
		public void update(Graphics g)
		{
			paint(g);
		}
	}
	class RGBarray
	{
		int[] colorarray,colorarraybackup;
		int[] Rarray=new int[256],Garray=new int[256],Barray=new int[256];
		RGBarray(BufferedImage img)
		{
			colorarray=img.getRGB(0,0,img.getWidth(null),img.getHeight(null),null,0,img.getWidth(null));
			colorarraybackup=img.getRGB(0,0,img.getWidth(null),img.getHeight(null),null,0,img.getWidth(null));
			analysis();
		}
		public void analysis()
		{
			Color temp;
			for(int i=0;i<256;i++)
			{
				Rarray[i]=0;
				Garray[i]=0;
				Barray[i]=0;
			}
			for(int i=0;i<colorarray.length;i++)
			{
				temp=new Color(colorarray[i]);
				Rarray[temp.getRed()]++;
				Garray[temp.getGreen()]++;
				Barray[temp.getBlue()]++;
			}
			Rmax=Rarray[0];
			Gmax=Garray[0];
			Bmax=Barray[0];
			for(int i=0;i<256;i++)
			{
				if(Rmax<Rarray[i])
				{
					Rmax=Rarray[i];
				}
				if(Gmax<Garray[i])
				{
					Gmax=Garray[i];
				}
				if(Bmax<Barray[i])
				{
					Bmax=Barray[i];
				}
			}
		}
		public void convert(int x)
		{
			int[][] temparr=new int[myheight][mywidth];
			int[] central={32,96,160,224};
			int j=colorarray.length-1;
			int oldmyheight=myheight,oldmywidth=mywidth,olduserheight=userheight,olduserwidth=userwidth;
			int tempint=0;
			int arrtemp=0;
			switch(x)
			{
				case -90:
					for(int i=0;i<myheight;i++)
					{
						for(int z=0;z<mywidth;z++)
						{
							temparr[i][z]=colorarray[tempint];
							tempint++;
						}
					}
					tempint=0;
					for(int i=mywidth-1;i>=0;i--)
					{
						for(int z=0;z<myheight;z++)
						{
							colorarray[tempint]=temparr[z][i];
							tempint++;
						}
					}
					myheight=oldmywidth;
					mywidth=oldmyheight;
					userheight=olduserwidth;
					userwidth=olduserheight;
					break;
				case 20:
					for(int i=0;i<colorarray.length-1;i++)
					{
						Color crtemp=new Color(colorarray[i]);
						Color crtemp2=new Color(255-crtemp.getRed(),255-crtemp.getGreen(),255-crtemp.getBlue());
						colorarray[i]=crtemp2.getRGB();
					}
					break;
				case 50:
					for(int i=0;i<colorarray.length-1;i++)
					{
						Color crtemp=new Color(colorarray[i]);
						int[] targetrgb=new int[3];
						int redtemp=crtemp.getRed(),greentemp=crtemp.getGreen(),bluetemp=crtemp.getBlue();
						targetrgb[0]=(int)(redtemp/64);
						targetrgb[1]=(int)(greentemp/64);
						targetrgb[2]=(int)(bluetemp/64);
						Color finalcr=new Color(central[targetrgb[0]],central[targetrgb[1]],central[targetrgb[2]]);
						colorarray[i]=finalcr.getRGB();
					}
					break;
				case 70:
					for(int i=0;i<colorarray.length;i++)
					{
						colorarray[i]=colorarraybackup[i];
					}
					mywidth=img.getWidth();
					myheight=img.getHeight();
					break;
				case 90:
					for(int i=0;i<myheight;i++)
					{
						for(int z=0;z<mywidth;z++)
						{
							temparr[i][z]=colorarray[tempint];
							tempint++;
						}
					}
					tempint=0;
					for(int i=0;i<mywidth;i++)
					{
						for(int z=myheight-1;z>=0;z--)
						{
							colorarray[tempint]=temparr[z][i];
							tempint++;
						}
					}
					myheight=oldmywidth;
					mywidth=oldmyheight;
					userheight=olduserwidth;
					userwidth=olduserheight;
					break;
				case 180:
					for(int i=0;i<colorarray.length-1;i++)
					{
						if(i>j)
						{
							break;
						}
						arrtemp=colorarray[i];
						colorarray[i]=colorarray[j];
						colorarray[j]=arrtemp;
						j--;
					}
					break;
				case 360:
					for(int i=0;i<myheight;i++)
					{
						for(int z=0;z<mywidth;z++)
						{
							temparr[i][z]=colorarray[tempint];
							tempint++;
						}
					}
					tempint=0;
					for(int i=0;i<myheight;i++)
					{
						for(int z=mywidth-1;z>=0;z--)
						{
							colorarray[tempint]=temparr[i][z];
							tempint++;
						}
					}
					break;
			}
		}
	}
	//=====Class for showing pic UI=====
	class Showfr extends Frame implements ActionListener
	{
		 private static final long serialVersionUID = 12587L;
		Button showit=new Button("Show");
		Choice showwhat=new Choice();
		Frame analysisframe=new Frame("RGB Analysis");
		RGBAnalysis R,G,B;
		Panel botp,topp;
		Font ft=new Font(null,Font.BOLD,20);
		CardLayout cardl=new CardLayout();
		void menuitil()
		{
			menuitem=new MenuItem("90¢X Rotate");
			menuitem.setActionCommand("90");
			menuitem.addActionListener(this);
			rightmenu.add(menuitem);
			menuitem=new MenuItem("-90¢X Rotate");
			menuitem.setActionCommand("-90");
			menuitem.addActionListener(this);
			rightmenu.add(menuitem);
			menuitem=new MenuItem("Horizontal Rotate");
			menuitem.setActionCommand("360");
			menuitem.addActionListener(this);
			rightmenu.add(menuitem);
			menuitem=new MenuItem("Vertical Rotate");
			menuitem.setActionCommand("180");
			menuitem.addActionListener(this);
			rightmenu.add(menuitem);
			menuitem=new MenuItem("Color Reduction");
			menuitem.setActionCommand("50");
			menuitem.addActionListener(this);
			rightmenu.add(menuitem);
			menuitem=new MenuItem("Picture Recover");
			menuitem.setActionCommand("70");
			menuitem.addActionListener(this);
			rightmenu.add(menuitem);
			menuitem=new MenuItem("Color Reverse");
			menuitem.setActionCommand("20");
			menuitem.addActionListener(this);
			rightmenu.add(menuitem);
			menuitem=new MenuItem("Show Analysis");
			menuitem.setActionCommand("Analy");
			menuitem.addActionListener(this);
			rightmenu.add(menuitem);
		}
		void analyframe()
		{
			analysisframe.setBounds(this.getX(),this.getY(),900,500);
			analysisframe.addWindowListener(new forshow(){public void windowClosing(WindowEvent e){analysisframe.dispose();}});
			analysisframe.setResizable(false);
			showit.setFont(new Font(null,Font.BOLD,14));
			showit.addActionListener(this);
			showit.setActionCommand("showit");
			topp=new Panel();
			botp=new Panel();
			R=new RGBAnalysis(Rmax,'R');
			G=new RGBAnalysis(Gmax,'G');
			B=new RGBAnalysis(Bmax,'B');
			topp.setLayout(cardl);
			botp.setLayout(new FlowLayout());
			topp.add(R,"Red");
			topp.add(G,"Green");
			topp.add(B,"Blue");
			botp.add(showwhat);
			botp.add(showit);
			showwhat.add("Red");
			showwhat.add("Green");
			showwhat.add("Blue");
			analysisframe.add(topp,BorderLayout.CENTER);
			analysisframe.add(botp,BorderLayout.SOUTH);
		}
		Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
		Showfr()
		{
			rightmenu=new PopupMenu();
			menuitil();
			this.addWindowListener(new forshow());
			this.setBounds(50,50,500,500);
			this.setLayout(null);
			this.setMinimumSize(new Dimension(0,155));
			this.addComponentListener(new cpl());
			this.addMouseListener(new mousetrack());
			this.add(rightmenu);
			analyframe();
		}
		void showfr(BufferedImage img)
		{
			temp=new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB);
			temp2=new BufferedImage(img.getHeight(null),img.getWidth(null),BufferedImage.TYPE_INT_RGB);
			mywidth=img.getWidth(null);
			myheight=img.getHeight(null);
			try
			{
				temp.setRGB(0,0,mywidth,myheight,array.colorarray,0,mywidth);
			}
			catch(ArrayIndexOutOfBoundsException e)
			{}
			if(img.getWidth(null)>screensize.getWidth()-100)
			{
				if(img.getHeight(null)>screensize.getHeight()-100)
				{
					this.setSize((int)(img.getWidth()*0.6),(int)(img.getHeight(null)*0.6));
				}
				else
				{
					this.setSize((int)(screensize.getWidth()),(int)(img.getHeight(null)-img.getWidth(null)-screensize.getWidth())+25);
				}
			}
			else if(img.getHeight(null)>screensize.getHeight()-100)
			{
				this.setSize(img.getWidth()-(int)(img.getHeight()-screensize.getHeight()),(int)(screensize.getHeight())+25);
			}
			else
			{
				this.setSize(img.getWidth(null),img.getHeight(null)+25);
			}
			repaint();
			this.setVisible(true);
			userwidth=img.getWidth();
			userheight=img.getHeight();
		}
		void renew()
		{
			array.analysis();
			R.xmax=String.valueOf(Rmax);
			G.xmax=String.valueOf(Gmax);
			B.xmax=String.valueOf(Bmax);
			R.repaint();
			G.repaint();
			B.repaint();
		}
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				int acint=Integer.parseInt(e.getActionCommand());
				array.convert(acint);
				try
				{
					if(temp.getWidth()==mywidth)
					{
						temp.setRGB(0,0,mywidth,myheight,array.colorarray,0,mywidth);
					}
					else
					{
						temp2.setRGB(0,0,mywidth,myheight,array.colorarray,0,mywidth);
					}
					this.setSize(userwidth,userheight);
					renew();
				}
				catch(ArrayIndexOutOfBoundsException z)
				{}
				repaint();
			}
			catch(NumberFormatException b)
			{
				if(e.getActionCommand()=="Analy")
				{
					renew();
					analysisframe.setVisible(true);
				}
				else
				{
					cardl.show(topp, showwhat.getSelectedItem());
				}
			}
		}
		public void paint(Graphics g)
		{
			userwidth=super.getWidth();
			userheight=super.getHeight();
			if(temp.getHeight()==myheight)
			{
				g.drawImage(temp,0,25,userwidth,userheight-25,this);
			}
			else
			{
				g.drawImage(temp2,0,25,userwidth,userheight-25,this);
			}
		}
		public void update(Graphics g)
		{
			paint(g);
		}
		class forshow extends WindowAdapter
		{
			public void windowClosing(WindowEvent e)
			{
				analysisframe.dispose();
				dispose();
			}
		}
		class cpl extends ComponentAdapter
		{
			public void componentResized(ComponentEvent e)
			{
				repaint();
			}
		}
		class mousetrack extends MouseAdapter
		{
			public void mousePressed(MouseEvent e)
			{
				if(e.isPopupTrigger())
				{
					rightmenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
			public void mouseReleased(MouseEvent e)
			{
				if(e.isPopupTrigger())
				{
					rightmenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}
	}
	//=====for window closing=========
	class closeX extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			main.dispose();
		}
	}
	//=====For OS X File Filter=========
	class myfilter implements FilenameFilter
	{
		String[] filters;
		public void cre8filter(String...filters)
		{
			this.filters=filters;
		}
		public boolean accept(File dir,String name)
		{
			for(String filter:filters)
			{
				if(name.endsWith(filter))
				{
					return true;
				}
			}
			return false;
		}
	}
}
