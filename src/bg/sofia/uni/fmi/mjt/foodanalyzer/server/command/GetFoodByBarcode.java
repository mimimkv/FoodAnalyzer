package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.decoder.QRDecoder;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.InvalidCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorage;

import java.io.FileNotFoundException;

public class GetFoodByBarcode extends AbstractCommand {

    private static final String NO_FOOD_FOUND_MESSAGE = "No food matched your request";
    private static final String CODE_ARGUMENT_TEXT = "--code=";
    private static final String IMAGE_ARGUMENT_TEXT = "--image=";
    private static final String SPACE_SYMBOL = " ";
    private static final String EMPTY_SYMBOL = "";
    private static final int FIRST_ARGUMENT_INDEX = 0;
    private static final int SECOND_ARGUMENT_INDEX = 1;


    public GetFoodByBarcode(String arguments) {
        super(arguments);
    }

    @Override
    public String execute(FoodStorage storage) throws InvalidCommandArgumentsException {
        assertArgumentsNotNull();
        assertStorageNotNull(storage);

        String[] tokens = getArguments().split(SPACE_SYMBOL);
        Food result;

        // if code argument is given, then image argument is ignored
        if (tokens[FIRST_ARGUMENT_INDEX].startsWith(CODE_ARGUMENT_TEXT)) {
            result = executeWithCodeArgument(tokens[FIRST_ARGUMENT_INDEX], storage);

        } else if (tokens.length >= 2 && tokens[SECOND_ARGUMENT_INDEX].startsWith(CODE_ARGUMENT_TEXT)) {
            result = executeWithCodeArgument(tokens[SECOND_ARGUMENT_INDEX], storage);

        } else if (tokens[FIRST_ARGUMENT_INDEX].startsWith(IMAGE_ARGUMENT_TEXT)) {
            try {
                result = executeWithImageArgument(tokens[FIRST_ARGUMENT_INDEX], storage);
            } catch (FileNotFoundException e) {
                return e.getMessage();
            }

        } else {
            throw new InvalidCommandArgumentsException(
                    String.format("%s is invalid argument when using command get-food-by-barcode",
                            tokens[FIRST_ARGUMENT_INDEX]));
        }

        if (result == null) {
            return NO_FOOD_FOUND_MESSAGE;
        }

        return result.toString();
    }

    private Food executeWithCodeArgument(String argument, FoodStorage storage) {
        String barcode = argument.replace(CODE_ARGUMENT_TEXT, EMPTY_SYMBOL);
        return storage.getFoodByBarCode(barcode);
    }

    private Food executeWithImageArgument(String argument, FoodStorage storage) throws FileNotFoundException {
        String image = argument.replace(IMAGE_ARGUMENT_TEXT, EMPTY_SYMBOL);
        String barcode = QRDecoder.getQRCode(image);
        return storage.getFoodByBarCode(barcode);
    }

}
