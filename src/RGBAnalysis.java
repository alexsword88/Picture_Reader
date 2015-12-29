import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;

class RGBAnalysis extends Panel
	{
		private static final long serialVersionUID = 11234L;
		RGBarray array;
		String xmax;
		char mycolor;
		int[] toptrix={91,111,101};
		int[] toptriy={45,45,35};
		int[] bottrix={880,880,890};
		int[] bottriy={391,411,401};
		RGBAnalysis(int maxnumber,char whatcolor,RGBarray temp)
		{
			array=temp;
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