package cn.mb.app;

public enum ProductType {

	PZ01("PZ01"),PZ02("PZ02");
	private ProductType(String name){
		this.name=name;
	}
	private String name;
	public String getName() {
		return name;
	}
}
