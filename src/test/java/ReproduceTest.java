import agh.ics.oop.data.Vector2d;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.maps.RectangularMap;
import agh.ics.oop.objects.Animal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReproduceTest {
    @Test
    //sprawdzam czy rodzicom ubywa energii po rozmnozeniu
    void reproduceTest(){
        AbstractWorldMap map = new RectangularMap(10,10,0.5,10,2,1);
        Animal parent1 = new Animal(map,new Vector2d(2,2),"s",null,null,null);
        Animal parent2 = new Animal(map,new Vector2d(3,2),"s",null,null,null);
        float energy1 = parent1.getEnergy();
        float energy2 = parent2.getEnergy();
        Animal child = new Animal(map,new Vector2d(2,2),"c",parent1,parent2,null);
        float energyAfter1 = parent1.getEnergy();
        float energyAfter2 = parent2.getEnergy();
        assertTrue(energy1 > energyAfter1);
        assertTrue(energy2 > energyAfter2);
    }
}
