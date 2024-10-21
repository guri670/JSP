package mvc.vo;

// 페이징을 하기위해서 기준이되는 데이터를 담는 클래스
public class Criteria {
	
	private int page =1 ; // page번호를 담는 멤버변수
	private int perPageNum =15 ; // 화면의 리스트 게시물 수 num/page
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPerPageNum() {
		return perPageNum;
	}
	public void setPerPageNum(int perPageNum) {
		this.perPageNum = perPageNum;
	}
}
