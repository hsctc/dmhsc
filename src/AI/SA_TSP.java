package AI;

public class SA_TSP {
	protected int cityNum = 8;//城市的个数
	
	protected double temperature = 100;//设置的温度
	
	protected double pathLength;//路径的长度
	
	protected double minLength;//最小的长度
	
	protected City[] orderMid = new City[cityNum];//城市的序列
	
	protected City[] minOrder = new City[cityNum];//最小长度的序列
	
	protected double mapkobLength = 20000;//马尔科夫链长度
	
	protected double rate = 0.9;//温度下降速率
	
	public CityPosition CP = new CityPosition();
	
	//执行算法
	public void run() throws Exception {
		//初始化一个城市序列，默认1-8
		initOrder(orderMid);
		
		initOrder(minOrder);
		
		pathLength = computeLength(orderMid);
		//System.out.println(pathLength);
		minLength = pathLength;//初始化设置成开始的长度
		
		//System.out.println(minLength);
		int count = 0;//相同路径的次数
		
		while(count < 2){
			boolean bChange = false;//判断是否路径无变化
			for (int i = 0; i < mapkobLength; i++) {
				
				//随即选择两个点，然后进行2变换
				int first = (int)Math.floor(cityNum*Math.random());
				int second = (int)Math.floor(cityNum*Math.random());
				if(first == second) continue;
				//计算路径增量
				double add = computeAddLength(orderMid, first, second);
				//System.out.println(first+" "+second+" "+add+" "+temperature);
				if(anneal(add)){
					//如果接受，交换节点
					if(first > second){
						int k = first;
						first = second;
						second = k;
					}
					for (; second > first; second--) {
						if(first == second) break;
						City city = orderMid[first];
						orderMid[first] = orderMid[second];
						orderMid[second] = city;
						first++;
					}
					pathLength += add;
					System.out.println(pathLength+" "+add+" "+minLength);
					if(pathLength < minLength){
						minLength = pathLength;
						for (int j = 0; j < orderMid.length; j++) {
							minOrder[j] = orderMid[j];
						}
						bChange = true;
					}
					
				}
				//else System.out.println("citiao bu");
			}
			//System.out.println(bChange);
			if(bChange == true){
				count =0;
			}
			else {
				count++;
			}
			temperature = temperature * rate;//降温处理
		}
		//计算完毕
		for (int i = 0; i < minOrder.length; i++) {
			System.out.print(minOrder[i].getNumber()+" ");
		}
		System.out.println();
		System.out.println(minLength);
	}
	
	//判断是否接受此条路径
	public boolean anneal(double add) throws Exception {
		if (temperature < 1.0E-4) {
			return false;
		}
		else {
			if(add < 0.0)
				return true;
			else {
				if ((Math.random() < Math.exp((add*-1) / temperature))&&add!=0.0)
					return true;
				else
					return false;
			}
		}
	}
	
	//计算路径增量
	public double computeAddLength(City[] order, int first, int second) throws Exception {
		double add = 0.0;
		double start = 0.0;//开始的长度
		double end = 0.0;//交换之后的长度
//		double start1 = Math.pow(order[(cityNum+first-1)%cityNum].getX()-order[first].getX(),2.0)+
//				Math.pow(order[(cityNum+first-1)%cityNum].getY()-order[first].getY(),2.0);
//		start1 = Math.sqrt(start1);//前一段的距离
//		double start2 = Math.pow(order[(second+1)%cityNum].getX()-order[second].getX(),2.0)+
//				Math.pow(order[(second+1)%cityNum].getY()-order[second].getY(),2.0);
//		start2 = Math.sqrt(start2);//后一段距离
//		start = start1 + start2;
//		
//		double end1 = Math.pow(order[(cityNum+first-1)%cityNum].getX()-order[second].getX(), 2.0)+
//				Math.pow(order[(cityNum+first-1)%cityNum].getY()-order[second].getY(), 2.0);
//		end1 = Math.sqrt(end1);
//		double end2 = Math.pow(order[(second+1)%cityNum].getX()-order[first].getX(), 2.0)+
//				Math.pow(order[(second+1)%cityNum].getY()-order[first].getY(), 2.0);
//		end2 = Math.sqrt(end2);
//		end = end1 + end2;
//		add = start - end;//前边减后边的
//		System.out.println(start1+" "+start2+" "+start+" "+end1+" "+end2+" "+end);
		start = computeLength(order);
		City[] test = new City[cityNum];
		for (int i = 0; i < order.length; i++) {
			test[i] = order[i];
		}
		for (; second > first; second--) {
			if(first == second) break;
			City city = test[first];
			test[first] = test[second];
			test[second] = city;
			first++;
		}
		end = computeLength(test);
		add = end - start;
		return add;
	}
	
	//计算两个城市之间的路径
	public double twoCityLength(City c1, City c2) throws Exception {
		double len = Math.sqrt(Math.pow(c1.getX()-c2.getX(), 2.0)+Math.pow(c1.getY()-c2.getY(), 2.0));
		return len;
	}
	
	//计算一个城市序列的路径长度
	public double computeLength(City[] order) throws Exception {
		double sum = 0.0;
		for (int i = 0; i < order.length-1; i++) {
			sum += Math.sqrt(Math.pow(order[i].getX()-order[i+1].getX(), 2.0)+Math.pow(order[i].getY()-order[i+1].getY(), 2.0));
		}
		sum += Math.sqrt(Math.pow(order[0].getX()-order[order.length-1].getX(), 2.0)+Math.pow(order[0].getY()-order[order.length-1].getY(), 2.0));
		return sum;
	}
	
	//初始化城市序列
	public void initOrder(City[] order) throws Exception {
		//默认序列是1-8
		for (int i = 0; i < cityNum; i++) {
			City city = new City();
			city.setNumber(i);
			order[i] = city;
		}
		//设置城市的坐标
		order[0].setX(CP.x1);order[0].setY(CP.y1);
		order[1].setX(CP.x2);order[1].setY(CP.y2);
		order[2].setX(CP.x3);order[2].setY(CP.y3);
		order[3].setX(CP.x4);order[3].setY(CP.y4);
		order[4].setX(CP.x5);order[4].setY(CP.y5);
		order[5].setX(CP.x6);order[5].setY(CP.y6);
		order[6].setX(CP.x7);order[6].setY(CP.y7);
		order[7].setX(CP.x8);order[7].setY(CP.y8);
	}
}
