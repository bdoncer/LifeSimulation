import agh.ics.oop.data.MapDirection;
import agh.ics.oop.data.MoveDirection;
import agh.ics.oop.data.Vector2d;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.maps.BendedMap;
import agh.ics.oop.maps.RectangularMap;
import agh.ics.oop.objects.Animal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapTests {
    @Test
    //sprawdzam czy zostanie na miejscu
    void RectangularMapTest(){
        AbstractWorldMap map = new RectangularMap(10,10,0.5,10,2,1);
        Animal first = new Animal(map,new Vector2d(9,10),"s",null,null,null);
        first.orientation = MapDirection.N;
        first.move(MoveDirection.F);
        assertEquals(9,first.position.x);
        assertEquals(10,first.position.y);
    }
    @Test
    //sprawdzam czy przejdzie na 2 strone
    void BendedMapTest(){
        AbstractWorldMap map = new BendedMap(10,10,0.5,10,2,1);
        Animal first = new Animal(map,new Vector2d(9,10),"s",null,null,null);
        first.orientation = MapDirection.N;
        first.move(MoveDirection.F);
        assertEquals(9,first.position.x);
        assertEquals(0,first.position.y);
    }
}
