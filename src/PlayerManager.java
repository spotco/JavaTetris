
public class PlayerManager {
	public int gridx,gridy;
	private GameManager game;
	
	private int type;
	private int status;
	private boolean leftqueue,rightqueue,rotatequeue,downqueue;
	
	public int ax,ay;
	public int bx,by;
	public int cx,cy;
	public int dx,dy;
	
	
	public PlayerManager(int gridx,int gridy,GameManager game) {
		this.gridx = gridx;
		this.gridy = gridy;
		this.game = game;
	}
	
	//return true is game over switch on game
	public boolean generateNew(int[][] grid,int rand) {
		status = 0;
		type = rand;
		if (type == 1) { //+ type
			ax = (gridx/2)-1;ay = 0; // abc
			bx = (gridx/2);by = 0; //  d
			cx = (gridx/2)+1;cy = 0;
			dx = (gridx/2);dy = 1;
		} else if (type == 2) { //box type
			ax = (gridx/2);ay = 0; // ab
			bx = (gridx/2)+1;by = 0; // cd
			cx = (gridx/2);cy = 1;
			dx = (gridx/2)+1;dy = 1;
		} else if (type == 3) { //- type
			ax = (gridx/2)-1;ay = 0; // abcd
			bx = (gridx/2);by = 0; //
			cx = (gridx/2)+1;cy = 0;
			dx = (gridx/2)+2;dy = 0;
		} else if (type == 4) { //L type
			ax = (gridx/2)-1;ay = 0; // abc
			bx = (gridx/2);by = 0; // d
			cx = (gridx/2)+1;cy = 0;
			dx = (gridx/2)-1;dy = 1;
		} else if (type == 5) { //BL type
			ax = (gridx/2)-1;ay = 0; // abc
			bx = (gridx/2);by = 0; //   d
			cx = (gridx/2)+1;cy = 0;
			dx = (gridx/2)+1;dy = 1;
		} else if (type == 6) { //z type
			ax = (gridx/2)-1;ay = 0; // ab
			bx = (gridx/2);by = 0; //  cd
			cx = (gridx/2);cy = 1;
			dx = (gridx/2)+1;dy = 1;
		} else if (type == 7) { //s type
			ax = (gridx/2);ay = 0; //  ab
			bx = (gridx/2)+1;by = 0;   // cd
			cx = (gridx/2)-1;cy = 1;
			dx = (gridx/2);dy = 1;
		}
		if (game.isActive(ax, ay) || game.isActive(bx, by) || game.isActive(cx, cy) || game.isActive(dx,dy)
			|| game.isActive(ax, ay+1) || game.isActive(bx, by+1) || game.isActive(cx, cy+1) || game.isActive(dx,dy+1)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void fall() {
		ay++;
		by++;
		cy++;
		dy++;
	}
	
	public boolean hitcheck(int[][] grid) {
		if ((ay == gridy-1) || (by == gridy-1) || (cy == gridy-1) || (dy == gridy-1)) {
			return true;
		} else if (!(grid[ax][ay+1] == 0) && !(grid[ax][ay+1] == 9)) {
			return true;
		} else if (!(grid[bx][by+1] == 0) && !(grid[bx][by+1] == 9)) {
			return true;
		} else if (!(grid[cx][cy+1] == 0) && !(grid[cx][cy+1] == 9)) {
			return true;
		} else if (!(grid[dx][dy+1] == 0) && !(grid[dx][dy+1] == 9)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void event(int[][] grid) {
		if (leftqueue) {
			leftevent(grid);
		} else if (rightqueue) {
			rightevent(grid);
		} else if (rotatequeue) {
			rotateevent(grid);
		} else if (downqueue) {
			downevent(grid);
		}
		leftqueue = false;
		rightqueue = false;
		rotatequeue = false;
		downqueue = false;
	}
	
	public void downevent(int[][] grid) {
		if ((ay+1<gridy) && (by+1<gridy) && (cy+1<gridy) && (dy+1<gridy) && canrotate(ax,ay+1,bx,by+1,cx,cy+1,dx,dy+1)) {
			ay++;
			by++;
			cy++;
			dy++;
		}
	}
	
	public void leftevent(int[][] grid) {
		boolean leftedgecheck = true;
		for(int y = 0; y < gridy; y++) {
			if (grid[0][y] == 9) {
				leftedgecheck = false;
			}
		}
		if (leftedgecheck && (grid[ax-1][ay] == 1 || grid[bx-1][by] == 1 || grid[cx-1][cy] == 1 || grid[dx-1][dy] == 1)) {
			leftedgecheck = false;
		}
		if (leftedgecheck) {
			ax--;
			bx--;
			cx--;
			dx--;
		}
	}
	
	public void rightevent(int[][] grid) {
		boolean rightedgecheck = true;
		for(int y = 0; y < gridy; y++) {
			if (grid[gridx-1][y] == 9) {
				rightedgecheck = false;
			}
		}
		if (rightedgecheck && (grid[ax+1][ay] == 1 || grid[bx+1][by] == 1 || grid[cx+1][cy] == 1 || grid[dx+1][dy] == 1)) {
			rightedgecheck = false;
		}
		if (rightedgecheck) {
			ax++;
			bx++;
			cx++;
			dx++;
		}
	}
	
	public void actionRight() {
		leftqueue = false;
		rotatequeue = false;
		downqueue = false;
		rightqueue = true;
	}
	
	public void actionLeft() {
		rightqueue = false;
		rotatequeue = false;
		downqueue = false;
		leftqueue = true;
	}
	
	public void actionRotate() {
		rotatequeue = true;
		rightqueue = false;
		downqueue = false;
		leftqueue = false;
	}
	
	public void actionDown() {
		rotatequeue = false;
		rightqueue = false;
		downqueue = true;
		leftqueue = false;
	}
	
	public void rotateevent(int[][] grid) {
		//status 0,1,2,3
		if (status == 0) {
			status0(grid);
		} else if (status == 1) {
			status1(grid);
		} else if (status == 2) {
			status2(grid);
		} else if (status == 3) {
			status3(grid);
		}
	}
	
	private boolean canrotate(int ax, int ay, int bx, int by, int cx, int cy, int dx, int dy) {
		if ( (ax > -1) && (bx > -1) && (cx > -1) && (dx > -1) &&
			 (ax < gridx) && (bx < gridx) && (cx < gridx) && (dx < gridx) &&
			 (ay > -1) && (by > -1) && (cy > -1) && (dy > -1) &&
			 (ay < gridy) && (by < gridy) && (cy < gridy) && (dy < gridy) &&
			 (!game.isStatic(ax,ay)) && (!game.isStatic(bx,by)) && (!game.isStatic(cx,cy)) && (!game.isStatic(dx,dy)) ) {
			return true;
		}
		return false;
	}
	
	private void status0(int[][] grid) {
		if (type == 1) { 
			if (/*DNC*/canrotate(ax+1,ay-1,bx,by,cx-1,cy+1,dx-1,dy-1)/*DNC*/) {
				status = 1;
				ax = ax + 1; ay = ay - 1;
				cx = cx - 1; cy = cy + 1;
				dx = dx - 1; dy = dy - 1;
			}
		} else if (type == 3) {
			if (/*DNC*/canrotate(ax+1,ay-1,bx,by,cx-1,cy+1,dx-2,dy+2)/*DNC*/) {
				status = 1;
				ax = ax + 1; ay = ay - 1;
				cx = cx - 1; cy = cy + 1;
				dx = dx - 2; dy = dy + 2;
			}
		} else if (type == 4) {	
			if (/*DNC*/canrotate(ax,ay,bx-1,by+1,cx-2,cy+2,dx-1,dy-1)/*DNC*/) {
				status = 1;
				bx = bx - 1; by = by + 1;
				cx = cx - 2; cy = cy + 2;
				dx = dx - 1; dy = dy - 1;
			}
		} else if (type == 5) {	
			if (/*DNC*/canrotate(ax+2,ay-2,bx+1,by-1,cx,cy,dx-1,dy-1)/*DNC*/) {
				status = 1;
				ax = ax+2; ay = ay-2;
				bx = bx+1; by = by-1;
				dx = dx-1; dy = dy-1;
			}
		} else if (type == 6) {
			if (/*DNC*/canrotate(ax+2,ay,bx+1,by+1,cx,cy,dx-1,dy+1)/*DNC*/) {
				status = 1;
				ax = ax + 2;
				bx = bx + 1; by = by + 1;
				dx = dx - 1; dy = dy + 1;
			}
		} else if (type == 7) {
			if (/*DNC*/canrotate(ax+1,ay+1,bx,by+2,cx+1,cy-1,dx,dy)/*DNC*/) {
				status = 1;
				ax = ax + 1; ay = ay + 1;
				by = by + 2;
				cx = cx + 1; cy = cy - 1;
			}
		}
		
	}
	
	private void status1(int[][] grid) {
		if (type == 1) { 
			if (/*DNC*/canrotate(ax+1,ay-1,bx,by,cx-1,cy+1,dx-1,dy-1)/*DNC*/) {
				status = 2;
				ax = ax + 1; ay = ay + 1;
				cx = cx - 1; cy = cy - 1;
				dx = dx + 1; dy = dy - 1;
			}
		} else if (type == 3) {
			if (/*DNC*/canrotate(ax-1,ay+1,bx,by,cx+1,cy-1,dx+2,dy-2)/*DNC*/) {
				status = 0;
				ax = ax - 1; ay = ay + 1;
				cx = cx + 1; cy = cy - 1;
				dx = dx + 2; dy = dy - 2;
			}
		} else if (type == 4) {	
			if (/*DNC*/canrotate(ax,ay,bx-1,by-1,cx-2,cy-2,dx+1,dy-1)/*DNC*/) {
				status = 2;
				bx = bx - 1; by = by - 1;
				cx = cx - 2; cy = cy - 2;
				dx = dx + 1; dy = dy - 1;
			}
		} else if (type == 5) {	
			if (/*DNC*/canrotate(ax+2,ay+2,bx+1,by+1,cx,cy,dx+1,dy-1)/*DNC*/) {
				status = 2;
				ax = ax+2; ay = ay+2;
				bx = bx+1; by = by+1;
				dx = dx+1; dy = dy-1;
			}
		} else if (type == 6) {
			if (/*DNC*/canrotate(ax-2,ay,bx-1,by-1,cx,cy,dx+1,dy-1)/*DNC*/) {
				status = 0;
				ax = ax - 2;
				bx = bx - 1; by = by - 1;
				dx = dx + 1; dy = dy - 1;
			}
		} else if (type == 7) {
			if (/*DNC*/canrotate(ax-1,ay-1,bx,by-2,cx-1,cy+1,dx,dy)/*DNC*/) {
				status = 0;
				ax = ax - 1; ay = ay - 1;
				by = by - 2;
				cx = cx - 1; cy = cy + 1;
			}
		}
	}
	
	private void status2(int[][] grid) {
		if (type == 1) { 
			if (/*DNC*/canrotate(ax-1,ay+1,bx,by,cx+1,cy-1,dx+1,dy+1)/*DNC*/) {
				status = 3;
				ax = ax - 1; ay = ay + 1;
				cx = cx + 1; cy = cy - 1;
				dx = dx + 1; dy = dy + 1;
			}
		} else if (type == 4) {	
			if (/*DNC*/canrotate(ax,ay,bx+1,by-1,cx+2,cy-2,dx+1,dy+1)/*DNC*/) {
				status = 3;
				bx = bx + 1; by = by - 1;
				cx = cx + 2; cy = cy - 2;
				dx = dx + 1; dy = dy + 1;
			}
		} else if (type == 5) {	
			if (/*DNC*/canrotate(ax-2,ay+2,bx-1,by-1,cx,cy,dx+1,dy+1)/*DNC*/) {
				status = 3;
				ax = ax-2; ay = ay+2;
				bx = bx-1; by = by+1;
				dx = dx+1; dy = dy+1;
			}
		}
	}
	
	private void status3(int[][] grid) {
		if (type == 1) { 
			if (/*DNC*/canrotate(ax-1,ay-1,bx,by,cx+1,cy+1,dx-1,dy+1)/*DNC*/) {
				status = 0;
				ax = ax - 1; ay = ay - 1;
				cx = cx + 1; cy = cy + 1;
				dx = dx - 1; dy = dy + 1;
			}
		} else if (type == 4) {	
			if (/*DNC*/canrotate(ax,ay,bx+1,by+1,cx+2,cy+2,dx-1,dy+1)/*DNC*/) {
				status = 0;
				bx = bx + 1; by = by + 1;
				cx = cx + 2; cy = cy + 2;
				dx = dx - 1; dy = dy + 1;
			}
		} else if (type == 5) {	
			if (/*DNC*/canrotate(ax-2,ay-2,bx-1,by-1,cx,cy,dx-1,dy+1)/*DNC*/) {
				status = 0;
				ax = ax-2; ay = ay-2;
				bx = bx-1; by = by-1;
				dx = dx-1; dy = dy+1;
			}
		}
	}
	
}
