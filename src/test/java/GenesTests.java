import agh.ics.oop.data.Vector2d;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.maps.RectangularMap;
import agh.ics.oop.objects.Animal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenesTests {
    @Test
    //sprawdzam czy geny u orginalu i kopii sa takie same
    void copyTest() {
        AbstractWorldMap map = new RectangularMap(10,10,0.5,10,2,1);
        Animal first = new Animal(map,new Vector2d(2,2),"s",null,null,null);
        Animal copy = new Animal(map,new Vector2d(2,2),"copy",null,null,first);
        for(int i=0;i<32;i++){
            assertEquals(first.genes[i],copy.genes[i]);
        }
    }
    @Test
    //sprawdzam czy geny dziecka pochodza od rodzicow
    void childTest() {
        AbstractWorldMap map = new RectangularMap(10,10,0.5,10,2,1);
        Animal parent1 = new Animal(map,new Vector2d(2,2),"s",null,null,null);
        Animal parent2 = new Animal(map,new Vector2d(3,2),"s",null,null,null);
        Animal child = new Animal(map,new Vector2d(2,2),"c",parent1,parent2,null);
        int sem = 0;
        for(int i=0;i<32;i++){
            if(child.genes[i] != parent1.genes[i] && child.genes[i] != parent2.genes[i])
            {
                sem += 1;
            }
        }
        assertEquals(0,sem);
    }
    @Test
    //sprawdzam czy geny sa z danego zakresu
    void startAnimalTest(){
        AbstractWorldMap map = new RectangularMap(10,10,0.5,10,2,1);
        Animal animal = new Animal(map,new Vector2d(2,2),"s",null,null,null);
        int sem = 0;
        for(int i = 0; i < 32; i++){
            if(animal.genes[i] < 0 || animal.genes[i] > 8){
                sem += 1;
            }
        }
        assertEquals(0,sem);
    }


}
