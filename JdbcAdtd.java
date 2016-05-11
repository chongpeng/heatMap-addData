	  
	#将从数据库中查到的值，采用插值法，扩展为  index*index个数据
	#关于从数据库中查询数据的过程，可以参考我的  reporsity: heatMap--heatmap.js
	  public BufferedImage ltJson3New(Subproject a,HttpServletRequest request) throws IOException{
			   JdbcAdtd db = new JdbcAdtd();
		    	int year=a.getYear(a.getDate())-1;
		    	Double range=10.0;
		    	Float centerX=a.getLongitude();
		    	Float centerY=a.getLatitude();
		    	Double minX=centerX-range*1.1f/96.0f;
		    	Double maxX=centerX+range*1.1f/96.0f;
		    	Double minY=centerY-range*1.1f/111.0f;
		    	Double maxY=centerY+range*1.1f/111.0f;
		    
			    int index=400;   //数据量  index*index    
			    Float arr[][]=new Float[index][index];
			    int   times[][]=new int[index][index];
			    Double x1=(double) 0;
			    Double y1=(double) 0;
			    Double x2=(double) 0;
			    Double y2=(double) 0;
		      	int i=0,j=0;
		      	Float defualtValue=(float) 5;   //默认值
		      	Float defualtZeroValue=(float) -1.0;  //不在十公里范围内
		      	Float defualtNeighborValue=(float)0.98; //邻居感染系数（关键因子，调节会改变整体数值得大小）
		      	Double difX=(maxX-minX)/index;
		      	Double difY=(maxY-minY)/index;
	      	for(int m=0;m<index;m++){
	      	  for(int n=0;n<index;n++){
	      			arr[m][n]=defualtValue; 
	      			times[m][n]=0;
	      		     }
	      	    }
	    	db.readIP(request);
	    	if(db.SetConnection(db.getURL(),db.getUSERNAME(),db.getPASSWORD())){
		      ResultSet rs=db.sendLightningIntensityQueryNew(year, centerX,centerY, range);
	    		try {
		            if (rs != null) {
		                while (rs.next()) {
			                	Float intensity=rs.getFloat("intensity");
			                	Float x=rs.getFloat("longitude");
			                	Float y=rs.getFloat("latitude");
			                	if(intensity<0){ 
			                		intensity=-intensity;    //雷电流强度不取负
			                		}    
			                     i = (int)Math.round(((x-minX)/difX));
			                     j = (int)Math.round(((y-minY)/difY));
		                        if(i<index&&j<index){
		                	        arr[i][j]=arr[i][j]+intensity;
		                	        times[i][j]=times[i][j]+1;
		                             }
		                        }
		                  }
	    		    } catch (SQLException e) {
		               e.printStackTrace();} 
	    		   db.close();
	    	}       	
		                //5年内的平均
		    	  for(int m=0;m<index;m++){
	        	   	for(int n=0;n<index;n++){
	        	   		if(times[m][n]!=0)
	        			   arr[m][n]=arr[m][n]/times[m][n];
	        		         }
	        	      }
		    	         //不在10公里内设为负值
		           for(int k=0;k<index;k++){
		              for(int l=0;l<index;l++){
		                if(!TestRange(centerX,centerY,minX+k*difX,minY+l*difY)){
		            	      arr[k][l]=defualtZeroValue;}
		                     }
		             } 
		  boolean flag=false;
		  int     value=0;
		  //插值算法(不按顺序插值)
		  while(!flag){         
		           //判断是否所有的值都不再是默认值
			       flag=true;
		           for(int k=0;k<index;k=k+2){
			              for(int l=0;l<index;l=l+2){    
			            	    value=0;      //默认值，非边缘
			           if((arr[k][l]==defualtValue)&&(k!=0)&&(k!=(index-1))&&(l!=0)&&(l!=(index-1))){
			            		   if((arr[k][l-1]!=defualtValue)&&(arr[k][l-1]!=defualtZeroValue))
			            		    	 value++;
			            		   if((arr[k][l+1]!=defualtValue)&&(arr[k][l+1]!=defualtZeroValue))
			            		    	 value++;
			            		   if((arr[k-1][l]!=defualtValue)&&(arr[k-1][l]!=defualtZeroValue))
			            		    	 value++;
			            		   if((arr[k+1][l]!=defualtValue)&&(arr[k+1][l]!=defualtZeroValue))
			            		    	 value++;
			            		     
			            	 switch(value){
			            		 case 0:  flag=false;  break;
			            		 case 1:  arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l]-3*defualtValue)*defualtNeighborValue; 
			            		         break;
			            		 case 2:   arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l]-2*defualtValue)/2; 
	            		                 break;
			            		 case 3:   arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l]-defualtValue)/3;
         		                         break;
			            		 case 4:   arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l])/4;
 		                                 break;
			            		    }
			            	    }  
			                 }
			              
			              for(int l=1;l<index;l=l+2){    
			            	    value=0;      //默认值，非边缘
			            	  if((arr[k][l]==defualtValue)&&(k!=0)&&(k!=(index-1))&&(l!=0)&&(l!=(index-1))){
			            		     if((arr[k][l-1]!=defualtValue)&&(arr[k][l-1]!=defualtZeroValue))
			            		    	 value++;
			            		     if((arr[k][l+1]!=defualtValue)&&(arr[k][l+1]!=defualtZeroValue))
			            		    	 value++;
			            		     if((arr[k-1][l]!=defualtValue)&&(arr[k-1][l]!=defualtZeroValue))
			            		    	 value++;
			            		     if((arr[k+1][l]!=defualtValue)&&(arr[k+1][l]!=defualtZeroValue))
			            		    	 value++;
			            		     
			            			 switch(value){
				            		 case 0:  flag=false;  break;
				            		 case 1:  arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l]-3*defualtValue)*defualtNeighborValue; 
				            		         break;
				            		 case 2:   arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l]-2*defualtValue)/2; 
		            		                 break;
				            		 case 3:   arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l]-defualtValue)/3;
	         		                         break;
				            		 case 4:   arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l])/4;
	 		                                 break;
				            		    }
			            	      }
			                  }
			              }
		           for(int k=1;k<index;k=k+2){
		        	      for(int l=0;l<index;l=l+2){    
			            	    value=0;      //默认值，非边缘
			            	  if((arr[k][l]==defualtValue)&&(k!=0)&&(k!=(index-1))&&(l!=0)&&(l!=(index-1))){
			            		     if((arr[k][l-1]!=defualtValue)&&(arr[k][l-1]!=defualtZeroValue))
			            		    	 value++;
			            		     if((arr[k][l+1]!=defualtValue)&&(arr[k][l+1]!=defualtZeroValue))
			            		    	 value++;
			            		     if((arr[k-1][l]!=defualtValue)&&(arr[k-1][l]!=defualtZeroValue))
			            		    	 value++;
			            		     if((arr[k+1][l]!=defualtValue)&&(arr[k+1][l]!=defualtZeroValue))
			            		    	 value++;
			            		     
			            			 switch(value){
				            		 case 0:  flag=false;  break;
				            		 case 1:  arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l]-3*defualtValue)*defualtNeighborValue; 
				            		         break;
				            		 case 2:   arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l]-2*defualtValue)/2; 
		            		                 break;
				            		 case 3:   arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l]-defualtValue)/3;
	         		                         break;
				            		 case 4:   arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l])/4;
	 		                                 break;
				            		    }
			            	      }  
			                 }
			              
			              for(int l=1;l<index;l=l+2){    
			            	    value=0;      //默认值，非边缘
			            	  if((arr[k][l]==defualtValue)&&(k!=0)&&(k!=(index-1))&&(l!=0)&&(l!=(index-1))){
			            		     if((arr[k][l-1]!=defualtValue)&&(arr[k][l-1]!=defualtZeroValue))
			            		    	 value++;
			            		     if((arr[k][l+1]!=defualtValue)&&(arr[k][l+1]!=defualtZeroValue))
			            		    	 value++;
			            		     if((arr[k-1][l]!=defualtValue)&&(arr[k-1][l]!=defualtZeroValue))
			            		    	 value++;
			            		     if((arr[k+1][l]!=defualtValue)&&(arr[k+1][l]!=defualtZeroValue))
			            		    	 value++;
			            		     
			            			 switch(value){
				            		 case 0:  flag=false;  break;
				            		 case 1:  arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l]-3*defualtValue)*defualtNeighborValue; 
				            		         break;
				            		 case 2:   arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l]-2*defualtValue)/2; 
		            		                 break;
				            		 case 3:   arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l]-defualtValue)/3;
	         		                         break;
				            		 case 4:   arr[k][l]=(arr[k][l-1]+arr[k][l+1]+arr[k-1][l]+arr[k+1][l])/4;
	 		                                 break;
				            		    }
			            	     }  
			                 }
		            }     
		  } 
		       //以上过程产生index*index个数据，据此作出图形  
		        String title="雷电流强度分布图";
		       DrawIntensity  drawIntensity=new DrawIntensity();
		             return  drawIntensity.returnInputStream(arr,difX,difY,minX,minY,maxX,maxY,index,title);
	   }
	   
	   
	   
	   
	   
	   /****************************判断10公里范围*****************************************/
	   
	     //测试两个地球上的点是否相距超过10公里
	 public boolean TestRange(double lng1,double lat1,double lng2,double lat2){
		     final double EARTH_RADIUS = 6378.137;  //地球半径
			 double radLat1 = rad(lat1);
			 double radLat2 = rad(lat2);
			 double a = radLat1 - radLat2;
			 double b = rad(lng1) - rad(lng2);
		     double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
			 s = s * EARTH_RADIUS;
			  if(s>10.0)
		         return false;
			  else 
				return true;  
	     } 
	 
	 private static double rad(double d)
	  {
	     return d * Math.PI / 180.0;     //计算弧长
	  }
