package com.westar.base.pojo;

public class SpeItemMaxScore {

	/*专项考核子项名称*/
	private String speItemName;

	/*专项考核子项id*/
	private Integer speItemId;
	
	/*得分上限*/
	private int maxScore;


	public String getSpeItemName() {
		return speItemName;
	}


	public void setSpeItemName(String speItemName) {
		this.speItemName = speItemName;
	}


	public Integer getSpeItemId() {
		return speItemId;
	}


	public void setSpeItemId(Integer speItemId) {
		this.speItemId = speItemId;
	}


	public int getMaxScore() {
		return maxScore;
	}


	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}
	
	
	
}
