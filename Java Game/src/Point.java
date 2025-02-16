class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int[] getPoint() {
        return new int[] {this.x, this.y};
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Point) {
            Point other = (Point) object;
            
            if (this.x == other.x && this.y == other.y) {
                return true;
            }
        }

        return false;
    }
}