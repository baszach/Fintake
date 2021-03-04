package layout;

import file.FileCommunicator;
import parser.ParsingCommunicator;
import request.RequestCommunicator;

import java.util.Optional;

public class MainLayout extends Layout {

    private final RequestCommunicator requestCommunicator;
    private final FileCommunicator fileCommunicator;
    private final ParsingCommunicator parsingCommunicator;

    public MainLayout(RequestCommunicator requestCommunicator, FileCommunicator fileCommunicator, ParsingCommunicator parsingCommunicator) {
        this.requestCommunicator = requestCommunicator;
        this.fileCommunicator = fileCommunicator;
        this.parsingCommunicator = parsingCommunicator;
        init();
    }

    public void init() {
        filePathButton.addActionListener(e -> {
            fileCommunicator.updatePath(filePathField.getText());
        });

        queryButton.addActionListener(e -> {
            requestCommunicator.updateSymbol(symbolField.getText());
            requestCommunicator.updateApiKey(apiKeyField.getText());
            Optional<String> jsonString = requestCommunicator.runQuery();
            jsonString.ifPresent(parsingCommunicator::updateJsonString);
        });

        magicButton.addActionListener(e -> {
            parsingCommunicator.parse();
            //TODO after receiving the parsed Map, display key-set for selection
        });
    }
}
