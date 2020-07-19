package com.redsponge.sponsole;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mojang.brigadier.arguments.IntegerArgumentType;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class SponsoleTest extends ApplicationAdapter {

    private Sponsole sponsole;
    private SpriteBatch batch;

    private boolean active;

    @Override
    public void create() {
        super.create();
        batch = new SpriteBatch();
        sponsole = new Sponsole(batch, 640 * 4, 360 * 4);
        sponsole.getCommandDispatcher().register(
                SponsoleCommand.literal("foo")
                .then(
                        SponsoleCommand.argument("bar", IntegerArgumentType.integer())
                        .executes(context -> {
                            context.getSource().output = "Called " + SponsoleColor.LIME + "foo" +
                                    SponsoleColor.WHITE + " with bar " + SponsoleColor.YELLOW + context.getArgument("bar", Integer.class);
                            return 0;
                        })
                )
                .executes(context -> {
                    context.getSource().output = "No arguments for foo!";
                    context.getSource().success = false;
                    return -1;
                })
        );
    }

    @Override
    public void render() {
        if(Gdx.input.isKeyJustPressed(Keys.GRAVE)) {
            sponsole.toggle();
        }
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(sponsole.isEnabled()) sponsole.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        sponsole.dispose();
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        sponsole.resize(width, height);
    }
}