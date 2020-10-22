package fr.unice.polytech.startingpoint;


import fr.unice.polytech.startingpoint.BuildingsPackage.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class CityTest {
    private City cite1;
    private ArrayList<BaseBuildings> buildingsToBuild;

    @BeforeEach
    void setUp(){
        cite1 = new City();
        buildingsToBuild = new ArrayList<>();
    }

    @Test void buildTestSize(){
        buildingsToBuild.add(new BaseBuildings("1",1,1, BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsToBuild.add(new BaseBuildings("2",1,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("3",1,1, BuildingsTypeEnum.NOBLE.toString()));
        buildingsToBuild.add(new BaseBuildings("4",1,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("5",1,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("6",1,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("7",1,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new LaboratoireBuilding(MerveilleEnum.LABORATOIRE.toString(),5,5, BuildingsTypeEnum.MERVEILLE.toString()));
        for(BaseBuildings building : buildingsToBuild){
            cite1.build(building);
        }
        assertEquals(8,cite1.getNbrBuildings());
        assertEquals(buildingsToBuild,cite1.getBuildings());
        assertEquals(8,cite1.getBuildings().size());
    }
    @Test void buildTestGoodTypeBuilt(){
        buildingsToBuild.add(new BaseBuildings("1",1,1, BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsToBuild.add(new BaseBuildings("2",1,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("3",1,1, BuildingsTypeEnum.NOBLE.toString()));
        buildingsToBuild.add(new BaseBuildings("4",1,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new LaboratoireBuilding(MerveilleEnum.LABORATOIRE.toString(),5,5, BuildingsTypeEnum.MERVEILLE.toString()));
        for(BaseBuildings building : buildingsToBuild){
            cite1.build(building);
        }
        assertEquals(5,cite1.getNbrBuildings());
        assertEquals(1,cite1.getNbrMilitaryBuildings());
        assertEquals(1,cite1.getNbrNobleBuildings());
        assertEquals(2,cite1.getNbrCommercialBuildings());
        assertEquals(0,cite1.getNbrReligiousBuildings());
        assertEquals(1,cite1.getNbrWonderBuildings());
    }

@Test void buildSameNameAlreadyBuiltBuildings(){
    buildingsToBuild.add(new BaseBuildings("1",1,1, BuildingsTypeEnum.MILITAIRE.toString()));
    buildingsToBuild.add(new BaseBuildings("2",1,1, BuildingsTypeEnum.MARCHAND.toString()));
    buildingsToBuild.add(new BaseBuildings("3",1,1, BuildingsTypeEnum.NOBLE.toString()));
    buildingsToBuild.add(new BaseBuildings("3",2,2, BuildingsTypeEnum.NOBLE.toString()));
    buildingsToBuild.add(new BaseBuildings("4",1,1, BuildingsTypeEnum.MARCHAND.toString()));
    buildingsToBuild.add(new LaboratoireBuilding(MerveilleEnum.LABORATOIRE.toString(),5,5, BuildingsTypeEnum.MERVEILLE.toString()));
    for(BaseBuildings building : buildingsToBuild){
        cite1.build(building);
    }
    assertEquals(5,cite1.getNbrBuildings());
    assertEquals(1,cite1.getNbrMilitaryBuildings());
    assertEquals(1,cite1.getNbrNobleBuildings());
    assertEquals(2,cite1.getNbrCommercialBuildings());
    assertEquals(0,cite1.getNbrReligiousBuildings());
    assertEquals(1,cite1.getNbrWonderBuildings());

}


    @Test void destroyBuildingTest(){
        BaseBuildings buildingToDestroy = new BaseBuildings("1",1,1, BuildingsTypeEnum.MARCHAND.toString());
        buildingsToBuild.add(new BaseBuildings("2",1,1, BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsToBuild.add(new BaseBuildings("3",1,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("4",1,1, BuildingsTypeEnum.NOBLE.toString()));
        buildingsToBuild.add(buildingToDestroy);
        buildingsToBuild.add(new LaboratoireBuilding(MerveilleEnum.LABORATOIRE.toString(),5,5, BuildingsTypeEnum.MERVEILLE.toString()));
        for(BaseBuildings building : buildingsToBuild){
            cite1.build(building);
        }
        cite1.destroyBuilding(buildingToDestroy);
        assertEquals(4,cite1.getNbrBuildings());
        assertEquals(1,cite1.getNbrMilitaryBuildings());
        assertEquals(1,cite1.getNbrNobleBuildings());
        assertEquals(1,cite1.getNbrCommercialBuildings());
        assertEquals(0,cite1.getNbrReligiousBuildings());
        assertEquals(1,cite1.getNbrWonderBuildings());
    }

    @Test void destroyBuildingNotHere(){
        BaseBuildings buildingToDestroy = new BaseBuildings("Ok.",1,1,BuildingsTypeEnum.NOBLE.toString());
        buildingsToBuild.add(new BaseBuildings("1",1,1, BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsToBuild.add(new BaseBuildings("2",1,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("3",1,1, BuildingsTypeEnum.NOBLE.toString()));
        buildingsToBuild.add(new BaseBuildings("4",1,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new LaboratoireBuilding(MerveilleEnum.LABORATOIRE.toString(),5,5, BuildingsTypeEnum.MERVEILLE.toString()));
        for(BaseBuildings building : buildingsToBuild){
            cite1.build(building);
        }
        cite1.destroyBuilding(buildingToDestroy);
        assertEquals(5,cite1.getNbrBuildings());
        assertEquals(1,cite1.getNbrMilitaryBuildings());
        assertEquals(1,cite1.getNbrNobleBuildings());
        assertEquals(2,cite1.getNbrCommercialBuildings());
        assertEquals(0,cite1.getNbrReligiousBuildings());
        assertEquals(1,cite1.getNbrWonderBuildings());
    }
    @Test void bonusDifferentBuildingsTestWithBonus(){
        buildingsToBuild.add(new BaseBuildings("1",2,15, BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsToBuild.add(new BaseBuildings("2",6,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("3",122,1, BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsToBuild.add(new BaseBuildings("4",3,2, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new LaboratoireBuilding(MerveilleEnum.COURDESMIRACLES.toString(),5,5, BuildingsTypeEnum.MERVEILLE.toString()));
        for(BaseBuildings building : buildingsToBuild){
            cite1.build(building);
        }
        assertFalse(cite1.bonusDifferentBuildings());
    }
    @Test void bonusDifferentBuildingsTestWithoutBonus(){
        buildingsToBuild.add(new BaseBuildings("1",2,15, BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsToBuild.add(new BaseBuildings("2",6,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("3",122,1, BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsToBuild.add(new BaseBuildings("4",3,2, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("5",4,2, BuildingsTypeEnum.NOBLE.toString()));
        buildingsToBuild.add(new LaboratoireBuilding(MerveilleEnum.CIMETIERE.toString(),5,5, BuildingsTypeEnum.MERVEILLE.toString()));
        for(BaseBuildings building : buildingsToBuild){
            cite1.build(building);
        }
        assertTrue(cite1.bonusDifferentBuildings());
    }
    @Test void bonusDifferentBuildingsTestBonusThanksCourDesMiracles(){
        buildingsToBuild.add(new BaseBuildings("1",2,15, BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsToBuild.add(new BaseBuildings("2",6,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("3",122,1, BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsToBuild.add(new BaseBuildings("4",3,2, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("5",4,2, BuildingsTypeEnum.MERVEILLE.toString()));
        BaseBuildings courDesMiracles = new LaboratoireBuilding(MerveilleEnum.COURDESMIRACLES.toString(),5,5, BuildingsTypeEnum.MERVEILLE.toString());
        courDesMiracles.setType(BuildingsTypeEnum.NOBLE.toString());
        buildingsToBuild.add(courDesMiracles);
        for(BaseBuildings building : buildingsToBuild){
            cite1.build(building);
        }
        assertTrue(cite1.bonusDifferentBuildings());
    }
    @Test void isBuildAlreadyBuilt(){
        buildingsToBuild.add(new BaseBuildings("1",2,15, BuildingsTypeEnum.NOBLE.toString()));
        buildingsToBuild.add(new BaseBuildings("2",6,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("3",122,1, BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsToBuild.add(new BaseBuildings("4",3,2, BuildingsTypeEnum.MERVEILLE.toString()));
        buildingsToBuild.add(new BaseBuildings("5",4,2, BuildingsTypeEnum.MARCHAND.toString()));
        for(BaseBuildings building : buildingsToBuild){
            cite1.build(building);
        }
        assertTrue(cite1.isBuild(new BaseBuildings("1",2,15, BuildingsTypeEnum.NOBLE.toString())));
    }
    @Test void isBuildNotBuilt(){
        buildingsToBuild.add(new BaseBuildings("1",2,15, BuildingsTypeEnum.NOBLE.toString()));
        buildingsToBuild.add(new BaseBuildings("2",6,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("3",122,1, BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsToBuild.add(new BaseBuildings("4",3,2, BuildingsTypeEnum.MERVEILLE.toString()));
        buildingsToBuild.add(new BaseBuildings("5",4,2, BuildingsTypeEnum.MARCHAND.toString()));
        for(BaseBuildings building : buildingsToBuild){
            cite1.build(building);
        }
        assertFalse(cite1.isBuild(new BaseBuildings("6",2,15, BuildingsTypeEnum.NOBLE.toString())));
    }
    @Test void isBuildSameNameButNotSameTypeOrPrice(){
        buildingsToBuild.add(new BaseBuildings("1",2,15, BuildingsTypeEnum.NOBLE.toString()));
        buildingsToBuild.add(new BaseBuildings("2",6,1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsToBuild.add(new BaseBuildings("3",122,1, BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsToBuild.add(new BaseBuildings("4",3,2, BuildingsTypeEnum.MERVEILLE.toString()));
        buildingsToBuild.add(new BaseBuildings("5",4,2, BuildingsTypeEnum.MARCHAND.toString()));
        for(BaseBuildings building : buildingsToBuild){
            cite1.build(building);
        }
        assertTrue(cite1.isBuild(new BaseBuildings("5",2,2, BuildingsTypeEnum.MARCHAND.toString())));
        assertTrue(cite1.isBuild(new BaseBuildings("5",4,2, BuildingsTypeEnum.NOBLE.toString())));
    }

}
