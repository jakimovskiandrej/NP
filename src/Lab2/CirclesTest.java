package Lab2;

import java.util.*;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;
    void moveDown() throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    int getCurrentXPosition();
    int getCurrentYPosition();
    TYPE getType();
}

class ObjectCanNotBeMovedException extends Exception {
    Movable m;
    public ObjectCanNotBeMovedException(Movable m) {
        this.m = m;
    }
    public void message(){
        if(m.getType() == TYPE.POINT){
            System.out.println(String.format("Point (%d,%d) is out of bounds",m.getCurrentXPosition(),m.getCurrentYPosition()));
        }else{
            System.out.println(String.format("Circle (%d,%d) is out of bounds",m.getCurrentXPosition(),m.getCurrentYPosition()));
        }
    }
}

class MovableObjectNotFittableException extends Exception {
    Movable m;
    public MovableObjectNotFittableException(Movable m) {
        this.m = m;
    }
    public void message(){
        if(m.getType() == TYPE.POINT){
            System.out.println(String.format("Point (%d,%d) is out of bounds",m.getCurrentXPosition(),m.getCurrentYPosition()));
        }else{
            System.out.println(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection",m.getCurrentXPosition(),m.getCurrentYPosition(),((MovableCircle)m).getRadius()));
        }
    }
}

class MovablePoint implements Movable {

    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void tryMoving(DIRECTION direction) throws ObjectCanNotBeMovedException {
        if(direction==DIRECTION.RIGHT) {
            if((x+xSpeed)>MovablesCollection.maxX) {
                throw new ObjectCanNotBeMovedException(new MovablePoint(x+xSpeed,y,1,1));
            }
        }
        else if (direction==DIRECTION.LEFT) {
            if((x-xSpeed)<0) {
                throw new ObjectCanNotBeMovedException(new MovablePoint(x-xSpeed,y,1,1));
            }
        }
        if(direction==DIRECTION.UP) {
            if((y+ySpeed)>MovablesCollection.maxY) {
                throw new ObjectCanNotBeMovedException(new MovablePoint(x,y+ySpeed,1,1));
            }
        }
        else if(direction==DIRECTION.DOWN) {
            if((y-ySpeed)<0) {
                throw new ObjectCanNotBeMovedException(new MovablePoint(x,y-ySpeed,1,1));
            }
        }
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        tryMoving(DIRECTION.UP);
        y+=ySpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        tryMoving(DIRECTION.DOWN);
        y-=ySpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        tryMoving(DIRECTION.RIGHT);
        x+=xSpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        tryMoving(DIRECTION.LEFT);
        x-=xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    @Override
    public TYPE getType() {
        return TYPE.POINT;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)\n", x,y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovablePoint that = (MovablePoint) o;
        return x == that.x && y == that.y && xSpeed == that.xSpeed && ySpeed == that.ySpeed;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + xSpeed;
        result = 31 * result + ySpeed;
        return result;
    }
}

class MovableCircle implements Movable {

    private int radius;
    private MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    public int getRadius() {
        return radius;
    }

    public MovablePoint getCenter() {
        return center;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        center.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        center.moveDown();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        center.moveRight();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        center.moveLeft();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public TYPE getType() {
        return TYPE.CIRCLE;
    }

    @Override
    public String toString() {
        return String.format("Movable circle with center coordinates (%d,%d) and radius %d\n", center.getCurrentXPosition(), center.getCurrentYPosition(), radius);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovableCircle that = (MovableCircle) o;
        return radius == that.radius && Objects.equals(center, that.center);
    }

    @Override
    public int hashCode() {
        int result = radius;
        result = 31 * result + Objects.hashCode(center);
        return result;
    }
}

class MovablesCollection {

    private Movable[] movable;
    public static int maxY;
    public static int maxX;
    int n;

    public MovablesCollection(int x_MAX, int y_MAX) {
        maxX = x_MAX;
        maxY = y_MAX;
        n = 0;
        movable = new Movable[0];
    }

    public static void setyMax(int y_MAX) {
        MovablesCollection.maxY = y_MAX;
    }

    public static void setxMax(int x_MAX) {
        MovablesCollection.maxX = x_MAX;
    }

    void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if(checkOutOfBounds(m)) {
            throw new MovableObjectNotFittableException(m);
        }
        movable = Arrays.copyOf(movable,n+1);
        movable[n++] = m;
    }

    void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        for(Movable m : movable) {
            if(m.getType()==type) {
                try {
                    if(direction==DIRECTION.UP) {
                        m.moveUp();
                    }
                    else if(direction==DIRECTION.DOWN) {
                        m.moveDown();
                    }
                    else if(direction==DIRECTION.RIGHT) {
                        m.moveRight();
                    }
                    else if(direction==DIRECTION.LEFT) {
                        m.moveLeft();
                    }
                } catch (ObjectCanNotBeMovedException e) {
                    e.message();
                }
            }
        }
    }

    public boolean checkOutOfBounds(Movable m) {
        if(m.getCurrentXPosition()<0 || m.getCurrentXPosition()>maxX) {
            if(m.getCurrentYPosition()<0 || m.getCurrentYPosition()>maxY) {
                return true;
            }
        }
        if(m.getType()==TYPE.CIRCLE) {
            int radius = ((MovableCircle) m).getRadius();
            int rightX = m.getCurrentXPosition() + radius;
            int leftX = m.getCurrentXPosition() - radius;
            int upY = m.getCurrentYPosition() + radius;
            int downY = m.getCurrentYPosition() - radius;

            if(rightX>maxX || leftX<0 || upY>maxY || downY<0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Collection of movable objects with size "+n+":\n");
        for (Movable m :movable) {
            sb.append(m);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovablesCollection that = (MovablesCollection) o;
        return Arrays.equals(movable, that.movable);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(movable);
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException e) {
                    e.message();
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e) {
                    e.message();
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);

        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }

}
