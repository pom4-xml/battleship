package battleship;


import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import battleship.logic.Player;
import battleship.logic.Position;
import battleship.logic.Ship;
import battleship.logic.SimulationGameController;
import battleship.logic.Table;
import battleship.logic.ship.Battleship;


import battleship.swing.BattleshipFrame;


import static org.easymock.EasyMock.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;


import static org.junit.jupiter.api.Assertions.*;

class SimulationGameControllerTest extends EasyMockSupport {
    SimulationGameController simulationGameController;
    Table table;
    List<Ship> listShips;

    ByteArrayOutputStream os;
    PrintStream ps;

    @BeforeEach
    void setup() {
        simulationGameController = new SimulationGameController();
        table = new Table();
        listShips = new ArrayList<>();

        Ship ship = new Battleship();
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 0));
        positions.add(new Position(1, 0));
        positions.add(new Position(2, 0));
        positions.add(new Position(3, 0));
        ship.setPositions(positions);

        listShips.add(ship);

        os = new ByteArrayOutputStream();
        ps = new PrintStream(os);
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
    }

    @Test
    void testGameLoopIsCalled() {
        // Crear un mock parcial de la clase principal. Esto no lo hemos dado en clase
        // pero asi podemos hacer un mock
        // de la misma clase que ejecutamos.
        SimulationGameController gameMock = createMockBuilder(SimulationGameController.class)
                .withConstructor()
                .addMockedMethods("gameLoop", "placeShips") // indicamos que vamos a mockear gameLoop y placeShips
                .createMock();

        gameMock.gameLoop(anyObject(BattleshipFrame.class), anyObject(BattleshipFrame.class));
        expectLastCall().once();

        gameMock.placeShips(anyObject(Player.class), anyObject(Table.class));
        expectLastCall().times(2);

        replayAll();

        gameMock.startGame();

        verifyAll();

    }

    @Test
    void testPlaceShipsNoShips() {
        List<Ship> emptyList = new ArrayList<>();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,() -> simulationGameController.placeShips(new Player("Edu", emptyList), table));assertEquals("Ship list can't be empty", ex.getMessage());
    }

    @Test
    void testPlaceShipsListShipsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,() -> simulationGameController.placeShips(new Player("Edu", null), table));assertEquals("Ship list can't be null", ex.getMessage());
    }

    @Test
    void testPlaceShipsNoPlayer() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,() -> simulationGameController.placeShips(null, table));assertEquals("Player can't be null", ex.getMessage());
    }

    @Test
    void testPlaceShipsNoTable() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,() -> simulationGameController.placeShips(new Player("Edu", listShips), null));assertEquals("Table can't be null", ex.getMessage());
    }

    @Test
    void testPlaceShipsBien() {
        SimulationGameController gameMock = createMockBuilder(SimulationGameController.class)
                .withConstructor()
                .addMockedMethods("tryPlaceShip") // indicamos que vamos a mockear gameLoop y placeShips
                .createMock();

        expect(gameMock.tryPlaceShip(anyObject(Player.class), anyObject(Ship.class)))
                .andReturn(true) // devuelve true
                .once(); // una vez solo

        replayAll();

        gameMock.placeShips(new Player("Edy", listShips), table);

        verifyAll();

        for (Ship ship : listShips) {
            for (Position position : ship.getPositions()) {
                assertEquals(1, table.getMyPlayerMatrix()[position.getX()][position.getY()]);
            }
        }

    }

    @Test
    void testTryPlaceShip_Normal() throws Exception {
        String input = "0\n0\ntrue\n";
        Field scannerField = SimulationGameController.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(simulationGameController, new Scanner(new ByteArrayInputStream(input.getBytes())));
        System.setOut(ps);

        listShips.clear();
        Ship shipSinPos = new Battleship();
        listShips.add(shipSinPos);

        boolean estado = simulationGameController.tryPlaceShip(new Player("raul", listShips), listShips.get(0));

        int i = 0;
        for (Position pos : shipSinPos.getPositions()) {
            assertEquals(pos, new Position(0, i));
            i++;
        }
        assertTrue(estado);

    }

    @Test
    void testTryPlaceShip_FueraRango_POSITIVO_HORIZONTAL() throws Exception {
        String input = "0\n10\ntrue\n";// +1 fuera del range
        Field scannerField = SimulationGameController.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(simulationGameController, new Scanner(new ByteArrayInputStream(input.getBytes())));
        System.setOut(ps);

        listShips.clear();
        Ship shipSinPos = new Battleship();
        listShips.add(shipSinPos);

        boolean estado = simulationGameController.tryPlaceShip(new Player("raul", listShips), listShips.get(0));

        assertFalse(estado);
        assertTrue(os.toString().trim().contains("Ship goes out of bounds."));

    }

    @Test
    void testTryPlaceShip_FueraRango_POSITIVO_VERTICAL() throws Exception {
        String input = "10\n0\nfalse\n";// +1 fuera del range
        Field scannerField = SimulationGameController.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(simulationGameController, new Scanner(new ByteArrayInputStream(input.getBytes())));
        System.setOut(ps);

        listShips.clear();
        Ship shipSinPos = new Battleship();
        listShips.add(shipSinPos);

        boolean estado = simulationGameController.tryPlaceShip(new Player("raul", listShips), listShips.get(0));

        assertFalse(estado);
        assertTrue(os.toString().trim().contains("Ship goes out of bounds."));

    }

    @Test
    void testTryPlaceShip_OVERLAPP() throws Exception {
        Player player = new Player("raul", listShips);

        String input = "0\n0\ntrue\n";
        Field scannerField = SimulationGameController.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(simulationGameController, new Scanner(new ByteArrayInputStream(input.getBytes())));
        System.setOut(ps);

        Ship shipSinPos = new Battleship();

        boolean estado = simulationGameController.tryPlaceShip(player, shipSinPos);

        assertFalse(estado);
        assertTrue(os.toString().trim().contains("Ship overlaps with another ship."));

    }

    @Test
    void testAllShipsDestroyed_BIEN() {
        Player player = new Player("raul", listShips);
        table.getMatrix()[0][0] = 2;
        table.getMatrix()[1][0] = 2;
        table.getMatrix()[2][0] = 2;
        table.getMatrix()[3][0] = 2;
        boolean estado = simulationGameController.allShipsDestroyed(player, table);
        assertTrue(estado);
    }

    @Test
    void testAllShipsDestroyed_MAL() {
        Player player = new Player("raul", listShips);
        table.getMatrix()[0][0] = 2;
        table.getMatrix()[1][0] = 2;
        table.getMatrix()[2][0] = 2;
        table.getMatrix()[3][0] = 1;
        boolean estado = simulationGameController.allShipsDestroyed(player, table);
        assertFalse(estado);
    }

    @Test
    void testAskForShot_EN_EL_RANGO() throws Exception {
        String input = "1\n1\ntrue\n";
        Field scannerField = SimulationGameController.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(simulationGameController, new Scanner(new ByteArrayInputStream(input.getBytes())));

        Position position = simulationGameController.askForShot();
        assertEquals(position, new Position(1, 1));
    }

    @Test
    void testAskForShot_FUERA_DEL_RANGO() throws Exception {
        String input = "10 10\n9 9\n";
        Field scannerField = SimulationGameController.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(simulationGameController, new Scanner(new ByteArrayInputStream(input.getBytes())));
        System.setOut(ps);

        Position position = simulationGameController.askForShot();
        assertEquals(position, new Position(9, 9));
        assertTrue(os.toString().trim().contains("Coordinates out of bounds"));
    }

    

}
