package PPJ_P01;

public
    class S18687_p01 {

    public static void main(String[] args) {

        int[][] arr = new int[23][23];

        placeOnes(arr);

        int[] mouse = randomPoint(arr);

        while (isCheese(arr, mouse)) {
            mouse = randomPoint(arr);
        }

        setPoint(arr, mouse, 2);

        printArray(arr);
        System.out.println();

        strategyOne(arr, mouse);
        //strategyTwo(arr, mouse);
        //strategyThree(arr, mouse);
    }

    static void printArray(int[][] arr) {
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    static int ceil(float f) {
        int i = (int) f;
        return f > i ? i+1 : i;
    }

    static int countCheese(int[][] arr) {
        return ceil((float) (arr.length * arr[0].length) * 0.1f);
    }

    static void placeOnes(int[][] arr) {
        int count = countCheese(arr);

        for(int k = 0; k < count; k++) {
            int[] point = randomPoint(arr);

            if(!isCheese(arr, point)) {
                setPoint(arr, point, 1);
            } else {
                k--;
            }
        }
    }

    static int randomInt(int bound) {
        return (int) (Math.random() * bound);
    }

    static int[] randomPoint(int[][] arr) {
        return new int[] {randomInt(arr.length), randomInt(arr[0].length)};
    }

    static int randomOffset() {
        double offset = Math.random() * 3d;
        return ((int) offset) - 1;
    }

    static int[] randomVector() {
        int xoff = randomOffset();
        int yoff = randomOffset();

        while(xoff == 0 && yoff == 0) {
            xoff = randomOffset();
            yoff = randomOffset();
        }

        return new int[] {xoff, yoff};
    }

    static int[] movePoint(int[] point, int[] vector) {
        return new int[] {
                point[0] + vector[0],
                point[1] + vector[1]
        };
    }

    static int getPoint(int[][] arr, int[] point) {
        return arr[point[0]][point[1]];
    }

    static void setPoint(int[][] arr, int[] point, int value) {
        arr[point[0]][point[1]] = value;
    }

    static boolean hasCheese(int[][] arr) {
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[0].length; j++) {
                if(arr[i][j] == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean isCheese(int[][] arr, int[] point) {
        return getPoint(arr, point) == 1;
    }

    static void move(int[][] arr, int[] current, int[] next) {

        setPoint(arr, current, 3);
        setPoint(arr, next, 2);

        System.out.println();
        printArray(arr);
    }

    static boolean isMovePossible(int[][] arr, int[] point, int[] vector) {
        return (point[0] + vector[0]) < arr.length && (point[1] + vector[1]) < arr[0].length &&
                (point[0] + vector[0]) >= 0 && (point[1] + vector[1]) >= 0;
    }

    static int squareDistane(int[] p1, int[] p2) {
        return (p2[0] - p1[0]) * (p2[0] - p1[0]) + (p2[1] - p1[1]) * (p2[1] - p1[1]);
    }

    static int[] closestCheese(int[][] cheese, boolean[] eaten, int[] mouse) {
        int minIndex = cheese.length;
        int minDistance = Integer.MAX_VALUE;

        for(int i = 0; i < cheese.length; i++) {
            if(!eaten[i]) {
                if(minDistance > squareDistane(mouse, cheese[i])) {
                    minIndex = i;
                    minDistance = squareDistane(mouse, cheese[i]);
                }
            }
        }

        eaten[minIndex] = true;
        return cheese[minIndex];
    }

    static int[] nextPoint(int[][] arr, int[] p1, int[] p2) {
        int bestDistance = squareDistane(p1, p2);
        int[] bestPoint = p1;

        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(i != 0 || j != 0) {
                    int[] nextPoint = movePoint(p1, new int[] {i, j});
                    if(squareDistane(nextPoint, p2) < bestDistance) {
                        bestDistance = squareDistane(nextPoint, p2);
                        bestPoint = nextPoint;
                    }
                }
            }
        }
        return bestPoint;
    }

    static int[][] findCheese(int[][] arr) {
        int count = countCheese(arr);
        int[][] cheese = new int[count][2];
        int index = 0;

        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[0].length; j++) {
                if(isCheese(arr, new int[] {i, j})) {
                    cheese[index][0] = i;
                    cheese[index][1] = j;
                    index++;
                }
            }
        }
        return cheese;
    }

    static int[] vector(int[] p1, int[] p2) {
        return new int[] { p2[0] - p1[0], p2[1] - p1[1] };
    }

    static int signum(int x) {
        if(x == 0) return x;
        return x > 0 ? 1 : -1;
    }

    static int flatten(int n) {
        return n != 0 ? 1 : 0;
    }

    static int direction(int a, int b) {
        return  b != 0 ? flatten(a / b) : flatten(a);
    }

    static int[] nextVector(int[] vector) {
        return new int[] {
                direction(vector[0], vector[1]) * signum(vector[0]),
                direction(vector[1], vector[0]) * signum(vector[1])
        };
    }

    static void moveVector(int[] v1, int[] v2) {
        v1[0] -= v2[0];
        v1[1] -= v2[1];
    }

    static void strategyOne(int[][] arr, int[] mouse) {
        while(hasCheese(arr)) {
            int[] move = randomVector();
            if(isMovePossible(arr, mouse, move)) {
                int[] nextMouse = movePoint(mouse, move);
                move(arr, mouse, nextMouse);
                mouse = nextMouse;
            }
        }
    }

    static void strategyTwo(int[][] arr, int[] mouse) {
        int[][] cheese = findCheese(arr);
        boolean[] eaten = new boolean[cheese.length];

        for(int i = 0; i < cheese.length; i++) {
            int[] closestCheese = closestCheese(cheese, eaten, mouse);
            int[] closestCheeseVector = vector(mouse, closestCheese);

            while(isCheese(arr, closestCheese)) {
                int[] nextVector = nextVector(closestCheeseVector);
                int[] nextMouse = movePoint(mouse, nextVector);
                move(arr, mouse, nextMouse);
                mouse = nextMouse;
                moveVector(closestCheeseVector, nextVector);
            }
        }
    }

    static void strategyThree(int[][] arr, int[] mouse) {
        int[][] cheese = findCheese(arr);

        for(int i = 0; i < cheese.length; i++) {
            while(isCheese(arr, cheese[i])) {
                int[] nextMouse = nextPoint(arr, mouse, cheese[i]);
                move(arr, mouse, nextMouse);
                mouse = nextMouse;
            }
       }
    }
}
