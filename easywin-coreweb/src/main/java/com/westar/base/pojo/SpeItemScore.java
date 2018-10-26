package com.westar.base.pojo;

public class SpeItemScore {
	
	/*专项考核子项名称*/
	private String speItemName;

	/*专项考核子项id*/
	private Integer speItemId;
	
	/*得分*/
	private double score;
	

	public Integer getSpeItemId() {
		return speItemId;
	}

	public void setSpeItemId(Integer speItemId) {
		this.speItemId = speItemId;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getSpeItemName() {
		return speItemName;
	}

	public void setSpeItemName(String speItemName) {
		this.speItemName = speItemName;
	}

}
