import java.awt.*;
import java.awt.event.*;
import javax.swing.JLabel;

public class dctshower extends Frame implements ActionListener,ItemListener
{
	Panel pl1=new Panel(),pl2=new Panel(),pl3=new Panel(),mainpanel=new Panel();
	Panel[] allpanel={pl1,pl2,pl3};;
	Boolean[] panelseted={false,false,false};
	Button left=new Button("<"),right=new Button(">");
	Choice showwhat=new Choice();
	JLabel[][] cardlabel;
	CardLayout card=new CardLayout();
	double[][] dctarray;
	int dctwidth,dctheight,index4card=0,maxindex=0,nowpanelindex=0;
	Processbar processbar;
	dctshower(double[][] temp,int tempwidth,int tempheight)
	{
		dctwidth=tempwidth;
		dctheight=tempheight;
		dctarray=temp;
		buttonsetting();
		this.setBounds(200,200,700,520);
		this.setLayout(null);
		this.setResizable(true);
		this.add(left);
		this.add(right);
		showwhat.add("Y");
		showwhat.add("Cb");
		showwhat.add("Cr");
		showwhat.setBounds(280,480,100,100);
		showwhat.addItemListener(this);
		this.addWindowListener(new closeX());
		mainpanel.setBounds(53,30,600,450);
		mainpanel.setLayout(card);
		pl1.setLayout(card);
		pl2.setLayout(card);
		pl3.setLayout(card);
		mainpanel.add(pl1,"Y");
		mainpanel.add(pl2,"Cb");
		mainpanel.add(pl3,"Cr");
		cardlabel=new JLabel[dctarray.length][3];
		this.add(mainpanel);
		this.add(showwhat);
		dctset(0);
		this.setVisible(true);
	}
	void buttonsetting()
	{
		left.setBounds(0,10,50,510);
		left.setActionCommand("left");
		left.addActionListener(this);
		right.setBounds(648,10,50,510);
		right.setActionCommand("right");
		right.addActionListener(this);
		if(index4card==0)
		{
			left.setEnabled(false);
		}
	}
	void dctset(int color)
	{
		processbar=new Processbar(100,100);
		processbar.open();
		processbar.totaltimes(dctheight/8*dctwidth/8);
		maxindex=0;
		for(int y=0,labelindex=0;y<dctheight;y+=8)
		{
			for(int x=0;x<dctwidth;x+=8,labelindex++)
			{
				processbar.increase();
				cardlabel[labelindex][color]=new JLabel(turnstring(x,y,color));
				allpanel[color].add(cardlabel[labelindex][color],maxindex);
				maxindex++;
				System.out.println("TEST:"+x+":"+y+":"+color);
			}
		}
		processbar.done();
		processbar.dispose();
		if(maxindex==1)
		{
			right.setEnabled(false);
		}
		panelseted[color]=true;
	}
	String turnstring(int x,int y,int color)
	{
		String target="<html><table border=1 style="+'"'+"font-size:22px;width:450px"+'"'+">";
		for(int i=0;i<8;i++)
		{
			target+="<tr>";
			for(int j=0;j<8;j++)
			{
				target+="<td>";
				target+=Math.round((float)(dctarray[arrayindex(x+j,y+i,dctwidth)][color]));
				target+="</td>";
			}
			target+="</tr>";
		}
		target+="</table></html>";
		return target;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand()=="right")
		{
			card.next(allpanel[nowpanelindex]);
			index4card++;
			if(index4card!=0)
			{
				left.setEnabled(true);
			}
			else if(index4card==maxindex-1)
			{
				right.setEnabled(false);
			}
		}
		else
		{
			card.previous(allpanel[nowpanelindex]);
			index4card--;
			if(index4card==0)
			{
				left.setEnabled(false);
			}
			else if(index4card<maxindex-1)
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
		if(!panelseted[nowpanelindex])
		{
			dctset(nowpanelindex);
		}
		card.show(allpanel[nowpanelindex],String.valueOf(index4card));
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
}
