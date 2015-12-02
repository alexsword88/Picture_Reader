import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class Showfr extends Frame implements ActionListener
	{
		private static final long serialVersionUID = 12587L;
		RGBarray rgbarray;
		Button showit=new Button("Show");
		Choice showwhat=new Choice();
		Frame analysisframe=new Frame("RGB Analysis");
		RGBAnalysis R,G,B;
		Panel botp,topp;
		PopupMenu rightmenu=new PopupMenu();
		MenuItem menuitem=new MenuItem("TESTING");
		Font ft=new Font(null,Font.BOLD,20);
		BufferedImage temp,temp2;
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
			R=new RGBAnalysis(rgbarray.Rmax,'R',rgbarray);
			G=new RGBAnalysis(rgbarray.Gmax,'G',rgbarray);
			B=new RGBAnalysis(rgbarray.Bmax,'B',rgbarray);
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
		Showfr(RGBarray temp)
		{
			rgbarray=temp;
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
			rgbarray.renewwh(img.getWidth(null),img.getHeight(null));
			try
			{
				temp.setRGB(0,0,rgbarray.mywidth,rgbarray.myheight,rgbarray.colorarray,0,rgbarray.mywidth);
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
			rgbarray.renewwh(img.getWidth(null),img.getHeight(null));
		}
		void renew()
		{
			rgbarray.analysis();
			R.xmax=String.valueOf(rgbarray.Rmax);
			G.xmax=String.valueOf(rgbarray.Gmax);
			B.xmax=String.valueOf(rgbarray.Bmax);
			R.repaint();
			G.repaint();
			B.repaint();
		}
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				int acint=Integer.parseInt(e.getActionCommand());
				rgbarray.convert(acint);
				try
				{
					if(temp.getWidth()==rgbarray.mywidth)
					{
						temp.setRGB(0,0,rgbarray.mywidth,rgbarray.myheight,rgbarray.colorarray,0,rgbarray.mywidth);
					}
					else
					{
						temp2.setRGB(0,0,rgbarray.mywidth,rgbarray.myheight,rgbarray.colorarray,0,rgbarray.mywidth);
					}
					this.setSize(rgbarray.userwidth,rgbarray.userheight);
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
			rgbarray.userwidth=super.getWidth();
			rgbarray.userheight=super.getHeight();
			if(temp.getHeight()==rgbarray.myheight)
			{
				g.drawImage(temp,0,25,rgbarray.userwidth,rgbarray.userheight-25,this);
			}
			else
			{
				g.drawImage(temp2,0,25,rgbarray.userwidth,rgbarray.userheight-25,this);
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