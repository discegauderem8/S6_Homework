import java.util.*;

public class LeeAlgorithmFinal {

    private static class Point2D {
        int y, x;

        public Point2D(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static void main(String[] args) {

        int[][] arr = new int[12][12];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = 0;
            }
        }

        Point2D a = new Point2D(2, 2);
        Point2D b1 = new Point2D(7, 4);
        Point2D b2 = new Point2D(1, 7);

        makeWalls(arr);
        pathFinder(arr, a, b1, b2); // a - стартовая точка b1/b2 - выходы

    }

    private static void printField(int[][] field) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                System.out.printf("%d ", field[i][j]);
            }
            System.out.println();
        }
    }

    private static void makeWalls(int[][] field) { //Нужен, так как алгоритм умеет "ходить" только в 4 стороны, и НЕ
        for (int i = 0; i < field[0].length; i++) {//заходит на крайние линии матрицы (иначе он будет выходить за
            field[0][i] = -1;                       //пределы массива.
                                                    //В итоге без стен точки в углах остаются незакрашенными
        }                                           //Тут же рисуем стены внутри самого лабиринта
        for (int i = 0; i < field[0].length; i++) {
            field[field.length - 1][i] = -1;
        }
        for (int i = 0; i < field.length; i++) {
            field[i][0] = -1;
        }
        for (int i = 0; i < field.length; i++) {
            field[i][field[0].length - 1] = -1;
        }
        field[1][1] = field[1][2] = field[1][3] = field[1][4] = -1;
        field[2][3] = field[3][3] = field[4][3] = field[5][3] = field[6][3] = field[7][3] = -1;
    }

    public static boolean isValid(Point2D a, int[][] field) {
        return a.y - 1 >= 0 && a.y + 1 <= field.length - 1 && a.x - 1 >= 0 && a.x + 1 <= field[0].length - 1;
    }

    private static void pathFinder(int[][] field, Point2D start, Point2D finish1, Point2D finish2) {
        Queue<Point2D> queue = new LinkedList<>();
        Queue<Integer> values = new LinkedList<>();
        Deque<Point2D> path = new ArrayDeque<>(); //будет использована при поиске обратного пути
        values.add(1);
        if (field[start.y][start.x] == 0) {
            System.out.println("Отлично! Стартовое значение не занято.");
        } else {
            System.out.println("Стартовое значение уже занято.");
        }
        queue.add(start);

        while (!queue.isEmpty()) { // блок while проверяет условие, выполняет код, потом снова проверяет условие
            int value = values.peek();//поэтому можно добавить в начало queue.remove()
            int newValue = value + 1;
            values.remove();
            Point2D currPos = queue.peek();
            queue.remove();

            if ((currPos.x == finish1.x && currPos.y == finish1.y) || (currPos.x == finish2.x && currPos.y == finish2.y)){

                Point2D finishFound = new Point2D(currPos.y, currPos.x);
                field[currPos.y][currPos.x] = value;
                path.add(finishFound);
                break;
                }

            if (field[currPos.y][currPos.x] == 0) {
                field[currPos.y][currPos.x] = value;
                if (isValid(currPos, field)) {//isValid проверяет не выходят ли значения x +- 1,y +- 1 за пределы массива
                    Point2D newPos1 = new Point2D(currPos.y - 1, currPos.x); // писать currPos.x/y +- 1 не вариант,
                    queue.add(newPos1);                                       // так как это ссылочный тип, и в очередь
                    values.add(newValue);
                    Point2D newPos2 = new Point2D(currPos.y, currPos.x + 1); // идет лишь последний вариант.
                    queue.add(newPos2);                                       // В итоге закрасится лишь одна линия
                    values.add(newValue);
                    Point2D newPos3 = new Point2D(currPos.y + 1, currPos.x);
                    queue.add(newPos3);
                    values.add(newValue);
                    Point2D newPos4 = new Point2D(currPos.y, currPos.x - 1);
                    queue.add(newPos4);
                    values.add(newValue);
                }
            }
        }
// Блок кода, который рисует карту выхода
        int[][] pathMap = new int [field.length][field[0].length];
        while(!path.isEmpty()){
            Point2D currPos = path.pop();
            pathMap[currPos.y][currPos.x] = field[currPos.y][currPos.x];//просто придаем значения из field)))

            if(field[currPos.y+1][currPos.x] == field[currPos.y][currPos.x]-1){ //заполняем в порядке ОБРАТНОЙ очередности
                Point2D newerPos1 = new Point2D(currPos.y+1, currPos.x);
                path.add(newerPos1);
            } else if (field[currPos.y][currPos.x-1] == field[currPos.y][currPos.x]-1){
                Point2D newerPos2 = new Point2D(currPos.y, currPos.x-1);
                path.add(newerPos2);
            } else if (field[currPos.y-1][currPos.x] == field[currPos.y][currPos.x]-1){
                Point2D newerPos3 = new Point2D(currPos.y-1, currPos.x);
                path.add(newerPos3);
            } else if (field[currPos.y][currPos.x+1] == field[currPos.y][currPos.x]-1){
                Point2D newerPos4 = new Point2D(currPos.y, currPos.x+1);
                path.add(newerPos4);
            }
        }
        path.add(start);

        makeWalls(pathMap);
        System.out.println("Путь к выходу из лабиринта: ");
        printField(pathMap);
        System.out.println("Более подробно ход поиска выглядит так:");
        printField(field);

    }
}


