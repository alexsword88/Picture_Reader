import java.awt.Color;
import java.awt.image.BufferedImage;

class RGBarray
	{
		int[] colorarray,colorarraybackup;
		int[] Rarray=new int[256],Garray=new int[256],Barray=new int[256];
		int mywidth=0,myheight=0,userwidth=0,userheight=0,Rmax=0,Gmax=0,Bmax=0;
		BufferedImage img;
		Codebook codebook;
		Showfr showfr;
		boolean flag4codebook=false;
		RGBarray(BufferedImage temp)
		{
			img=temp;
			colorarray=img.getRGB(0,0,img.getWidth(null),img.getHeight(null),null,0,img.getWidth(null));
			colorarraybackup=img.getRGB(0,0,img.getWidth(null),img.getHeight(null),null,0,img.getWidth(null));
			analysis();
		}
		public void renewwh(int tempwidth,int tempheight)
		{
			mywidth=tempwidth;
			myheight=tempheight;
			if(!flag4codebook)
			{
				codebook=new Codebook(this);
				flag4codebook=true;
			}
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
		public void input(Showfr temp)
		{
			showfr=temp;
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
				case 30:
					codebook.LBG();
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
