import java.awt.Color;
import java.util.*;
public class Codebook
{
	Color[][] codebook=new Color[256][4];
	int[][] temp4depart;
	int[] indexarray,diffarray,picarray;
	boolean flag4changed=false;
	RGBarray rgbarray;
	int mywidth,myheight;
	Codebook(RGBarray temp)
	{
		rgbarray=temp;
		mywidth=rgbarray.mywidth;
		myheight=rgbarray.myheight;
		if(mywidth%2!=0)
		{
			mywidth-=(mywidth%2);
			if(myheight%2!=0)
			{
				myheight+=(myheight%2);
			}
		}
		else
		{
			if(myheight%2!=0)
			{
				myheight+=(myheight%2);
			}
		}
		indexarray=new int[(mywidth/2)*(myheight/2)];
		diffarray=new int[(mywidth/2)*(myheight/2)];
		randomcode();
		compress(rgbarray.colorarray);
	}
	void randomcode()
	{
		for(int i=0;i<256;i++)
		{
			for(int j=0;j<4;j++)
			{
				codebook[i][j]=new Color(randomnum(),randomnum(),randomnum());
			}
		}
	}
	void compress(int[] picarraytemp)
	{
		picarray=picarraytemp;
		depart(picarray,picarray.length);
		do
		{
			flag4changed=false;
			assign();
			update();
		}while(flag4changed);
		System.out.println("Fin");
	}
	void assign()
	{
		int arrayx=0,arrayy=0,diff=0,index=0;
		for(int j=0;j<indexarray.length;j++)
		{
			diff=Math.abs(total(arrayx,arrayy,0)-total(codebook[0][0],codebook[0][1],codebook[0][2],codebook[0][3],0))
					+Math.abs(total(arrayx,arrayy,1)-total(codebook[0][0],codebook[0][1],codebook[0][2],codebook[0][3],1))
					+Math.abs(total(arrayx,arrayy,2)-total(codebook[0][0],codebook[0][1],codebook[0][2],codebook[0][3],2));
			for(int i=0;i<256;i++)
			{
				int tempdiff=Math.abs(total(arrayx,arrayy,0)-total(codebook[i][0],codebook[i][1],codebook[i][2],codebook[i][3],0))
						+Math.abs(total(arrayx,arrayy,1)-total(codebook[i][0],codebook[i][1],codebook[i][2],codebook[i][3],1))
						+Math.abs(total(arrayx,arrayy,2)-total(codebook[i][0],codebook[i][1],codebook[i][2],codebook[i][3],2));
				if(tempdiff<diff)
				{
					diff=tempdiff;
					index=i;
				}
			}
			indexarray[j]=index;
			diffarray[j]=diff;
			if(arrayx+2>=mywidth)
			{
				arrayx=0;
				arrayy+=2;
			}
			else
			{
				arrayx+=2;
			}
		}
	}
	void update()
	{
		List<Integer> updatereq=new ArrayList<Integer>();
		int[][] renewcodeword={{0,0,0},{0,0,0},{0,0,0},{0,0,0}};
		int count=0;
		boolean flag4same=false;
		int tempindex=0;
		for(int i:diffarray)
		{
			if(i>1000)
			{
				flag4changed=true;
				for(int j:updatereq)
				{
					System.out.println(j);
					System.out.println(indexarray[tempindex]);
					if(j==indexarray[tempindex])
					{
						flag4same=true;
					}
				}
				if(!flag4same)
				{
					updatereq.add(indexarray[tempindex]);
				}
			}
			flag4same=false;
			tempindex++;
		}
		/*int zzz=0;
		System.out.println("FIRST");
		for(Color[] i:codebook)
		{
			for(Color z:i)
			{
			System.out.print(","+z.getRed()+",");
			if(zzz>10)
			{
				System.out.println();
				zzz=0;
			}
			zzz++;
			}
		}*/
		if(!flag4same)
		{
			for(int i:updatereq)
			{
				System.out.println(i+":updatereq");
				int arrayx=0,arrayy=0;
				for(int j=0;j<indexarray.length;j++)
				{
					if(indexarray[j]==i)
					{
						renewcodeword[0][0]+=temp4depart[arrayindex(arrayx,arrayy,rgbarray.mywidth)][0];
						renewcodeword[0][1]+=temp4depart[arrayindex(arrayx,arrayy,rgbarray.mywidth)][1];
						renewcodeword[0][2]+=temp4depart[arrayindex(arrayx,arrayy,rgbarray.mywidth)][2];
						count++;
						try
						{
							renewcodeword[1][0]+=temp4depart[arrayindex(arrayx+1,arrayy,rgbarray.mywidth)][0];
							renewcodeword[1][1]+=temp4depart[arrayindex(arrayx+1,arrayy,rgbarray.mywidth)][1];
							renewcodeword[1][2]+=temp4depart[arrayindex(arrayx+1,arrayy,rgbarray.mywidth)][2];
						}
						catch(ArrayIndexOutOfBoundsException e)
						{
							renewcodeword[1][0]+=0;
							renewcodeword[1][1]+=0;
							renewcodeword[1][2]+=0;
						}
						try
						{
							renewcodeword[2][0]+=temp4depart[arrayindex(arrayx,arrayy+1,rgbarray.mywidth)][0];
							renewcodeword[2][1]+=temp4depart[arrayindex(arrayx,arrayy+1,rgbarray.mywidth)][1];
							renewcodeword[2][2]+=temp4depart[arrayindex(arrayx,arrayy+1,rgbarray.mywidth)][2];
						}
						catch(ArrayIndexOutOfBoundsException e)
						{
							renewcodeword[2][0]+=0;
							renewcodeword[2][1]+=0;
							renewcodeword[2][2]+=0;
						}
						try
						{
							renewcodeword[3][0]+=temp4depart[arrayindex(arrayx+1,arrayy+1,rgbarray.mywidth)][0];
							renewcodeword[3][1]+=temp4depart[arrayindex(arrayx+1,arrayy+1,rgbarray.mywidth)][1];
							renewcodeword[3][2]+=temp4depart[arrayindex(arrayx+1,arrayy+1,rgbarray.mywidth)][2];
						}
						catch(ArrayIndexOutOfBoundsException e)
						{
							renewcodeword[3][0]+=0;
							renewcodeword[3][1]+=0;
							renewcodeword[3][2]+=0;
						}
					}
					if(arrayx+2>=mywidth)
					{
						arrayx=0;
						arrayy+=2;
					}
					else
					{
						arrayx+=2;
					}
				}
				codebook[i][0]=new Color((int)(renewcodeword[0][0]/count),(int)(renewcodeword[0][1]/count),(int)(renewcodeword[0][2]/count));
				codebook[i][1]=new Color((int)(renewcodeword[1][0]/count),(int)(renewcodeword[1][1]/count),(int)(renewcodeword[1][2]/count));
				codebook[i][2]=new Color((int)(renewcodeword[2][0]/count),(int)(renewcodeword[2][1]/count),(int)(renewcodeword[2][2]/count));
				codebook[i][3]=new Color((int)(renewcodeword[3][0]/count),(int)(renewcodeword[3][1]/count),(int)(renewcodeword[3][2]/count));
			}
		}
		System.out.println("update part2 ok");
	}
	void depart(int[] array,int length)
	{
		temp4depart=new int[length][3];
		int index=0;
		for(int i:array)
		{
			temp4depart[index][0]=new Color(i).getRed();
			temp4depart[index][1]=new Color(i).getGreen();
			temp4depart[index][2]=new Color(i).getBlue();
		}
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
			temp3=temp4depart[arrayindex(x+1,y+1,rgbarray.mywidth)][color];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			temp=0;
			temp3=0;
		}
		try
		{
			temp2=temp4depart[arrayindex(x,y+1,rgbarray.mywidth)][color];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			temp2=0;
		}
		return total(picarray[arrayindex(x,y,rgbarray.mywidth)],temp,temp2,temp3);
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
