package mvc.vo;

// 페이지 하단에 페이징 네비게이션에 필요한 변수들을 담아놓는 클래스
public class PageMaker {
	
	private int displayPageNum = 10; //페이지 목록번호 리스트를 담는 변수
	private int startPage; // 목록의 시작번호를 담는 변수
	private int endPage; // 목록의 마지막 번호를 담는 변수  
	private int totalCnt; // 총 게시물 수를 담는 변수
	
	private boolean prev; // 이전버튼
	private boolean next; // 다음버튼


	private SearchCriteria scri;

	public int getDisplayPageNum() {
		return displayPageNum;
	}

	public void setDisplayPageNum(int displayPageNum) {
		this.displayPageNum = displayPageNum;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) { 
		// 총 게시물이 몇개인지 받는 메소드
		this.totalCnt = totalCnt;
		calcData(); // 페이지 목록 리스트 번호를 나타내주기 위한 계산식
		
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}
	
	public SearchCriteria getScri() {
		return scri;
	}

	public void setScri(SearchCriteria scri) {
		this.scri = scri;
	}
	
	public void calcData() {
		// 1. 기본적으로 1에서 부터 10까지 나타나게 설정한다.(페이지 네비게이션)
		endPage = (int)(Math.ceil(scri.getPage()/(double)displayPageNum)*displayPageNum);
		// Math.ceil 모두 올림처리하는 메소드
		
		// 2. endPage가 설정되었으면 시작페이지도 설정
		startPage = (endPage-displayPageNum)+1;
		
		// 3. 실제 게시물수에 따라서 endPage를 구하는식
		int tempEndPage = (int)(Math.ceil(totalCnt/(double)scri.getPerPageNum()));
		
		// 4. 설정한 endPage와 실제 endPage를 비교해서 최종 endPage를 구한다.
		if(endPage > tempEndPage) {
			endPage = tempEndPage;
		}
		
		// 5. 이전 다음 버튼 만들기
		prev = (startPage ==1 ? false : true); //삼항연산자 사용
		// startPage가 1이면 prev 는 false 아니면 true
		next = (endPage*scri.getPerPageNum() >= totalCnt ? false : true );
		
	}
}
