package com.magicwork.foldline;

/**
 * ����̶�
 * @author licq
 *
 */
public class CrossAxisScale {
	private String title;//�̶�����
	private int percentPos;//�԰ٷֱȱ�ע����λ��
	
	
	public CrossAxisScale(String title, int percentPos) {
		super();
		this.title = title;
		this.percentPos = percentPos;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPercentPos() {
		return percentPos;
	}
	public void setPercentPos(int percentPos) {
		this.percentPos = percentPos;
	}
	
	
}
