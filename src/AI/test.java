package AI;

public class test {
	public static void main(String[] args) throws Exception {
		City[] order = new City[3];//城市的序列
		City city = new City();
		order[0] = city;
		order[0].setNumber(0);order[0].setX(1);order[0].setY(1);
		city = new City();
		order[1] = city;
		order[1].setNumber(1);order[1].setX(2);order[1].setY(2);
		city = new City();
		order[2] = city;
		order[2].setNumber(2);order[2].setX(1);order[2].setY(2);
		SA_TSP sa_TSP = new SA_TSP();
		sa_TSP.run();
		//double sum = sa_TSP.computeAddLength(order, 2, 1);
		//System.out.println(sum);
		
	}
	public static void test(int[] t)
	{
		t[0]=2;
	}
}
