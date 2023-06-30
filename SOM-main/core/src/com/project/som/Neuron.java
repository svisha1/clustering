package com.project.som;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class Neuron {
    int x;
    int y;
    float[] w;
    Color color;
    public Neuron(int n, int x, int y) {
        this.x = x;
        this.y = y;
        w = new float[n];
        for (int i = 0; i < n; i++)
            w[i] = (float)new Random().nextDouble(-1, 1);
        color = new Color(1, 1, 1, 1);
    }

    public void recolor() {
        if(w.length == 1)
            color = new Color(w[0]*1, w[0]*1, w[0]*1, 1);
        if(w.length == 2)
            color = new Color(w[0]*1, w[1]*1, 1, 1);
        if(w.length >= 3)
            color = new Color(w[0]*1, w[1]*1, w[2]*1, 1);
    }
}
