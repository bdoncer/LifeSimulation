import agh.ics.oop.data.MapDirection;
import agh.ics.oop.data.MoveDirection;
import agh.ics.oop.data.Vector2d;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.maps.RectangularMap;
import agh.ics.oop.objects.Animal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveTests {
    @Test
    //sprawdzam czy porusza sie do przodu
    void forwardMoveTest(){
        AbstractWorldMap map = new RectangularMap(10,10,0.5,10,2,1);
        Animal first = new Animal(map,new Vector2d(2,2),"s",null,null,null);
        first.orientation = MapDirection.N;
        first.move(MoveDirection.F);
        assertEquals(2,first.position.x);
        assertEquals(3,first.position.y);
    }

    @Test
    //sprawdzam czy porusza sie do tyłu
    void backwardMoveTest(){
        AbstractWorldMap map = new RectangularMap(10,10,0.5,10,2,1);
        Animal first = new Animal(map,new Vector2d(2,2),"s",null,null,null);
        first.orientation = MapDirection.N;
        first.move(MoveDirection.B);
        assertEquals(2,first.position.x);
        assertEquals(1,first.position.y);
    }

}
