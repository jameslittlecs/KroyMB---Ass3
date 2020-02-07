package com.mozarellabytes.kroy.Minigame;

public class Attack {

	private String name;
	private int maxPP;
	private int PP;
	private Unit self;
	private int targetHealthChange, selfHealthChange, targetDamageChange, selfDamageChange;
	private boolean selected;
	
	public Attack(String name, int maxPP, Unit self, int targetHealthChange, int selfHealthChange, int targetDamageChange, int selfDamageChange, boolean selected) {
		this.name = name;
		this.maxPP = maxPP;
		this.PP = maxPP;
		this.self = self;
		this.targetHealthChange = targetHealthChange;
		this.selfHealthChange = selfHealthChange;
		this.targetDamageChange = targetDamageChange;
		this.selfDamageChange = selfDamageChange;
		this.selected = selected;
	}
	
	//Methods
	public void performAttack(Unit target) {
		self.setHP(self.getHP()+this.getSelfHealthChange());
		target.setHP(target.getHP()+this.getTargetHealthChange());
		self.setDamage(self.getDamage()+this.getSelfDamageChange());
		target.setDamage(target.getDamage()+this.getTargetDamageChange());
		this.PP -= 1;
	}
	
	public boolean hasPP() {
		if(this.getPP()>0) {
			return true;
		}
		return false;
	}
	//Getters and Setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMaxPP() {
		return maxPP;
	}
	public void setMaxPP(int pP) {
		maxPP = pP;
	}
	public int getPP() {
		return PP;
	}
	public void setPP(int pP) {
		PP = pP;
	}
	public Unit getSelf() {
		return self;
	}
	public void setSelf(Unit self) {
		this.self = self;
	}
	public int getTargetHealthChange() {
		return targetHealthChange;
	}
	public void setTargetHealthChange(int targetHealthChange) {
		this.targetHealthChange = targetHealthChange;
	}
	public int getSelfHealthChange() {
		return selfHealthChange;
	}
	public void setSelfHealthChange(int selfHealthChange) {
		this.selfHealthChange = selfHealthChange;
	}
	public int getTargetDamageChange() {
		return targetDamageChange;
	}
	public void setTargetDamageChange(int targetDamageChange) {
		this.targetDamageChange = targetDamageChange;
	}
	public int getSelfDamageChange() {
		return selfDamageChange;
	}
	public void setSelfDamageChange(int selfDamageChange) {
		this.selfDamageChange = selfDamageChange;
	}
		public boolean getSelected() {
		return this.selected;
	}
	public void setSelected(boolean selected) {
		this.selected =  selected;
	}
	
	
}
