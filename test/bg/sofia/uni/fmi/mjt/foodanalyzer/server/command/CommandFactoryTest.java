package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodAnalyzerException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.InvalidCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.InvalidCommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandFactoryTest {

    @Test
    public void testGetCommandWithNullArgument() {
        String message = "IllegalArgumentException expected when method getCommand is called with null argument";
        assertThrows(IllegalArgumentException.class, () -> CommandFactory.getCommand(null), message);
    }

    @Test
    public void testGetCommandWithInvalidCommand() {
        String invalidCommand = "get-food-by-ingredients cheese";
        String message = "InvalidCommandException expected when method getCommand is called with non-existing command";
        assertThrows(InvalidCommandException.class, () -> CommandFactory.getCommand(invalidCommand), message);
    }

    @Test
    public void testGetCommandWithNoArguments() {
        String command = "get-food";
        String message =
                "InvalidCommandArgumentsException expected when method getCommand is called with no arguments";
        assertThrows(InvalidCommandArgumentsException.class, () -> CommandFactory.getCommand(command), message);
    }

    @Test
    public void testGetCommandReturnsGetFoodCommand() throws FoodAnalyzerException {
        String command = "get-food raffaello";
        String message = String.format(
                "GetFoodCommand instance expected to be returned when method getCommand() is called with '%s' argument",
                command);

        Command actual = CommandFactory.getCommand(command);

        assertTrue(actual instanceof GetFoodCommand, message);
    }

    @Test
    public void testGetCommandReturnsGetFoodReportCommand() throws FoodAnalyzerException {
        String command = "get-food-report 111111";
        String message = String.format(
                "GetFoodReportCommand instance expected to be returned when method getCommand() is called with '%s' argument",
                command);

        Command actual = CommandFactory.getCommand(command);

        assertTrue(actual instanceof GetFoodReportCommand, message);
    }

    @Test
    public void testGetCommandReturnsGetFoodByBarcodeCommand() throws FoodAnalyzerException {
        String command = "get-food-by-barcode 123456";
        String message = String.format(
                "GetFoodByBarcodeCommand instance expected to be returned when method getCommand() is called with '%s' argument",
                command);

        Command actual = CommandFactory.getCommand(command);

        assertTrue(actual instanceof GetFoodByBarcode, message);
    }

}