package AI;

public class SA_TSP {
	protected int cityNum = 8;//���еĸ���
	
	protected double temperature = 100;//���õ��¶�
	
	protected double pathLength;//·���ĳ���
	
	protected double minLength;//��С�ĳ���
	
	protected City[] orderMid = new City[cityNum];//���е�����
	
	protected City[] minOrder = new City[cityNum];//��С���ȵ�����
	
	protected double mapkobLength = 20000;//����Ʒ�������
	
	protected double rate = 0.9;//�¶��½�����
	
	public CityPosition CP = new CityPosition();
	
	//ִ���㷨
	public void run() throws Exception {
		//��ʼ��һ���������У�Ĭ��1-8
		initOrder(orderMid);
		
		initOrder(minOrder);
		
		pathLength = computeLength(orderMid);
		//System.out.println(pathLength);
		minLength = pathLength;//��ʼ�����óɿ�ʼ�ĳ���
		
		//System.out.println(minLength);
		int count = 0;//��ͬ·���Ĵ���
		
		while(count < 2){
			boolean bChange = false;//�ж��Ƿ�·���ޱ仯
			for (int i = 0; i < mapkobLength; i++) {
				
				//�漴ѡ�������㣬Ȼ�����2�任
				int first = (int)Math.floor(cityNum*Math.random());
				int second = (int)Math.floor(cityNum*Math.random());
				if(first == second) continue;
				//����·������
				double add = computeAddLength(orderMid, first, second);
				//System.out.println(first+" "+second+" "+add+" "+temperature);
				if(anneal(add)){
					//������ܣ������ڵ�
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
			temperature = temperature * rate;//���´���
		}
		//�������
		for (int i = 0; i < minOrder.length; i++) {
			System.out.print(minOrder[i].getNumber()+" ");
		}
		System.out.println();
		System.out.println(minLength);
	}
	
	//�ж��Ƿ���ܴ���·��
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
	
	//����·������
	public double computeAddLength(City[] order, int first, int second) throws Exception {
		double add = 0.0;
		double start = 0.0;//��ʼ�ĳ���
		double end = 0.0;//����֮��ĳ���
//		double start1 = Math.pow(order[(cityNum+first-1)%cityNum].getX()-order[first].getX(),2.0)+
//				Math.pow(order[(cityNum+first-1)%cityNum].getY()-order[first].getY(),2.0);
//		start1 = Math.sqrt(start1);//ǰһ�εľ���
//		double start2 = Math.pow(order[(second+1)%cityNum].getX()-order[second].getX(),2.0)+
//				Math.pow(order[(second+1)%cityNum].getY()-order[second].getY(),2.0);
//		start2 = Math.sqrt(start2);//��һ�ξ���
//		start = start1 + start2;
//		
//		double end1 = Math.pow(order[(cityNum+first-1)%cityNum].getX()-order[second].getX(), 2.0)+
//				Math.pow(order[(cityNum+first-1)%cityNum].getY()-order[second].getY(), 2.0);
//		end1 = Math.sqrt(end1);
//		double end2 = Math.pow(order[(second+1)%cityNum].getX()-order[first].getX(), 2.0)+
//				Math.pow(order[(second+1)%cityNum].getY()-order[first].getY(), 2.0);
//		end2 = Math.sqrt(end2);
//		end = end1 + end2;
//		add = start - end;//ǰ�߼���ߵ�
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
	
	//������������֮���·��
	public double twoCityLength(City c1, City c2) throws Exception {
		double len = Math.sqrt(Math.pow(c1.getX()-c2.getX(), 2.0)+Math.pow(c1.getY()-c2.getY(), 2.0));
		return len;
	}
	
	//����һ���������е�·������
	public double computeLength(City[] order) throws Exception {
		double sum = 0.0;
		for (int i = 0; i < order.length-1; i++) {
			sum += Math.sqrt(Math.pow(order[i].getX()-order[i+1].getX(), 2.0)+Math.pow(order[i].getY()-order[i+1].getY(), 2.0));
		}
		sum += Math.sqrt(Math.pow(order[0].getX()-order[order.length-1].getX(), 2.0)+Math.pow(order[0].getY()-order[order.length-1].getY(), 2.0));
		return sum;
	}
	
	//��ʼ����������
	public void initOrder(City[] order) throws Exception {
		//Ĭ��������1-8
		for (int i = 0; i < cityNum; i++) {
			City city = new City();
			city.setNumber(i);
			order[i] = city;
		}
		//���ó��е�����
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
