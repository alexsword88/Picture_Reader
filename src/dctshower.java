import java.awt.*;
import java.awt.event.*;

public class dctshower extends Frame implements ActionListener,ItemListener
{
	panel4dct pl1,pl2,pl3;
	Panel mainpanel=new Panel();
	panel4dct[] allpanel=new panel4dct[3];
	Boolean[] panelseted={false,false,false};
	Button left=new Button("<"),right=new Button(">");
	Choice showwhat=new Choice();
	CardLayout card=new CardLayout();
	double[][] dctarray;
	int dctwidth,dctheight,cardx=0,cardy=0,maxindex,nowpanelindex=0;
	Processbar processbar;
	dctshower(double[][] temp,int tempwidth,int tempheight)
	{
		dctwidth=tempwidth;
		dctheight=tempheight;
		dctarray=temp;
		maxindex=dctwidth/8*dctheight/8;
		pl1=new panel4dct(0);
		allpanel[0]=pl1;
		pl2=new panel4dct(1);
		allpanel[1]=pl2;
		pl3=new panel4dct(2);
		allpanel[2]=pl3;
		buttonsetting();
		this.setBounds(200,200,700,470);
		this.setLayout(null);
		this.setResizable(true);
		this.add(left);
		this.add(right);
		showwhat.add("Y");
		showwhat.add("Cb");
		showwhat.add("Cr");
		showwhat.setBounds(280,430,100,100);
		showwhat.addItemListener(this);
		this.addWindowListener(new closeX());
		mainpanel.setBounds(53,35,590,390);
		mainpanel.setLayout(card);
		pl1.setLayout(card);
		pl2.setLayout(card);
		pl3.setLayout(card);
		mainpanel.add(pl1,"Y");
		mainpanel.add(pl2,"Cb");
		mainpanel.add(pl3,"Cr");
		this.add(mainpanel);
		this.add(showwhat);
		this.setVisible(true);
	}
	void buttonsetting()
	{
		left.setBounds(0,10,50,460);
		left.setActionCommand("left");
		left.addActionListener(this);
		right.setBounds(648,10,50,460);
		right.setActionCommand("right");
		right.addActionListener(this);
		left.setEnabled(false);
		if(maxindex==1)
		{
			right.setEnabled(false);
		}
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand()=="right")
		{
			allpanel[nowpanelindex].right();
			if(cardx!=0&&cardy>=0)
			{
				left.setEnabled(true);
			}
			if(cardx>dctwidth&&cardy>dctheight)
			{
				right.setEnabled(false);
			}
		}
		else
		{
			allpanel[nowpanelindex].left();
			if(cardx==0&&cardy==0)
			{
				left.setEnabled(false);
			}
			if(cardx<dctwidth&&cardy<dctheight)
			{
				right.setEnabled(true);
			}
		}
	}
	public void itemStateChanged(ItemEvent e)
	{
		String temp=((Choice)e.getSource()).getSelectedItem();
		if(temp=="Y")
		{
			nowpanelindex=0;
		}
		else if(temp=="Cb")
		{
			nowpanelindex=1;
		}
		else
		{
			nowpanelindex=2;
		}
		card.show(mainpanel,temp);
	}
	int arrayindex(int x,int y,int width)
	{
		return y*width+x;
	}
	class closeX extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			dispose();
		}
	}
	class panel4dct extends Panel
	{
		int panelno,x=20,y=30;
		Font myfont=new Font("Calibri",Font.BOLD,25);
		panel4dct(int temp)
		{
			panelno=temp;
			this.setBounds(0,0,590,450);
			this.setLayout(null);
		}
		public void paint(Graphics g)
		{
			g.setColor(new Color(255,255,255));
			g.fillRect(0,0,590,450);
			g.setColor(new Color(0,0,0));
			g.setFont(myfont);
			for(int yy=0,controly=0;yy<8*50;yy+=50,controly++)
			{
				for(int xx=0,controlx=0;xx<8*70;xx+=70,controlx++)
				{
					g.drawString(String.valueOf((int)(dctarray[arrayindex(cardx+controlx,cardy+controly,dctwidth)][panelno])), x+xx, y+yy);
				}
			}
		}
		void right()
		{
			if(cardx+8<dctwidth)
			{
				cardx+=8;
			}
			else
			{
				cardx=0;
				cardy+=8;
			}
			System.out.println(cardx+","+cardy);
			repaint();
		}
		void left()
		{
			if(cardx>0)
			{
				cardx-=8;
			}
			else
			{
				cardx=dctwidth-8;
				cardy-=8;
			}
			System.out.println(cardx+","+cardy);
			repaint();
		}
	}
}
