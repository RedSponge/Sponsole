package com.redsponge.sponsole;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder;

import java.util.HashMap;
import java.util.Map;

public class Sponsole implements Disposable {
    
    private FitViewport viewport;
    private SpriteBatch batch;
    private Stage stage;
    private Texture pixel;

    private Skin skin;

    private TextField input;
    private Label output;
    private BitmapFont noMarkup;

    private CommandDispatcher<CommandResult> commandDispatcher;
    private Map<String, String> helps;

    private boolean isEnabled;

    public Sponsole(SpriteBatch batch, int width, int height) {
        this.batch = batch;
        viewport = new FitViewport(width, height);
        stage = new Stage(viewport, batch);
        skin = new Skin(Gdx.files.internal("skin/sponsole.json"));
        initPixelTexture();
        SceneComposerStageBuilder builder = new SceneComposerStageBuilder();
        builder.build(stage, skin, Gdx.files.internal("sponsole_scene.json"));

        BitmapFont ssp = skin.getFont("ssp");

        noMarkup = new BitmapFont(new BitmapFontData(ssp.getData().getFontFile(), ssp.getData().flipped), ssp.getRegion(), ssp.usesIntegerPositions());
        ssp.getData().markupEnabled = true;

        noMarkup.getData().markupEnabled = false;
        System.out.println(noMarkup.getData().markupEnabled + " " + ssp.getData().markupEnabled);

        commandDispatcher = new CommandDispatcher<>();
        addListeners();

        helps = new HashMap<>();
        registerHelpCommand();
        registerClearCommand();
        registerHelp("help", "shows all commands");
        registerHelp("clear", "clears the string");
    }

    private void registerClearCommand() {
        commandDispatcher.register(
                SponsoleCommand.literal("clear")
                .executes(context -> {
                    context.getSource().output = "";
                    output.setText("");
                    return 0;
                })
        );
    }

    public boolean enable() {
        if(isEnabled) return false;
        Gdx.input.setInputProcessor(stage);
        stage.setKeyboardFocus(input);
        isEnabled = true;
        return true;
    }

    public boolean enable(InputMultiplexer multiplexer) {
        if(isEnabled) return false;
        multiplexer.addProcessor(stage);
        stage.setKeyboardFocus(input);
        isEnabled = true;
        return true;
    }

    public boolean disable() {
        if(!isEnabled) return false;
        input.setText("");
        Gdx.input.setInputProcessor(null);
        isEnabled = false;
        return true;
    }

    public boolean disable(InputMultiplexer multiplexer) {
        if(!isEnabled) return false;
        input.setText("");
        multiplexer.removeProcessor(stage);
        isEnabled = false;
        return true;
    }

    public void toggle() {
        if(isEnabled) disable();
        else enable();
    }

    public void toggle(InputMultiplexer multiplexer) {
        if(isEnabled) disable(multiplexer);
        else enable(multiplexer);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    private void registerHelpCommand() {
        commandDispatcher.register(
                SponsoleCommand.literal("help")
                .executes(context -> {
                    StringBuilder builder = new StringBuilder();
                    Map<CommandNode<CommandResult>, String> map = commandDispatcher.getSmartUsage(commandDispatcher.getRoot(), context.getSource());
                    for (String value : map.values()) {
                        builder.append("> ").append(value);
                        if(helps.containsKey(value)) {
                            builder.append(" - ").append(helps.get(value));
                        }
                        builder.append('\n');
                    }
                    builder.deleteCharAt(builder.length - 1);
                    context.getSource().output = builder.toString();
                    return 0;
                })
        );
    }

    private void addListeners() {
        input = stage.getRoot().findActor("command_input");
        output = stage.getRoot().findActor("command_output");

        input.getStyle().font = noMarkup;

        input.setTextFieldListener((textField, c) -> {
            if(c == '\n') {
                commandSent(textField.getText());
                textField.setText("");
            }
        });
    }

    private void commandSent(String cmd) {
        CommandResult result = new CommandResult();
        try {
            commandDispatcher.execute(cmd, result);
        } catch (CommandSyntaxException e) {
            result.success = false;
            result.output = "Command '" + cmd + "' failed! Error:\n" + e.getMessage();
        }
        SponsoleColor c = result.success ? SponsoleColor.WHITE : SponsoleColor.ERROR;
        output.setText(output.getText() + c.toString() + result.output + "\n");
    }

    public CommandDispatcher<CommandResult> getCommandDispatcher() {
        return commandDispatcher;
    }

    public void registerHelp(String command, String help) {
        helps.put(command, help);
    }

    private void initStage() {
        Table base = new Table(skin);
        Table topPart = new Table(skin);
        Label outputLabel = new Label("Test", skin);
        outputLabel.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        ScrollPane pane = new ScrollPane(outputLabel, skin);
        topPart.add(pane).grow();

        Table bottomPart = new Table(skin);
        Label entry = new Label(">", skin);
        TextField input = new TextField("This is some demo input", skin);
        input.setFillParent(true);

        bottomPart.add(entry);
        bottomPart.add(input).growX();

//        base.add(topPart);
        base.add(bottomPart).grow();

        base.setPosition(50, 50);
        stage.addActor(base);
    }

    private void initPixelTexture() {
        Pixmap pm = new Pixmap(1, 1, Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.drawPixel(0, 0);
        pixel = new Texture(pm);
        pm.dispose();
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void dispose() {
        pixel.dispose();
        stage.dispose();
        skin.dispose();
        noMarkup.dispose();
    }
}
