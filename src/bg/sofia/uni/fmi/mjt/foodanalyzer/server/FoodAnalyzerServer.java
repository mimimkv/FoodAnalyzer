package bg.sofia.uni.fmi.mjt.foodanalyzer.server;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodAnalyzerException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FoodAnalyzerServer implements Server {

    private static final int SERVER_PORT = 7777;
    private static final int BUFFER_SIZE = 4096;
    private static final String HOST = "localhost";
    private static final String FILE_NAME = "log_file.txt";
    private static final String UNEXPECTED_ERROR_MESSAGE = "Error occurred while processing client request";


    private static final Logger LOGGER = Logger.getLogger(FoodAnalyzerServer.class.getName());
    private final CommandExecutor commandExecutor;

    private boolean isServerWorking;

    private ByteBuffer buffer;
    private Selector selector;

    public FoodAnalyzerServer() {
        this.commandExecutor = new CommandExecutor();
    }

    @Override
    public void startServer() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            FileHandler file = new FileHandler(FILE_NAME, true);
            file.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(file);

            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isServerWorking = true;
            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            String clientInput = getClientInput(clientChannel);
                            if (clientInput == null) {
                                continue;
                            }

                            String output;
                            try {
                                output = commandExecutor.executeCommand(clientInput) + System.lineSeparator();
                            } catch (FoodAnalyzerException e) {
                                output = e.getMessage() + System.lineSeparator();
                            }
                            writeClientOutput(clientChannel, output);

                        } else if (key.isAcceptable()) {
                            accept(selector, key);
                        }

                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    System.out.println(UNEXPECTED_ERROR_MESSAGE);
                    LOGGER.log(Level.SEVERE, UNEXPECTED_ERROR_MESSAGE, e);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to start server", e);
            System.out.println("Unable to start server.Try again later.");
        }
    }

    @Override
    public void stopServer() {
        this.isServerWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(HOST, SERVER_PORT));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }

    @Override
    public void run() {
        startServer();
    }
}
