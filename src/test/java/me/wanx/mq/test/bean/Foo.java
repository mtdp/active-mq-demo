package me.wanx.mq.test.bean;

public class Foo {
	private int i;
	private String j;
	public Foo(){}
	public Foo(int i ,String j){
		this.i = i;
		this.j = j;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public String getJ() {
		return j;
	}
	public void setJ(String j) {
		this.j = j;
	}
	
	@Override
	public String toString() {
		return "toString method:"+i+","+j;
	}
}
