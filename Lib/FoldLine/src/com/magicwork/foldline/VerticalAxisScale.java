package com.magicwork.foldline;

/**
 * ����̶�
 * @author licq
 *
 */
public class VerticalAxisScale {
	private String title;//�̶�����
	private int percentPos;//�԰ٷֱȱ�ע����λ��
	
	
	public VerticalAxisScale(String title, int postionPercent) {
		super();
		this.title = title;
		this.percentPos = postionPercent;
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
