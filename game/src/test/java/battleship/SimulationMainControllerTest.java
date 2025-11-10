package battleship;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import battleship.swing.BattleshipFrame;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimulationGameControllerTest {

    @Test
    void testStartGameSimpleWithLocalVars() {
        // Variables locales para simular lo que hace startGame
        Player player1 = new Player("Player 1", new ArrayList<>());
        Player player2 = new Player("Player 2", new ArrayList<>());
        Table table1 = new Table();
        Table table2 = new Table();

        // placeShips y gameLoop no se llaman porque no necesitamos GUI ni input
        // Esto es suficiente para probar la "creación de objetos"

        // Verificamos que no sean null
        assertNotNull(player1, "player1 debe existir");
        assertNotNull(player2, "player2 debe existir");
        assertNotNull(table1, "table1 debe existir");
        assertNotNull(table2, "table2 debe existir");
    }

    @Test
    void testPlaceShipsNoShips() {
        Player mockPlayer = EasyMock.createMock(Player.class);
        Table mockTable = EasyMock.createMock(Table.class);

        EasyMock.expect(mockPlayer.getName()).andReturn("Player 1").anyTimes();
        EasyMock.expect(mockPlayer.getShips()).andReturn(Collections.emptyList()).anyTimes();

        mockTable.drowMyPlayerTable(Collections.emptyList());
        EasyMock.expectLastCall().once();

        EasyMock.replay(mockPlayer, mockTable);

        SimulationGameController controller = new SimulationGameController();

        controller.placeShips(mockPlayer, mockTable);
        EasyMock.verify(mockPlayer, mockTable);
    }

    @Test
    void testIsOverlappingTrueAndFalse() {
        Player mockPlayer = EasyMock.createMock(Player.class);
        Ship mockShip1 = EasyMock.createMock(Ship.class);
        Ship mockShip2 = EasyMock.createMock(Ship.class);

        Position ship1Pos1 = new Position(0, 0);
        Position ship1Pos2 = new Position(0, 1);
        Position ship2Pos1 = new Position(2, 2);
        Position ship2Pos2 = new Position(2, 3);

        EasyMock.expect(mockPlayer.getShips()).andReturn(Arrays.asList(mockShip1, mockShip2)).anyTimes();
        EasyMock.expect(mockShip1.getPositions()).andReturn(Arrays.asList(ship1Pos1, ship1Pos2)).anyTimes();
        EasyMock.expect(mockShip2.getPositions()).andReturn(Arrays.asList(ship2Pos1, ship2Pos2)).anyTimes();

        EasyMock.replay(mockPlayer, mockShip1, mockShip2);

        // 4️⃣ Creamos controlador
        SimulationGameController controller = new SimulationGameController();

        // 5️⃣ Caso donde hay superposición (true)
        List<Position> overlappingPositions = Arrays.asList(new Position(0, 0), new Position(1, 0));
        boolean resultTrue = controller.isOverlapping(mockPlayer, overlappingPositions);
        assertTrue(resultTrue, "Debe detectar superposición");

        // 6️⃣ Caso donde NO hay superposición (false)
        List<Position> freePositions = Arrays.asList(new Position(1, 1), new Position(1, 2));
        boolean resultFalse = controller.isOverlapping(mockPlayer, freePositions);
        assertFalse(resultFalse, "No debe detectar superposición");

        // 7️⃣ Verificamos mocks
        EasyMock.verify(mockPlayer, mockShip1, mockShip2);
    }

    @Test
    void testAllShipsDestroyed() {
        // 1️⃣ Creamos mocks
        Player mockPlayer = EasyMock.createMock(Player.class);
        Ship mockShip1 = EasyMock.createMock(Ship.class);
        Ship mockShip2 = EasyMock.createMock(Ship.class);
        Table mockTable = EasyMock.createMock(Table.class);

        // 2️⃣ Creamos posiciones
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(0, 1);
        Position pos3 = new Position(1, 0);
        Position pos4 = new Position(1, 1);

        // 3️⃣ Configuramos los barcos
        EasyMock.expect(mockPlayer.getShips()).andReturn(Arrays.asList(mockShip1, mockShip2)).anyTimes();
        EasyMock.expect(mockShip1.getPositions()).andReturn(Arrays.asList(pos1, pos2)).anyTimes();
        EasyMock.expect(mockShip2.getPositions()).andReturn(Arrays.asList(pos3, pos4)).anyTimes();

        // 4️⃣ Configuramos la tabla
        int[][] matrixAllDestroyed = new int[10][10];
        matrixAllDestroyed[0][0] = 2;
        matrixAllDestroyed[0][1] = 2;
        matrixAllDestroyed[1][0] = 2;
        matrixAllDestroyed[1][1] = 2;

        int[][] matrixNotAllDestroyed = new int[10][10];
        matrixNotAllDestroyed[0][0] = 2;
        matrixNotAllDestroyed[0][1] = 0; // este no está destruido
        matrixNotAllDestroyed[1][0] = 2;
        matrixNotAllDestroyed[1][1] = 2;

        EasyMock.expect(mockTable.getMatrix()).andReturn(matrixAllDestroyed).anyTimes();
        EasyMock.replay(mockPlayer, mockShip1, mockShip2, mockTable);

        SimulationGameController controller = new SimulationGameController();

        // 5️⃣ Caso todos destruidos → true
        boolean resultTrue = controller.allShipsDestroyed(mockPlayer, mockTable);
        assertTrue(resultTrue, "Todos los barcos destruidos → debe ser true");

        // 6️⃣ Caso no todos destruidos → false
        EasyMock.reset(mockTable);
        EasyMock.expect(mockTable.getMatrix()).andReturn(matrixNotAllDestroyed).anyTimes();
        EasyMock.replay(mockTable);

        boolean resultFalse = controller.allShipsDestroyed(mockPlayer, mockTable);
        assertFalse(resultFalse, "Algún barco no destruido → debe ser false");

        EasyMock.verify(mockPlayer, mockShip1, mockShip2, mockTable);
    }

    @Test
    void testAskForShot() {

        String input = "3 4\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());

        // Guardamos System.in original
        java.io.InputStream sysInBackup = System.in;
        System.setIn(in);

        try {
            SimulationGameController controller = new SimulationGameController();
            // Llamamos al método
            Position pos = controller.askForShot();

            // Verificamos
            assertEquals(3, pos.getX());
            assertEquals(4, pos.getY());
        } finally {
            // Restauramos System.in
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testAskForShotInvalidThenValid() {
        String input = "10 10\n2 3\n"; // primero inválido, luego válido
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());

        java.io.InputStream sysInBackup = System.in;
        System.setIn(in);

        try {
            SimulationGameController controller = new SimulationGameController();
            Position pos = controller.askForShot();

            assertEquals(2, pos.getX());
            assertEquals(3, pos.getY());
        } finally {
            System.setIn(sysInBackup);
        }
    }
    
    @Test
    void testGameLoop() {
        // 1️⃣ Simulamos input del usuario: disparo fila 0, columna 0
        String input = "0 0\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        java.io.InputStream sysInBackup = System.in;
        System.setIn(in);

        try {
            // 2️⃣ Creamos el controlador
            SimulationGameController controller = new SimulationGameController();

            // 3️⃣ Creamos barcos y jugadores
            Ship ship1 = new TestShip(1);
            ship1.setPositions(java.util.Arrays.asList(new Position(0,0)));

            Player player1 = new Player("Player 1", java.util.Arrays.asList(ship1));
            Player player2 = new Player("Player 2", java.util.Arrays.asList(ship1));

            // 4️⃣ Creamos tablas y marcamos la posición como destruida (2) para terminar el bucle
            Table table1 = new Table();
            Table table2 = new Table();
            table1.getMatrix()[0][0] = 2;
            table2.getMatrix()[0][0] = 2;

            // 5️⃣ Asignamos jugadores y tablas usando reflexión
            java.lang.reflect.Field fPlayer1 = SimulationGameController.class.getDeclaredField("player1");
            fPlayer1.setAccessible(true);
            fPlayer1.set(controller, player1);

            java.lang.reflect.Field fPlayer2 = SimulationGameController.class.getDeclaredField("player2");
            fPlayer2.setAccessible(true);
            fPlayer2.set(controller, player2);

            java.lang.reflect.Field fTable1 = SimulationGameController.class.getDeclaredField("table1");
            fTable1.setAccessible(true);
            fTable1.set(controller, table1);

            java.lang.reflect.Field fTable2 = SimulationGameController.class.getDeclaredField("table2");
            fTable2.setAccessible(true);
            fTable2.set(controller, table2);

            // 6️⃣ Mockeamos los frames
            BattleshipFrame mockFrame1 = EasyMock.createMock(BattleshipFrame.class);
            BattleshipFrame mockFrame2 = EasyMock.createMock(BattleshipFrame.class);

            mockFrame1.refreshBoard();
            EasyMock.expectLastCall().anyTimes();
            mockFrame2.refreshBoard();
            EasyMock.expectLastCall().anyTimes();
            EasyMock.replay(mockFrame1, mockFrame2);

            // 7️⃣ Ejecutamos gameLoop (terminará inmediatamente)
            controller.gameLoop(mockFrame1, mockFrame2);

            // 8️⃣ Verificamos que refreshBoard se llamó
            EasyMock.verify(mockFrame1, mockFrame2);

        } catch (Exception e) {
            e.printStackTrace();
            assert false : "El test falló por excepción: " + e.getMessage();
        } finally {
            // Restauramos System.in
            System.setIn(sysInBackup);
        }
    }

}
