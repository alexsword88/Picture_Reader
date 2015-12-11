import java.awt.Color;
import java.util.*;
public class Codebook
{
	int[][] temp4depart;
	double[][] YCbCrarray,dctarray,idctarray;
	int[] indexarray,diffarray,picarray;
	boolean flag4changed=false;
	RGBarray rgbarray;
	int mywidth,myheight;
	Codebook(RGBarray temp)
	{
		rgbarray=temp;
		mywidth=rgbarray.mywidth;
		myheight=rgbarray.myheight;
		if(mywidth%8!=0)
		{
			mywidth+=(8-(mywidth%8));
			if(myheight%8!=0)
			{
				myheight+=(8-(myheight%8));
			}
		}
		else
		{
			if(myheight%8!=0)
			{
				myheight+=(8-(myheight%8));
			}
		}
		indexarray=new int[(mywidth/8)*(myheight/8)];
		diffarray=new int[(mywidth/8)*(myheight/8)];
		compress(rgbarray.colorarray);
	}
	void compress(int[] picarraytemp)
	{
		picarray=picarraytemp;
		System.out.println(mywidth*myheight);
		System.out.println(mywidth+","+myheight);
		rgbtoyuv(picarray,picarray.length);
		/*do
		{
			flag4changed=false;
			assign();
			update();
		}while(flag4changed);*/
		int count=0;
		fullDCT(YCbCrarray,0);
		count=0;
		/*System.out.print("[");
		for(int i=0;i<YCbCrarray.length;i++)
		{
			System.out.print(YCbCrarray[i][0]+",");
				if(count%8==0)
				{
					System.out.println();
				}
				count++;
		}
		System.out.print("\b ");
		System.out.print("]");*/
		fullIDCT(dctarray,0);
		yuvtorgb(idctarray);
		
		System.out.println("Fin");
	}
	void assign()
	{
		
	}
	void update()
	{
		
	}
	void rgbtoyuv(int[] array,int length)
	{
		temp4depart=new int[length][3];
		YCbCrarray=new double[mywidth*myheight][3];
		dctarray=new double[mywidth*myheight][3];
		idctarray=new double[mywidth*myheight][3];
		int index=0;
		for(int i:array)
		{
			temp4depart[index][0]=new Color(i).getRed();
			temp4depart[index][1]=new Color(i).getGreen();
			temp4depart[index][2]=new Color(i).getBlue();
			index++;
		}
		index=0;
		for(int i=0;i<mywidth*myheight;i++)
		{
			if(((index%rgbarray.mywidth)!=(rgbarray.mywidth-1))&&(index<rgbarray.mywidth))
			{
				YCbCrarray[i][0]=0.299*temp4depart[index][0]+0.587*temp4depart[index][1]+0.114*temp4depart[index][2];
				YCbCrarray[i][1]=0.564*(temp4depart[index][2]-YCbCrarray[i][0]);
				YCbCrarray[i][2]=0.713*(temp4depart[index][0]-YCbCrarray[i][0]);
				index++;
			}
			else
			{
				YCbCrarray[i][0]=0;
				YCbCrarray[i][1]=0;
				YCbCrarray[i][2]=0;
				if((i%mywidth)==(mywidth-1))
				{
					index++;
				}
			}
		}
	}
	void yuvtorgb(double[][] array)
	{
		int index=0;
		for(int i=0;i<rgbarray.myheight;i++)
		{
			for(int j=0;j<rgbarray.mywidth;j++)
			{
				rgbarray.colorarray[index]=new Color(
									Math.round((float)(array[arrayindex(j,i,mywidth)][0]+1.402*array[arrayindex(j,i,mywidth)][2])),
									Math.round((float)(array[arrayindex(j,i,mywidth)][0]-0.344*array[arrayindex(j,i,mywidth)][1]-0.714*array[arrayindex(j,i,mywidth)][2])),
									Math.round((float)(array[arrayindex(j,i,mywidth)][0]+1.772*array[arrayindex(j,i,mywidth)][1]))).getRGB();
				index++;
			}
		}
	}
	void fullDCT(double[][] temparray,int color)
	{
		for(int y=0;y<myheight;y+=8)
		{
			for(int x=0;x<mywidth;x+=8)
			{
				DCT(temparray,x,y,color);
			}
		}
	}
	void fullIDCT(double[][] temparray,int color)
	{
		for(int y=0;y<myheight;y+=8)
		{
			for(int x=0;x<mywidth;x+=8)
			{
				IDCT(temparray,x,y,color);
			}
		}
	}
	void DCT(double[][] temparray,int x,int y,int color)
	{
		double[][] temp88=new double[8][8];
		double temp=0,temp2=0;
		int xx=0,yy=0,count=0;
		System.out.println("ZZZ:"+x+","+y);
		for(int j=0;j<8;j++)
		{
			for(int i=0;i<8;i++)
			{
				temp88[i][j]=temparray[arrayindex(x,y,mywidth)][color];
				x++;
			}
			y++;
			x-=8;
		}
		y-=8;
		do
		{
			temp=1;
			temp2=0;
			if(xx==0)
			{
				temp=(Math.sqrt(2)/2);
				if(yy==0)
				{
					temp*=(Math.sqrt(2)/2);
				}
				temp/=4;
			}
			else
			{
				if(yy==0)
				{
					temp*=(Math.sqrt(2)/2);
				}
				temp/=4;
			}
			for(int j=0;j<8;j++)
			{
				for(int i=0;i<8;i++)
				{
					temp2+=Math.cos(((2*j+1)*yy*Math.PI)/16)*Math.cos(((2*i+1)*xx*Math.PI)/16)*temp88[j][i];
				}
			}
			temp*=temp2;
			dctarray[arrayindex(x,y,mywidth)][color]=temp;
			x++;
			xx++;
			count++;
			if(count%8==0)
			{
				x-=8;
				xx-=8;
				yy++;
				y++;
			}
		}while(count!=64);
	}
	void IDCT(double[][] temparray,int x,int y,int color)
	{
		double[][] temp88=new double[8][8];
		double temp=0,temp2=0;
		int xx=0,yy=0,count=0;
		for(int j=0;j<8;j++)
		{
			for(int i=0;i<8;i++)
			{
				temp88[i][j]=temparray[arrayindex(x,y,mywidth)][color];
				x++;
			}
			y++;
			x-=8;
		}
		y-=8;
		do
		{
			temp2=0;
			for(int j=0;j<8;j++)
			{
				for(int i=0;i<8;i++)
				{
					temp=1;
					if(i==0)
					{
						temp=(Math.sqrt(2)/2);
						if(j==0)
						{
							temp*=(Math.sqrt(2)/2);
						}
						temp/=4;
					}
					else
					{
						if(j==0)
						{
							temp*=(Math.sqrt(2)/2);
						}
						temp/=4;
					}
					temp2+=temp*Math.cos(((2*yy+1)*j*Math.PI)/16)*Math.cos(((2*xx+1)*i*Math.PI)/16)*temp88[j][i];
				}
			}
			idctarray[arrayindex(x,y,mywidth)][color]=temp2;
			x++;
			xx++;
			count++;
			if(count%8==0)
			{
				x-=8;
				xx-=8;
				yy++;
				y++;
			}
		}while(count!=64);
	}
	int arrayindex(int x,int y,int width)
	{
		return y*width+x;
	}
	int total(int x,int y,int color)
	{
		int temp=0,temp2=0,temp3=0;
		try
		{
			temp=temp4depart[arrayindex(x+1,y,rgbarray.mywidth)][color];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			temp=0;
		}
		try
		{
			temp2=temp4depart[arrayindex(x,y+1,rgbarray.mywidth)][color];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			temp2=0;
		}
		try
		{
			temp3=temp4depart[arrayindex(x+1,y+1,rgbarray.mywidth)][color];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			temp3=0;
		}
		return total(temp4depart[arrayindex(x,y,rgbarray.mywidth)][color],temp,temp2,temp3);
	}
	int total(Color a,Color b,Color c,Color d,int color)
	{
		int total=0;
		switch(color)
		{
			case 0:
				total=a.getRed()+b.getRed()+c.getRed()+d.getRed();
				break;
			case 1:
				total=a.getGreen()+b.getGreen()+c.getGreen()+d.getGreen();
				break;
			case 2:
				total=a.getBlue()+b.getBlue()+c.getBlue()+d.getBlue();
				break;
		}
		return total;
	}
	int total(int a,int b,int c,int d)
	{
		return (Math.abs(a)+Math.abs(b)+Math.abs(c)+Math.abs(d));
	}
	int randomnum()
	{
		return (int)(Math.random()*256);
	}
}
