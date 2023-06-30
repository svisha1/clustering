package com.project.som;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

public class SOM {
    public final int WIDTH=100, HEIGHT=100, WIDTH_BLOCK=7, HEIGHT_BLOCK=7;
    Neuron[] neurons;
    Neuron[] neighbors;
    Neuron currentWinner;
    int x;
    int y;
    float sigma0;
    double lambda;
    double sigma;
    double L;
    double theta;
    double r;

    //float[][] Ts = new float[][] {{0},{0.1f},{0.2f},{0.3f},{0.4f},{0.5f},{0.6f},{0.7f},{0.8f},{0.9f},{1}};
    //float[][] Ts = new float[][] {{0, 0, 0},{1, 0, 0},{0, 1, 0},{0, 0, 1},{1, 1, 1}};
    float[][] Ts;

    public SOM (int n, float[][] Ts) {
        this.Ts = Ts;
        this.neurons = new Neuron[WIDTH*HEIGHT];  //Инициализация массива нейронов
        this.x=1;           //Инициализации координаты x
        this.y=1;           //Инициализации координаты y
        this.sigma0 = Math.max(WIDTH*WIDTH_BLOCK,HEIGHT*HEIGHT_BLOCK)/2; //Константа
        this.lambda = 0;    //Инициализации ламбда
        this.sigma=0;       //Инициализации сигма
        this.L=0;
        this.theta = 0;
        this.r = 0;
        //this.neighbors=[];  //Инициализации массива соседей
        for(int i =0; i < WIDTH*HEIGHT; i++) //Пробегаемся по всем ячейкам сетки
        {
            this.neurons[i] = new Neuron(n,this.x,this.y); //Наполняем массив нейронов экземплярами класса
            if(this.x+WIDTH_BLOCK < WIDTH*WIDTH_BLOCK)     //Если еще не дошли до правой стенки
            {
                this.x+=WIDTH_BLOCK+1;                       //Тогда устанавливаем нейрон в данной строке
            }
            else                                           //Иначе
            {
                this.x=1;                                    //Переходим к левой стенке
                this.y+=HEIGHT_BLOCK+1;                      //Переходим на новую строку
            }
        }
    }

    public void recolor() {
        for (Neuron n : neurons)
            n.recolor();
    }

    public int indexMinimum(ArrayList<Double> D)
    {
        int index=0;
        double min = D.get(index); // Устанавливаем первый жлемент списка как минимальный
        for(int i = 1;i<D.size();i++) //Пробегаемся по всем элементам кроме первого
        {
            if(D.get(i)<min)  // Если текущий элемент меньше предыдущего минимума
            {
                index = i;  // Тогда меняем индекс минимального элемента
                min = D.get(i); // Изменяем значение минимального элемента
            }
        }
        return index; //Возвращаем индекс минимального элемента
    }

    public void search(float[][] y)
    {
        for (Neuron n: neurons)
            n.color = new Color(0.15f, 0.15f, 0.15f, 1);

        for (float[] f: y)
            neurons[neuronWinner(f)].recolor();
    }

    public int neuronWinner(float[] y)
    {
        ArrayList<Double> D = new ArrayList<>();
        //float[] D=[]; //Список для хранения растояний между нейронами и входным воздействием
        for (Neuron n: neurons) {
            float s=0;
            int j = 0;
            for (float f: y) {
                s += Math.pow((f - n.w[j]), 2);
                j++;
            }
            D.add(Math.sqrt(s));
        }
        return indexMinimum(D); // Возвращение индекса победившего нейрона
    }

    public void learn(int T, float L0)
    {
        lambda = T/Math.log(sigma0); //Вычисление лямбда
        for (float[] value: Ts)
        {
            currentWinner = neurons[neuronWinner(value)]; //Получаем нейрон победителя
            for(int t = 0; t < T; t++)     //Обучаем T раз на каждом примере
            {
                sigma = sigma0 * Math.exp(-(t/lambda)); //Вычисляем сигма
                L = L0 * Math.exp(-(t/lambda));             //Вычисляем коэффициент скорости обучения
                neighbors = filter();
                int indexNeuron = 0;
                for (Neuron neuron: neighbors)
                {
                    r = Math.sqrt( Math.pow((neuron.x-currentWinner.x), 2)+Math.pow((neuron.y-currentWinner.y),2));
                    theta = Math.exp(-((Math.pow(r, 2)) / (2*(Math.pow(sigma,2)))));  //Вычисление тета

                    int indexWeight = 0;
                    for(float weight: neuron.w) {
                        neighbors[indexNeuron].w[indexWeight] += theta * L * (value[indexWeight] - weight);
                        indexWeight++;
                    }

                    indexNeuron++;
                }
            }
        }
    }

    Neuron[] filter() {
        ArrayList<Neuron> ns = new ArrayList<>();
        for (Neuron n: neurons) {
            if(Math.sqrt(Math.pow((n.x-this.currentWinner.x), 2)+Math.pow((n.y-this.currentWinner.y),2)  ) < sigma)
                ns.add(n);
        }
        Neuron[] nn = new Neuron[ns.size()];
        for (int i = 0; i < ns.size(); i++)
            nn[i] = ns.get(i);
        return nn;
    }
}