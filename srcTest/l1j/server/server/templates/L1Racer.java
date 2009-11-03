package l1j.server.server.templates;

import l1j.server.server.model.L1Object;

public class L1Racer {
        private int num;
        private int 승리횟수;
        private int 패횟수;
        
	public int getNum(){
		return num;
	}public void setNum(int num) {
		this.num = num;
	}

	public int get_승리횟수() {
		return 승리횟수;
	}public void set_승리횟수(int 승리횟수) {
		this.승리횟수 = 승리횟수;
	}

        public int get_패횟수() {
		return 패횟수;
	}public void set_패횟수(int 패횟수) {
		this.패횟수 = 패횟수;
	}
}


