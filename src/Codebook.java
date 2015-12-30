import java.awt.Color;
import java.util.*;
public class Codebook
{
	int[][] temp4depart,newpicarrayr,newpicarrayg,newpicarrayb,codebookr,codebookg,codebookb;
	double[][] YCbCrarray,dctarray,idctarray;
	int[] indexarray,picarray;
	RGBarray rgbarray;
	int mywidth,myheight,lbgwidth,lbgheight,lbgdiff;
	Processbar process;
	dctshower dctshow;
	Codebook(RGBarray temp)
	{
		rgbarray=temp;
		mywidth=rgbarray.mywidth;
		myheight=rgbarray.myheight;
		lbgwidth=rgbarray.mywidth;
		lbgheight=rgbarray.myheight;
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
		picarray=rgbarray.colorarray;
		colordepart(picarray,picarray.length);
	}
	void LBG()
	{
		process=new Processbar(rgbarray.showfr.getX(),rgbarray.showfr.getY());
		process.open();
		codebookr=new int[256][16];
		codebookg=new int[256][16];
		codebookb=new int[256][16];
		if(lbgwidth%4!=0)
		{
			lbgwidth+=(4-(lbgwidth%4));
			if(lbgheight%4!=0)
			{
				lbgheight+=(4-(lbgheight%4));
			}
		}
		else
		{
			if(myheight%4!=0)
			{
				lbgheight+=(4-(lbgheight%4));
			}
		}
		newpicarrayr=new int[lbgheight/4*lbgwidth/4][16];
		newpicarrayg=new int[lbgheight/4*lbgwidth/4][16];
		newpicarrayb=new int[lbgheight/4*lbgwidth/4][16];
		indexarray=new int[lbgheight/4*lbgwidth/4];
		lbgdiff=lbgwidth-rgbarray.mywidth;
		int[][] temparray=new int[lbgheight*lbgwidth][3];
		int index=0;
		for(int j=0;j<3;j++)
		{
			index=0;
			for(int i=0;i<lbgheight*lbgwidth;i++)
			{
				if(((i%lbgwidth)!=(lbgwidth-lbgdiff))&&((index<picarray.length-1)))
				{
					temparray[i][j]=temp4depart[index][j];
					index++;
				}
				else
				{
					for(int z=0;z<lbgdiff;z++)
					{
						try
						{
							temparray[i+z][j]=0;
						}
						catch(ArrayIndexOutOfBoundsException e)
						{}
					}
				}
			}
		}
		int x=0,y=0,xx=0,yy=0;
		for(int i=0;i<lbgheight/4*lbgwidth/4;i++)
		{
			x=0;
			y=0;
			for(int j=0;j<16;j++)
			{
				newpicarrayr[i][j]=temparray[arrayindex(xx+x,yy+y,lbgwidth)][0];
				newpicarrayg[i][j]=temparray[arrayindex(xx+x,yy+y,lbgwidth)][1];
				newpicarrayb[i][j]=temparray[arrayindex(xx+x,yy+y,lbgwidth)][2];
				if(x/3==1)
				{
					x=0;
					y++;
				}
				else
				{
					x++;
				}
			}
			if(xx+4<lbgwidth)
			{
				xx+=4;				
			}
			else
			{
				xx=0;
				yy+=4;
			}
		}
		ArrayList<Integer> randomed=new ArrayList<Integer>(0);
		for(int i=0;i<lbgheight/4;i++)
		{
			randomed.add((lbgwidth/4)+(i*lbgwidth/4-1));
		}
		for(int i=0;i<lbgwidth/4;i++)
		{
			randomed.add(arrayindex(i,lbgheight/4-1,lbgwidth/4));
		}
		for(int i=0;i<256;i++)
		{
			boolean tempflag=false;
			do
			{
				tempflag=false;
				index=randomnum(0,(lbgheight/4*lbgwidth/4)-1);
				for(int z:randomed)
				{
					if(index==z)
					{
						tempflag=true;
					}
				}
				if((randomed.size()-(lbgwidth*lbgheight-1))>=lbgwidth*lbgheight)
				{
					break;
				}
			}while(tempflag);
			for(int j=0;j<16;j++)
			{
				codebookr[i][j]=newpicarrayr[index][j];
				codebookg[i][j]=newpicarrayg[index][j];
				codebookb[i][j]=newpicarrayb[index][j];
			}
		}
		for(int i=0;i<100;i++)
		{
			assign();
			update();
			process.increase();
		}
		process.done();
		try
		{
			Thread.sleep(500);
		}
		catch(Exception e)
		{}
		show();
		process.dispose();
	}
	void jpegcompress()
	{
		rgbtoyuv(picarray,picarray.length);
		fullDCT(YCbCrarray);
		fullIDCT(dctarray);
		yuvtorgb(idctarray,picarray.length);
	}
	void showdct()
	{
		rgbtoyuv(picarray,picarray.length);
		process=new Processbar(rgbarray.showfr.getX(),rgbarray.showfr.getY());
		fullDCT(YCbCrarray);
		dctshow=new dctshower(dctarray,mywidth,myheight);
	}
	void assign()
	{
		int min=0,tempdiff=0,index=0;
		for(int i=0;i<lbgheight/4*lbgwidth/4;i++)
		{
			min=0;
			for(int j=0;j<16;j++)
			{
				min+=total(newpicarrayr[i][j]-codebookr[0][j]);
			}
			min=(int)(Math.sqrt(min));
			for(int z=0;z<256;z++)
			{
				tempdiff=0;
				for(int j=0;j<16;j++)
				{
					tempdiff+=total(newpicarrayr[i][j]-codebookr[z][j]);
				}
				tempdiff=(int)(Math.sqrt(tempdiff));
				if(tempdiff<min)
				{
					min=tempdiff;
					indexarray[index]=z;
				}
			}
			index++;
		}
	}
	void update()
	{
		for(int i:indexarray)
		{
			int[] avgr={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			int[] avgg={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			int[] avgb={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			int count=0,index=0;
			for(int j:indexarray)
			{
				if(j==i)
				{
					for(int z=0;z<16;z++)
					{
						avgr[z]+=newpicarrayr[index][z];
						avgg[z]+=newpicarrayg[index][z];
						avgb[z]+=newpicarrayb[index][z];
					}
					count++;
				}
				index++;
			}
			for(int z=0;z<16;z++)
			{
				codebookr[i][z]=avgr[z]/count;
				codebookg[i][z]=avgg[z]/count;
				codebookb[i][z]=avgb[z]/count;
			}
		}
	}
	void show()
	{
		int[][] temparray=new int[lbgwidth*lbgheight][3];
		int x=0,y=0,xx=0,yy=0,index=0,target;
		for(int i=0;i<lbgheight/4*lbgwidth/4;i++)
		{
			x=0;
			y=0;
			target=indexarray[index];
			for(int j=0;j<16;j++)
			{
				temparray[arrayindex(xx+x,yy+y,lbgwidth)][0]=codebookr[target][j];
				temparray[arrayindex(xx+x,yy+y,lbgwidth)][1]=codebookg[target][j];
				temparray[arrayindex(xx+x,yy+y,lbgwidth)][2]=codebookb[target][j];
				if(x/3==1)
				{
					x=0;
					y++;
				}
				else
				{
					x++;
				}
			}
			if(xx+4<lbgwidth)
			{
				xx+=4;				
			}
			else
			{
				xx=0;
				yy+=4;
			}
			index++;
		}
		index=0;
		for(int i=0;i<lbgheight*lbgwidth;i++)
		{
			if(((i%lbgwidth)!=(lbgwidth-lbgdiff))&&((index<picarray.length-1)))
			{
				rgbarray.colorarray[index]=new Color(temparray[i][0],temparray[i][1],temparray[i][2]).getRGB();
				index++;
			}
		}
	}
	void colordepart(int[] array,int length)
	{
		int index=0;
		temp4depart=new int[length][3];
		for(int i:array)
		{
			temp4depart[index][0]=new Color(i).getRed();
			temp4depart[index][1]=new Color(i).getGreen();
			temp4depart[index][2]=new Color(i).getBlue();
			index++;
		}
	}
	void rgbtoyuv(int[] array,int length)
	{
		YCbCrarray=new double[mywidth*myheight][3];
		dctarray=new double[mywidth*myheight][3];
		idctarray=new double[mywidth*myheight][3];
		int index=0;
		for(int i=0;i<mywidth*myheight;i++)
		{
			if(((i%rgbarray.mywidth)!=(rgbarray.mywidth-1))&&(index<length-1))
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
	void yuvtorgb(double[][] array,int length)
	{
		int index=0;
		for(int i=0;i<mywidth*myheight;i++)
		{
			if(((i%rgbarray.mywidth)!=(rgbarray.mywidth-1))&&(index<length-1))
			{
				rgbarray.colorarray[index]=new Color(
						Math.round((float)(array[i][0]+1.402*array[i][2])),
						Math.round((float)(array[i][0]-0.344*array[i][1]-0.714*array[i][2])),
						Math.round((float)(array[i][0]+1.772*array[i][1]))).getRGB();
				index++;
			}
			else
			{
				if((i%mywidth)==(mywidth-1))
				{
					index++;
				}
			}
		}
	}
	void fullDCT(double[][] temparray)
	{
		process.open();
		process.totaltimes(myheight/8*mywidth/8*3);
		for(int z=0;z<3;z++)
		{
			for(int y=0;y<myheight;y+=8)
			{
				for(int x=0;x<mywidth;x+=8)
				{
					process.increase();
					DCT(temparray,x,y,z);
				}
			}
		}
		process.done();
		process.dispose();
	}
	void fullIDCT(double[][] temparray)
	{
		for(int z=0;z<3;z++)
		{
			for(int y=0;y<myheight;y+=8)
			{
				for(int x=0;x<mywidth;x+=8)
				{
					IDCT(temparray,x,y,z);
				}
			}
		}
	}
	void DCT(double[][] temparray,int x,int y,int color)
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
	int total(int... number)
	{
		int total=0;
		for(int i:number)
		{
			total+=Math.pow(i,2);
		}
		return total;
	}
	int randomnum(int start,int end)
	{
		return start+(int)(Math.random()*(end-start+1));
	}
	int randomnum()
	{
		return (int)(Math.random()*256);
	}
}
