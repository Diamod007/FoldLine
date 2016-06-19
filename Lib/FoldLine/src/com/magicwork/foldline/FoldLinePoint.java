package com.magicwork.foldline;

/**
 * ���ߵ�
 * 
 * @author licq
 *
 */
public class FoldLinePoint {
	private int percentY;// ��Y���ϵĸ߶�
	private String tip;// �����ʱ��ʾ��ֵ
	private String scaleTip;// �̶�ֵ
	private boolean isShowScale = true;

	public FoldLinePoint(int percentY, String tip, String scaleTip, boolean isShowScale) {
		super();
		this.percentY = percentY;
		this.tip = tip;
		this.scaleTip = scaleTip;
		this.isShowScale = isShowScale;
	}

	public int getPercentY() {
		return percentY;
	}

	public void setPercentY(int percentY) {
		this.percentY = percentY;
	}

	public boolean isShowScale() {
		return isShowScale;
	}

	public void setShowScale(boolean isShowScale) {
		this.isShowScale = isShowScale;
	}

	public String getScaleTip() {
		return scaleTip;
	}

	public void setScaleTip(String scaleTip) {
		this.scaleTip = scaleTip;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

}
