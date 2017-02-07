package com.ts.entity.ai;

public class Total {
	int num = 0;
	int count = 1;

	public Total() {

	}

	public Total(int num, int count) {
		super();
		this.count = count;
		this.num = num;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
